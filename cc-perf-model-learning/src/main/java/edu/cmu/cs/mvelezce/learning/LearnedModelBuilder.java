package edu.cmu.cs.mvelezce.learning;

import edu.cmu.cs.mvelezce.builder.E2EModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.List;
import java.util.Set;

public class LearnedModelBuilder extends E2EModelBuilder {

  public static final String OUTPUT_DIR = "../cc-perf-model-learning/" + Options.DIRECTORY;

  public LearnedModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  //  public static void model() {
  //    //    Map<Set<String>, Double> learnedModel = new HashMap<>();
  //    //
  //    //  String dataDir = Evaluation.DIRECTORY + "/" + this.getProgramName() + Approach.DATA_DIR
  // +
  //    // "/"
  //    //          + Evaluation.PAIR_WISE;
  //    //
  //    //  File termsFile = new File(dataDir + "/" + Approach.TERMS_FILE);
  //    //  List<String> rawTerms = this.parseFile(termsFile);
  //    //
  //    //  File coefsFile = new File(dataDir + "/" + Approach.COEFS_FILE);
  //    //  List<String> coefs = this.parseFile(coefsFile);
  //    //
  //    //        if(rawTerms.size() != coefs.size()) {
  //    //    throw new RuntimeException("The terms and coefs files are not the same length");
  //    //  }
  //    //
  //    //  File pValueFile = new File(dataDir + "/" + Approach.PVALUES_FILE);
  //    //  List<String> pValues = this.parseFile(pValueFile);
  //    //  List<String> significantTerms = this.removeTermsWithPValueGreaterThan(rawTerms, pValues,
  //    // 0.05);
  //    //
  //    //  List<Set<String>> terms = this.parseTerms(significantTerms, options);
  //    //
  //    //        for(int i = 0; i < terms.size(); i++) {
  //    //    Set<String> term = terms.get(i);
  //    //    String rawTerm = significantTerms.get(i);
  //    //    int index = rawTerms.indexOf(rawTerm);
  //    //    String coef = coefs.get(index);
  //    //    learnedModel.put(term, Double.valueOf(coef));
  //    //  }
  //    //
  //    //  Set<String> empty = new HashSet<>();
  //    //
  //    //        if(!learnedModel.containsKey(empty)) {
  //    //    learnedModel.put(empty, 0.0);
  //    //  }
  //    //
  //    //        return learnedModel;
  //  }

  @Override
  public String outputDir() {
    throw new UnsupportedOperationException("pick output based on using matlab? maybe this class should be abstract?");
  }
}
