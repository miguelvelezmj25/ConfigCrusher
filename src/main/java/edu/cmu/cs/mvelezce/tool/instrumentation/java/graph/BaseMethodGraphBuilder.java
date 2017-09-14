package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public abstract class BaseMethodGraphBuilder implements IMethodGraphBuilder {

    private MethodNode methodNode;

    public BaseMethodGraphBuilder(MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    @Override
    public MethodGraph build() {
        MethodGraph graph = new MethodGraph();

        this.getBlocks(graph);
        this.addInstructions(graph);
        this.addEdges(graph);
        this.connectEntryNode(graph);
        this.connectExitNode(graph);

        return graph;
    }

    @Override
    public void connectEntryNode(MethodGraph graph) {
        AbstractInsnNode instruction = this.methodNode.instructions.getFirst();

        if(instruction.getType() != AbstractInsnNode.LABEL) {
            throw new RuntimeException();
        }

        LabelNode labelNode = (LabelNode) instruction;
        MethodBlock firstBlock = graph.getMethodBlock(labelNode);
        graph.addEdge(graph.getEntryBlock(), firstBlock);
    }

    @Override
    public void connectExitNode(MethodGraph graph) {
        for(MethodBlock methodBlock : graph.getBlocks()) {
            for(AbstractInsnNode instruction : methodBlock.getInstructions()) {
                int opcode = instruction.getOpcode();

                if(opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                    graph.addEdge(methodBlock, graph.getExitBlock());
                }
            }
        }
    }

    public MethodNode getMethodNode() { return this.methodNode; }
}
