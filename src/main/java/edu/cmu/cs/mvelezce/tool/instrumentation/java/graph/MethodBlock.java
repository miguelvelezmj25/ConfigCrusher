package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

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
    // TODO is this needed?
//    private AbstractInsnNode insnNode = null;
    private List<AbstractInsnNode> instructions = new ArrayList<>();
    private Set<MethodBlock> successors = new HashSet<>();
    private Set<MethodBlock> predecessors = new HashSet<>();
    private boolean withReturn;

//    public MethodBlock(String ID, Label label, Label originalLabel, List<AbstractInsnNode> instructions) {
//        this.ID = ID;
//        this.label = label;
//        this.originalLabel = originalLabel;
//        this.instructions = instructions;
//    }
//
//    public MethodBlock(Label label, List<AbstractInsnNode> instructions) {
//        this(label.toString(), label, label, instructions);
//    }
//
//    public MethodBlock(Label label, Label originalLabel, List<AbstractInsnNode> instructions) {
//        this(label.toString(), label, originalLabel, instructions);
//    }
//
//    public MethodBlock(String ID) {
//        this(ID, new Label(), new Label(), new ArrayList<>());
//    }
//
//    public MethodBlock(Label label) {
//        this(label.toString(), label, label, new ArrayList<>());
//    }

    public MethodBlock(AbstractInsnNode insnNode) {
        this(MethodBlock.asID(insnNode));
//        this.insnNode = insnNode;
    }

    public MethodBlock(String ID) {
        this.ID = ID;
    }

    public static String asID(AbstractInsnNode insnNode) {
        return insnNode.hashCode() + "";
    }

    public void addSuccessor(MethodBlock methodBlock) {
//        if(this.successors.size() >= 2) {
//            throw new IllegalArgumentException("A method block cannot have more than 2 successors");
//        }

        successors.add(methodBlock);
    }

    public void addPredecessor(MethodBlock methodBlock) {
        predecessors.add(methodBlock);
    }

    public void reset() {
        this.predecessors.clear();
        this.successors.clear();
    }

//    public Label getLabel() {
//        return this.label;
//    }
//
//    public Label getOriginalLabel() {
//        return this.originalLabel;
//    }

    public String getID() {
        return this.ID;
    }

    public List<AbstractInsnNode> getInstructions() {
        return this.instructions;
    }

    public Set<MethodBlock> getSuccessors() {
        return this.successors;
    }

    //    public boolean isWithRet() {
//        return this.withRet;
//    }
//
//    public void setWithRet(boolean withRet) {
//        this.withRet = withRet;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if(this == o) {
//            return true;
//        }
//        if(o == null || !(o instanceof MethodBlock)) {
//            return false;
//        }
//
//        MethodBlock that = (MethodBlock) o;
//
//        return ID.equals(that.ID);
//    }
//
//    @Override
//    public int hashCode() {
//        return ID.hashCode();
//    }

    public Set<MethodBlock> getPredecessors() {
        return this.predecessors;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        MethodBlock that = (MethodBlock) o;

        return ID.equals(that.ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public String toString() {
        return this.ID;
//        return "MethodBlock{" +
//                "ID='" + ID + '\'' +
//                ", label=" + label +
//                ", instructions=" + instructions.size() +
//                ", successors=" + successors.size() +
//                ", predecessors=" + predecessors.size() +
//                '}';
    }

    public boolean isWithReturn() {
        return withReturn;
    }

    public void setWithReturn(boolean withReturn) {
        this.withReturn = withReturn;
    }
}
