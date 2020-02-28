package edu.cmu.cs.mvelezce.analysis.idta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import org.apache.commons.io.FileUtils;

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
      Partitioning partitioning =
          Partitioning.getPartitioning(Partition.getPartitions(idtaResult.getInfo()));
      results.put(javaRegion, partitioning);
    }

    return results;
  }

  private List<IDTAResult> parseIDTAResults() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    File resultsDir =
        new File(
            IDTAAnalysis.IDTA_OUTPUT_DIR
                + "/"
                + this.getProgramName()
                + "/cc/"
                + this.workloadSize
                + "/partitions/");

    Collection<File> results = FileUtils.listFiles(resultsDir, new String[] {"json"}, false);

    if (results.isEmpty()) {
      throw new RuntimeException("There are no idta results for " + this.getProgramName());
    }

    List<IDTAResult> idtaResults = new ArrayList<>();

    for (File file : results) {
      List<IDTAResult> idtaResult =
          mapper.readValue(file, new TypeReference<List<IDTAResult>>() {});
      idtaResults.addAll(idtaResult);
    }

    return idtaResults;
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
