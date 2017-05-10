package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.*;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphBuilder {

    public static MethodGraph buildMethodGraph(MethodNode methodNode) {
        Map<AbstractInsnNode, LabelNode> instructionToNewLabel = new HashMap<>();
        MethodGraph graph = new MethodGraph();

        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();
        List<AbstractInsnNode> labelInstructions = new ArrayList<>();

        while(instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();
            int instructionType = instruction.getType();

            if(instructionType == AbstractInsnNode.LABEL) {
                LabelNode labelNode = (LabelNode) instruction;
                labelInstructions = new ArrayList<>();
                MethodBlock methodBlock = new MethodBlock(labelNode.getLabel(), labelInstructions);
                graph.addMethodBlock(methodBlock);
            }
            else if(instructionType == AbstractInsnNode.JUMP_INSN) {
                AbstractInsnNode nextInstruction = instruction.getNext();

                if(nextInstruction.getType() != AbstractInsnNode.LABEL) {
                    labelInstructions.add(instruction);

                    LabelNode labelNode = new LabelNode();
                    labelInstructions = new ArrayList<>();
                    labelInstructions.add(labelNode);
                    MethodBlock methodBlock = new MethodBlock(labelNode.getLabel(), labelInstructions);
                    graph.addMethodBlock(methodBlock);

                    instructionToNewLabel.put(instruction, labelNode);
                    instruction = instructionsIterator.next();
                }
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

                AbstractInsnNode nextInstruction = instruction.getNext();

                if(nextInstruction.getType() != AbstractInsnNode.LABEL) {
                    labelNode = instructionToNewLabel.get(instruction);
                    successor = graph.getMethodBlock(labelNode.getLabel());
                    graph.addEdge(currentMethodBlock, successor);

                    currentMethodBlock = successor;
                }
            }

            previousInstruction = instruction;
        }

        return graph;
    }
}
