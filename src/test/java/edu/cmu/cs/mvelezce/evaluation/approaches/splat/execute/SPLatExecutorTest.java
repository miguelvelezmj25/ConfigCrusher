package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute;

import edu.cmu.cs.mvelezce.tool.compression.Compression;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class SPLatExecutorTest {

    @Test
    public void runningExample() throws IOException {
        String programName = "running-example";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Compression executor = new SPLatExecutor(programName);
        Set<Set<String>> configurations = executor.compressConfigurations(args);
        System.out.println("Configurations executed: " + configurations.size());
    }

    @Test
    public void counter() throws IOException {
        String programName = "pngtasticColorCounter";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Compression executor = new SPLatExecutor(programName);
        Set<Set<String>> configurations = executor.compressConfigurations(args);
        System.out.println("Configurations executed: " + configurations.size());
    }

    @Test
    public void optimizer() throws IOException {
        String programName = "pngtasticOptimizer";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Compression executor = new SPLatExecutor(programName);
        Set<Set<String>> configurations = executor.compressConfigurations(args);
        System.out.println("Configurations executed: " + configurations.size());
    }

    @Test
    public void grep() throws IOException {
        String programName = "grep";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Compression executor = new SPLatExecutor(programName);
        Set<Set<String>> configurations = executor.compressConfigurations(args);
        System.out.println("Configurations executed: " + configurations.size());
    }

    @Test
    public void kanzi() throws IOException {
        String programName = "kanzi";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Compression executor = new SPLatExecutor(programName);
        Set<Set<String>> configurations = executor.compressConfigurations(args);
        System.out.println("Configurations executed: " + configurations.size());
    }

    @Test
    public void prevayler() throws IOException {
        String programName = "prevayler";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Compression executor = new SPLatExecutor(programName);
        Set<Set<String>> configurations = executor.compressConfigurations(args);
        System.out.println("Configurations executed: " + configurations.size());
    }

}
