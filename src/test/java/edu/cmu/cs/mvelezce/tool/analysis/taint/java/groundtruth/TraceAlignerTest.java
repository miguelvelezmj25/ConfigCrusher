package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import org.junit.Test;

public class TraceAlignerTest {

  @Test
  public void align() {
    String s1 = "abccba";
    String s2 = "abba";

    TraceAligner.align(s1, s2);
  }

}