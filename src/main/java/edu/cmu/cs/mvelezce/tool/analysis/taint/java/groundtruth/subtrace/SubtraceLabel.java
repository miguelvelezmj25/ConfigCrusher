package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

import com.beust.jcommander.internal.Nullable;
import com.google.common.base.Objects;

import java.util.UUID;

public class SubtraceLabel {

  // Helper field for IDing, not part of equals and hashcode
  private final UUID uuid = UUID.randomUUID();;
  private final UUID context;
  private final ControlFlowStatement controlFlowStatement;
  private final int execCount;

  SubtraceLabel(@Nullable UUID context, ControlFlowStatement controlFlowStatement, int execCount) {
    this.context = context;
    this.controlFlowStatement = controlFlowStatement;
    this.execCount = execCount;
  }

  UUID getUUID() {
    return uuid;
  }

  public ControlFlowStatement getControlFlowStatement() {
    return controlFlowStatement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SubtraceLabel that = (SubtraceLabel) o;
    return execCount == that.execCount
        && Objects.equal(context, that.context)
        && Objects.equal(controlFlowStatement, that.controlFlowStatement);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(context, controlFlowStatement, execCount);
  }
}
