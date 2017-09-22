package edu.cmu.cs.mvelezce.tool.performance.entry;

import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntry2;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class PerformanceEntry2Test {
    @Test
    public void testPerformanceEntry2() throws IOException, InterruptedException {
        String programName = "running-example";

        // Program arguments
        String[] args = new String[0];

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntry2> result = executor.execute(args);
        result.size();
    }

}