package edu.cmu.cs.mvelezce.pretty.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.influence.LocalPerformanceInfluenceModel;
import edu.cmu.cs.mvelezce.pretty.constraint.BaseConstraintPrettyBuilder;
import edu.cmu.cs.mvelezce.utils.config.Options;
import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;
import scala.collection.JavaConverters;

import java.text.DecimalFormat;
import java.util.*;

public class IDTAPrettyBuilder extends BaseConstraintPrettyBuilder {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.000");
  private static final String OUTPUT_DIR =
      "../cc-perf-model/" + Options.DIRECTORY + "/model/java/pretty/idta/programs";

  public IDTAPrettyBuilder(
      String programName, Collection<String> options, PerformanceModel<FeatureExpr> model) {
    super(programName, options, model);
  }

  @Override
  protected LocalPerformanceInfluenceModel buildInfluenceLocalModel(
      LocalPerformanceModel<FeatureExpr> localModel) {
    Map<FeatureExpr, Double> perfModel = localModel.getModel();
    List<String> features = this.getSingleFeatures(perfModel);
    Set<Set<String>> terms = ConfigHelper.getConfigurations(features);
    LinkedHashMap<Set<String>, Double> influenceModel = new LinkedHashMap<>();

    // Base time
    if (perfModel.size() == 1) {
      influenceModel.put(new HashSet<>(), perfModel.entrySet().iterator().next().getValue());

      return new LocalPerformanceInfluenceModel(
          localModel.getRegion(), influenceModel, toHumanReadable(influenceModel));
    }

    for (int termSize = 0; termSize <= features.size(); termSize++) {
      Set<Set<String>> termsOfSize = this.getTermsOfSize(terms, termSize);

      for (Set<String> termOfSize : termsOfSize) {
        String stringConstraint = ConstraintUtils.parseAsConstraint(termOfSize, this.getOptions());
        FeatureExpr constraint =
            FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringConstraint);
        double influence = perfModel.get(constraint);

        for (Map.Entry<Set<String>, Double> influenceModelEntry : influenceModel.entrySet()) {
          if (termOfSize.containsAll(influenceModelEntry.getKey())) {
            influence -= influenceModelEntry.getValue();
          }
        }

        influenceModel.put(termOfSize, influence);
      }
    }

    return new LocalPerformanceInfluenceModel(
        localModel.getRegion(), influenceModel, this.toHumanReadable(influenceModel));
  }

  protected LinkedHashMap<Set<String>, String> toHumanReadable(
      LinkedHashMap<Set<String>, Double> influenceModel) {
    LinkedHashMap<Set<String>, String> influenceToHumanReadableData = new LinkedHashMap<>();

    for (Map.Entry<Set<String>, Double> entry : influenceModel.entrySet()) {
      double data = entry.getValue();
      data = data / 1E9;
      influenceToHumanReadableData.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return influenceToHumanReadableData;
  }

  private Set<Set<String>> getTermsOfSize(Set<Set<String>> terms, int size) {
    Set<Set<String>> termsOfSize = new HashSet<>();

    for (Set<String> term : terms) {
      if (term.size() == size) {
        termsOfSize.add(term);
      }
    }

    return termsOfSize;
  }

  private List<String> getSingleFeatures(Map<FeatureExpr, Double> perfModel) {
    Set<String> features = new HashSet<>();

    for (FeatureExpr key : perfModel.keySet()) {
      features.addAll(JavaConverters.asJavaCollection(key.collectDistinctFeatures()));
    }

    return new ArrayList<>(features);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}
