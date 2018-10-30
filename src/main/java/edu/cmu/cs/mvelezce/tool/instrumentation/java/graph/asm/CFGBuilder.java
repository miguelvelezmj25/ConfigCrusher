package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.analysis.Analyzer;
import jdk.internal.org.objectweb.asm.tree.analysis.AnalyzerException;
import jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter;
import jdk.internal.org.objectweb.asm.tree.analysis.BasicValue;
import jdk.internal.org.objectweb.asm.tree.analysis.Frame;

public class CFGBuilder {

  private final String owner;
  private final MethodNode methodNode;

  public CFGBuilder(String owner, MethodNode methodNode) {
    this.owner = owner;
    this.methodNode = methodNode;
  }

  public MethodGraph buildCFG() throws AnalyzerException {
    Analyzer<BasicValue> analyzer = this.getASMAnalyzer();
    Frame<BasicValue>[] frames = analyzer.getFrames();
    MethodGraph graph = this.addMethodBlocks();
    this.addEdges(graph, frames);

    return graph;
  }

  private Analyzer<BasicValue> getASMAnalyzer() throws AnalyzerException {
    Analyzer<BasicValue> analyzer = new BuildCFGAnalyzer();
    analyzer.analyze(this.owner, this.methodNode);

    return analyzer;
  }

  private void addEdges(MethodGraph graph, Frame<BasicValue>[] frames) {
    Map<CFGNode<BasicValue>, Integer> nodesToIndexes = this.cacheNodesToIndex(frames);
    InsnList insnList = this.methodNode.instructions;

    for (int i = 0; i < frames.length; i++) {
      CFGNode<BasicValue> cfgNode = (CFGNode<BasicValue>) frames[i];

      if (cfgNode == null) {
        continue;
      }

      Set<CFGNode<BasicValue>> succs = cfgNode.getSuccessors();

      if (succs == null || succs.isEmpty()) {
        continue;
      }

      AbstractInsnNode insn = insnList.get(i);
      MethodBlock block = graph.getMethodBlock(insn);

      for (CFGNode<BasicValue> succ : succs) {
        int succIndex = nodesToIndexes.get(succ);
        AbstractInsnNode succInsn = insnList.get(succIndex);
        MethodBlock succBlock = graph.getMethodBlock(succInsn);
        graph.addEdge(block, succBlock);
      }
    }

    this.connectToEntryBlock(graph, insnList);
    this.connectToExitBlock(graph);
  }

  private void connectToExitBlock(MethodGraph graph) {
    for (MethodBlock methodBlock : graph.getBlocks()) {
      for (AbstractInsnNode instruction : methodBlock.getInstructions()) {
        int opcode = instruction.getOpcode();

        if (opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
          methodBlock.setWithReturn(true);
          graph.addEdge(methodBlock, graph.getExitBlock());
        }
      }
    }
  }

  private void connectToEntryBlock(MethodGraph graph, InsnList insnList) {
    MethodBlock firstBlock = graph.getMethodBlock(insnList.getFirst());
    graph.addEdge(graph.getEntryBlock(), firstBlock);
  }

  private MethodGraph addMethodBlocks() {
    MethodGraph graph = new MethodGraph();
    Iterator<AbstractInsnNode> insnIter = this.methodNode.instructions.iterator();

    while (insnIter.hasNext()) {
      AbstractInsnNode in = insnIter.next();
      MethodBlock block = new MethodBlock(in);
      block.getInstructions().add(in);
      graph.addMethodBlock(block);
    }

    return graph;
  }

  private Map<CFGNode<BasicValue>, Integer> cacheNodesToIndex(Frame<BasicValue>[] frames) {
    Map<CFGNode<BasicValue>, Integer> nodesToIndexes = new HashMap<>();

    for (int i = 0; i < frames.length; i++) {
      CFGNode<BasicValue> cfgNode = (CFGNode<BasicValue>) frames[i];
      nodesToIndexes.put(cfgNode, i);
    }

    return nodesToIndexes;
  }

  private static class BuildCFGAnalyzer extends Analyzer<BasicValue> {

    BuildCFGAnalyzer() {
      super(new BasicInterpreter());
    }

    protected Frame<BasicValue> newFrame(int nLocals, int nStack) {
      return new CFGNode<>(nLocals, nStack);
    }

    protected Frame<BasicValue> newFrame(Frame<? extends BasicValue> src) {
      return new CFGNode<>(src);
    }

    protected void newControlFlowEdge(int src, int dst) {
      CFGNode<BasicValue> cfgNode = (CFGNode<BasicValue>) this.getFrames()[src];
      cfgNode.getSuccessors().add((CFGNode<BasicValue>) this.getFrames()[dst]);
    }
  }
}
