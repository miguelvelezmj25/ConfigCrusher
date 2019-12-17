package edu.cmu.cs.mvelezce.builder.idta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.constraint.BaseConstraintPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTALocalPerformanceModel;
import edu.cmu.cs.mvelezce.region.RegionsManager;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IDTAPerformanceModelBuilder extends BaseConstraintPerformanceModelBuilder {

  private static final String OUTPUT_DIR =
      "../cc-perf-model/" + Options.DIRECTORY + "/model/java/idta/programs";

  public IDTAPerformanceModelBuilder(String programName) {
    this(programName, new ArrayList<>(), new HashMap<>(), new HashSet<>());
  }

  IDTAPerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, options, regionsToData, performanceEntries);

    Set<FeatureExpr> baseConstraints = new HashSet<>();
    baseConstraints.add(SATFeatureExprFactory.True());

    regionsToData.put(RegionsManager.PROGRAM_REGION, baseConstraints);
  }

  //  protected void validateOneConfigCoversOneConstraint(
  //          MultiEntryLocalPerformanceModel<FeatureExpr> localModel) {
  //    for (PerformanceEntry entry : this.getPerformanceEntries()) {
  //      FeatureExpr configConstraint = this.getPerfEntryToExecConstraint().get(entry);
  //      Set<FeatureExpr> coveredConstraints = new HashSet<>();
  //
  //      for (FeatureExpr regionConstraint : localModel.getModel().keySet()) {
  //        if (configConstraint.implies(regionConstraint).isTautology()) {
  //          coveredConstraints.add(regionConstraint);
  //        }
  //      }
  //
  //      if (coveredConstraints.size() > 1) {
  //        throw new RuntimeException(
  //                "Expected that one executed configuration would cover at most one region
  // constraint"
  //                        + localModel.getRegion());
  //      }
  //    }
  //  }

  @Override
  public PerformanceModel<FeatureExpr> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<FeatureExpr>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      LocalPerformanceModel<FeatureExpr> localModel =
          new IDTALocalPerformanceModel(
              readLocalModel.getRegion(),
              this.parseConstraintsToData(readLocalModel.getModel()),
              this.parseConstraintsToData(readLocalModel.getModelToMin()),
              this.parseConstraintsToData(readLocalModel.getModelToMax()),
              this.parseConstraintsToData(readLocalModel.getModelToDiff()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToPerfHumanReadable()),
              this.parseConstraintsToHumanReadableData(readLocalModel.getModelToMinHumanReadable()),
              this.parseConstraintsToHumanReadableData(readLocalModel.getModelToMaxHumanReadable()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToDiffHumanReadable()));
      localModels.add(localModel);
    }

    return new PerformanceModel<>(localModels);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}
