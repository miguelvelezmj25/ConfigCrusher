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

  public Set<Set<String>> getMinConfigsToExecute(String[] args)
      throws IOException, InterruptedException {
    Map<Set<String>, List<String>> configsToTraces = this.executeSubtraceInstrumentedProgram(args);
    List<String> alignedTrace = this.alignTraces(args, configsToTraces);

    throw new UnsupportedOperationException("Implement");
//    return this.getMinConfigsToExecute(args, subtraceAnalysisInfos);
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

  private Set<Set<String>> getMinConfigsToExecute(String[] args,
      Set<SubtraceAnalysisInfo> subtraceAnalysisInfos)
      throws IOException {
    SatConfigAnalyzer satConfigAnalyzer = new SatConfigAnalyzer(this.programName,
        subtraceAnalysisInfos, this.options);

    return satConfigAnalyzer.analyze(args);
  }

}
