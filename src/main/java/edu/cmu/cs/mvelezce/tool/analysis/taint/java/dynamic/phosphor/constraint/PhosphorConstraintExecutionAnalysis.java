package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public class PhosphorConstraintExecutionAnalysis {

  private static final String PHOSPHOR_OUTPUT_DIR =
      BaseAdapter.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/implicit-optimized";

  private final String programName;

  PhosphorConstraintExecutionAnalysis(String programName) {
    this.programName = programName;
  }

  Set<DecisionTaints> getResults() throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + programName;
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dir + " must have 1 file.");
    }

    List<DecisionTaints> taintResults = this.readResults(serializedFiles.iterator().next());

    return new HashSet<>(taintResults);
  }

  private Collection<File> getSerializedFiles(String dir) {
    File dirFile = new File(dir);

    return FileUtils.listFiles(dirFile, null, false);
  }

  private List<DecisionTaints> readResults(File serializedFile) throws IOException {
    return this.deserialize(serializedFile);
  }

  private List<DecisionTaints> deserialize(File file) throws IOException {
    FileInputStream fis = new FileInputStream(file);
    ObjectInputStream ois = new ObjectInputStream(fis);

    try {
      return (List<DecisionTaints>) ois.readObject();
    }
    catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(cnfe);
    } finally {
      ois.close();
      fis.close();
    }
  }

}
