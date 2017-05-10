package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraph {

    // TODO create a single exit block for the graph
    private MethodBlock entryBlock = null;
    private MethodBlock exitBlock = null;
    private Map<Label, MethodBlock> blocks = new HashMap<>();

    public void addMethodBlock(MethodBlock methodBlock) {
        if(this.entryBlock == null) {
            this.entryBlock = methodBlock;
        }

        this.blocks.put(methodBlock.getLabel(), methodBlock);
        this.exitBlock = methodBlock;
    }

    public void addEdge(MethodBlock from, MethodBlock to) {
        if(from.getSuccessors().contains(to) && to.getPredecessors().contains(from)) {
            return;
        }

        from.addSuccessor(to);
        to.addPredecessor(from);
    }

    public static Map<MethodBlock, Set<MethodBlock>> getDominators(MethodGraph methodGraph) {
        Map<MethodBlock, Set<MethodBlock>> blockToDominators = new HashMap<>();

        for(MethodBlock block : methodGraph.blocks.values()) {
            blockToDominators.put(block, new HashSet<>());
        }

        Set<MethodBlock> dominators = new HashSet<>();
        dominators.add(methodGraph.entryBlock);
        blockToDominators.put(methodGraph.entryBlock, dominators);

        Set<MethodBlock> blocks = new HashSet<>(methodGraph.blocks.values());
        blocks.remove(methodGraph.entryBlock);

        boolean change = true;

        while(change) {
            change = false;

            for(MethodBlock block : blocks) {
                dominators = new HashSet<>();
                dominators.add(block);

                Set<MethodBlock> predecessorsDominators = new HashSet<>();
                Set<MethodBlock> predecessors = block.getPredecessors();

                if(!predecessors.isEmpty()) {
                    Iterator<MethodBlock> predecessorsIterator = predecessors.iterator();
                    MethodBlock predecessor = predecessorsIterator.next();
                    predecessorsDominators.addAll(blockToDominators.get(predecessor));

                    while(predecessorsIterator.hasNext()) {
                        predecessor = predecessorsIterator.next();
                        Set<MethodBlock> currentPredecesorDominators = blockToDominators.get(predecessor);

                        if(currentPredecesorDominators.isEmpty()) {
                            predecessorsDominators.addAll(blockToDominators.get(predecessor));
                        }
                        else {
                            predecessorsDominators.retainAll(blockToDominators.get(predecessor));
                        }
                    }
                }

                dominators.addAll(predecessorsDominators);

                Set<MethodBlock> previousDominators = blockToDominators.get(block);

                if(!previousDominators.equals(dominators)) {
                    change = true;
                    blockToDominators.put(block, dominators);
                }
            }
        }

        return blockToDominators;
    }

    public Map<MethodBlock, Set<MethodBlock>> getDominators() {
        return MethodGraph.getDominators(this);
    }

    public static MethodBlock getImmediateDominator(MethodGraph methodGraph, MethodBlock methodBlock) {
        Map<MethodBlock, Set<MethodBlock>> blocksToDominators = MethodGraph.getDominators(methodGraph);
        Set<MethodBlock> dominators = new HashSet<>(blocksToDominators.get(methodBlock));
        dominators.remove(methodBlock);

        for(MethodBlock dominator : dominators) {
            if(dominators.equals(blocksToDominators.get(dominator))) {
                return dominator;
            }
        }

        return null;
    }

    public MethodBlock getImmediateDominator(MethodBlock methodBlock) {
        return MethodGraph.getImmediateDominator(this, methodBlock);
    }

    public static MethodGraph reverseGraph(MethodGraph methodGraph) {
        MethodGraph reversedGraph = new MethodGraph();
        Set<MethodBlock> blocks = new HashSet<>(methodGraph.blocks.values());

        for(MethodBlock block : blocks) {
            MethodBlock newBlock = new MethodBlock(block.getID(), block.getLabel());
            reversedGraph.addMethodBlock(newBlock);
        }

        for(MethodBlock block : blocks) {
            for(MethodBlock successor : block.getSuccessors()) {
                MethodBlock newBlock = reversedGraph.blocks.get(block.getLabel());
                MethodBlock newSuccessorBlock = reversedGraph.blocks.get(successor.getLabel());
                reversedGraph.addEdge(newSuccessorBlock, newBlock);
            }

            for(MethodBlock predecessor : block.getPredecessors()) {
                MethodBlock newBlock = reversedGraph.blocks.get(block.getLabel());
                MethodBlock newPredecessorBlock = reversedGraph.blocks.get(predecessor.getLabel());
                reversedGraph.addEdge(newBlock, newPredecessorBlock);
            }
        }

        return reversedGraph;
    }

    public MethodGraph reverseGraph() {
        return MethodGraph.reverseGraph(this);
    }

    // Breadth first search
    public void taintBranch(MethodBlock currentBlock, Set<MethodBlock> currentTaintedBlocks, Queue<MethodBlock> blocksWithTwoSuccessors) {
        Queue<MethodBlock> blockQueue = new LinkedList<>();
        blockQueue.add(currentBlock);

        while(!blockQueue.isEmpty()) {
            currentBlock = blockQueue.remove();

            currentTaintedBlocks.add(currentBlock);
            Set<MethodBlock> successorsOne = currentBlock.getSuccessors();

            for(MethodBlock methodBlock : successorsOne) {
                if(!currentTaintedBlocks.contains(methodBlock)) {
                    blockQueue.add(methodBlock);
                }
            }

            if(successorsOne.size() == 2 && !blocksWithTwoSuccessors.contains(currentBlock)) {
                blocksWithTwoSuccessors.add(currentBlock);
            }
        }
    }

    public MethodBlock getWhereBranchesConverge(MethodBlock rootBlock) {
        if(rootBlock.getSuccessors().size() == 1) {
            return rootBlock.getSuccessors().iterator().next();
        }

        Set<MethodBlock> successors = rootBlock.getSuccessors();
        Iterator<MethodBlock> successorsIterator = successors.iterator();
        MethodBlock currentBlock = successorsIterator.next();

        Set<Set<MethodBlock>> taintedBlocks = new HashSet<>();
        Set<MethodBlock> currentTaintedBlocks = new LinkedHashSet<>();
        taintedBlocks.add(currentTaintedBlocks);
        currentTaintedBlocks.add(currentBlock);

        Queue<MethodBlock> blockQueue = new LinkedList<>();
        blockQueue.add(currentBlock);

        Queue<MethodBlock> blocksWithTwoSuccessors = new LinkedList<>();

        // Taint one of the branches
        this.taintBranch(currentBlock, currentTaintedBlocks, blocksWithTwoSuccessors);

        currentBlock = successorsIterator.next();
        currentTaintedBlocks = new LinkedHashSet<>();
        taintedBlocks.add(currentTaintedBlocks);
        currentTaintedBlocks.add(currentBlock);
        blockQueue = new LinkedList<>();
        blockQueue.add(currentBlock);

        // Taint the other branch
        this.taintBranch(currentBlock, currentTaintedBlocks, blocksWithTwoSuccessors);

        // Get set of tainted blocks with the most blocks
        Set<MethodBlock> longestPath = null;

        for(Set<MethodBlock> path : taintedBlocks) {
            if(longestPath == null || path.size() > longestPath.size()) {
                longestPath = path;
            }
        }

        // Create a new set of tainted blocks and remove the one that has the one with the most blocks
        Set<Set<MethodBlock>> hold = new HashSet<>(taintedBlocks);
        hold.remove(longestPath);

        // Find that first node that is in both sets to find where to get all the paths from the root node
        for(MethodBlock possibleMethodBlock : longestPath) {
            boolean inAllPaths = true;

            for(Set<MethodBlock> path : taintedBlocks) {
                if(!path.contains(possibleMethodBlock)) {
                    inAllPaths = false;
                    break;
                }
            }

            if(inAllPaths) {
                currentBlock = possibleMethodBlock;
                break;
            }
        }

        // Get all paths between the root node and the first node in both sets of tainted blocks
        Set<List<MethodBlock>> pathsFromRootToCurrentBlock = new HashSet<>();
        List<MethodBlock> visitedBlock = new ArrayList<>();
        visitedBlock.add(rootBlock);
        this.findAllPaths(rootBlock, currentBlock, visitedBlock, pathsFromRootToCurrentBlock);

        // If all blocks of all paths have their successors as part of the paths, then we found were the branches converge
        Set<MethodBlock> allAncestors = new HashSet<>();

        for(List<MethodBlock> pathFromRootToCurrentBlock : pathsFromRootToCurrentBlock) {
            allAncestors.addAll(pathFromRootToCurrentBlock);
        }

        boolean done = true;

        for(MethodBlock ancestor : allAncestors) {
            if(ancestor == currentBlock) {
                continue;
            }

            if(!allAncestors.containsAll(ancestor.getSuccessors())) {
                done = false;
            }
        }

        if(done) {
            return currentBlock;
        }

        // For each inner block with two successors, taint the blocks reached by those blocks
        while(!blocksWithTwoSuccessors.isEmpty()) {
            currentBlock = blocksWithTwoSuccessors.remove();

            for(MethodBlock successor : currentBlock.getSuccessors()) {
                currentTaintedBlocks = new LinkedHashSet<>();
                taintedBlocks.add(currentTaintedBlocks);
                currentTaintedBlocks.add(successor);

                blockQueue = new LinkedList<>();
                blockQueue.add(successor);

                while(!blockQueue.isEmpty()) {
                    successor = blockQueue.remove();

                    currentTaintedBlocks.add(successor);
                    Set<MethodBlock> successorsOne = successor.getSuccessors();

                    for(MethodBlock methodBlock : successorsOne) {
                        if(!currentTaintedBlocks.contains(methodBlock)) {
                            blockQueue.add(methodBlock);
                        }
                    }
                }
            }
        }

        longestPath = null;

        for(Set<MethodBlock> path : taintedBlocks) {
            if(longestPath == null || path.size() > longestPath.size()) {
                longestPath = path;
            }
        }

        taintedBlocks.remove(longestPath);

        for(MethodBlock possibleMethodBlock : longestPath) {
            boolean inAllPaths = true;

            for(Set<MethodBlock> path : taintedBlocks) {
                if(!path.contains(possibleMethodBlock)) {
                    inAllPaths = false;
                    break;
                }
            }

            if(inAllPaths) {
                return possibleMethodBlock;
            }
        }

        return null;
    }

    public void findAllPaths(MethodBlock start, MethodBlock end, List<MethodBlock> visitedBlocks, Set<List<MethodBlock>> paths) {
        Set<MethodBlock> successors = start.getSuccessors();

        for(MethodBlock methodBlock : successors) {
            if(visitedBlocks.contains(methodBlock)) {
                continue;
            }

            if(methodBlock.equals(end)) {
                visitedBlocks.add(methodBlock);
                paths.add(new ArrayList<>(visitedBlocks));
                visitedBlocks.remove(visitedBlocks.size()-1);
            }
        }

        for(MethodBlock methodBlock : successors) {
            if(visitedBlocks.contains(methodBlock) || methodBlock.equals(end)) {
                continue;
            }

            visitedBlocks.add(methodBlock);
            this.findAllPaths(methodBlock, end, visitedBlocks, paths);
            visitedBlocks.remove(visitedBlocks.size()-1);
        }
    }

    public String toDotString(String methodName) {
        StringBuilder dotString = new StringBuilder("digraph " + methodName + " {\n");

        for(MethodBlock methodBlock : this.blocks.values()) {
            for(MethodBlock successor : methodBlock.getSuccessors()) {
                if(methodBlock.getLabel().info == null) {
                    dotString.append(methodBlock.getID());
                }
                else {
                    dotString.append(methodBlock.getLabel().info);
                }

                dotString.append(" -> ");

                if(successor.getLabel().info == null) {
                    dotString.append(successor.getID());
                }
                else {
                    dotString.append(successor.getLabel().info);
                }
                dotString.append(";\n");
            }
        }

        dotString.append("}");

        return dotString.toString();
    }

    public MethodBlock getMethodBlock(Label label) {
        return this.blocks.get(label);
    }

    public int getBlockCount() { return this.blocks.size(); }

    public int getEdgeCount() {
        int edges = 0;

        for(MethodBlock methodBlock : this.blocks.values()) {
            edges += methodBlock.getSuccessors().size();
        }

        return edges;
    }

    public MethodBlock getExitBlock() { return this.exitBlock; }

    public MethodBlock getEntryBlock() { return this.entryBlock; }
}
