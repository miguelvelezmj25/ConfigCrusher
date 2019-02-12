package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO measure overhead (time and memory)
public class SubtracesPipeline {

  private final String programName;
  private final Set<String> options;

  public SubtracesPipeline(String programName, Set<String> options) {
    this.programName = programName;
    this.options = options;
  }

  public Set<Set<Set<String>>> getMinConfigsToExecute(String[] args)
      throws IOException, InterruptedException {
    Map<Set<String>, List<String>> configsToTraces = this.executeSubtraceInstrumentedProgram(args);
    List<String> alignedTrace = this.alignTraces(args, configsToTraces);
    Set<ConfigSubtraceValueInfo> configSubtraceValues = this
        .getValuesForSubtraceForConfigs(args, configsToTraces, alignedTrace);
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = this
        .getConfigsWithSubtraceValues(args, configSubtraceValues);

    return this.getMinConfigsToExecute(args, subtraceAnalysisInfos);
  }

  private Set<SubtraceAnalysisInfo> getConfigsWithSubtraceValues(String[] args,
      Set<ConfigSubtraceValueInfo> configSubtraceValues) throws IOException {
    SubtracesAnalysis subtracesAnalysis = new SubtracesAnalysis(this.programName,
        configSubtraceValues);

    return subtracesAnalysis.analyze(args);
  }

  private Set<ConfigSubtraceValueInfo> getValuesForSubtraceForConfigs(String[] args,
      Map<Set<String>, List<String>> configsToTraces, List<String> alignedTrace)
      throws IOException {
    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(this.programName,
        configsToTraces, alignedTrace);

    return subtracesValueAnalysis.analyze(args);
  }

  private List<String> alignTraces(String[] args, Map<Set<String>, List<String>> configsToTraces)
      throws IOException {
    TracesAligner tracesAligner = new TracesAligner(this.programName, configsToTraces);

    return tracesAligner.analyze(args);
  }

  private Map<Set<String>, List<String>> executeSubtraceInstrumentedProgram(String[] args)
      throws IOException, InterruptedException {
    SubtracesAnalysisExecutor executor = new SubtracesAnalysisExecutor(this.programName,
        this.options);

    return executor.analyze(args);
  }

  private Set<Set<Set<String>>> getMinConfigsToExecute(String[] args,
      Set<SubtraceAnalysisInfo> subtraceAnalysisInfos)
      throws IOException {
    SatConfigAnalyzer satConfigAnalyzer = new SatConfigAnalyzer(this.programName,
        subtraceAnalysisInfos, this.options);

    return satConfigAnalyzer.analyze(args);
  }

}
