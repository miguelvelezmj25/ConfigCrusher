package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by mvelezce on 6/28/17.
 */
public class FormatterTest {

    @Test
    public void instrumentSleep() throws IOException, InterruptedException {
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";

        Formatter.format(srcDirectory, classDirectory);
    }

    @Test
    public void instrumentElevator() throws IOException, InterruptedException {
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/";

        Formatter.format(srcDirectory, classDirectory);
    }

}