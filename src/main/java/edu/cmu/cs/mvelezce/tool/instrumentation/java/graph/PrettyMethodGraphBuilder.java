package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.util.Printer;

import java.util.List;

/**
 * Created by mvelezce on 5/3/17.
 */
public class PrettyMethodGraphBuilder extends BaseMethodGraphBuilder {

    private MethodGraph graph;
    private Printer printer;

    public PrettyMethodGraphBuilder(MethodNode methodNode, Printer printer) {
        super(methodNode);

        MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
        this.graph = builder.build();
        this.printer = printer;
    }

    @Override
    public PrettyMethodGraph build() {
        PrettyMethodGraph graph = new PrettyMethodGraph();

        this.getBlocks(graph);
        this.addInstructions(graph);
        this.addEdges(graph);
        this.connectEntryNode(graph);
        this.connectExitNode(graph);

        return graph;
    }

    @Override
    public void addEdges(MethodGraph prettyGraph) {
        for(MethodBlock block : this.graph.getBlocks()) {
            if(block == this.graph.getEntryBlock() || block == this.graph.getExitBlock()) {
                continue;
            }

            MethodBlock prettyBlock = prettyGraph.getMethodBlock(block.getID());

            for(MethodBlock succ : block.getSuccessors()) {
                MethodBlock prettySucc = prettyGraph.getMethodBlock(succ.getID());
                prettyGraph.addEdge(prettyBlock, prettySucc);
            }
        }
    }

    @Override
    public void getBlocks(MethodGraph prettyGraph) {
        for(MethodBlock block : this.graph.getBlocks()) {
            if(block == this.graph.getEntryBlock() || block == this.graph.getExitBlock()) {
                continue;
            }

            PrettyMethodBlock prettyBlock = new PrettyMethodBlock(block.getID());
            prettyGraph.addMethodBlock(prettyBlock);
        }
    }

    @Override
    public void addInstructions(MethodGraph prettyGraph) {
        for(MethodBlock block : this.graph.getBlocks()) {
            if(block == this.graph.getEntryBlock() || block == this.graph.getExitBlock()) {
                continue;
            }

            List<AbstractInsnNode> blockInstructions = block.getInstructions();
            InsnList methodInstructions = this.getMethodNode().instructions;
            int startIndex = methodInstructions.indexOf(blockInstructions.get(0));
            // Exclusive
            int endIndex = startIndex + blockInstructions.size();

            PrettyMethodBlock prettyBlock = (PrettyMethodBlock) prettyGraph.getMethodBlock(block.getID());
            List<String> prettyInstructions = prettyBlock.getPrettyInstructions();
            List<Object> printerInstructions = this.printer.getText();

            for(Object o : printerInstructions) {
                System.out.println(o.toString().trim());
            }

            for(int i = startIndex; i < endIndex; i++) {
                prettyInstructions.add(printerInstructions.get(i).toString().trim());
            }

        }
    }

    @Override
    public void connectEntryNode(MethodGraph prettyGraph) {
        MethodBlock entry = this.graph.getEntryBlock();
        MethodBlock prettyEntry = prettyGraph.getEntryBlock();

        for(MethodBlock succ : entry.getSuccessors()) {
            MethodBlock prettyBlock = prettyGraph.getMethodBlock(succ.getID());
            prettyGraph.addEdge(prettyEntry, prettyBlock);
        }
    }

    @Override
    public void connectExitNode(MethodGraph prettyGraph) {
        MethodBlock exit = this.graph.getExitBlock();
        MethodBlock prettyExit = prettyGraph.getExitBlock();

        for(MethodBlock pred : exit.getPredecessors()) {
            MethodBlock prettyBlock = prettyGraph.getMethodBlock(pred.getID());
            prettyGraph.addEdge(prettyBlock, prettyExit);
        }
    }
}
