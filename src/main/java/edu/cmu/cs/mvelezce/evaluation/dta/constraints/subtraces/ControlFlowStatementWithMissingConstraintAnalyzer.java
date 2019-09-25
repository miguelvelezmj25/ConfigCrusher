package edu.cmu.cs.mvelezce.evaluation.dta.constraints.subtraces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace.ControlFlowStatement;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace.SubtraceLabel;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace.SubtraceManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

class ControlFlowStatementWithMissingConstraintAnalyzer
    implements Analysis<ControlFlowStatementsWithMissingConstraint> {

  private final String programName;
  private final String stringConstraint;
  private final Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint;

  ControlFlowStatementWithMissingConstraintAnalyzer(
      String programName,
      String stringConstraint,
      Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint) {
    this.programName = programName;
    this.stringConstraint = stringConstraint;
    this.subtracesOutcomeConstraint = subtracesOutcomeConstraint;
  }

  ControlFlowStatementWithMissingConstraintAnalyzer(String programName, String stringConstraint) {
    this(programName, stringConstraint, new HashSet<>());
  }

  @Override
  public ControlFlowStatementsWithMissingConstraint analyze(String[] args) throws Exception {
    Options.getCommandLine(args);

    String outputFile = this.outputDir();
    File file = new File(outputFile);
    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      String[] extensions = new String[] {"json"};
      Collection<File> files = FileUtils.listFiles(file, extensions, false);

      for (File resultFile : files) {
        ControlFlowStatementsWithMissingConstraint result = this.readFromFile(resultFile);

        if (!this.stringConstraint.equals(result.getMissingConstraint())) {
          continue;
        }

        return result;
      }

      throw new RuntimeException(
          "Could not find results for " + this.stringConstraint + " in " + this.programName);
    }

    ControlFlowStatementsWithMissingConstraint results = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(results);
    }

    return results;
  }

  @Override
  public ControlFlowStatementsWithMissingConstraint analyze() throws Exception {
    FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(this.stringConstraint);
    Set<UUID> uuidsWithConstraint =
        this.getUUIDSWithConstraint(this.subtracesOutcomeConstraint, constraint);

    System.err.println("Maybe also pass the ids to subtrace labels map in the constructor");
    Map<UUID, SubtraceLabel> idsToSubtraceLabels =
        SubtraceManager.readIdsToSubtraceLabels(this.programName);

    Set<SubtraceLabel> labelsWithConstraint =
        getSubtraceLabelsWithConstraint(uuidsWithConstraint, idsToSubtraceLabels);
    Set<ControlFlowStatement> controlFlowStatementsWithMissingConstraint =
        getControlFlowStatementsWithMissingConstraint(labelsWithConstraint);

    return new ControlFlowStatementsWithMissingConstraint(
        this.stringConstraint, controlFlowStatementsWithMissingConstraint);
  }

  private Set<ControlFlowStatement> getControlFlowStatementsWithMissingConstraint(
      Set<SubtraceLabel> labelsWithConstraint) {
    Set<ControlFlowStatement> controlFlowStatements = new HashSet<>();

    for (SubtraceLabel subtraceLabel : labelsWithConstraint) {
      ControlFlowStatement statement = subtraceLabel.getControlFlowStatement();
      controlFlowStatements.add(statement);
    }

    return controlFlowStatements;
  }

  @Override
  public void writeToFile(ControlFlowStatementsWithMissingConstraint result) throws IOException {
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, result);
  }

  @Override
  public ControlFlowStatementsWithMissingConstraint readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(
        file, new TypeReference<ControlFlowStatementsWithMissingConstraint>() {});
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY
        + "/analysis/spec/traces/missingConstraints/java/programs/"
        + this.programName;
  }

  private Set<SubtraceLabel> getSubtraceLabelsWithConstraint(
      Set<UUID> uuidsWithConstraint, Map<UUID, SubtraceLabel> idsToSubtraceLabels) {
    Set<SubtraceLabel> subtraceLabelsWithConstraint = new HashSet<>();

    for (UUID uuidWithConstraint : uuidsWithConstraint) {
      SubtraceLabel subtraceLabel = idsToSubtraceLabels.get(uuidWithConstraint);

      if (subtraceLabel == null) {
        throw new RuntimeException(
            "Could not find subtrace label with UUID " + uuidsWithConstraint);
      }

      subtraceLabelsWithConstraint.add(subtraceLabel);
    }

    return subtraceLabelsWithConstraint;
  }

  private Set<UUID> getUUIDSWithConstraint(
      Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint, FeatureExpr constraint) {
    Set<UUID> uuidsWithConstraint = new HashSet<>();

    for (SubtraceOutcomeConstraint subtraceOutcomeConstraint : subtracesOutcomeConstraint) {
      Map<String, FeatureExpr> outcomesToConstraints =
          subtraceOutcomeConstraint.getOutcomesToConstraints();

      for (FeatureExpr featureExpr : outcomesToConstraints.values()) {
        if (!featureExpr.equals(constraint)) {
          continue;
        }

        uuidsWithConstraint.add(subtraceOutcomeConstraint.getSubtraceLabelUUID());
      }
    }

    return uuidsWithConstraint;
  }
}
