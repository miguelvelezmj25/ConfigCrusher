package edu.cmu.cs.mvelezce.instrument.region.utils.graphBuilder;

import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.builder.cfg.CFGBuilder;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.HashMap;
import java.util.Map;

public class MethodGraphBuilder {

  private static final Map<MethodNode, MethodGraph> METHOD_NODES_TO_GRAPHS = new HashMap<>();

  public static MethodGraph getMethodGraph(MethodNode methodNode, ClassNode classNode) {
    MethodGraph graph = METHOD_NODES_TO_GRAPHS.get(methodNode);

    if (graph == null) {
      graph = CFGBuilder.getCfg(methodNode, classNode);
      METHOD_NODES_TO_GRAPHS.put(methodNode, graph);
    }

    return graph;
  }

  public static Map<MethodNode, MethodGraph> getMethodNodesToGraphs() {
    return METHOD_NODES_TO_GRAPHS;
  }
}
