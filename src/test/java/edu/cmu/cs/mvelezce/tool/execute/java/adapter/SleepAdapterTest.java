package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import edu.cmu.cs.mvelezce.*;
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
        Adapter adapter = new SleepAdapter(Sleep1.CLASS, Sleep1.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute2() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep2.CLASS, Sleep2.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute3() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep3.CLASS, Sleep3.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute4() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep4.CLASS, Sleep4.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute7() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep7.CLASS, Sleep7.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute8() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep8.CLASS, Sleep8.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute9() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep9.CLASS, Sleep9.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Execute
        adapter.execute(configuration);
    }

    @Test
    public void testExecute10() throws Exception {
        // Adapter
        Adapter adapter = new SleepAdapter(Sleep10.CLASS, Sleep10.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        // Execute
        adapter.execute(configuration);
    }
}