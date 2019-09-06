package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace.SubtraceLabel;
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

  SubtraceLabeler(String programName) {
    this(programName, new HashMap<>());
  }

  SubtraceLabeler(String programName, Map<Set<String>, List<String>> configsToTraces) {
    super(programName, new HashSet<>(), new HashSet<>());

    this.configsToTraces = configsToTraces;
    System.err.println(
        "Check this labeler since there might be some cases (e.g., exceptions) that we are not handling correctly");
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
      } else {
        this.processDecision(entry, stack, labeledTrace);
      }
    }

    if (!stack.isEmpty()) {
      this.printStack(stack);
      throw new RuntimeException("The stack is not empty");
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
        this.exitDecision(decision, stack);
        // TODO check that the decision exited is the one that we expected
        break;
      case SubtracesLogger.EXIT_DECISION_AT_RETURN:
        this.exitDecisionAtReturn(decision, stack);
        break;
      default:
        throw new RuntimeException("Unexpected entry: " + entry);
    }
  }

  private void exitDecisionAtReturn(String decision, Deque<UUID> stack) {
    while (!stack.isEmpty() && this.isDecisionAtTopOfStack(decision, stack)) {
      stack.removeFirst();
    }
  }

  private boolean isDecisionAtTopOfStack(String decision, Deque<UUID> stack) {
    String decisionInStack = this.getTopDecisionInStack(stack);

    String[] decisionEntries = decision.split("\\.");
    String[] decisionInStackEntries = decisionInStack.split("\\.");

    return decisionEntries[0].equals(decisionInStackEntries[0])
        && decisionEntries[1].equals(decisionInStackEntries[1]);
  }

  private void printStack(Deque<UUID> stack) {
    Map<UUID, SubtraceLabel> labelsToSubtraceLabels = SubtraceManager.getLabelsToSubtraceLabels();

    for (UUID uuid : stack) {
      System.out.println(labelsToSubtraceLabels.get(uuid));
    }

    System.out.println();
  }

  private String getTopDecisionInStack(Deque<UUID> stack) {
    UUID uuidTop = stack.peekFirst();
    Map<UUID, SubtraceLabel> labelsToSubtraceLabels = SubtraceManager.getLabelsToSubtraceLabels();
    SubtraceLabel subtraceLabel = labelsToSubtraceLabels.get(uuidTop);

    if (subtraceLabel == null) {
      throw new RuntimeException(
          "Could not find a subtraceLabel object corresponding to the uuid " + uuidTop);
    }

    return subtraceLabel.getDecision();
  }

  private boolean isSameDecisionAtTopOfStack(String decision, Deque<UUID> stack) {
    String decisionInStack = getTopDecisionInStack(stack);

    return decisionInStack.equals(decision);
  }

  private void exitDecision(String decision, Deque<UUID> stack) {
    while (!stack.isEmpty() && this.isSameDecisionAtTopOfStack(decision, stack)) {
      stack.removeFirst();
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

  // TODO abstract since it is repeated with SubtracesAnalysisExecutor
  @Override
  public Map<Set<String>, List<String>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<ConfigToTraceInfo> configToTraceInfoList =
        mapper.readValue(file, new TypeReference<List<ConfigToTraceInfo>>() {});

    Map<Set<String>, List<String>> configsToLabeledTraces = new HashMap<>();

    for (ConfigToTraceInfo configToTraceInfo : configToTraceInfoList) {
      configsToLabeledTraces.put(configToTraceInfo.getConfig(), configToTraceInfo.getTrace());
    }

    return configsToLabeledTraces;
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY
        + "/analysis/spec/traces/labeled/java/programs/"
        + this.getProgramName();
  }
}
