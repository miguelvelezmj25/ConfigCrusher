package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodBlock {

    private String ID;
    private Label label;
    private List<AbstractInsnNode> instructions;
    private Set<MethodBlock> successors;
    private Set<MethodBlock> predecessors;

    public MethodBlock(String ID, Label label, List<AbstractInsnNode> instructions) {
        this.ID = ID;
        this.label = label;
        this.instructions = instructions;
        this.successors = new HashSet<>();
        this.predecessors = new HashSet<>();
    }

    public MethodBlock(String ID, Label label) {
        this(ID, label, new ArrayList<>());
    }

    public MethodBlock(String ID) {
        this(ID, new Label());
    }

    public MethodBlock(Label label) {
        this(label.toString(), label);
    }

    public MethodBlock(Label label, List<AbstractInsnNode> instructions) {
        this(label.toString(), label, instructions);
    }

    public void addSuccessor(MethodBlock methodBlock) {
        // TODO make new block that allows this so that we can check this in this method
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

    public List<AbstractInsnNode> getInstructions() { return this.instructions; }

    public Set<MethodBlock> getSuccessors() { return this.successors; }

    public Set<MethodBlock> getPredecessors () { return  this.predecessors; }

    // TODO be careful with the instanceof
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof MethodBlock)) return false;

        MethodBlock that = (MethodBlock) o;

        return ID.equals(that.ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public String toString() {
        return "MethodBlock{" +
                ID +
                '}';
    }
}
