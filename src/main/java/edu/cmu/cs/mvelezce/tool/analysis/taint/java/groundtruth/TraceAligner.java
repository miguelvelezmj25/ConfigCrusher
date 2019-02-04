package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.EditScript;
import org.apache.commons.text.diff.StringsComparator;

// TODO encode aligning trace as aligning strings and use the efficient myers algo.
public class TraceAligner {

  private static final Random RANDOM = new Random();
  private static final int CHAR_MAX_VALUE = Character.MAX_VALUE + 1;

  private final String programName;
  private final Map<Set<String>, List<String>> configsToTraces;

  public TraceAligner(String programName, Map<Set<String>, List<String>> configsToTraces) {
    this.programName = programName;
    this.configsToTraces = configsToTraces;

    // TODO encode entries as chars.
  }

  // TODO make private. Only for testing
  public static String alignStrings(Set<String> strings) {
    String alignedString = "";

    for (String string : strings) {
      alignedString = TraceAligner.alignStrings(alignedString, string);
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

    // TODO names
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




