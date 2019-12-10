package edu.cmu.cs.mvelezce.exhaustive.builder.gt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.exhaustive.builder.ExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.exhaustive.model.constraint.ExhaustiveLocalPerformanceModel;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroundTruthExhaustiveModelBuilder extends ExhaustiveModelBuilder {

  private static final String OUTPUT_DIR =
      "../cc-perf-model-exhaustive/" + Options.DIRECTORY + "/model/java/programs/gt";

  public GroundTruthExhaustiveModelBuilder(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  GroundTruthExhaustiveModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  @Override
  protected void populateMultiEntryLocalModel(
      MultiEntryLocalPerformanceModel<FeatureExpr> localModel) {
    throw new UnsupportedOperationException(
        "Separate these steps into two executions. Otherwise, it takes too long");
    //    this.validateOneConfigCoversOneConstraint(localModel);
    //
    //    super.populateMultiEntryLocalModel(localModel);
  }

  @Override
  public PerformanceModel<FeatureExpr> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<FeatureExpr>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      LocalPerformanceModel<FeatureExpr> localModel =
          new ExhaustiveLocalPerformanceModel(
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
    return OUTPUT_DIR;
  }
}
