package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.alldynamic.AllDynamicAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO check what methods are already done in the super class
public class BFPhosphorAnalysis extends PhosphorAnalysis {

  private String mainClass;

  BFPhosphorAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public BFPhosphorAnalysis(String programName) {
    super(programName);
  }

  public BFPhosphorAnalysis(String programName, String mainClass, Set<String> options) {
    this(programName, options);

    this.mainClass = mainClass;
  }

  @Override
  void runDynamicAnalysis() throws IOException, InterruptedException {
    Set<String> options = this.getOptions();
    Set<Set<String>> configs = Helper.getConfigurations(options);

    for (Set<String> config : configs) {
      this.runPhosphorAnalysis(config);
      this.postProcessPhosphorAnalysis(config);
//      System.out.println();
    }
  }

  @Override
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.getProgramName() + "/bf";
  }

  // TODO this method is hardcoded to run all dynamic examples
  @Override
  List<String> buildCommandAsList(Set config) {
    if (this.mainClass == null) {
      return super.buildCommandAsList(config);
    }

    String programName = this.getProgramName();
    Adapter adapter = new AllDynamicAdapter(programName, this.mainClass);

    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);

    List<String> commandList = new ArrayList<>();
    commandList.add("./examples.sh");
    commandList.add(adapter.getMainClass());
    commandList.addAll(configList);

    return commandList;
  }
}
