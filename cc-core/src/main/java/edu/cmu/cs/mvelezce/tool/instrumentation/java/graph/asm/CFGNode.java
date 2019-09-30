package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm;

import java.util.HashSet;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.analysis.Frame;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

public class CFGNode<V extends Value> extends Frame<V> {

  private final Set<CFGNode<V>> successors = new HashSet<>();
  private final Set<CFGNode<V>> predecessors = new HashSet<>();

  CFGNode(int nLocals, int nStack) {
    super(nLocals, nStack);
  }

  CFGNode(Frame<? extends V> src) {
    super(src);
  }

  public Set<CFGNode<V>> getSuccessors() {
    return successors;
  }

  public Set<CFGNode<V>> getPredecessors() {
    return predecessors;
  }
}
