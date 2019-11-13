package edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot;

import java.util.List;

public interface JProfilerSnapshotEntry {

  boolean getLeaf();

  String getMethodName();

  String getMethodSignature();

  long getTime();

  int getCount();

  int getLineNumber();

  double getPercent();

  List<Node> getNodes();

  String getClassName();
}
