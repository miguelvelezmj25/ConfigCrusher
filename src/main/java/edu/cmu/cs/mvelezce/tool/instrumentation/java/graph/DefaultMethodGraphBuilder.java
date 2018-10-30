package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import java.util.List;
import java.util.ListIterator;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.LookupSwitchInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.TableSwitchInsnNode;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * Created by mvelezce on 5/3/17.
 */
public class DefaultMethodGraphBuilder extends BaseMethodGraphBuilder {

  @Override
  public void addEdges(MethodGraph graph, MethodNode methodNode) {
    InsnList instructions = methodNode.instructions;
    ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

    AbstractInsnNode instruction = instructionsIterator.next();
    MethodBlock block = graph.getMethodBlock(instruction);

    while (instructionsIterator.hasNext()) {
      instruction = instructionsIterator.next();
      MethodBlock possibleBlock = graph.getMethodBlock(instruction);

      if (possibleBlock != null) {
        AbstractInsnNode previousInstruction = instruction.getPrevious();

        // This can happen when the possible block is the target of a jump instruction
        if (previousInstruction.getType() != AbstractInsnNode.JUMP_INSN) {
          // Check if it is a return statement
          int opcode = previousInstruction.getOpcode();

          if (opcode != Opcodes.RET && (opcode < Opcodes.IRETURN || opcode > Opcodes.RETURN)
              && opcode != Opcodes.ATHROW) {
            if (!block.getSuccessors().contains(possibleBlock)) {
              graph.addEdge(block, possibleBlock);
            }
          }
        }

        block = possibleBlock;
      }

      int type = instruction.getType();
      int opcode = instruction.getOpcode();

      if (type != AbstractInsnNode.JUMP_INSN && type != AbstractInsnNode.LOOKUPSWITCH_INSN
          && type != AbstractInsnNode.TABLESWITCH_INSN && opcode != Opcodes.ATHROW) {
        continue;
      }

      if (type == AbstractInsnNode.JUMP_INSN) {
        JumpInsnNode jumpInsn = (JumpInsnNode) instruction;
        MethodBlock destinationBlock = graph.getMethodBlock(jumpInsn.label);
        graph.addEdge(block, destinationBlock);

        if (opcode == Opcodes.GOTO) {
          continue;
        }

        if (opcode < Opcodes.LCMP || opcode > Opcodes.IF_ACMPNE) {
          if (opcode != Opcodes.IFNULL && opcode != Opcodes.IFNONNULL) {
            throw new RuntimeException("New type of jump instruction");
          }
        }

        AbstractInsnNode nextInstruction = instruction.getNext();

        if (nextInstruction == null) {
          throw new RuntimeException("How to handle this?");
        }

        destinationBlock = graph.getMethodBlock(nextInstruction);
        graph.addEdge(block, destinationBlock);
      }
      else if (opcode == Opcodes.ATHROW) {
        for (TryCatchBlockNode tryCatchBlock : methodNode.tryCatchBlocks) {
          AbstractInsnNode insnNode = tryCatchBlock.start;
          AbstractInsnNode endNode = tryCatchBlock.end;

          while (insnNode != endNode && insnNode.getNext() != endNode) {
            insnNode = insnNode.getNext();

            if (insnNode == instruction) {
              MethodBlock destinationBlock = graph.getMethodBlock(tryCatchBlock.handler);

              if (destinationBlock == null) {
                throw new RuntimeException(
                    "Do not have a node for the handler of a try catch block in "
                        + methodNode.name);
              }

              graph.addEdge(block, destinationBlock);
            }
          }
        }
      }
      else {
        if (type == AbstractInsnNode.TABLESWITCH_INSN) {
          TableSwitchInsnNode tableSwitchInsn = (TableSwitchInsnNode) instruction;
          MethodBlock destinationBlock = graph.getMethodBlock(tableSwitchInsn.dflt);
          graph.addEdge(block, destinationBlock);

          for (LabelNode labelNode : tableSwitchInsn.labels) {
            destinationBlock = graph.getMethodBlock(labelNode);
            graph.addEdge(block, destinationBlock);
          }
        }
        else {
          LookupSwitchInsnNode lookupSwitchInsn = (LookupSwitchInsnNode) instruction;
          MethodBlock destinationBlock = graph.getMethodBlock(lookupSwitchInsn.dflt);
          graph.addEdge(block, destinationBlock);

          for (LabelNode labelNode : lookupSwitchInsn.labels) {
            destinationBlock = graph.getMethodBlock(labelNode);
            graph.addEdge(block, destinationBlock);
          }
        }
      }
    }
  }

  @Override
  public void addInstructions(MethodGraph graph, MethodNode methodNode) {
    InsnList instructions = methodNode.instructions;
    ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

    AbstractInsnNode instruction = instructionsIterator.next();
    MethodBlock block = graph.getMethodBlock(instruction);
    List<AbstractInsnNode> blockInstructions = block.getInstructions();
    blockInstructions.add(instruction);

    while (instructionsIterator.hasNext()) {
      instruction = instructionsIterator.next();
      MethodBlock possibleBlock = graph.getMethodBlock(instruction);

      if (possibleBlock != null) {
        block = possibleBlock;
        blockInstructions = block.getInstructions();
      }

      blockInstructions.add(instruction);
    }

    // Check if the method does not have a return statement
    instructionsIterator = instructions.iterator();
    boolean hasReturn = false;

    while (instructionsIterator.hasNext()) {
      instruction = instructionsIterator.next();
      int opcode = instruction.getOpcode();

      if (opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
        hasReturn = true;
        break;
      }
    }

    if (hasReturn) {
      return;
    }

    // TODO do not hard code 3. This can happen if the method has a while(true) loop. Then there is no return
    if (graph.getBlockCount() == 3) {
      return;
    }

    AbstractInsnNode lastInstruction = instructions.getLast();

    if (lastInstruction.getType() == AbstractInsnNode.LABEL) {
      lastInstruction = lastInstruction.getPrevious();
    }

    int lastInstOpcode = lastInstruction.getOpcode();

    if (lastInstOpcode != Opcodes.ATHROW) {
      throw new RuntimeException(
          "This method does not have a return statement and the last instruction is not" +
              " a throw instruction");
    }

    MethodBlock possibleBlock = graph.getMethodBlock(lastInstruction);

    while (possibleBlock == null) {
      lastInstruction = lastInstruction.getPrevious();
      possibleBlock = graph.getMethodBlock(lastInstruction);
    }

    possibleBlock.setWithReturn(true);
    graph.addEdge(possibleBlock, graph.getExitBlock());
  }

  @Override
  public void buildBlocks(MethodGraph graph, MethodNode methodNode) {
    InsnList instructions = methodNode.instructions;
    ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

    AbstractInsnNode instruction = instructionsIterator.next();

    if (instruction.getType() != AbstractInsnNode.LABEL) {
      throw new RuntimeException();
    }

    MethodBlock block = new MethodBlock(instruction);
    graph.addMethodBlock(block);

    while (instructionsIterator.hasNext()) {
      instruction = instructionsIterator.next();
      int type = instruction.getType();
      int opcode = instruction.getOpcode();

      if (type != AbstractInsnNode.JUMP_INSN && type != AbstractInsnNode.LOOKUPSWITCH_INSN
          && type != AbstractInsnNode.TABLESWITCH_INSN && opcode != Opcodes.ATHROW
          && opcode != Opcodes.RET && (opcode < Opcodes.IRETURN || opcode > Opcodes.RETURN)) {
        continue;
      }

      if (type == AbstractInsnNode.JUMP_INSN) {
        JumpInsnNode jumpInsn = (JumpInsnNode) instruction;
        block = graph.getMethodBlock(jumpInsn.label);

        if (block == null) {
          block = new MethodBlock(jumpInsn.label);
          graph.addMethodBlock(block);
        }

        AbstractInsnNode nextInstruction = instruction.getNext();

        if (nextInstruction == null) {
          continue;
        }

        block = graph.getMethodBlock(nextInstruction);

        if (block == null) {
          block = new MethodBlock(nextInstruction);
          graph.addMethodBlock(block);
        }
      }
      else if (opcode == Opcodes.ATHROW) {
        for (TryCatchBlockNode tryCatchBlock : methodNode.tryCatchBlocks) {
          AbstractInsnNode insnNode = tryCatchBlock.start;
          AbstractInsnNode endNode = tryCatchBlock.end;

          while (insnNode != endNode && insnNode.getNext() != endNode) {
            insnNode = insnNode.getNext();

            if (insnNode == instruction) {
              block = new MethodBlock(tryCatchBlock.handler);
              graph.addMethodBlock(block);
            }
          }
        }
      }
      else if (opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
        AbstractInsnNode nextInstruction = instruction.getNext();

        // This is the last instruction of the method
        if (nextInstruction == null) {
          continue;
        }

        block = graph.getMethodBlock(nextInstruction);

        // This is a return in the middle of the body and there is a block that was already created for the next
        // instruction
        if (block != null) {
          continue;
        }

        // This is a return at the end of the method
        if (nextInstruction.getNext() == null) {
          continue;
        }

        block = new MethodBlock(nextInstruction);
        graph.addMethodBlock(block);
      }
      else {
        if (type == AbstractInsnNode.TABLESWITCH_INSN) {
          TableSwitchInsnNode tableSwitchInsn = (TableSwitchInsnNode) instruction;
          block = graph.getMethodBlock(tableSwitchInsn.dflt);

          if (block == null) {
            block = new MethodBlock(tableSwitchInsn.dflt);
            graph.addMethodBlock(block);
          }

          for (LabelNode labelNode : tableSwitchInsn.labels) {
            block = graph.getMethodBlock(labelNode);

            if (block == null) {
              block = new MethodBlock(labelNode);
              graph.addMethodBlock(block);
            }
          }
        }
        else {
          LookupSwitchInsnNode lookupSwitchInsn = (LookupSwitchInsnNode) instruction;
          block = graph.getMethodBlock(lookupSwitchInsn.dflt);

          if (block == null) {
            block = new MethodBlock(lookupSwitchInsn.dflt);
            graph.addMethodBlock(block);
          }

          for (LabelNode labelNode : lookupSwitchInsn.labels) {
            block = graph.getMethodBlock(labelNode);

            if (block == null) {
              block = new MethodBlock(labelNode);
              graph.addMethodBlock(block);
            }
          }
        }

        if (instruction.getNext().getType() != AbstractInsnNode.LABEL) {
          throw new RuntimeException("New case");
        }
      }
    }

    for (TryCatchBlockNode tryCatchBlockNode : methodNode.tryCatchBlocks) {
      block = graph.getMethodBlock(tryCatchBlockNode.handler);

      if (block != null) {
        continue;
      }

      block = new MethodBlock(tryCatchBlockNode.handler);
      graph.addMethodBlock(block);
    }
  }

}
