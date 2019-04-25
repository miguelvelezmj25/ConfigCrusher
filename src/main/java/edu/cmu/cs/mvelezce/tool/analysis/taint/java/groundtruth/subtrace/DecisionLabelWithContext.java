package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

import com.beust.jcommander.internal.Nullable;
import java.util.Objects;
import java.util.UUID;

public class DecisionLabelWithContext extends DecisionLabel {

  private final UUID context;

  DecisionLabelWithContext(@Nullable UUID context, String decision) {
    super(decision);

    this.context = context;
  }

  public UUID getContext() {
    return context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    DecisionLabelWithContext that = (DecisionLabelWithContext) o;

    return Objects.equals(context, that.context);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (context != null ? context.hashCode() : 0);
    return result;
  }
}
