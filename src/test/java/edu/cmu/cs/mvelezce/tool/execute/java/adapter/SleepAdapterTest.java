package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepAdapterTest {

    @Test
    public void testExecute1() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep1.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep1.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep1.FILENAME, Adapter.MAIN);
        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

}