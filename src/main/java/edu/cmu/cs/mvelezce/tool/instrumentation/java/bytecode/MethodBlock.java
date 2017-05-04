package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.InsnList;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodBlock {

    private String ID;
    private Label label;
    private InsnList instructions;
    private Collection<MethodBlock> successors = new HashSet<>(2);
    private Collection<MethodBlock> predecessors = new HashSet<>();

    public MethodBlock(String ID, Label label, InsnList instructions) {
        this.ID = ID;
        this.label = label;
        this.instructions = instructions;
    }

    public MethodBlock(Label label, InsnList instructions) {
        this(label.toString(), label, instructions);
    }

    public void addSuccessor(MethodBlock methodBlock) {
        if(this.successors.size() >= 2) {
            throw new IllegalArgumentException("A method block cannot have more than 2 successors");
        }

        successors.add(methodBlock);
    }

    public void addPredecessor(MethodBlock methodBlock) {
        predecessors.add(methodBlock);
    }

    public String getID() { return this.ID; }

    public Label getLabel() { return this.label; }

    public InsnList getInstructions() { return this.instructions; }

    public Collection<MethodBlock> getSuccessors() { return this.successors; }

    public Collection<MethodBlock> getPredecessors () { return  this.predecessors; }

    @Override
    public String toString() {
        return "MethodBlock{" +
                "ID='" + this.ID + '\'' +
                ", label=" + this.label +
                ", instructions=" + this.instructions.size() +
                ", successors=" + this.successors.size() +
                ", predecessors=" + this.predecessors.size() +
                '}';
    }
}
