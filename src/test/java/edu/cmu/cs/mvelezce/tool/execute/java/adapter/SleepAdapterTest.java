package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import edu.cmu.cs.mvelezce.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.java.programs.Sleep2;
import edu.cmu.cs.mvelezce.java.programs.Sleep3;
import edu.cmu.cs.mvelezce.java.programs.Sleep4;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
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
        Adapter adapter = new SleepAdapter(Sleep1.CLASS, Sleep1.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep1.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep1.FILENAME, Adapter.MAIN);
        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute2() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep2.CLASS, Sleep2.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep2.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep2.FILENAME, Adapter.MAIN);
        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute3() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep3.CLASS, Sleep3.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep3.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep3.FILENAME, Adapter.MAIN);
        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute4() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep4.CLASS, Sleep4.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep4.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep4.FILENAME, Adapter.MAIN);
        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

}