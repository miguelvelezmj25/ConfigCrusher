package edu.cmu.cs.mvelezce.instrument.region.utils.comparator.edge;

import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;

import java.util.Comparator;

public final class EdgeComparator implements Comparator<Edge> {

  private static final EdgeComparator INSTANCE = new EdgeComparator();

  private EdgeComparator() {}

  public static EdgeComparator getInstance() {
    return INSTANCE;
  }

  @Override
  public int compare(Edge e1, Edge e2) {
    String sig1 = e1.src().getBytecodeSignature();
    String sig2 = e2.src().getBytecodeSignature();
    int compare1 = sig1.compareTo(sig2);

    if (compare1 != 0) {
      return compare1;
    }

    int tag1 = this.getBytecodeOffsetTag(e1);
    int tag2 = this.getBytecodeOffsetTag(e2);

    return Integer.compare(tag1, tag2);
  }

  private int getBytecodeOffsetTag(Edge edge) {
    for (Tag tag : edge.srcUnit().getTags()) {
      if (tag instanceof BytecodeOffsetTag) {
        return ((BytecodeOffsetTag) tag).getBytecodeOffset();
      }
    }

    throw new RuntimeException("Could not find a bytecode offset tag for this edge " + edge);
  }
}
