package edu.cmu.cs.mvelezce.builder;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.partition.BasePartitionPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.PartialPartition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.region.RegionsManager;

import java.util.*;

public abstract class E2EModelBuilder extends BasePartitionPerformanceModelBuilder {

  protected static final Map<JavaRegion, Partitioning> REGIONS_TO_DATA = new HashMap<>();

  public E2EModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, REGIONS_TO_DATA, performanceEntries);
  }

  protected void addProgramRegionToData() {
    Set<Partition> partitions = new HashSet<>();
    List<String> options = this.getOptions();

    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      Set<String> config = entry.getConfiguration();
      String configPartition = ConstraintUtils.parseAsConstraint(config, options);
      Partition partition =
          new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, configPartition));
      partitions.add(partition);
    }

    REGIONS_TO_DATA.put(RegionsManager.PROGRAM_REGION, new PartialPartition(partitions));
  }
}
