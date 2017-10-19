package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.Set;

public abstract class BaseMethodGraphBuilder implements MethodGraphBuilder {

    private MethodNode methodNode;
    private MethodGraph graph;

    public BaseMethodGraphBuilder(MethodNode methodNode) {
        this.methodNode = methodNode;
        this.graph = new MethodGraph();
    }

    @Override
    public MethodGraph build() {
        this.getBlocks();
        this.addInstructions();
        this.addEdges();
        this.connectEntryNode();
        this.connectExitNode();

        System.out.println(this.graph.toDotString(this.methodNode.name));

        if(!graph.getEntryBlock().getPredecessors().isEmpty()) {
            throw new RuntimeException("The entry block has predecessors");
        }

        if(!graph.getExitBlock().getSuccessors().isEmpty()) {
            throw new RuntimeException("The exit block has successors");
        }

        Set<MethodBlock> exitPreds = graph.getExitBlock().getPredecessors();

        for(MethodBlock block : exitPreds) {
            if(!block.isWithReturn()) {
                throw new RuntimeException("A block(" + block.getID() + ") connected to the exit block does not have a return instruction");
            }
        }

        return graph;
    }

    @Override
    public void connectEntryNode() {
        AbstractInsnNode instruction = this.methodNode.instructions.getFirst();

        if(instruction.getType() != AbstractInsnNode.LABEL) {
            throw new RuntimeException();
        }

        LabelNode labelNode = (LabelNode) instruction;
        MethodBlock firstBlock = this.graph.getMethodBlock(labelNode);
        this.graph.addEdge(this.graph.getEntryBlock(), firstBlock);

        if(this.graph.getEntryBlock().getSuccessors().isEmpty()) {
            throw new RuntimeException("The exit node does not have predecessors");
        }
    }

    @Override
    public void connectExitNode() {
        for(MethodBlock methodBlock : this.graph.getBlocks()) {
            for(AbstractInsnNode instruction : methodBlock.getInstructions()) {
                int opcode = instruction.getOpcode();

                if(opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                    methodBlock.setWithReturn(true);
                    this.graph.addEdge(methodBlock, this.graph.getExitBlock());
                }
            }
        }

        if(this.graph.getExitBlock().getPredecessors().isEmpty()) {
            throw new RuntimeException("The exit node does not have predecessors");
        }
    }

    public MethodNode getMethodNode() {
        return this.methodNode;
    }

    public MethodGraph getGraph() {
        return this.graph;
    }
}
