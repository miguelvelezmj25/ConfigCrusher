package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;

import java.util.HashMap;
import java.util.Map;

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

    public MethodBlock getMethodBlock(Label label) {
        return this.blocks.get(label);
    }

    public int getBlockCount() { return this.blocks.size(); }

    public int getEdgeCount() {
        int edges = 0;

        for (MethodBlock methodBlock : this.blocks.values()) {
            edges += methodBlock.getSuccessors().size();
        }

        return edges;
    }

    public MethodBlock getEntryBlock() { return this.entryBlock; }
}
