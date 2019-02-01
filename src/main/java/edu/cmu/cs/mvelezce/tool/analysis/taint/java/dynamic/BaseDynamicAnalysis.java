package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic;

import edu.cmu.cs.mvelezce.tool.Options;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public abstract class BaseDynamicAnalysis<T> implements DynamicAnalysis<T> {

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
  public T analyze(String[] args) throws IOException, InterruptedException {
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

    T analysisResults = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(analysisResults);
    }

    return analysisResults;
  }

  @Override
  public void writeToFile(T analysisResults) throws IOException {
    throw new UnsupportedOperationException("Implement");
//    ObjectMapper mapper = new ObjectMapper();
//    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
//    File file = new File(outputFile);
//    file.getParentFile().mkdirs();
//
//    List<RegionToInfo> decisionsAndInfos = new ArrayList<>();
//
//    for (Map.Entry<JavaRegion, T> entry : analysisResults.entrySet()) {
//      RegionToInfo<T> regionToInfo = new RegionToInfo<>(entry.getKey(), entry.getValue());
//      decisionsAndInfos.add(regionToInfo);
//    }
//
//    mapper.writeValue(file, decisionsAndInfos);
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
