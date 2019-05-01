package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.workload;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import java.io.IOException;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class ConstraintComparatorTest {

  private static Set<ConfigConstraint> getMediumConstraints(String programName) throws IOException {
    String mediumFileName = "medium.json";

    return ConstraintComparator.readFromFile(programName, mediumFileName);
  }

  private static Set<ConfigConstraint> getSmallConstraints(String programName) throws IOException {
    String mediumFileName = "small.json";

    return ConstraintComparator.readFromFile(programName, mediumFileName);
  }

  @Test
  public void measureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<ConfigConstraint> mediumConstraints = getMediumConstraints(programName);
    Set<ConfigConstraint> smallConstraints = getSmallConstraints(programName);

    Assert.assertEquals(mediumConstraints, smallConstraints);
  }
}