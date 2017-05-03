package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodBlock {

    private Label label;
    private InsnList instructions;
    private Collection<MethodBlock> successors = new HashSet<>(2);
    private Collection<MethodBlock> predecessors = new HashSet<>();

    public MethodBlock(Label label, InsnList instructions) {
        this.label = label;
        this.instructions = instructions;
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

    public Label getLabel() { return this.label; }

    public InsnList getInstructions() { return this.instructions; }

    public Collection<MethodBlock> getSuccessors() { return this.successors; }

    public Collection<MethodBlock> getPredecessors () { return  this.predecessors; }

    @Override
    public String toString() {
        String result =  "MethodBlock{";
        result += "label=" + this.label;
        result += ", instructions=" + this.instructions.size();
        result += ", successors=" + this.successors.size();
        result += ", predecessors=" + this.predecessors.size() + '}';

        return result;
    }
}
