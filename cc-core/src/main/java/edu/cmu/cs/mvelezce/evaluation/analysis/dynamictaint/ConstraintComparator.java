package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public abstract class ConstraintComparator extends BaseComparator {

  private static final String DATA_DIR = "/constraints/data/";

  protected ConstraintComparator(String programName) {
    super(programName);
  }

  protected abstract String getDir();

  public Set<ConfigConstraint> readFromFile(String programName, String fileName)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File file = new File(BASE_DIR + this.getDir() + programName + DATA_DIR + fileName);

    return mapper
        .readValue(file, new TypeReference<Set<ConfigConstraint>>() {
        });
  }

  public void compareConstraints(Set<ConfigConstraint> singleConstraints,
      Set<ConfigConstraint> multiConstraints) throws FileNotFoundException {
    writeMissingConstraintsInSingle(singleConstraints, multiConstraints);
//    writeMissingConstraintsInMulti(singleConstraints, multiConstraints);
  }

  private void writeMissingConstraintsInMulti(Set<ConfigConstraint> singleConstraints,
      Set<ConfigConstraint> multiConstraints) {
//    writeMissingConstraints(multiConstraints, singleConstraints);
  }

  private void writeMissingConstraintsInSingle(Set<ConfigConstraint> singleConstraints,
      Set<ConfigConstraint> multiConstraints) throws FileNotFoundException {
    File outputFile = new File(
        BASE_DIR + this.getDir() + this.getDir() + MISSING_DIR + "single.txt");
    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);

    writeMissingConstraints(writer, singleConstraints, multiConstraints);

    writer.close();
  }

  private void writeMissingConstraints(PrintWriter writer, Set<ConfigConstraint> constraints1,
      Set<ConfigConstraint> constraints2) {
    Set<ConfigConstraint> missingConstraints = new HashSet<>(constraints2);
    missingConstraints.removeAll(constraints1);

    throw new UnsupportedOperationException("Finish implementing");
  }
}