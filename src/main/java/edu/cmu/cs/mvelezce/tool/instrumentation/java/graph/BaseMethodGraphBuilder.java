package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import java.util.ListIterator;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

public abstract class BaseMethodGraphBuilder implements MethodGraphBuilder {

  private final MethodNode methodNode;
  private final MethodGraph graph;

  public BaseMethodGraphBuilder(MethodNode methodNode) {
    this.methodNode = methodNode;
    this.graph = new MethodGraph();
  }

  @Override
  public MethodGraph build() {
    if (this.methodNode.instructions.size() == 0) {
      return graph;
    }

    int instructionsInMethodNodeCount = this.getNumberOfInstructionsInMethodNode();

    this.getBlocks();
    this.addInstructions();

    int instructionsInGraphCount = this.getNumberOfInstructionsInGraph();

    if (instructionsInMethodNodeCount != instructionsInGraphCount) {
      throw new RuntimeException(
          "The number of instructions in the method does not match the total number of" +
              " instructions in the graph");
    }

    this.addEdges();
    this.connectEntryNode();
    this.connectExitNode();

//        System.out.println(this.graph.toDotString(this.methodNode.name));

    this.checkEntryAndExitBlocks();
    this.processTryCatchBlocks();

    return graph;
  }

  private void processTryCatchBlocks() {
    for (TryCatchBlockNode tryCatchBlockNode : this.methodNode.tryCatchBlocks) {
      AbstractInsnNode handler = tryCatchBlockNode.handler;
      MethodBlock block = this.graph.getMethodBlock(handler);

      if (block.getPredecessors().isEmpty()) {
        block.setCatchWithImplicitThrow(true);
      }
    }
  }

  private void checkEntryAndExitBlocks() {
    if (!this.graph.getEntryBlock().getPredecessors().isEmpty()) {
      throw new RuntimeException("The entry block has predecessors");
    }

    if (!this.graph.getExitBlock().getSuccessors().isEmpty()) {
      throw new RuntimeException("The exit block has successors");
    }

    Set<MethodBlock> exitPreds = graph.getExitBlock().getPredecessors();

    for (MethodBlock block : exitPreds) {
      if (!block.isWithReturn() && !this.graph.isWithWhileTrue()) {
        throw new RuntimeException("A block(" + block.getID()
            + ") connected to the exit block does not have a return instruction");
      }
    }
  }

  private int getNumberOfInstructionsInGraph() {
    int count = 0;

    for (MethodBlock block : this.graph.getBlocks()) {
      count += block.getInstructions().size();
    }

    return count;
  }

  private int getNumberOfInstructionsInMethodNode() {
    int count = 0;
    ListIterator<AbstractInsnNode> instructionIter = this.methodNode.instructions.iterator();

    while (instructionIter.hasNext()) {
      instructionIter.next();
      count++;
    }

    return count;
  }

  @Override
  public void connectEntryNode() {
    AbstractInsnNode instruction = this.methodNode.instructions.getFirst();

    if (instruction.getType() != AbstractInsnNode.LABEL) {
      throw new RuntimeException("The first instruction of the method node is not a label.");
    }

    LabelNode labelNode = (LabelNode) instruction;
    MethodBlock firstBlock = this.graph.getMethodBlock(labelNode);
    this.graph.addEdge(this.graph.getEntryBlock(), firstBlock);
  }

  @Override
  public void connectExitNode() {
    for (MethodBlock methodBlock : this.graph.getBlocks()) {
      for (AbstractInsnNode instruction : methodBlock.getInstructions()) {
        int opcode = instruction.getOpcode();

        if (opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
          methodBlock.setWithReturn(true);
          this.graph.addEdge(methodBlock, this.graph.getExitBlock());
        }
      }
    }

    // TODO do not hard code 3. This can happen if the method has a while(true) loop. Then there is no return
    if (this.getGraph().getBlockCount() == 3) {
      throw new UnsupportedOperationException(
          "There seems to be a special case for graphs with 3 blocks. Test it");
//      Set<MethodBlock> blocks = this.graph.getBlocks();
//      blocks.remove(this.graph.getEntryBlock());
//      blocks.remove(this.graph.getExitBlock());
//
//      this.graph.addEdge(blocks.iterator().next(), this.graph.getExitBlock());
//      this.graph.setWithWhileTrue(true);
    }
  }

  public MethodNode getMethodNode() {
    return this.methodNode;
  }

  public MethodGraph getGraph() {
    return this.graph;
  }
}
