package edu.cmu.cs.mvelezce.analysis.instrumentation;

import org.junit.Test;

/**
 * Created by mvelezce on 3/31/17.
 */
public class PlayTest {

    @Test
    public void testReadJar() throws Exception {
        Play.readJar("org.sat4j.core.jar");
    }

    @Test
    public void testReadFile() throws Exception {
        Play.readFile("edu/cmu/cs/mvelezce/analysis/instrumentation/Play");
    }

}