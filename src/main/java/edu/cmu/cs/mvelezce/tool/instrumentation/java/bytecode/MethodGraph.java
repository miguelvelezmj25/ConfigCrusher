package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;

import java.util.*;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraph {

    // TODO create a single exit block for the graph
    private MethodBlock entryBlock = null;
    private MethodBlock exitBlock = null;
    private Map<String, MethodBlock> blocks = new HashMap<>();

    public static MethodBlock getWhereToEndInstrumenting(MethodGraph methodGraph, MethodBlock start) {
        MethodBlock immediatePostDominator = MethodGraph.getImmediatePostDominator(methodGraph, start);
        Set<Set<MethodBlock>> stronglyConnectedComponents = MethodGraph.getStronglyConnectedComponents(methodGraph, start);

        boolean inLoop = false;

        for(Set<MethodBlock> stronglyConnectedComponent : stronglyConnectedComponents) {
            if(stronglyConnectedComponent.contains(immediatePostDominator) && stronglyConnectedComponent.size() > 1) {
                inLoop = true;
                break;
            }
        }

        if(!inLoop) {
            return immediatePostDominator;
        }

        throw new RuntimeException("We need to do some more work");
    }

    public static MethodBlock getImmediatePostDominator(MethodGraph methodGraph, MethodBlock start) {
        MethodGraph reversedGraph = MethodGraph.reverseGraph(methodGraph);
        return MethodGraph.getImmediateDominator(reversedGraph, start);
    }

    public static Set<Set<MethodBlock>> getStronglyConnectedComponents(MethodGraph methodGraph, MethodBlock start) {
        // DFS on the graph to find the order in which the blocks were last visited
        Stack<MethodBlock> visited = new Stack<>();
        Stack<MethodBlock> dfs = new Stack<>();
        dfs.push(start);

        while(!dfs.isEmpty()) {
            MethodBlock currentBlock = dfs.peek();

            if(currentBlock.getSuccessors().isEmpty()) {
                visited.push(dfs.pop());
            }
            else {
                boolean done = true;

                for(MethodBlock successor : currentBlock.getSuccessors()) {
                    if(!visited.contains(successor) && !dfs.contains(successor)) {
                        dfs.push(successor);

                        done = false;
                        break;
                    }
                }

                if(done) {
                    visited.push(dfs.pop());
                }
            }
        }

        // Reverse the graph
        MethodGraph reversedGraph = MethodGraph.reverseGraph(methodGraph);

        // DFS in order of last visited block from the first pass
        Set<Set<MethodBlock>> stronglyConnectedComponents = new HashSet<>();
        Set<MethodBlock> stronglyConnectedComponent = new HashSet<>();

        while(!visited.isEmpty()) {
            MethodBlock currentBlock = visited.pop();
            currentBlock = reversedGraph.getMethodBlock(currentBlock.getID());
            stronglyConnectedComponent.add(currentBlock);

            if(currentBlock.getSuccessors().isEmpty()) {
                stronglyConnectedComponents.add(stronglyConnectedComponent);
                stronglyConnectedComponent = new HashSet<>();
            }
            else {
                boolean done = true;

                for(MethodBlock successor : currentBlock.getSuccessors()) {
                    if(visited.contains(successor)) {
                        stronglyConnectedComponent.add(successor);
                        visited.remove(successor);
                        visited.push(successor);

                        done = false;
                        break;
                    }
                }

                if(done) {
                    stronglyConnectedComponents.add(stronglyConnectedComponent);
                    stronglyConnectedComponent = new HashSet<>();
                }
            }
        }

        return stronglyConnectedComponents;
    }

    public static MethodGraph reverseGraph(MethodGraph methodGraph) {
        MethodGraph reversedGraph = new MethodGraph();
        Set<MethodBlock> blocks = new HashSet<>(methodGraph.blocks.values());

        for(MethodBlock block : blocks) {
            MethodBlockReversed newBlock = new MethodBlockReversed(block.getID());
            reversedGraph.addMethodBlock(newBlock);
        }

        for(MethodBlock block : blocks) {
            for(MethodBlock successor : block.getSuccessors()) {
                MethodBlock newBlock = reversedGraph.blocks.get(block.getID());
                MethodBlock newSuccessorBlock = reversedGraph.blocks.get(successor.getID());
                reversedGraph.addEdge(newSuccessorBlock, newBlock);
            }
        }

        reversedGraph.entryBlock = reversedGraph.blocks.get(methodGraph.exitBlock.getID());
        reversedGraph.exitBlock = reversedGraph.blocks.get(methodGraph.entryBlock.getID());
        System.out.println(reversedGraph.toDotString("reverse"));

        return reversedGraph;
    }

    public static MethodBlock getImmediateDominator(MethodGraph methodGraph, MethodBlock start) {
        Map<MethodBlock, Set<MethodBlock>> blocksToDominators = MethodGraph.getDominators(methodGraph);
        Set<MethodBlock> dominators = new HashSet<>(blocksToDominators.get(start));
        dominators.remove(start);

        for(MethodBlock dominator : dominators) {
            if(dominators.equals(blocksToDominators.get(dominator))) {
                return dominator;
            }
        }

        throw new RuntimeException("Could not find an immediate dominator");
    }

    public static Map<MethodBlock, Set<MethodBlock>> getDominators(MethodGraph methodGraph) {
        Map<MethodBlock, Set<MethodBlock>> blocksToDominators = new HashMap<>();

        for(MethodBlock block : methodGraph.blocks.values()) {
            blocksToDominators.put(block, new HashSet<>(methodGraph.blocks.values()));
        }

        Set<MethodBlock> dominators = new HashSet<>();
        dominators.add(methodGraph.entryBlock);
        blocksToDominators.put(methodGraph.entryBlock, dominators);

        Set<MethodBlock> blocks = new HashSet<>(methodGraph.blocks.values());
        blocks.remove(methodGraph.entryBlock);

        boolean change = true;

        while(change) {
            change = false;

            for(MethodBlock block : blocks) {
                dominators = new HashSet<>();
                dominators.add(block);

                Set<MethodBlock> predecessorsDominators = new HashSet<>(methodGraph.blocks.values());

                for(MethodBlock predecessor : block.getPredecessors()) {
                    predecessorsDominators.retainAll(blocksToDominators.get(predecessor));
                }

                dominators.addAll(predecessorsDominators);
                Set<MethodBlock> previousDominators = blocksToDominators.get(block);

                if(!previousDominators.equals(dominators)) {
                    change = true;
                    blocksToDominators.put(block, dominators);
                }
            }
        }

        for(Map.Entry<MethodBlock, Set<MethodBlock>> blockToDominators : blocksToDominators.entrySet()) {
            System.out.println(blockToDominators.getKey() + " - " + blockToDominators.getValue());
        }

        return blocksToDominators;
    }

    public void addMethodBlock(MethodBlock methodBlock) {
        if(this.entryBlock == null) {
            this.entryBlock = methodBlock;
        }

        this.blocks.put(methodBlock.getID(), methodBlock);
        this.exitBlock = methodBlock;
    }

    public void addEdge(MethodBlock from, MethodBlock to) {
        from.addSuccessor(to);
        to.addPredecessor(from);
    }

    public Map<MethodBlock, Set<MethodBlock>> getDominators() {
        return MethodGraph.getDominators(this);
    }

    public MethodBlock getImmediateDominator(MethodBlock methodBlock) {
        return MethodGraph.getImmediateDominator(this, methodBlock);
    }



    public MethodGraph reverseGraph() {
        return MethodGraph.reverseGraph(this);
    }

    public MethodBlock getImmediatePostDominator(MethodBlock methodBlock) {
        return MethodGraph.getImmediatePostDominator(this, methodBlock);
    }

    public Set<Set<MethodBlock>> getStronglyConnectedComponents(MethodBlock methodBlock) {
        return MethodGraph.getStronglyConnectedComponents(this, methodBlock);
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

    public MethodBlock getMethodBlock(String ID) {
        return this.blocks.get(ID);
    }

    public MethodBlock getMethodBlock(Label label) {
        return this.getMethodBlock(label.toString());
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

    @Override
    public String toString() {
        return "MethodGraph{" +
                "entryBlock=" + entryBlock +
                ", exitBlock=" + exitBlock +
                ", blocks=" + blocks.values() +
                '}';
    }
}
