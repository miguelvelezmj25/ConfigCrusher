package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.ListIterator;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphBuilder {

    public static MethodGraph buildMethodGraph(MethodNode methodNode) {
        MethodGraph graph = new MethodGraph();

        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();
        InsnList labelInstructions = new InsnList();

        while(instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();

            if(instruction.getType() == AbstractInsnNode.LABEL) {
                LabelNode labelNode = (LabelNode) instruction;

                labelInstructions = new InsnList();
                MethodBlock methodBlock = new MethodBlock(labelNode.getLabel(), labelInstructions);
                graph.addMethodBlock(methodBlock);
            }

            labelInstructions.add(instruction);
        }

        instructions = methodNode.instructions;
        instructionsIterator = instructions.iterator();
        MethodBlock currentMethodBlock = null;
        AbstractInsnNode previousInstruction = null;


        while(instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();

            if(instruction.getType() == AbstractInsnNode.LABEL) {
                LabelNode labelNode = (LabelNode) instruction;
                MethodBlock successor = graph.getMethodBlock(labelNode.getLabel());

                // TODO what other opcodes should not have an edge with the next label?
                if(currentMethodBlock != null && previousInstruction.getOpcode() != Opcodes.GOTO) {
                    successor.addPredecessor(currentMethodBlock);
                    currentMethodBlock.addSuccessor(successor);
                }

                currentMethodBlock = successor;
            }
            else if(instruction.getType() == AbstractInsnNode.JUMP_INSN) {
                JumpInsnNode jumpNode = (JumpInsnNode) instruction;

                LabelNode labelNode = jumpNode.label;

                MethodBlock successor = graph.getMethodBlock(labelNode.getLabel());
                successor.addPredecessor(currentMethodBlock);
                currentMethodBlock.addSuccessor(successor);
            }

            previousInstruction = instruction;
        }

        return graph;
    }
}
