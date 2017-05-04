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

//    public MethodBlock getNextCommonSuccessor(MethodBlock blockOne, MethodBlock blockTwo) {
//        if(blockOne == blockTwo) {
//            return blockOne;
//        }
//
//        Set<MethodBlock> visitedBlockOne = new HashSet<>();
//        Set<MethodBlock> visitedBlockTwo = new HashSet<>();
//        Queue<MethodBlock> blockOneQueue = new LinkedList<>();
//        blockOneQueue.add(blockOne);
//
//        while(!blockOneQueue.isEmpty()) {
//            MethodBlock currentBlockOne = blockOneQueue.remove();
//
//            visitedBlockOne.add(currentBlockOne);
//            Collection<MethodBlock> successorsCurrentBlockOne = currentBlockOne.getSuccessors();
//
//            for(MethodBlock methodBlock : successorsCurrentBlockOne) {
//                if(!visitedBlockOne.contains(methodBlock)) {
//                    blockOneQueue.add(methodBlock);
//                }
//            }
//        }
//
//        Queue<MethodBlock> blockTwoQueue = new LinkedList<>();
//        blockTwoQueue.add(blockTwo);
//
//        while(!blockTwoQueue.isEmpty()) {
//            MethodBlock currentBlockTwo = blockTwoQueue.remove();
//
//            if(visitedBlockOne.contains(currentBlockTwo)) {
//                // TODO return the next successor that does not go back to this block.
//                return currentBlockTwo;
//            }
//
//            visitedBlockTwo.add(currentBlockTwo);
//            Collection<MethodBlock> successorsCurrentBlockTwo = currentBlockTwo.getSuccessors();
//
//            for(MethodBlock methodBlock : successorsCurrentBlockTwo) {
//                if(!visitedBlockTwo.contains(methodBlock)) {
//                    blockTwoQueue.add(methodBlock);
//                }
//            }
//        }
//
//        return null;
//    }

    public MethodBlock getWhereBranchesConverge(MethodBlock rootBlock) {
        if(rootBlock.getSuccessors().size() == 1) {
            return rootBlock.getSuccessors().iterator().next();
        }

        Collection<MethodBlock> successors = rootBlock.getSuccessors();
        Iterator<MethodBlock> successorsIterator = successors.iterator();
        MethodBlock currentBlock = successorsIterator.next();
        Set<Set<MethodBlock>> paths = new HashSet<>();
        Set<MethodBlock> currentPath = new LinkedHashSet<>();

        System.out.println("Adding new path starting with block " + currentBlock);
        paths.add(currentPath);
        currentPath.add(currentBlock);

        Queue<MethodBlock> blockQueue = new LinkedList<>();
        blockQueue.add(currentBlock);

        Queue<MethodBlock> blocksWithTwoSuccessors = new LinkedList<>();

        while(!blockQueue.isEmpty()) {
            currentBlock = blockQueue.remove();
            System.out.println("Current block of one: " + currentBlock);

            currentPath.add(currentBlock);
            Collection<MethodBlock> successorsOne = currentBlock.getSuccessors();

            for(MethodBlock methodBlock : successorsOne) {
                if(!currentPath.contains(methodBlock)) {
                    blockQueue.add(methodBlock);
                }
            }

            if(successorsOne.size() == 2) {
                blocksWithTwoSuccessors.add(currentBlock);
            }
        }

        currentBlock = successorsIterator.next();
        blockQueue = new LinkedList<>();
        blockQueue.add(currentBlock);
        currentPath = new LinkedHashSet<>();

        System.out.println("Adding new path starting with block " + currentBlock);
        paths.add(currentPath);
        currentPath.add(currentBlock);

        while(!blockQueue.isEmpty()) {
            currentBlock = blockQueue.remove();
            System.out.println("Current block of two: " + currentBlock);

            currentPath.add(currentBlock);
            Collection<MethodBlock> successorsOne = currentBlock.getSuccessors();

            for(MethodBlock methodBlock : successorsOne) {
                if(!currentPath.contains(methodBlock)) {
                    blockQueue.add(methodBlock);
                }
            }

            if(successorsOne.size() == 2) {
                blocksWithTwoSuccessors.add(currentBlock);
            }
        }

        while(!blocksWithTwoSuccessors.isEmpty()) {
            currentBlock = blocksWithTwoSuccessors.remove();
            System.out.println("Current block with two paths: " + currentBlock);
            successors = currentBlock.getSuccessors();
            successorsIterator = successors.iterator();
            currentBlock = successorsIterator.next();
            System.out.println("Current block: " + currentBlock);

            currentPath = new LinkedHashSet<>();

            System.out.println("Adding new path starting with block " + currentBlock);
            paths.add(currentPath);
            currentPath.add(currentBlock);

            blockQueue = new LinkedList<>();
            blockQueue.add(currentBlock);

            while(!blockQueue.isEmpty()) {
                currentBlock = blockQueue.remove();

                currentPath.add(currentBlock);
                Collection<MethodBlock> successorsOne = currentBlock.getSuccessors();

                for(MethodBlock methodBlock : successorsOne) {
                    if(!currentPath.contains(methodBlock)) {
                        blockQueue.add(methodBlock);
                    }
                }
            }

            currentBlock = successorsIterator.next();
            System.out.println("Current block: " + currentBlock);

            currentPath = new LinkedHashSet<>();

            System.out.println("Adding new path starting with block " + currentBlock);
            paths.add(currentPath);
            currentPath.add(currentBlock);

            blockQueue = new LinkedList<>();
            blockQueue.add(currentBlock);

            while(!blockQueue.isEmpty()) {
                currentBlock = blockQueue.remove();

                currentPath.add(currentBlock);
                Collection<MethodBlock> successorsOne = currentBlock.getSuccessors();

                for(MethodBlock methodBlock : successorsOne) {
                    if(!currentPath.contains(methodBlock)) {
                        blockQueue.add(methodBlock);
                    }
                }
            }
        }

        Set<MethodBlock> longestPath = null;

        for(Set<MethodBlock> path : paths) {
            if(longestPath == null || path.size() > longestPath.size()) {
                longestPath = path;
            }
        }

        paths.remove(longestPath);

        Set<MethodBlock> previousPath = null;

        for(MethodBlock possibleMethodBlock : longestPath) {
            boolean inAllPaths = true;

            for(Set<MethodBlock> path : paths) {
                if(!path.contains(possibleMethodBlock)) {
                    inAllPaths = false;
                    break;
                }

                previousPath = path;
            }

            if(inAllPaths) {
                return possibleMethodBlock;
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
