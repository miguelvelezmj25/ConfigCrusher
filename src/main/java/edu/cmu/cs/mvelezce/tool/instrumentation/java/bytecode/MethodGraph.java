package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;

import java.util.*;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraph {

    private MethodBlock entryBlock;
    private Map<Label, MethodBlock> blocks = new HashMap<>();

    public void addMethodBlock(MethodBlock methodBlock) {
        if(this.entryBlock == null) {
            this.entryBlock = methodBlock;
        }

        this.blocks.put(methodBlock.getLabel(), methodBlock);
    }

    public void addEdge(MethodBlock from, MethodBlock to) {
        from.addSuccessor(to);
        to.addPredecessor(from);
    }

    public MethodBlock getNextCommonSuccessor(MethodBlock blockOne, MethodBlock blockTwo) {
        if(blockOne == blockTwo) {
            return blockOne;
        }

        Set<MethodBlock> visitedBlockOne = new HashSet<>();
        Set<MethodBlock> visitedBlockTwo = new HashSet<>();
        Queue<MethodBlock> blockOneQueue = new LinkedList<>();
        blockOneQueue.add(blockOne);


        while(!blockOneQueue.isEmpty()) {
            MethodBlock currentBlockOne = blockOneQueue.remove();

            visitedBlockOne.add(currentBlockOne);
            Collection<MethodBlock> successorsCurrentBlockOne = currentBlockOne.getSuccessors();

            for(MethodBlock methodBlock : successorsCurrentBlockOne) {
                if(!visitedBlockOne.contains(methodBlock)) {
                    blockOneQueue.add(methodBlock);
                }
            }
        }

        Queue<MethodBlock> blockTwoQueue = new LinkedList<>();
        blockTwoQueue.add(blockTwo);

        while(!blockTwoQueue.isEmpty()) {
            MethodBlock currentBlockTwo = blockTwoQueue.remove();

            if(currentBlockTwo != blockOne && currentBlockTwo != blockTwo && visitedBlockOne.contains(currentBlockTwo)) {
                // TODO return the next successor that does not go back to this block.
                return currentBlockTwo;
            }

            visitedBlockTwo.add(currentBlockTwo);
            Collection<MethodBlock> successorsCurrentBlockTwo = currentBlockTwo.getSuccessors();

            for(MethodBlock methodBlock : successorsCurrentBlockTwo) {
                if(!visitedBlockTwo.contains(methodBlock)) {
                    blockTwoQueue.add(methodBlock);
                }
            }
        }

        return null;
    }

    public String toDotString() {
        StringBuilder dotString = new StringBuilder("digraph MethodGraph {\n");

        for(MethodBlock methodBlock : this.blocks.values()) {
            for(MethodBlock successor : methodBlock.getSuccessors()) {
                dotString.append(methodBlock.getID());
                dotString.append(" -> ");
                dotString.append(successor.getID());
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

    public MethodBlock getEntryBlock() { return this.entryBlock; }
}
