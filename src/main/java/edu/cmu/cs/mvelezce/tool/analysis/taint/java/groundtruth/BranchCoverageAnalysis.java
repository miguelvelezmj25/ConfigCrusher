package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jboss.util.file.Files;

public class BranchCoverageAnalysis extends BaseDynamicAnalysis<DecisionTruthTable> {

  private final Map<String, DecisionTruthTable> sinksToTruthTables = new HashMap<>();

  BranchCoverageAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  @Override
  public Map<JavaRegion, DecisionTruthTable> analyze() throws IOException, InterruptedException {
    Set<Set<String>> configs = Helper.getConfigurations(this.getOptions());

    for (Set<String> config : configs) {
      this.runProgram(config);

      try {
        this.processResults(config);
      }
      catch (ClassNotFoundException cnfe) {
        throw new RuntimeException("There was an error when processing the results", cnfe);
      }
    }

    Files.delete(BranchCoverageLogger.RESULTS_FILE);
    Map<JavaRegion, DecisionTruthTable> regionsToConstraints = new HashMap<>();

    for (Map.Entry<String, DecisionTruthTable> entry : this.sinksToTruthTables.entrySet()) {
      String sink = entry.getKey();
      JavaRegion region = new JavaRegion.Builder(this.getPackageName(sink), this.getClassName(sink),
          this.getMethodSignature(sink)).startBytecodeIndex(this.getDecisionOrder(sink)).build();

      regionsToConstraints.put(region, entry.getValue());
    }

    return regionsToConstraints;
  }

  @Override
  public Map<JavaRegion, DecisionTruthTable> readFromFile(File file) {
    throw new UnsupportedOperationException("Implement");
    //    FileOutputStream fos = new FileOutputStream("table.ser");
//    ObjectOutputStream oos = new ObjectOutputStream(fos);
//    oos.writeObject(sinksToTruthTable);
//    oos.close();
//    fos.close();

//    FileInputStream fis = new FileInputStream("table.ser");
//    ObjectInputStream ois = new ObjectInputStream(fis);
//    Map<String, Map<Set<String>, Boolean>> res = (Map<String, Map<Set<String>, Boolean>>) ois.readObject();
//    ois.close();
//    fis.close();

//    for (Map.Entry<String, DecisionTruthTable> entry : sinksToTruthTable.entrySet()) {
//      System.out.println(entry.getKey());
//      System.out.println(entry.getValue());
//
//      System.out.println();
//    }
  }

  @Override
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.getProgramName() + "/gt";
  }

  private void runProgram(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(config);
    builder.command(commandList);
    Process process = builder.start();

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();
  }

  private List<String> buildCommandAsList(Set<String> config) {
    List<String> commandList = new ArrayList<>();
    commandList.add("java");
    commandList.add("-cp");

    String programName = this.getProgramName();
    String ccClasspath = "./target/classes";
    Adapter adapter;

    switch (programName) {
      case DynamicRunningExampleAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + ":../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes");
        adapter = new DynamicRunningExampleAdapter();
        break;
      default:
        throw new RuntimeException("Could not find a phosphor script to run " + programName);
    }

    commandList.add(adapter.getMainClass());
    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }

  private void processResults(Set<String> config) throws IOException, ClassNotFoundException {
    Map<String, Boolean> sinksToDecisions = this.getSinksToDecisions();
    this.addSinks(sinksToDecisions.keySet());

    for (Map.Entry<String, Boolean> entry : sinksToDecisions.entrySet()) {
      String sink = entry.getKey();
      DecisionTruthTable decisionTruthTable = this.sinksToTruthTables.get(sink);
      decisionTruthTable.addEntry(config, entry.getValue());
      this.sinksToTruthTables.put(sink, decisionTruthTable);
    }
  }

  private void addSinks(Set<String> executedSinks) {
    for (String sink : executedSinks) {
      if (!this.sinksToTruthTables.containsKey(sink)) {
        this.sinksToTruthTables.put(sink, new DecisionTruthTable(this.getOptions()));
      }
    }
  }

  private Map<String, Boolean> getSinksToDecisions() throws IOException, ClassNotFoundException {
    Map<String, Boolean> sinksToDecisions;

    try (FileInputStream fis = new FileInputStream(BranchCoverageLogger.RESULTS_FILE);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      sinksToDecisions = (Map<String, Boolean>) ois.readObject();
    }

    return sinksToDecisions;
  }
}
