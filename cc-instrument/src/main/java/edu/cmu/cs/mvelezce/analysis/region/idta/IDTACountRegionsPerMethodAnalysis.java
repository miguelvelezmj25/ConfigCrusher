package edu.cmu.cs.mvelezce.analysis.region.idta;

import edu.cmu.cs.mvelezce.analysis.region.BaseCountRegionsPerMethodAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;

import java.util.Map;

public class IDTACountRegionsPerMethodAnalysis
    extends BaseCountRegionsPerMethodAnalysis<Partitioning> {

  public IDTACountRegionsPerMethodAnalysis(Map<JavaRegion, Partitioning> regionsToData) {
    super(regionsToData);
  }
}
