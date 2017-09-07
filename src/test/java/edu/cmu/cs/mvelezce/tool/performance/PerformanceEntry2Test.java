package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.tool.execute.java.DefaultExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

public class PerformanceEntry2Test {
    @Test
    public void testPerformanceEntry2() throws IOException {
        String programName = "running-example";

        // Program arguments
        String[] args = new String[0];

        Executor executor = new DefaultExecutor(programName);
        Set<PerformanceEntry2> result = executor.execute(args);
        result.size();
    }

}