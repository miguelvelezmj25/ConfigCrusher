package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.cc.TaintLabel;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

public class PhosphorAnalysis extends BaseDynamicAnalysis {

  private static final String PHOSPHOR_OUTPUT_DIR =
      BaseAdapter.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/implicit-optimized";

  public PhosphorAnalysis(String programName) {
    super(programName);
  }

  @Override
  public Map<JavaRegion, Set<Set<String>>> analyze() throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + this.getProgramName();
    Collection<File> serializedFiles = getSerializedFiles(dir);

    if (serializedFiles.size() != 2) {
      throw new RuntimeException("The directory " + dir + " does not have 2 files.");
    }

    readPhosphorTaintResults(serializedFiles);

    return null;

  }

  private static void readPhosphorTaintResults(Collection<File> serializedFiles)
      throws IOException {
    Map<String, Set<TaintLabel>> sinksToTaintsFromTaints = new HashMap<>();
    Map<String, Set<TaintLabel>> sinksToTaintsFromStacks = new HashMap<>();

    for (File file : serializedFiles) {
      if (file.getName().contains("taints")) {
        sinksToTaintsFromTaints = deserialize(file);
      }
      else {
        sinksToTaintsFromStacks = deserialize(file);
      }
    }

    Map<String, Set<TaintLabel>> sinksToTaintLabels = merge(sinksToTaintsFromTaints,
        sinksToTaintsFromStacks);

    Map<String, Set<String>> sinksToTaints = changeTaintLabelsToTaints(sinksToTaintLabels);

    for (Map.Entry<String, Set<String>> entry : sinksToTaints.entrySet()) {
      System.out.println(entry.getKey() + " --> " + entry.getValue());
    }
  }

  private static Map<String, Set<String>> changeTaintLabelsToTaints(
      Map<String, Set<TaintLabel>> sinksToTaintLabels) {
    Map<String, Set<String>> sinksToTaints = new HashMap<>();

    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaintLabels.entrySet()) {
      Set<String> taints = new HashSet<>();

      for (TaintLabel taintLabel : entry.getValue()) {
        taints.add(taintLabel.getSource());
      }

      sinksToTaints.put(entry.getKey(), taints);
    }

    return sinksToTaints;
  }

  private static Map<String, Set<TaintLabel>> merge(Map<String, Set<TaintLabel>> sinksToTaints1,
      Map<String, Set<TaintLabel>> sinksToTaints2) {
    Map<String, Set<TaintLabel>> sinksToTaints = new HashMap<>(sinksToTaints1);

    for (String sink : sinksToTaints2.keySet()) {
      if (!sinksToTaints.containsKey(sink)) {
        sinksToTaints.put(sink, new HashSet<>());
      }
    }

    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaints2.entrySet()) {
      String sink = entry.getKey();
      Set<TaintLabel> taints = sinksToTaints.get(sink);
      taints.addAll(entry.getValue());
      sinksToTaints.put(sink, taints);
    }

    return sinksToTaints;
  }

  private static Map<String, Set<TaintLabel>> deserialize(File file)
      throws IOException {
    FileInputStream fis = new FileInputStream(file);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Map<String, Set<TaintLabel>> sinksToTaints;

    try {
      sinksToTaints = (Map<String, Set<TaintLabel>>) ois.readObject();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    ois.close();
    fis.close();

    return sinksToTaints;
  }

  private static Collection<File> getSerializedFiles(String dir) {
    File dirFile = new File(dir);

    return FileUtils.listFiles(dirFile, null, false);
  }

  static Set<Map<String, Boolean>> calculateConstraints(Set<Set<String>> taintsAtSinks) {
    if (taintsAtSinks == null || taintsAtSinks.isEmpty()) {
      throw new IllegalArgumentException("The taints at sinks cannot be empty");
    }

    Set<Map<String, Boolean>> constraints = new HashSet<>();

    for (Set<String> taintsAtSink : taintsAtSinks) {
      constraints.addAll(PhosphorAnalysis.buildConstraints(taintsAtSink));
    }

    return constraints;
  }

  /**
   * Builds a set of pratial configurations
   */
  static Set<Map<String, Boolean>> buildConstraints(Set<String> taintsAtSink) {
    if (taintsAtSink == null || taintsAtSink.isEmpty()) {
      throw new IllegalArgumentException("The taints at sink cannot be empty");
    }

    Set<Map<String, Boolean>> constraints = new HashSet<>();
    Set<Set<String>> configs = Helper.getConfigurations(taintsAtSink);

    for (Set<String> config : configs) {
      Map<String, Boolean> constraint = new HashMap<>();

      for (String taint : taintsAtSink) {
        boolean value = false;

        if (config.contains(taint)) {
          value = true;
        }

        constraint.put(taint, value);
      }

      constraints.add(constraint);
    }

    return constraints;
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Pair<Map<String, Boolean>, Set<String>> buildConfiguration(
      Set<Map<String, Boolean>> constraintsToEvaluate) {
    if (constraintsToEvaluate == null || constraintsToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
    }

    Map<String, Boolean> constraintToEvaluate = constraintsToEvaluate.iterator().next();
    Set<String> config = completeConfig(constraintToEvaluate);

    return Pair.of(constraintToEvaluate, config);
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Set<String> completeConfig(Map<String, Boolean> constraintToEvaluate) {
    if (constraintToEvaluate == null || constraintToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraint to evaluate should not be empty");
    }

    Set<String> config = new HashSet<>();

    for (Map.Entry<String, Boolean> entry : constraintToEvaluate.entrySet()) {
      if (entry.getValue()) {
        config.add(entry.getKey());
      }
    }

    return config;
  }

}
