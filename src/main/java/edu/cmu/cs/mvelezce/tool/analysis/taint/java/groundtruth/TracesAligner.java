package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.EditScript;
import org.apache.commons.text.diff.StringsComparator;

// TODO encode aligning trace as aligning strings and use the efficient myers algo.
public class TracesAligner {

  private static final Random RANDOM = new Random();
  private static final int CHAR_MAX_VALUE = Character.MAX_VALUE + 1;

  private final String programName;
  private final Map<Set<String>, List<String>> configsToTraces;
  private final Map<String, Character> traceElementsToChars = new HashMap<>();
  private final Map<Character, String> charsToTraceElements = new HashMap<>();


  public TracesAligner(String programName, Map<Set<String>, List<String>> configsToTraces) {
    this.programName = programName;
    this.configsToTraces = configsToTraces;

    this.encodeTraceElementsAsChars();
  }

  private void encodeTraceElementsAsChars() {
    Set<String> uniqueTraceElements = this.getUniqueTraceElements();
    this.encodeTraceElementsAsChars(uniqueTraceElements);
  }

  public List<String> align() {
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
        stringBuilder.append(this.traceElementsToChars.get(traceElement));
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
      uniqueTraceElements.addAll(trace);
    }

    return uniqueTraceElements;
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




