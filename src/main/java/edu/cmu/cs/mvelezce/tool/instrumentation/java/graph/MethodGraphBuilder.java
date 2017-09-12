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
                    // Check if it is a return statement
                    int opcode = previousInstruction.getOpcode();

                    if(opcode != Opcodes.RET && (opcode < Opcodes.IRETURN || opcode > Opcodes.RETURN)
                            && opcode != Opcodes.ATHROW) {
                        if(!block.getSuccessors().contains(possibleBlock)) {
                            graph.addEdge(block, possibleBlock);
                        }
                    }
                }

                block = possibleBlock;
            }

            int type = instruction.getType();
            int opcode = instruction.getOpcode();

            if(type != AbstractInsnNode.JUMP_INSN && type != AbstractInsnNode.LOOKUPSWITCH_INSN
                    && type != AbstractInsnNode.TABLESWITCH_INSN && opcode != Opcodes.ATHROW) {
                continue;
            }

            if(type == AbstractInsnNode.JUMP_INSN) {
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
            else if(opcode == Opcodes.ATHROW) {
                for(TryCatchBlockNode tryCatchBlock : methodNode.tryCatchBlocks) {
                    AbstractInsnNode insnNode = tryCatchBlock.start;

                    while(insnNode.getNext().getType() != AbstractInsnNode.LABEL) {
                        insnNode = insnNode.getNext();

                        if(insnNode == instruction) {
                            MethodBlock destinationBlock = graph.getMethodBlock(tryCatchBlock.handler);
                            graph.addEdge(block, destinationBlock);
                        }
                    }
                }

            }
            else {
                if(type == AbstractInsnNode.TABLESWITCH_INSN) {
                    TableSwitchInsnNode tableSwitchInsn = (TableSwitchInsnNode) instruction;
                    MethodBlock destinationBlock = graph.getMethodBlock(tableSwitchInsn.dflt);
                    graph.addEdge(block, destinationBlock);

                    for(LabelNode labelNode : tableSwitchInsn.labels) {
                        destinationBlock = graph.getMethodBlock(labelNode);
                        graph.addEdge(block, destinationBlock);
                    }
                }
                else {
                    LookupSwitchInsnNode lookupSwitchInsn = (LookupSwitchInsnNode) instruction;
                    MethodBlock destinationBlock = graph.getMethodBlock(lookupSwitchInsn.dflt);
                    graph.addEdge(block, destinationBlock);

                    for(LabelNode labelNode : lookupSwitchInsn.labels) {
                        destinationBlock = graph.getMethodBlock(labelNode);
                        graph.addEdge(block, destinationBlock);
                    }
                }
            }
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

        if(instruction.getType() != AbstractInsnNode.LABEL) {
            throw new RuntimeException();
        }

        MethodBlock block = new MethodBlock(instruction);
        graph.addMethodBlock(block);

        while (instructionsIterator.hasNext()) {
            instruction = instructionsIterator.next();
            int type = instruction.getType();
            int opcode = instruction.getOpcode();

            if(type != AbstractInsnNode.JUMP_INSN && type != AbstractInsnNode.LOOKUPSWITCH_INSN
                    && type != AbstractInsnNode.TABLESWITCH_INSN && opcode != Opcodes.ATHROW) {
                continue;
            }

            if(type == AbstractInsnNode.JUMP_INSN) {
                JumpInsnNode jumpInsn = (JumpInsnNode) instruction;
                block = graph.getMethodBlock(jumpInsn.label);

                if(block == null) {
                    block = new MethodBlock(jumpInsn.label);
                    graph.addMethodBlock(block);
                }

                AbstractInsnNode nextInstruction = instruction.getNext();
                block = graph.getMethodBlock(nextInstruction);

                if(block == null) {
                    block = new MethodBlock(nextInstruction);
                    graph.addMethodBlock(block);
                }
            }
            else if(opcode == Opcodes.ATHROW) {
                for(TryCatchBlockNode tryCatchBlock : methodNode.tryCatchBlocks) {
                    AbstractInsnNode insnNode = tryCatchBlock.start;

                    while(insnNode.getNext().getType() != AbstractInsnNode.LABEL) {
                        insnNode = insnNode.getNext();

                        if(insnNode == instruction) {
                            block = new MethodBlock(tryCatchBlock.handler);
                            graph.addMethodBlock(block);
                        }
                    }
                }
            }
            else {
                if(type == AbstractInsnNode.TABLESWITCH_INSN) {
                    TableSwitchInsnNode tableSwitchInsn = (TableSwitchInsnNode) instruction;
                    block = graph.getMethodBlock(tableSwitchInsn.dflt);

                    if(block == null) {
                        block = new MethodBlock(tableSwitchInsn.dflt);
                        graph.addMethodBlock(block);
                    }

                    for(LabelNode labelNode : tableSwitchInsn.labels) {
                        block = graph.getMethodBlock(labelNode);

                        if(block == null) {
                            block = new MethodBlock(labelNode);
                            graph.addMethodBlock(block);
                        }
                    }
                }
                else {
                    LookupSwitchInsnNode lookupSwitchInsn = (LookupSwitchInsnNode) instruction;
                    block = graph.getMethodBlock(lookupSwitchInsn.dflt);

                    if(block == null) {
                        block = new MethodBlock(lookupSwitchInsn.dflt);
                        graph.addMethodBlock(block);
                    }

                    for(LabelNode labelNode : lookupSwitchInsn.labels) {
                        block = graph.getMethodBlock(labelNode);

                        if(block == null) {
                            block = new MethodBlock(labelNode);
                            graph.addMethodBlock(block);
                        }
                    }
                }

                if(instruction.getNext().getType() != AbstractInsnNode.LABEL) {
                    throw new RuntimeException("New case");
                }
            }
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
