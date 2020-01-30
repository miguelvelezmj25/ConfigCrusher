package edu.cmu.cs.mvelezce.analysis.idta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.idta.partition.TotalPartition;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IDTAAnalysis extends BaseAnalysis<Map<JavaRegion, Partitioning>> {

  private static final String IDTA_OUTPUT_DIR =
      Executor.USER_HOME
          + "/Documents/Programming/Java/Projects/subtrace-explorer/src/main/resources/idta/analysis";

  private final String workloadSize;

  public IDTAAnalysis(String programName, String workloadSize) {
    super(programName);

    this.workloadSize = workloadSize;
  }

  @Override
  public Map<JavaRegion, Partitioning> analyze() throws IOException {
    Map<JavaRegion, Partitioning> results = new HashMap<>();
    List<IDTAResult> idtaResults = this.parseIDTAResults();

    for (IDTAResult idtaResult : idtaResults) {
      JavaRegion javaRegion =
          new JavaRegion.Builder(
                  idtaResult.getPackageName(),
                  idtaResult.getClassName(),
                  idtaResult.getMethodSignature())
              .startIndex(idtaResult.getDecisionIndex())
              .build();
      Partitioning partitioning = this.getPartitioning(idtaResult.getInfo());
      results.put(javaRegion, partitioning);
    }

    return results;
  }

  private Partitioning getPartitioning(Set<String> prettyPartitions) {
    Set<Partition> partitions = new HashSet<>();

    for (String prettyPartition : prettyPartitions) {
      Partition partition =
          new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, prettyPartition));
      partitions.add(partition);
    }

    return new TotalPartition(partitions);
  }

  private List<IDTAResult> parseIDTAResults() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File inputFile =
        new File(
            IDTAAnalysis.IDTA_OUTPUT_DIR
                + "/"
                + this.getProgramName()
                + "/cc/"
                + this.workloadSize
                + "/partitions/"
                + this.getProgramName()
                + ".json");

    return mapper.readValue(inputFile, new TypeReference<List<IDTAResult>>() {});
  }

  @Override
  public void writeToFile(Map<JavaRegion, Partitioning> results) {
    throw new UnsupportedOperationException(
        "Should not be called since we already have the control flow statement data in the IDTA results. This class only create Java Regions to partitionings");
  }

  @Override
  public Map<JavaRegion, Partitioning> readFromFile(File file) {
    throw new UnsupportedOperationException(
        "Should not be called since we already have the control flow statement data in the IDTA results. This class only create Java Regions to partitionings");
  }

  @Override
  public String outputDir() {
    throw new UnsupportedOperationException(
        "Should not be called since we already have the control flow statement data in the IDTA results. This class only create Java Regions to partitionings");
  }
}
