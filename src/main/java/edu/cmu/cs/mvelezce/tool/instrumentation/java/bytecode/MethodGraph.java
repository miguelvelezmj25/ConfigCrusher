package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;

import java.util.*;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraph {

    private MethodBlock entryBlock;
    private MethodBlock exitBlock;
    private Map<Label, MethodBlock> blocks = new HashMap<>();

    public void addMethodBlock(MethodBlock methodBlock) {
        if(this.entryBlock == null) {
            this.entryBlock = methodBlock;
        }

        this.blocks.put(methodBlock.getLabel(), methodBlock);
        this.exitBlock = methodBlock;
    }

    public void addEdge(MethodBlock from, MethodBlock to) {
        from.addSuccessor(to);
        to.addPredecessor(from);
    }

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


        Set<MethodBlock> longestPath = null;

        for(Set<MethodBlock> path : taintedBlocks) {
            if(longestPath == null || path.size() > longestPath.size()) {
                longestPath = path;
            }
        }

        Set<Set<MethodBlock>> hold = new HashSet<>(taintedBlocks);
        hold.remove(longestPath);

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

        Set<List<MethodBlock>> pathsFromRootToCurrentBlock = new HashSet<>();
        List<MethodBlock> visitedBlock = new ArrayList<>();
        visitedBlock.add(rootBlock);
        this.findAllPaths(rootBlock, currentBlock, visitedBlock, pathsFromRootToCurrentBlock);

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


        // TODO remove loop to avoid expanding where to end the instrumentation
//        this.removeLoops(rootBlock, taintedBlocks);

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

//
//        // add node v to current path from s
//        path.push(v);
//        onPath.add(v);
//
//        // found path from s to t - currently prints in reverse order because of stack
//        if (v.equals(t))
//            System.out.println(path);
//
//            // consider all neighbors that would continue path with repeating a node
//        else {
//            for (MethodBlock w : v.getSuccessors()) {
//                if (!onPath.contains(w)) findAllPaths(w, t);
//            }
//        }
//
//        // done exploring from v, so remove from path
//        path.pop();
//        onPath.remove(v);
    }

    // TODO make it work
    public void removeLoops(MethodBlock methodBlock, Set<Set<MethodBlock>> taintedBlocks) {
        Set<MethodBlock> visited = new HashSet<>();
        Stack<MethodBlock> stack = new Stack<>();
        stack.push(methodBlock);
        visited.add(methodBlock);

        while(!stack.isEmpty()) {
            MethodBlock currentBlock = stack.pop();
        }

//        for(Set<MethodBlock> taintedBlock : taintedBlocks) {
//            for(MethodBlock block : taintedBlock) {
//                Set<MethodBlock> path = new LinkedHashSet<>();
//                Stack<MethodBlock> blockStack = new Stack<>();
//                blockStack.push(block);
//
//                while(!blockStack.isEmpty()) {
//                    MethodBlock currentBlock = blockStack.pop();
//
//                    path.add(currentBlock);
//                    Set<MethodBlock> successorsOne = currentBlock.getSuccessors();
//
//                    for(MethodBlock methodBlock : successorsOne) {
////                        if(!path.contains(methodBlock)) {
//                            blockStack.push(methodBlock);
////                        }
//                    }
//
//                }
//            }
//        }
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
