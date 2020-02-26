package edu.cmu.cs.mvelezce.allmethodsareregions;

import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.idta.transform.IDTAMethodTransformer;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.utils.RegionsWithPartitionsUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public class AllMethodsAreRegionsAnalysis extends BaseAnalysis<Map<JavaRegion, Partitioning>> {

  private final AllMethodsRegionCreator allMethodsRegionCreator;
  private final Partitioning partitioning;
  private final List<String> options;

  public AllMethodsAreRegionsAnalysis(
      String programName,
      List<String> options,
      String mainClass,
      String classDir,
      Partitioning partitioning)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException,
          InvocationTargetException {
    super(programName);

    this.options = options;
    this.allMethodsRegionCreator =
        new AllMethodsRegionCreator(
            programName, new DefaultClassTransformer(classDir), mainClass, false);
    this.partitioning = partitioning;
  }

  @Override
  public Map<JavaRegion, Partitioning> analyze() throws IOException {
    return this.allMethodsRegionCreator.createRegions(this.partitioning);
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
  public void writeToFile(Map<JavaRegion, Partitioning> results) throws IOException {
    RegionsWithPartitionsUtils.writeToFile(results, this.outputDir(), this.options);
  }

  @Override
  public Map<JavaRegion, Partitioning> readFromFile(File file) throws IOException {
    return RegionsWithPartitionsUtils.readFromFile(file);
  }
}
