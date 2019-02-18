package edu.cmu.cs.mvelezce.tool.compression.dynamic.phosphor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ExecTaints;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ExecVarCtx;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.SinkData;
import edu.cmu.cs.mvelezce.tool.compression.BaseDynamicCompression;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PhosphorCompression extends BaseDynamicCompression<Set<Set<Set<String>>>> {

  public PhosphorCompression(String programName) {
    super(programName, new HashSet<>(), new ArrayList<>());
  }

  public PhosphorCompression(String programName, Set<String> options,
      Collection<SinkData> constraints) {
    super(programName, options, constraints);
  }

  @Override
  public Set<Set<Set<String>>> compressConfigurations() {
    Set<ConfigConstraint> configConstraints = this.getConfigConstraints();
    List<String> constraints = this.getStringConstraints(configConstraints);

    return MinConfigsGenerator.getSatConfigs(this.getOptions(), constraints);
  }

  private List<String> getStringConstraints(Set<ConfigConstraint> configConstraints) {
    List<String> stringConstraints = new ArrayList<>();

    for (ConfigConstraint configConstraint : configConstraints) {
      String constraint = this.getConstraint(configConstraint);
      stringConstraints.add(constraint);
    }

    return stringConstraints;
  }

  private String getConstraint(ConfigConstraint configConstraint) {
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, Boolean> partialConfig = configConstraint.getPartialConfig();
    stringBuilder.append("(");

    Iterator<Entry<String, Boolean>> partialConfigIter = partialConfig.entrySet().iterator();

    while (partialConfigIter.hasNext()) {
      Entry<String, Boolean> entry = partialConfigIter.next();

      if (!entry.getValue()) {
        stringBuilder.append("!");
      }

      stringBuilder.append(entry.getKey());

      if (partialConfigIter.hasNext()) {
        stringBuilder.append(" && ");
      }
    }

    stringBuilder.append(")");

    return stringBuilder.toString();
  }

  private Set<ConfigConstraint> getConfigConstraints() {
    Set<ConfigConstraint> configConstraints = new HashSet<>();

    for (SinkData sinkData : this.getConstraints()) {
      Map<ExecVarCtx, ExecTaints> data = sinkData.getData();
      Set<ConfigConstraint> configConstraintsAtSink = this.getConfigConstraintsAtSink(data);
      configConstraints.addAll(configConstraintsAtSink);
    }

    return configConstraints;
  }

  private Set<ConfigConstraint> getConfigConstraintsAtSink(Map<ExecVarCtx, ExecTaints> data) {
    Set<ConfigConstraint> configConstraintsAtSink = new HashSet<>();

    for (Map.Entry<ExecVarCtx, ExecTaints> ctxAndTaints : data.entrySet()) {
      Set<ConfigConstraint> configConstraintsForCtx = this.getConfigConstraintsForCtx(ctxAndTaints);
      configConstraintsAtSink.addAll(configConstraintsForCtx);
    }

    return configConstraintsAtSink;
  }

  private Set<ConfigConstraint> getConfigConstraintsForCtx(
      Entry<ExecVarCtx, ExecTaints> ctxAndTaints) {
    ExecVarCtx execVarCtx = ctxAndTaints.getKey();
    ExecTaints execTaints = ctxAndTaints.getValue();
    List<Set<String>> taints = execTaints.getTaints();

    throw new UnsupportedOperationException("Implement");

//    Set<ConfigConstraint> configConstraintsForTaints = this
//        .getConfigConstraintsForTaints(execVarCtx, taints);
//
//    return new HashSet<>(configConstraintsForTaints);
  }

  private Set<ConfigConstraint> getConfigConstraintsForTaints(ExecVarCtx execVarCtx,
      Set<Set<String>> taints) {
    Set<ConfigConstraint> configConstraintsForTaints = new HashSet<>();

    for (Set<String> taint : taints) {
      Set<ConfigConstraint> configConstraintsForTaint = getConfigConstraintsForTaint(execVarCtx,
          taint);
      configConstraintsForTaints.addAll(configConstraintsForTaint);
    }

    return configConstraintsForTaints;
  }

  // TODO test what happens with one ctx multiple taints, one ctx multiple execs
  private Set<ConfigConstraint> getConfigConstraintsForTaint(ExecVarCtx execVarCtx,
      Set<String> taint) {
    Set<ConfigConstraint> configConstraints = new HashSet<>();
    Set<Set<String>> configs = Helper.getConfigurations(taint);

    for (Set<String> config : configs) {
      ConfigConstraint configConstraint = new ConfigConstraint();

      for (String option : taint) {
        boolean value = false;

        if (config.contains(option)) {
          value = true;
        }

        configConstraint.addEntry(option, value);
      }

      Map<String, Boolean> execVarCtxPartialConfig = execVarCtx.getPartialConfig();

      for (Map.Entry<String, Boolean> entry : execVarCtxPartialConfig.entrySet()) {
        configConstraint.addEntry(entry.getKey(), entry.getValue());
      }

      configConstraints.add(configConstraint);
    }

    return configConstraints;
  }

  @Override
  public Set<Set<Set<String>>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<Set<Set<String>>>>() {
    });
  }

  @Override
  public String getOutputDir() {
    return Options.DIRECTORY + "/compression/phosphor/java/programs/" + this.getProgramName();
  }
}
