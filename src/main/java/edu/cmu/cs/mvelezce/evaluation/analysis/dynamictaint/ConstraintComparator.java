package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ConstraintComparator {

  private static final String BASE_DIR = "./src/main/resources/evaluation/dynamicTaint/";
  private static final String DATA_DIR = "/constraints/data/";

  static Set<ConfigConstraint> readFromFile(String programName, String fileName)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File file = new File(BASE_DIR + programName + DATA_DIR + fileName);

    return mapper
        .readValue(file, new TypeReference<Set<ConfigConstraint>>() {
        });
  }

}