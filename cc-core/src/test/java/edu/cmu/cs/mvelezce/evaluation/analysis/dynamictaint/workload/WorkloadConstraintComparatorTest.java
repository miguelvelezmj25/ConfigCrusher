package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.workload;

import edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.ConstraintComparator;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import java.io.IOException;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class WorkloadConstraintComparatorTest {

  private static Set<ConfigConstraint> getMediumConstraints(ConstraintComparator comparator,
      String programName) throws IOException {
    String mediumFileName = "medium.json";

    return comparator.readFromFile(programName, mediumFileName);
  }

  private static Set<ConfigConstraint> getSmallConstraints(ConstraintComparator comparator,
      String programName) throws IOException {
    String mediumFileName = "small.json";

    return comparator.readFromFile(programName, mediumFileName);
  }

  @Test
  public void measureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    ConstraintComparator comparator = new WorkloadConstraintComparator(programName);

    Set<ConfigConstraint> mediumConstraints = getMediumConstraints(comparator, programName);
    Set<ConfigConstraint> smallConstraints = getSmallConstraints(comparator, programName);

    Assert.assertEquals(mediumConstraints, smallConstraints);
  }
}