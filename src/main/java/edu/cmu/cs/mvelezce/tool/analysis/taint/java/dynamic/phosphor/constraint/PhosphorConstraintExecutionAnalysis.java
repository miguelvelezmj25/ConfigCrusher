package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;

public class PhosphorConstraintExecutionAnalysis {

  private static final String PHOSPHOR_OUTPUT_DIR =
      BaseAdapter.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/implicit-optimized";

  private final String programName;

  public PhosphorConstraintExecutionAnalysis(String programName) {
    this.programName = programName;
  }

  public Set<DecisionTaints> getResults() throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + programName;
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dir + " must have 1 file.");
    }

    return this.readResults(serializedFiles.iterator().next());
  }

  private Collection<File> getSerializedFiles(String dir) {
    File dirFile = new File(dir);

    return FileUtils.listFiles(dirFile, null, false);
  }

  private Set<DecisionTaints> readResults(File serializedFile) throws IOException {
    return this.deserialize(serializedFile);
  }

  private Set<DecisionTaints> deserialize(File file) throws IOException {
    Set<DecisionTaints> results = new HashSet<>();

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;
    while ((line = reader.readLine()) != null) {
      Taint contextTaints = this.getTaints(reader.readLine());
      Taint conditionTaints = this.getTaints(reader.readLine());

      DecisionTaints decisionTaints = new DecisionTaints(line, contextTaints, conditionTaints);
      results.add(decisionTaints);
    }

    reader.close();

    return results;
  }

  @Nullable
  private Taint getTaints(String data) {
    if (data.isEmpty()) {
      return null;
    }

    int[] tags = this.getTags(data);

    return Taint.getCCResultTaint(tags);
  }

  private int[] getTags(String data) {
//    if (data.length() != 1) {
//      throw new RuntimeException("The entry should have only one character");
//    }
//
    List<Integer> tags = new ArrayList<>();
//    char characer = data.charAt(0);
//    int tag = (int) characer;
    tags.add(Integer.valueOf(data));

    return ArrayUtils.toPrimitive(tags.toArray(new Integer[0]));
  }

}
