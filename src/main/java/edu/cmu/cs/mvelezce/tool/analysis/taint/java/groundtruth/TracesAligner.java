package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.EditScript;
import org.apache.commons.text.diff.StringsComparator;

/**
 * Encodes traces to string and uses diff algo by Myers
 */
public class TracesAligner implements Analysis<List<String>> {

  private static final Random RANDOM = new Random();
  private static final int CHAR_MAX_VALUE = Character.MAX_VALUE + 1;

  private final String programName;
  private final Map<Set<String>, List<String>> configsToTraces;
  private final Map<String, Character> traceElementsToChars = new HashMap<>();
  private final Map<Character, String> charsToTraceElements = new HashMap<>();


  TracesAligner(String programName) {
    this.programName = programName;
    this.configsToTraces = new HashMap<>();
  }

  TracesAligner(String programName, Map<Set<String>, List<String>> configsToTraces) {
    this.programName = programName;
    this.configsToTraces = configsToTraces;

    this.encodeTraceElementsAsChars();
  }

  private void encodeTraceElementsAsChars() {
    Set<String> uniqueTraceElements = this.getUniqueTraceElements();
    this.encodeTraceElementsAsChars(uniqueTraceElements);
  }

  @Override
  public List<String> analyze() {
    Set<String> encodedTraces = this.encodeTracesAsStrings();
    String alignedStrings = alignStrings(encodedTraces);

    return this.alignTracesFromStrings(alignedStrings);
  }

  private List<String> alignTracesFromStrings(String alignedStrings) {
    List<String> alignedTraces = new ArrayList<>();

    for (int i = 0; i < alignedStrings.length(); i++) {
      String traceElement = this.charsToTraceElements.get(alignedStrings.charAt(i));
      alignedTraces.add(traceElement);
    }

    return alignedTraces;
  }

  private Set<String> encodeTracesAsStrings() {
    Set<String> encodedTraces = new HashSet<>();

    for (List<String> trace : this.configsToTraces.values()) {
      StringBuilder stringBuilder = new StringBuilder();

      for (String traceElement : trace) {
        Character character = this.traceElementsToChars.get(traceElement);

        if (character != null) {
          stringBuilder.append(character);
        }
      }

      String encodedTrace = stringBuilder.toString();
      encodedTraces.add(encodedTrace);
    }

    return encodedTraces;
  }

  private void encodeTraceElementsAsChars(Set<String> uniqueTraceElements) {
    Set<Character> usedChars = new HashSet<>();

    for (String element : uniqueTraceElements) {
      char character = this.getUniqueChar(usedChars);
      this.traceElementsToChars.put(element, character);
      this.charsToTraceElements.put(character, element);
    }
  }

  private char getUniqueChar(Set<Character> usedChars) {
    char character;

    do {
      character = this.generateRandomChar();

    } while (usedChars.contains(character));

    usedChars.add(character);

    return character;
  }

  private Set<String> getUniqueTraceElements() {
    Set<String> uniqueTraceElements = new HashSet<>();

    for (List<String> trace : this.configsToTraces.values()) {
      List<String> updatedTrace = this.removeCFDEvalFromTrace(trace);
      uniqueTraceElements.addAll(updatedTrace);
    }

    return uniqueTraceElements;
  }

  private List<String> removeCFDEvalFromTrace(List<String> trace) {
    List<String> updatedTrace = new ArrayList<>();

    for (String element : trace) {
      if (element.startsWith(SubtracesLogger.LABEL)) {
        updatedTrace.add(element);
      }
    }

    return updatedTrace;
  }

  // TODO name
  @VisibleForTesting
  static String alignStrings(Set<String> strings) {
    String alignedString = "";

    for (String string : strings) {
      alignedString = TracesAligner.alignStrings(alignedString, string);
    }

    return alignedString;
  }

  private static String alignStrings(String s1, String s2) {
    StringsComparator comparator = new StringsComparator(s1, s2);
    EditScript<Character> script = comparator.getScript();

    LongestCommonSubSequenceVisitor lcsVisitor = new LongestCommonSubSequenceVisitor();
    script.visit(lcsVisitor);

    return listOfCharsToString(lcsVisitor.allChars);
  }

  private static String listOfCharsToString(List<Character> allChars) {
    StringBuilder stringBuilder = new StringBuilder();

    for (Character character : allChars) {
      stringBuilder.append(character);
    }

    return stringBuilder.toString();
  }

  private char generateRandomChar() {
    return (char) RANDOM.nextInt(CHAR_MAX_VALUE);
  }

  @Override
  public List<String> analyze(String[] args) throws IOException {
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

    List<String> alignedTrace = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(alignedTrace);
    }

    return alignedTrace;
  }

  @Override
  public List<String> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<List<String>>() {
    });
  }

  @Override
  public void writeToFile(List<String> alignedTrace) throws IOException {
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, alignedTrace);
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY + "/analysis/spec/alignedTrace/java/programs/" + this.programName;
  }

  private static class LongestCommonSubSequenceVisitor implements CommandVisitor<Character> {

    //    private final List<Character> lcs = new ArrayList<>();
    private final List<Character> allChars = new ArrayList<>();

    @Override
    public void visitInsertCommand(Character c) {
//      System.out.println("insert from second string " + c);
      allChars.add(c);
    }

    @Override
    public void visitKeepCommand(Character c) {
//      System.out.println("keep from first string " + c);
      allChars.add(c);
    }

    @Override
    public void visitDeleteCommand(Character c) {
//      System.out.println("delete from first string " + c);
      allChars.add(c);
    }
  }

}




