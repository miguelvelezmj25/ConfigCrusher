package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

import java.util.UUID;

public class SubtraceLabel extends DecisionLabel {

  private final UUID uuid;
  private final int execCount;

  public SubtraceLabel(String decision, int execCount) {
    super(decision);

    this.execCount = execCount;
    this.uuid = UUID.randomUUID();
  }



}
