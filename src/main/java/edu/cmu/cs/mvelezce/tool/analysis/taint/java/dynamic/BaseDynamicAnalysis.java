package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    String outputFile = this.outputDir();
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

    Map<JavaRegion, Set<Constraint>> regionsToConstraints = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(regionsToConstraints);
    }

    return regionsToConstraints;
  }

  @Override
  public void writeToFile(Map<JavaRegion, Set<Constraint>> regionsToConstraints)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    List<RegionToInfo> decisionsAndConstraints = new ArrayList<>();

    for (Map.Entry<JavaRegion, Set<Constraint>> entry : regionsToConstraints.entrySet()) {
      RegionToInfo<Set<Constraint>> regionToConstraints = new RegionToInfo<>(entry.getKey(),
          entry.getValue());
      decisionsAndConstraints.add(regionToConstraints);
    }

    mapper.writeValue(file, decisionsAndConstraints);
  }

  @Override
  public Map<JavaRegion, Set<Constraint>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo<Set<Constraint>>> results = mapper
        .readValue(file, new TypeReference<List<RegionToInfo<Set<Constraint>>>>() {
        });

    Map<JavaRegion, Set<Constraint>> regionsToConstraints = new HashMap<>();

    for (RegionToInfo<Set<Constraint>> result : results) {
      regionsToConstraints.put(result.getRegion(), new HashSet<>(result.getInfo()));
    }

    return regionsToConstraints;
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

  @Override
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.programName + "/cc/";
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
