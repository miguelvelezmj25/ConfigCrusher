package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import edu.cmu.cs.mvelezce.java.programs.*;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
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
//        Regions.addProgram(program);

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
//        Regions.addProgram(program);

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
//        Regions.addProgram(program);

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
//        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute7() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep7.CLASS, Sleep7.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep7.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep7.FILENAME, Adapter.MAIN);
//        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute8() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep8.CLASS, Sleep8.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep8.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep8.FILENAME, Adapter.MAIN);
//        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute9() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep9.CLASS, Sleep9.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep9.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep9.FILENAME, Adapter.MAIN);
//        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute10() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep10.CLASS, Sleep10.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep10.CLASS);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Add program region
        JavaRegion program = new JavaRegion(Sleep10.FILENAME, Adapter.MAIN);
//        Regions.addProgram(program);

        // Execute
        adapter.execute(configuration);
    }
}