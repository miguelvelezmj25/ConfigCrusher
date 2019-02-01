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

  // TODO delme, just testing the alignment of strings
  public static void align(String x, String y) {
    StringsComparator comparator = new StringsComparator(x, y);
    EditScript<Character> script = comparator.getScript();

    LongestCommonSubSequence lcs = new LongestCommonSubSequence();
    script.visit(lcs);
    System.out.println(lcs.lcs);
    System.out.println(lcs.all);
  }

  private char generateRandomChar() {
    return (char) RANDOM.nextInt(CHAR_MAX_VALUE);
  }

  private static class LongestCommonSubSequence implements CommandVisitor<Character> {

    private final List<Character> lcs = new ArrayList<>();
    private final List<Character> all = new ArrayList<>();

    @Override
    public void visitInsertCommand(Character c) {
      System.out.println("insert from second string " + c);
      all.add(c);
    }

    @Override
    public void visitKeepCommand(Character c) {
      System.out.println("keep from first string " + c);
      lcs.add(c);
      all.add(c);
    }

    @Override
    public void visitDeleteCommand(Character c) {
      System.out.println("delete from first string " + c);
      all.add(c);
    }
  }

}




