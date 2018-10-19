package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public abstract class BaseDynamicAnalysis implements DynamicAnalysis<Set<Constraint>> {

  protected static final String DIRECTORY = Options.DIRECTORY + "/analysis/java/dynamic/programs";

  private final String programName;
  private final Set<String> options;
  private final Set<String> initialConfig;

  public BaseDynamicAnalysis(String programName, Set<String> options, Set<String> initialConfig) {
    this.programName = programName;
    this.options = options;
    this.initialConfig = initialConfig;
  }

  @Override
  public Map<JavaRegion, Set<Constraint>> analyze(String[] args) throws IOException {
    Options.getCommandLine(args);

    String outputFile = BaseDynamicAnalysis.DIRECTORY + "/" + this.programName;
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      Collection<File> files = FileUtils.listFiles(file, null, true);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case "
                + outputFile);
      }

      return this.readFromFile(files.iterator().next());
    }

    Map<JavaRegion, Set<Constraint>> regionsToOptionsSet = this.analyze();

//        if(Options.checkIfSave()) {
//            this.writeToFile(regionsToOptionsSet);
//        }

    return regionsToOptionsSet;
  }

  @Override
  public void writeToFile(Map<JavaRegion, Set<Constraint>> relevantRegionsToOptions)
      throws IOException {
    throw new UnsupportedOperationException("Implement");
//        ObjectMapper mapper = new ObjectMapper();
//        String outputFile = BaseDynamicAnalysis.DIRECTORY + "/" + this.programName + "/" + this.programName
//                + Options.DOT_JSON;
//        File file = new File(outputFile);
//        file.getParentFile().mkdirs();
//
//        List<DecisionAndOptions> decisionsAndOptions = new ArrayList<>();
//
//        for(Map.Entry<JavaRegion, Set<Set<String>>> regionToOptionsSet : relevantRegionsToOptions.entrySet()) {
//            DecisionAndOptions decisionAndOptions = new DecisionAndOptions(regionToOptionsSet.getKey(), regionToOptionsSet.getValue());
//            decisionsAndOptions.add(decisionAndOptions);
//        }
//
//        mapper.writeValue(file, decisionsAndOptions);
  }

  @Override
  public Map<JavaRegion, Set<Constraint>> readFromFile(File file) {
    throw new UnsupportedOperationException("Implement");
//        ObjectMapper mapper = new ObjectMapper();
//        List<DecisionAndOptions> results = mapper.readValue(file, new TypeReference<List<DecisionAndOptions>>() {
//        });
//        Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();
//
//        for(DecisionAndOptions result : results) {
//            regionsToOptionsSet.put(result.getRegion(), result.getOptions());
//        }
//
//        return regionsToOptionsSet;
//    return null;
  }

  // TODO should this be static helper method?
  public Map<Region, Set<Set<String>>> transform(
      Map<? extends Region, Set<Set<String>>> regionsToOptionSet) {
    throw new UnsupportedOperationException("Implement");
//        Map<Region, Set<Set<String>>> result = new HashMap<>();
//
//        for(Map.Entry<? extends Region, Set<Set<String>>> entry : regionsToOptionSet.entrySet()) {
//            Region region = new Region(entry.getKey().getRegionID());
//            result.put(region, entry.getValue());
//        }
//
//        return result;
//    return null;
  }

  public String getProgramName() {
    return this.programName;
  }

  public Set<String> getOptions() {
    return options;
  }

  public Set<String> getInitialConfig() {
    return initialConfig;
  }
}
