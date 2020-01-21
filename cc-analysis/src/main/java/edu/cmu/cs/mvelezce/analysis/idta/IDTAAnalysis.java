package edu.cmu.cs.mvelezce.analysis.idta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mijecu25.meme.utils.execute.Executor;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IDTAAnalysis extends BaseAnalysis<Map<JavaRegion, Set<FeatureExpr>>> {

  private static final String IDTA_OUTPUT_DIR =
      Executor.USER_HOME
          + "/Documents/Programming/Java/Projects/subtrace-explorer/src/main/resources/idta/analysis";

  private final String workloadSize;

  public IDTAAnalysis(String programName, String workloadSize) {
    super(programName);

    this.workloadSize = workloadSize;
  }

  @Override
  public Map<JavaRegion, Set<FeatureExpr>> analyze() throws IOException {
    Map<JavaRegion, Set<FeatureExpr>> results = new HashMap<>();
    List<IDTAResult> idtaResults = this.parseIDTAResults();

    for (IDTAResult idtaResult : idtaResults) {
      JavaRegion javaRegion =
          new JavaRegion.Builder(
                  idtaResult.getPackageName(),
                  idtaResult.getClassName(),
                  idtaResult.getMethodSignature())
              .startIndex(idtaResult.getDecisionIndex())
              .build();
      Set<FeatureExpr> constraints = this.getConstraints(idtaResult.getInfo());
      results.put(javaRegion, constraints);
    }

    return results;
  }

  private Set<FeatureExpr> getConstraints(Set<String> prettyConstraints) {
    Set<FeatureExpr> constraints = new HashSet<>();

    for (String prettyConstraint : prettyConstraints) {
      FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(prettyConstraint);
      constraints.add(constraint);
    }

    return constraints;
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
  public void writeToFile(Map<JavaRegion, Set<FeatureExpr>> results) {
    throw new UnsupportedOperationException(
        "Should not be called since we already have the control flow statement data in the IDTA results. This class only create Java Regions to constraints");
  }

  @Override
  public Map<JavaRegion, Set<FeatureExpr>> readFromFile(File file) {
    throw new UnsupportedOperationException(
        "Should not be called since we already have the control flow statement data in the IDTA results. This class only create Java Regions to constraints");
  }

  @Override
  public String outputDir() {
    throw new UnsupportedOperationException(
        "Should not be called since we already have the control flow statement data in the IDTA results. This class only create Java Regions to constraints");
  }
}
