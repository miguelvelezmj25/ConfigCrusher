package edu.cmu.cs.mvelezce.allmethodsareregions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.idta.transform.IDTAMethodTransformer;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.utils.RegionsWithConstraintsUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllMethodsAreRegionsAnalysis extends BaseAnalysis<Map<JavaRegion, Set<FeatureExpr>>> {

  private final AllMethodsRegionCreator allMethodsRegionCreator;
  private final Set<FeatureExpr> constraints;
  private final List<String> options;

  public AllMethodsAreRegionsAnalysis(
      String programName,
      List<String> options,
      String mainClass,
      String classDir,
      Set<FeatureExpr> constraints)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException,
          InvocationTargetException {
    super(programName);

    this.options = options;
    this.allMethodsRegionCreator =
        new AllMethodsRegionCreator(
            programName, new DefaultClassTransformer(classDir), mainClass, false);
    this.constraints = constraints;
  }

  @Override
  public Map<JavaRegion, Set<FeatureExpr>> analyze() throws IOException {
    return this.allMethodsRegionCreator.createRegions(this.constraints);
  }

  @Override
  public String outputDir() {
    return IDTAMethodTransformer.DEBUG_DIR
        + "/"
        + this.getProgramName()
        + "/"
        + this.getProgramName()
        + Options.DOT_JSON;
  }

  @Override
  public void writeToFile(Map<JavaRegion, Set<FeatureExpr>> results) throws IOException {
    RegionsWithConstraintsUtils.writeToFile(results, this.outputDir(), this.options);
  }

  @Override
  public Map<JavaRegion, Set<FeatureExpr>> readFromFile(File file) throws IOException {
    return RegionsWithConstraintsUtils.readFromFile(file);
  }
}
