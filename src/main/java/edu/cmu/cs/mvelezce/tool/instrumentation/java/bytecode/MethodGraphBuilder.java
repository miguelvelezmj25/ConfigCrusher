package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphBuilder {

    public static MethodGraph buildMethodGraph(MethodNode methodNode) {
        MethodGraph graph = new MethodGraph();

        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();
        List<AbstractInsnNode> labelInstructions = new ArrayList<>();

        while(instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();

            if(instruction.getType() == AbstractInsnNode.LABEL) {
                LabelNode labelNode = (LabelNode) instruction;

                labelInstructions = new ArrayList<>();
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
            int instructionType = instruction.getType();

            if(instructionType == AbstractInsnNode.LABEL) {
                LabelNode labelNode = (LabelNode) instruction;
                MethodBlock successor = graph.getMethodBlock(labelNode.getLabel());

                if(currentMethodBlock != null && (previousInstruction.getOpcode() < Opcodes.GOTO || previousInstruction.getOpcode() > Opcodes.RETURN)) {
                    graph.addEdge(currentMethodBlock, successor);
                }

                currentMethodBlock = successor;
            }
            else if(instructionType == AbstractInsnNode.JUMP_INSN) {
                JumpInsnNode jumpNode = (JumpInsnNode) instruction;

                LabelNode labelNode = jumpNode.label;

                MethodBlock successor = graph.getMethodBlock(labelNode.getLabel());
                graph.addEdge(currentMethodBlock, successor);
            }

            previousInstruction = instruction;
        }

        return graph;
    }
}
