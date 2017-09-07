package edu.cmu.cs.mvelezce.tool.performancemodel;

import edu.cmu.cs.mvelezce.tool.execute.java.DefaultExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class PerformanceEntry2Test {
    @Test
    public void testPerformanceEntry2() throws IOException, InterruptedException {
        String programName = "running-example";

        // Program arguments
        String[] args = new String[0];

        Executor executor = new DefaultExecutor(programName);
        Set<PerformanceEntry2> result = executor.execute(args);
        result.size();
    }

}