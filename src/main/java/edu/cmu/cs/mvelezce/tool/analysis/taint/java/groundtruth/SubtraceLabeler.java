package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace.SubtraceManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SubtraceLabeler extends BaseDynamicAnalysis<Map<Set<String>, List<String>>> {

  private final Map<Set<String>, List<String>> configsToTraces;

  private SubtraceManager subtraceManager = new SubtraceManager();

  SubtraceLabeler(String programName, Map<Set<String>, List<String>> configsToTraces) {
    super(programName, new HashSet<>(), new HashSet<>());

    this.configsToTraces = configsToTraces;
  }

  @Override
  public Map<Set<String>, List<String>> analyze() {
    Map<Set<String>, List<String>> configsToLabeledTraces = new HashMap<>();

    for (Map.Entry<Set<String>, List<String>> entry : this.configsToTraces.entrySet()) {
      this.subtraceManager = new SubtraceManager();
      List<String> trace = entry.getValue();
      List<String> labeledTrace = this.getLabeledTrace(trace);
      configsToLabeledTraces.put(entry.getKey(), labeledTrace);
    }

    return configsToLabeledTraces;
  }

  private List<String> getLabeledTrace(List<String> trace) {
    if (!this.checkSingleThreadedProgram(trace)) {
      throw new RuntimeException("We expected the program to be single threaded");
    }

    List<String> labeledTrace = new ArrayList<>();
    Deque<UUID> stack = new ArrayDeque<>();

    for (String entry : trace) {
      if (entry.equals(SubtracesLogger.TRUE) || entry.equals(SubtracesLogger.FALSE)) {
        labeledTrace.add(entry);
      }
      else {
        this.processDecision(entry, stack, labeledTrace);
      }
    }

    return labeledTrace;
  }

  private boolean checkSingleThreadedProgram(List<String> trace) {
    Set<String> threadIds = new HashSet<>();

    for (String entry : trace) {
      if (entry.equals(SubtracesLogger.TRUE) || entry.equals(SubtracesLogger.FALSE)) {
        continue;
      }

      String[] elements = entry.split(" ");
      String threadId = elements[0].trim();
      threadIds.add(threadId);
    }

    return threadIds.size() == 1;
  }

  private void processDecision(String entry, Deque<UUID> stack, List<String> labeledTrace) {
    String[] elements = entry.split(" ");

    if (elements.length != 4) {
      throw new RuntimeException("Expected the entry of a trace to have 4 elements: " + entry);
    }

    String action = elements[2].trim();
    String decision = elements[3].trim();

    switch (action) {
      case SubtracesLogger.ENTER_DECISION:
        labeledTrace.add(this.enterDecision(decision, stack));
        break;
      case SubtracesLogger.EXIT_DECISION:
        stack.removeFirst();
        // TODO check that the decision exited is the one that we expected
        break;
      case SubtracesLogger.EXIT_DECISION_AT_RETURN:
        throw new UnsupportedOperationException("Implement");
      default:
        throw new RuntimeException("Unexpected entry: " + entry);
    }
  }

  private String enterDecision(String decision, Deque<UUID> stack) {
    UUID label = this.subtraceManager.getLabel(decision, stack);
    stack.addFirst(label);

    return label.toString();
  }

  // TODO abstract since it is repeated with SubtracesAnalysisExecutor
  @Override
  public void writeToFile(Map<Set<String>, List<String>> configsToTraces) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    List<ConfigToTraceInfo> infos = new ArrayList<>();

    for (Map.Entry<Set<String>, List<String>> entry : configsToTraces.entrySet()) {
      ConfigToTraceInfo configToTraceInfo = new ConfigToTraceInfo(entry.getKey(), entry.getValue());
      infos.add(configToTraceInfo);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, infos);
  }

  @Override
  public Map<Set<String>, List<String>> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY + "/analysis/spec/traces/labeled/java/programs/" + this
        .getProgramName();
  }
}
