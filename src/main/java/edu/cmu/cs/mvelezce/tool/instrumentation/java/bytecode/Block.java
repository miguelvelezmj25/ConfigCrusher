package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.InsnList;

/**
 * Created by mvelezce on 5/3/17.
 */
public class Block {

    private Label label;
    private InsnList instructions;

    public Block(Label label, InsnList instructions) {
        this.label = label;
        this.instructions = instructions;
    }

    public Label getLabel() { return this.label; }

    public void setLabel(Label label) { this.label = label; }

    public InsnList getInstructions() { return this.instructions; }

    public void setInstructions(InsnList instructions) { this.instructions = instructions; }

    @Override
    public String toString() {
        return "Block{" +
                "label=" + this.label +
                ", instructions=" + this.instructions.size() +
                '}';
    }
}
