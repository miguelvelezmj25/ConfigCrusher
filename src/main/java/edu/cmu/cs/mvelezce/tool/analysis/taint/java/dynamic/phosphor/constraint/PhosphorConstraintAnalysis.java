package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraintAnalyzer;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.ifOr2.IfOr2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit.ImplicitAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit2.Implicit2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext.OrContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext2.OrContext2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext3.OrContext3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext6.OrContext6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample8.PhosphorExample8Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler.PrevaylerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample2.SimpleForExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample4.SimpleForExample4Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample5.SimpleForExample5Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sound.SoundAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces2.Subtraces2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces6.Subtraces6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces7.Subtraces7Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.throwIf.ThrowIfAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.variabilityContext1.VariabilityContext1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.variabilityContext2.VariabilityContext2Adapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public class PhosphorConstraintAnalysis extends BaseDynamicAnalysis<Set<ConfigConstraint>> {

  private static final String PHOSPHOR_SCRIPTS_DIR = "../phosphor/Phosphor/scripts/run-instrumented/implicit-optimized";
  private static final String DIRECTORY = Options.DIRECTORY + "/analysis/java/dynamic/programs";

  private final ConfigConstraintAnalyzer configConstraintAnalyzer;
  private final PhosphorConstraintExecutionAnalysis phosphorConstraintExecutionAnalysis;
  private final PhosphorConstraintCalculator phosphorConstraintCalculator;

  PhosphorConstraintAnalysis(String programName, List<String> options, Set<String> initialConfig) {
    super(programName, new HashSet<>(options), initialConfig);

    this.configConstraintAnalyzer = new ConfigConstraintAnalyzer(new HashSet<>(options));
    this.phosphorConstraintExecutionAnalysis = new PhosphorConstraintExecutionAnalysis(programName);
    this.phosphorConstraintCalculator = new PhosphorConstraintCalculator(options);
  }

  PhosphorConstraintAnalysis(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  @Override
  public Set<ConfigConstraint> analyze() throws IOException, InterruptedException {
    Set<ConfigConstraint> constraints = this.runConstraintAnalysis();
    return this.getSimplifiedConstraints(constraints);
  }

  private Set<ConfigConstraint> getSimplifiedConstraints(Set<ConfigConstraint> constraints) {
    Set<ConfigConstraint> simplifiedConstraints = new HashSet<>();

    for (ConfigConstraint candidateConstraintToAdd : constraints) {
      boolean add = true;

      for (ConfigConstraint constraint : constraints) {
        if (candidateConstraintToAdd.equals(constraint)) {
          continue;
        }

        if (candidateConstraintToAdd.isSubConstraintOf(constraint)) {
          add = false;
          break;
        }
      }

      if (add) {
        simplifiedConstraints.add(candidateConstraintToAdd);
      }
    }

    return simplifiedConstraints;
  }

  private Set<ConfigConstraint> runConstraintAnalysis() throws IOException, InterruptedException {
    Set<ConfigConstraint> constraints = new HashSet<>();
    Set<String> options = this.getOptions();
    Set<ConfigConstraint> configConstraintsToSatisfy = new HashSet<>();
    Set<ConfigConstraint> satisfiedConfigConstraints = new HashSet<>();
    Set<ConfigConstraint> exploredConfigConstraints = new HashSet<>();
    Set<String> config = this.getInitialConfig();

    while (config != null) {
      ConfigConstraint configConstraint = ConfigConstraint.fromConfig(config, options);
      exploredConfigConstraints.add(configConstraint);
      Set<ConfigConstraint> satisfiedConfigConstraintsByConfig = this.configConstraintAnalyzer
          .getConstraintsSatisfiedByConfig(configConstraint);
      satisfiedConfigConstraints.addAll(satisfiedConfigConstraintsByConfig);

      this.runPhosphorAnalysis(config);
      Set<DecisionTaints> results = this.phosphorConstraintExecutionAnalysis.getResults();

      Set<ConfigConstraint> analysisConstraints = this.phosphorConstraintCalculator
          .deriveConstraints(results, config);
      constraints.addAll(analysisConstraints);

      configConstraintsToSatisfy.addAll(analysisConstraints);
      configConstraintsToSatisfy.removeAll(satisfiedConfigConstraints);

      Set<Set<String>> configsToRun = this.configConstraintAnalyzer
          .getConfigsThatSatisfyConfigConstraints(configConstraintsToSatisfy,
              exploredConfigConstraints);

      config = this.getNextConfig(configsToRun);
    }

    return constraints;
  }

  @Nullable
  private Set<String> getNextConfig(Set<Set<String>> configsToRun) {
    if (configsToRun.isEmpty()) {
      return null;
    }

    return configsToRun.iterator().next();
  }

  private void runPhosphorAnalysis(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(config);
    builder.command(commandList);
    builder.directory(new File(PHOSPHOR_SCRIPTS_DIR));

    System.out.println("Running program");
    Process process = builder.start();

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();
  }

  private List<String> buildCommandAsList(Set<String> config) {
    List<String> commandList = new ArrayList<>();

    String programName = this.getProgramName();
    Adapter adapter;

    switch (programName) {
      case DynamicRunningExampleAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new DynamicRunningExampleAdapter();
        break;
      case PhosphorExample2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new PhosphorExample2Adapter();
        break;
      case PhosphorExample8Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new PhosphorExample8Adapter();
        break;
      case PhosphorExample3Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new PhosphorExample3Adapter();
        break;
      case SimpleExample1Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SimpleExample1Adapter();
        break;
      case Example1Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Example1Adapter();
        break;
      case MultiFacetsAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new MultiFacetsAdapter();
        break;
      case SimpleForExample2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SimpleForExample2Adapter();
        break;
      case SimpleForExample4Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SimpleForExample4Adapter();
        break;
      case SimpleForExample5Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SimpleForExample5Adapter();
        break;
      case OrContextAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new OrContextAdapter();
        break;
      case OrContext2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new OrContext2Adapter();
        break;
      case OrContext3Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new OrContext3Adapter();
        break;
      case OrContext6Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new OrContext6Adapter();
        break;
      case IfOr2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new IfOr2Adapter();
        break;
      case VariabilityContext1Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new VariabilityContext1Adapter();
        break;
      case VariabilityContext2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new VariabilityContext2Adapter();
        break;
      case SubtracesAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SubtracesAdapter();
        break;
      case Subtraces2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Subtraces2Adapter();
        break;
      case Subtraces6Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Subtraces6Adapter();
        break;
      case Subtraces7Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Subtraces7Adapter();
        break;
      case ImplicitAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new ImplicitAdapter();
        break;
      case Implicit2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Implicit2Adapter();
        break;
      case TrivialAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new TrivialAdapter();
        break;
      case ThrowIfAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new ThrowIfAdapter();
        break;
      case SoundAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SoundAdapter();
        break;
      case PrevaylerAdapter.PROGRAM_NAME:
        commandList.add("./prevayler.sh");
        commandList.add(PrevaylerAdapter.PROGRAM_NAME);
        adapter = new PrevaylerAdapter();
        break;
      case MeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        commandList.add("./measureDiskOrderedScan.sh");
        adapter = new MeasureDiskOrderedScanAdapter();
        ((MeasureDiskOrderedScanAdapter) adapter).preProcess();
        break;
      default:
        throw new RuntimeException("Could not find a phosphor script to run " + programName);
    }

    // TODO change the following method to take a Config object
    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.add(adapter.getMainClass());
    commandList.addAll(configList);

    return commandList;
  }

  @Override
  public void writeToFile(Set<ConfigConstraint> constraints) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();
    mapper.writeValue(file, constraints);
  }

  @Override
  public Set<ConfigConstraint> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper
        .readValue(file, new TypeReference<Set<ConfigConstraint>>() {
        });
  }

  @Override
  public String outputDir() {
    return DIRECTORY + "/" + this.getProgramName() + "/cc/constraints";
  }
}
