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
public class MethodBlockReversed extends MethodBlock {

    public MethodBlockReversed(String ID) {
        super(ID);
    }

    public void addPredecessor(MethodBlock methodBlock) {
        // TODO make new block that allows this so that we can check this in this method
        if(this.getPredecessors().size() >= 2) {
            throw new IllegalArgumentException("A method block cannot have more than 2 successors");
        }

        this.getPredecessors().add(methodBlock);
    }

    public void addSuccessor(MethodBlock methodBlock) {
        this.getSuccessors().add(methodBlock);
    }
}
