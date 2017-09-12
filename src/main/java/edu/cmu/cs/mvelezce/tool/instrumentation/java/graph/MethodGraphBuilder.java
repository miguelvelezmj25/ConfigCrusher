package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphBuilder {

    private MethodNode methodNode;

    public MethodGraphBuilder(MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    public MethodGraph build() {
        MethodGraph graph = new MethodGraph();

        this.getBlocks(graph);
        this.getInstructions(graph);
        this.addEdges(graph);
        this.connectToEnterNode(graph);
        MethodGraphBuilder.connectToExitNode(graph);


//
//            if(instructionType == AbstractInsnNode.LABEL) {
//                labelNode = (LabelNode) instruction;
//                MethodBlock possibleBlock = graph.getMethodBlock(labelNode);
//
//                if(possibleBlock != null) {
//                    if(block == null) {
//
//                        block = possibleBlock;
//                    }
//                    else {
//                        if(block.getSuccessors().contains(possibleBlock)) {
//                            block = possibleBlock;
//                        }
//                        else {
//                            System.out.println("nope");
//
//                        }
//
//                    }
//                }
//            }
//            else if(instructionType == AbstractInsnNode.JUMP_INSN) {
//                JumpInsnNode jumpInsn = (JumpInsnNode) instruction;
//                MethodBlock destinationBlock = new MethodBlock(jumpInsn.label);
//                graph.addEdge(block, destinationBlock);
//
//                if(jumpInsn.getOpcode() == Opcodes.GOTO) {
//                    continue;
//                }
//
//                AbstractInsnNode nextInstruction = instruction.getNext();
//
//                if(nextInstruction.getType() == AbstractInsnNode.LABEL) {
//                    labelNode = (LabelNode) nextInstruction;
//                    destinationBlock = graph.getMethodBlock(labelNode);
//                    graph.addEdge(block, destinationBlock);
//                }
//                else {
//                    throw new RuntimeException();
//                }
//            }


//        MethodBlock currentMethodBlock = null;
//        AbstractInsnNode previousInstruction = null;
//        Set<MethodBlock> blocksWithReturn = new HashSet<>();
//
//        while (instructionsIterator.hasNext()) {
//            AbstractInsnNode instruction = instructionsIterator.next();
//            int instructionType = instruction.getType();
//
//            if(instructionType == AbstractInsnNode.LABEL) {
//                LabelNode labelNode = (LabelNode) instruction;
//                MethodBlock successor = graph.getMethodBlock(labelNode.getLabel());
//
//                if(currentMethodBlock != null && (previousInstruction.getOpcode() < Opcodes.GOTO || previousInstruction.getOpcode() > Opcodes.RETURN) && previousInstruction.getOpcode() != Opcodes.ATHROW) {
//                    graph.addEdge(currentMethodBlock, successor);
//                }
//
//                if(currentMethodBlock != null) {
//                    for(TryCatchBlockNode tryCatchBlockNode : methodNode.tryCatchBlocks) {
//                        if(tryCatchBlockNode.end.getLabel().equals(currentMethodBlock.getOriginalLabel())) {
//                            if(tryCatchBlockNode.handler.getLabel().equals(successor.getOriginalLabel())) {
//                                graph.addEdge(currentMethodBlock, successor);
//                            }
//                        }
//                    }
//                }
//
//                currentMethodBlock = successor;
//            }
//            else if(instructionType == AbstractInsnNode.JUMP_INSN) {
//                JumpInsnNode jumpNode = (JumpInsnNode) instruction;
//                LabelNode labelNode = jumpNode.label;
//                MethodBlock successor = graph.getMethodBlock(labelNode.getLabel());
//                graph.addEdge(currentMethodBlock, successor);
//
//                AbstractInsnNode nextInstruction = instruction.getNext();
//
//                if(nextInstruction.getType() != AbstractInsnNode.LABEL) {
//                    labelNode = instructionToNewLabel.get(instruction);
//                    successor = graph.getMethodBlock(labelNode.getLabel());
//                    graph.addEdge(currentMethodBlock, successor);
//
//                    currentMethodBlock = successor;
//                }
//            }
//            else if(instructionType == AbstractInsnNode.TABLESWITCH_INSN) {
//                TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode) instruction;
//                MethodBlock successor = graph.getMethodBlock(tableSwitchInsnNode.dflt.getLabel());
//                graph.addEdge(currentMethodBlock, successor);
//
//                for(LabelNode labelNode : tableSwitchInsnNode.labels) {
//                    successor = graph.getMethodBlock(labelNode.getLabel());
//                    graph.addEdge(currentMethodBlock, successor);
//                }
//            }
//            else if(instructionType == AbstractInsnNode.LOOKUPSWITCH_INSN) {
//                LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode) instruction;
//                MethodBlock successor = graph.getMethodBlock(lookupSwitchInsnNode.dflt.getLabel());
//                graph.addEdge(currentMethodBlock, successor);
//
//                for(LabelNode labelNode : lookupSwitchInsnNode.labels) {
//                    successor = graph.getMethodBlock(labelNode.getLabel());
//                    graph.addEdge(currentMethodBlock, successor);
//                }
//            }
//            else if(instruction.getOpcode() == Opcodes.ATHROW || (instruction.getOpcode() >= Opcodes.GOTO && instruction.getOpcode() <= Opcodes.RETURN)) {
//                blocksWithReturn.add(currentMethodBlock);
//            }
//
//            previousInstruction = instruction;
//
//            for(MethodBlock blockWithReturn : blocksWithReturn) {
//                // TODO this is what makes the exit block to have an edge with itself
//                graph.addEdge(blockWithReturn, graph.getExitBlock());
//                blockWithReturn.setWithRet(true);
//            }
//        }

        return graph;
    }

    private void addEdges(MethodGraph graph) {
        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        AbstractInsnNode instruction = instructionsIterator.next();
        MethodBlock block = graph.getMethodBlock(instruction);

        while (instructionsIterator.hasNext()) {
            instruction = instructionsIterator.next();
            MethodBlock possibleBlock = graph.getMethodBlock(instruction);

            if(possibleBlock != null) {
                AbstractInsnNode previousInstruction = instruction.getPrevious();

                // This can happen when the possible block is the target of a jump instruction
                if(previousInstruction.getType() != AbstractInsnNode.JUMP_INSN) {
                    if(!block.getSuccessors().contains(possibleBlock)) {
                        graph.addEdge(block, possibleBlock);
                    }
                }

                block = possibleBlock;
            }

            int instructionType = instruction.getType();

            if(instructionType != AbstractInsnNode.JUMP_INSN) {
                continue;
            }

            JumpInsnNode jumpInsn = (JumpInsnNode) instruction;
            MethodBlock destinationBlock = graph.getMethodBlock(jumpInsn.label);
            graph.addEdge(block, destinationBlock);

            if(jumpInsn.getOpcode() == Opcodes.GOTO) {
                continue;
            }

            if(jumpInsn.getOpcode() < Opcodes.LCMP || jumpInsn.getOpcode() > Opcodes.IF_ACMPNE) {
                throw new RuntimeException("New type of jump instruction");
            }

            AbstractInsnNode nextInstruction = instruction.getNext();
            destinationBlock = graph.getMethodBlock(nextInstruction);
            graph.addEdge(block, destinationBlock);
        }

    }

    private void getInstructions(MethodGraph graph) {
        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        AbstractInsnNode instruction = instructionsIterator.next();
        MethodBlock block = graph.getMethodBlock(instruction);
        List<AbstractInsnNode> blockInstructions = block.getInstructions();
        blockInstructions.add(instruction);

        while (instructionsIterator.hasNext()) {
            instruction = instructionsIterator.next();
            MethodBlock possibleBlock = graph.getMethodBlock(instruction);

            if(possibleBlock != null) {
                block = possibleBlock;
                blockInstructions = block.getInstructions();
            }

            blockInstructions.add(instruction);
        }

    }

    private void getBlocks(MethodGraph graph) {
        InsnList instructions = this.methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        AbstractInsnNode instruction = instructionsIterator.next();
        int instructionType = instruction.getType();

        if(instructionType != AbstractInsnNode.LABEL) {
            throw new RuntimeException();
        }

        MethodBlock block = new MethodBlock(instruction);

        while (instructionsIterator.hasNext()) {
            instruction = instructionsIterator.next();
            instructionType = instruction.getType();

            if(instructionType != AbstractInsnNode.JUMP_INSN) {
                continue;
            }

            graph.addMethodBlock(block);

            JumpInsnNode jumpInsn = (JumpInsnNode) instruction;
            block = new MethodBlock(jumpInsn.label);
            graph.addMethodBlock(block);

            AbstractInsnNode nextInstruction = instruction.getNext();
            block = new MethodBlock(nextInstruction);
            graph.addMethodBlock(block);
        }
    }

    private void connectToEnterNode(MethodGraph graph) {
        AbstractInsnNode instruction = this.methodNode.instructions.getFirst();

        if(instruction.getType() != AbstractInsnNode.LABEL) {
            throw new RuntimeException();
        }

        LabelNode labelNode = (LabelNode) instruction;
        MethodBlock firstBlock = graph.getMethodBlock(labelNode);
        graph.addEdge(graph.getEntryBlock(), firstBlock);
    }

    private static void connectToExitNode(MethodGraph graph) {
        for(MethodBlock methodBlock : graph.getBlocks()) {
            for(AbstractInsnNode instruction : methodBlock.getInstructions()) {
                int opcode = instruction.getOpcode();

                if(opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                    graph.addEdge(methodBlock, graph.getExitBlock());
                }
            }
        }
    }
}
