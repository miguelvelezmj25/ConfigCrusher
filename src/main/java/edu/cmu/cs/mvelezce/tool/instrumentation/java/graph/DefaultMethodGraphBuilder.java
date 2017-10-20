package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by mvelezce on 5/3/17.
 */
public class DefaultMethodGraphBuilder extends BaseMethodGraphBuilder {

    public DefaultMethodGraphBuilder(MethodNode methodNode) {
        super(methodNode);
    }

    @Override
    public void addEdges() {
        InsnList instructions = this.getMethodNode().instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        AbstractInsnNode instruction = instructionsIterator.next();
        MethodBlock block = this.getGraph().getMethodBlock(instruction);

        while(instructionsIterator.hasNext()) {
            instruction = instructionsIterator.next();
            MethodBlock possibleBlock = this.getGraph().getMethodBlock(instruction);

            if(possibleBlock != null) {
                AbstractInsnNode previousInstruction = instruction.getPrevious();

                // This can happen when the possible block is the target of a jump instruction
                if(previousInstruction.getType() != AbstractInsnNode.JUMP_INSN) {
                    // Check if it is a return statement
                    int opcode = previousInstruction.getOpcode();

                    if(opcode != Opcodes.RET && (opcode < Opcodes.IRETURN || opcode > Opcodes.RETURN)
                            && opcode != Opcodes.ATHROW) {
                        if(!block.getSuccessors().contains(possibleBlock)) {
                            this.getGraph().addEdge(block, possibleBlock);
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
                MethodBlock destinationBlock = this.getGraph().getMethodBlock(jumpInsn.label);
                this.getGraph().addEdge(block, destinationBlock);

                if(opcode == Opcodes.GOTO) {
                    continue;
                }


                if(opcode < Opcodes.LCMP || opcode > Opcodes.IF_ACMPNE) {
                    if(opcode != Opcodes.IFNULL && opcode != Opcodes.IFNONNULL) {
                        throw new RuntimeException("New type of jump instruction");
                    }
                }


                AbstractInsnNode nextInstruction = instruction.getNext();
                destinationBlock = this.getGraph().getMethodBlock(nextInstruction);
                this.getGraph().addEdge(block, destinationBlock);
            }
            else if(opcode == Opcodes.ATHROW) {
                for(TryCatchBlockNode tryCatchBlock : this.getMethodNode().tryCatchBlocks) {
                    AbstractInsnNode insnNode = tryCatchBlock.start;
                    AbstractInsnNode endNode = tryCatchBlock.end;

                    while(insnNode != endNode && insnNode.getNext() != endNode) {
                        insnNode = insnNode.getNext();

                        if(insnNode == instruction) {
                            MethodBlock destinationBlock = this.getGraph().getMethodBlock(tryCatchBlock.handler);

                            if(destinationBlock == null) {
                                throw new RuntimeException("Do not have a node for the handler of a try catch block in "
                                        + this.getMethodNode().name);
                            }

                            this.getGraph().addEdge(block, destinationBlock);
                        }
                    }
                }
            }
            else {
                if(type == AbstractInsnNode.TABLESWITCH_INSN) {
                    TableSwitchInsnNode tableSwitchInsn = (TableSwitchInsnNode) instruction;
                    MethodBlock destinationBlock = this.getGraph().getMethodBlock(tableSwitchInsn.dflt);
                    this.getGraph().addEdge(block, destinationBlock);

                    for(LabelNode labelNode : tableSwitchInsn.labels) {
                        destinationBlock = this.getGraph().getMethodBlock(labelNode);
                        this.getGraph().addEdge(block, destinationBlock);
                    }
                }
                else {
                    LookupSwitchInsnNode lookupSwitchInsn = (LookupSwitchInsnNode) instruction;
                    MethodBlock destinationBlock = this.getGraph().getMethodBlock(lookupSwitchInsn.dflt);
                    this.getGraph().addEdge(block, destinationBlock);

                    for(LabelNode labelNode : lookupSwitchInsn.labels) {
                        destinationBlock = this.getGraph().getMethodBlock(labelNode);
                        this.getGraph().addEdge(block, destinationBlock);
                    }
                }
            }
        }
    }

    @Override
    public void addInstructions() {
        InsnList instructions = this.getMethodNode().instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        AbstractInsnNode instruction = instructionsIterator.next();
        MethodBlock block = this.getGraph().getMethodBlock(instruction);
        List<AbstractInsnNode> blockInstructions = block.getInstructions();
        blockInstructions.add(instruction);

        while(instructionsIterator.hasNext()) {
            instruction = instructionsIterator.next();
            MethodBlock possibleBlock = this.getGraph().getMethodBlock(instruction);

            if(possibleBlock != null) {
                block = possibleBlock;
                blockInstructions = block.getInstructions();
            }

            blockInstructions.add(instruction);
        }

        // Check if the method does not have a return statement
        instructionsIterator = instructions.iterator();
        boolean hasReturn = false;

        while(instructionsIterator.hasNext()) {
            instruction = instructionsIterator.next();
            int opcode = instruction.getOpcode();

            if(opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                hasReturn = true;
                break;
            }
        }

        if(hasReturn) {
            return;
        }

        AbstractInsnNode lastInstruction = instructions.getLast();

        if(lastInstruction.getType() == AbstractInsnNode.LABEL) {
            lastInstruction = lastInstruction.getPrevious();
        }

        int lastInstOpcode = lastInstruction.getOpcode();

        if(lastInstOpcode != Opcodes.ATHROW) {
            throw new RuntimeException("This method does not have a return statement and the last instruction is not" +
                    " a throw instruction");
        }

        MethodBlock possibleBlock = this.getGraph().getMethodBlock(lastInstruction);

        while(possibleBlock == null) {
            lastInstruction = lastInstruction.getPrevious();
            possibleBlock = this.getGraph().getMethodBlock(lastInstruction);
        }

        possibleBlock.setWithReturn(true);
        this.getGraph().addEdge(possibleBlock, this.getGraph().getExitBlock());
    }

    @Override
    public void getBlocks() {
        InsnList instructions = this.getMethodNode().instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        AbstractInsnNode instruction = instructionsIterator.next();

        if(instruction.getType() != AbstractInsnNode.LABEL) {
            throw new RuntimeException();
        }

        MethodBlock block = new MethodBlock(instruction);
        this.getGraph().addMethodBlock(block);

        while(instructionsIterator.hasNext()) {
            instruction = instructionsIterator.next();
            int type = instruction.getType();
            int opcode = instruction.getOpcode();

            if(type != AbstractInsnNode.JUMP_INSN && type != AbstractInsnNode.LOOKUPSWITCH_INSN
                    && type != AbstractInsnNode.TABLESWITCH_INSN && opcode != Opcodes.ATHROW
                    && opcode != Opcodes.RET && (opcode < Opcodes.IRETURN || opcode > Opcodes.RETURN)) {
                continue;
            }

            if(type == AbstractInsnNode.JUMP_INSN) {
                JumpInsnNode jumpInsn = (JumpInsnNode) instruction;
                block = this.getGraph().getMethodBlock(jumpInsn.label);

                if(block == null) {
                    block = new MethodBlock(jumpInsn.label);
                    this.getGraph().addMethodBlock(block);
                }

                AbstractInsnNode nextInstruction = instruction.getNext();
                block = this.getGraph().getMethodBlock(nextInstruction);

                if(block == null) {
                    block = new MethodBlock(nextInstruction);
                    this.getGraph().addMethodBlock(block);
                }
            }
            else if(opcode == Opcodes.ATHROW) {
                for(TryCatchBlockNode tryCatchBlock : this.getMethodNode().tryCatchBlocks) {
                    AbstractInsnNode insnNode = tryCatchBlock.start;
                    AbstractInsnNode endNode = tryCatchBlock.end;

                    while(insnNode != endNode && insnNode.getNext() != endNode) {
                        insnNode = insnNode.getNext();

                        if(insnNode == instruction) {
                            block = new MethodBlock(tryCatchBlock.handler);
                            this.getGraph().addMethodBlock(block);
                        }
                    }
                }
            }
            else if(opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                AbstractInsnNode nextInstruction = instruction.getNext();

                // This is the last instruction of the method
                if(nextInstruction == null) {
                    continue;
                }

                block = this.getGraph().getMethodBlock(nextInstruction);

                // This is a return in the middle of the body and there is a block that was already created for the next
                // instruction
                if(block != null) {
                    continue;
                }

                // This is a return at the end of the method
                if(nextInstruction.getNext() == null) {
                    continue;
                }

                block = new MethodBlock(nextInstruction);
                this.getGraph().addMethodBlock(block);
            }
            else {
                if(type == AbstractInsnNode.TABLESWITCH_INSN) {
                    TableSwitchInsnNode tableSwitchInsn = (TableSwitchInsnNode) instruction;
                    block = this.getGraph().getMethodBlock(tableSwitchInsn.dflt);

                    if(block == null) {
                        block = new MethodBlock(tableSwitchInsn.dflt);
                        this.getGraph().addMethodBlock(block);
                    }

                    for(LabelNode labelNode : tableSwitchInsn.labels) {
                        block = this.getGraph().getMethodBlock(labelNode);

                        if(block == null) {
                            block = new MethodBlock(labelNode);
                            this.getGraph().addMethodBlock(block);
                        }
                    }
                }
                else {
                    LookupSwitchInsnNode lookupSwitchInsn = (LookupSwitchInsnNode) instruction;
                    block = this.getGraph().getMethodBlock(lookupSwitchInsn.dflt);

                    if(block == null) {
                        block = new MethodBlock(lookupSwitchInsn.dflt);
                        this.getGraph().addMethodBlock(block);
                    }

                    for(LabelNode labelNode : lookupSwitchInsn.labels) {
                        block = this.getGraph().getMethodBlock(labelNode);

                        if(block == null) {
                            block = new MethodBlock(labelNode);
                            this.getGraph().addMethodBlock(block);
                        }
                    }
                }

                if(instruction.getNext().getType() != AbstractInsnNode.LABEL) {
                    throw new RuntimeException("New case");
                }
            }
        }
    }

}
