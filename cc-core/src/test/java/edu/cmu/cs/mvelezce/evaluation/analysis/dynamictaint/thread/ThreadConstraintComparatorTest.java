package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.thread;

import edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.ConstraintComparator;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class ThreadConstraintComparatorTest {

  private static Set<ConfigConstraint> getSingleThreadedConstraints(ConstraintComparator comparator,
      String programName) throws IOException {
    String mediumFileName = "single.json";

    return comparator.readFromFile(programName, mediumFileName);
  }

  private static Set<ConfigConstraint> getMultiThreadedConstraints(ConstraintComparator comparator,
      String programName) throws IOException {
    String mediumFileName = "multi.json";

    return comparator.readFromFile(programName, mediumFileName);
  }

  @Test
  public void measureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    ConstraintComparator comparator = new ThreadConstraintComparator(programName);

    Set<ConfigConstraint> singleConstraints = getSingleThreadedConstraints(comparator, programName);
    Set<ConfigConstraint> multiConstraints = getMultiThreadedConstraints(comparator, programName);

    comparator.compareConstraints(singleConstraints, multiConstraints);
  }
}