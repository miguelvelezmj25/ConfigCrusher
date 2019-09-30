package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.varexj;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

public class VarexJAnalysis extends BaseDynamicRegionAnalysis<Set<Constraint>> {

  private static final String VAREXJ_OUTPUT_DIR =
      "/Users/mvelezce/Documents/Programming/Java/runtime-EclipseApplication/test/coverage.xml";
  private static final Map<String, String> PROGRAM_TO_CLASS = new HashMap<>();

  static {
    PROGRAM_TO_CLASS.put(PhosphorExample2Adapter.PROGRAM_NAME, "Example2.java");
    PROGRAM_TO_CLASS.put(PhosphorExample3Adapter.PROGRAM_NAME, "Example3.java");
  }

  private final Set<JavaRegion> sinks;

  VarexJAnalysis(String programName, Set<JavaRegion> sinks) {
    super(programName, new HashSet<>(), new HashSet<>());

    this.sinks = sinks;
  }

  @Override
  public Map<JavaRegion, Set<Constraint>> analyze() throws IOException {
    Coverage output = this.readVarexJOutput();
    Collection<Interaction> programResults = this.getProgramResults(output);
    something(programResults);

    throw new RuntimeException("Implement");
  }

  // TODO implement
  private Map<JavaRegion, Set<Constraint>> something(Collection<Interaction> programResults) {
    Map<JavaRegion, Set<Constraint>> todo = new HashMap<>();

    Map<Integer, Interaction> lineNumbersToInteractions = this
        .cachedLineNumbersToInteractions(programResults);

    for (JavaRegion sink : this.sinks) {
      // Gives us the ctx of the sink being executed
      int lineNumber = sink.getStartRegionIndex();
      Interaction sinkCtx = lineNumbersToInteractions.get(lineNumber);

      if (sinkCtx == null) {
        throw new RuntimeException("There is no interaction for sink at line " + lineNumber);
      }

      // We have to check the next line of a sink to know what taints are influencing the sink
      Interaction sinkInteraction = lineNumbersToInteractions.get(lineNumber + 1);

      if (sinkInteraction == null) {
        throw new RuntimeException("There is no interaction for sink at line " + lineNumber);
      }

      this.buildCtx(sinkCtx);

    }

    return todo;
  }

  private void buildCtx(Interaction sinkCtx) {
    if (sinkCtx.getInteraction() != 1) {
      throw new RuntimeException("We do not know how to handle interactions other than 1");
    }

    Object value = sinkCtx.getValue();

    if (!(value instanceof String)) {
      throw new RuntimeException(
          "The value " + value + " is an instance of " + value.getClass() + ", not of String");
    }

    String constraintInfo = this.getConstraintInfo((String) value);
    Set<String> taints = new HashSet<>(Arrays.asList(constraintInfo.split("&")));

    if (taints.size() == 1) {
      for (String taint : taints) {

      }
    }
  }

  private Map<Integer, Interaction> cachedLineNumbersToInteractions(
      Collection<Interaction> programResults) {
    Map<Integer, Interaction> lineNumbersToInteractions = new HashMap<>();

    for (Interaction interaction : programResults) {
      lineNumbersToInteractions.put(interaction.getLine(), interaction);
    }

    return lineNumbersToInteractions;
  }

  private String getConstraintInfo(String value) {
    String[] entries = value.split("\n");

    if (entries.length != 2) {
      throw new RuntimeException("The interaction value does not have 2 entries");
    }

    String entry = entries[0];
    int indexOfColon = entry.indexOf(":");
    entry = entry.substring(indexOfColon + 1).trim();
    entry = entry.replace("(", "");
    entry = entry.replace(")", "");

    return entry;
  }

  private Collection<Interaction> getProgramResults(Coverage coverage) {
    String programName = this.getProgramName();
    String mainClass = PROGRAM_TO_CLASS.get(programName);

    if (mainClass == null) {
      throw new RuntimeException(
          "Could not find a the main class analyzed by VarexJ for " + programName);
    }

    return coverage.getCoverage(mainClass);
  }

  private Coverage readVarexJOutput() throws IOException {
    try {
      File file = new File(VAREXJ_OUTPUT_DIR);
      XMLReader xmlReader = new XMLReader();

      return xmlReader.readFromFile(file);
    }
    catch (ParserConfigurationException | SAXException | UnsupportedCoverageException e) {
      throw new RuntimeException("There was an error trying to read the output of VarexJ", e);
    }
  }

  @Override
  public Map<JavaRegion, Set<Constraint>> analyze(String[] args) throws IOException {
    Options.getCommandLine(args);

    String outputFile = this.outputDir();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      Collection<File> files = FileUtils.listFiles(file, null, true);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case "
                + outputFile);
      }

      return this.readFromFile(files.iterator().next());
    }

    Map<JavaRegion, Set<Constraint>> regionsToConstraints = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(regionsToConstraints);
    }

    return regionsToConstraints;
  }

  @Override
  public void writeToFile(Map<JavaRegion, Set<Constraint>> regionsToInfo) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  public Map<JavaRegion, Set<Constraint>> readFromFile(File file) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  public Map<Region, Set<Set<String>>> transform(
      Map<? extends Region, Set<Set<String>>> regionsToOptionSet) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  public String outputDir() {
    return BaseDynamicRegionAnalysis.DIRECTORY + "/" + this.getProgramName() + "/varexj";
  }
}
