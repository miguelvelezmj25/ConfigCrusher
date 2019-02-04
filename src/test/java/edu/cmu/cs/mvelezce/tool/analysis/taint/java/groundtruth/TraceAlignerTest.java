package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class TraceAlignerTest {

  @Test
  public void alignStrings() {
    String s1 = "abbacc";
    String s2 = "aacddc";
    String s3 = "aacc";

    Set<String> strings = new HashSet<>();
    strings.add(s1);
    strings.add(s2);
    strings.add(s3);

    String alignedString = TraceAligner.alignStrings(strings);

    String expectedAlignedString = "abbacddc";
    Assert.assertEquals(expectedAlignedString, alignedString);

  }

}