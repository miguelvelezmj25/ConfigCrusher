package edu.cmu.cs.mvelezce.tool;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * TODO add options for each component of the pipeline
 * Created by mvelezce on 4/28/17.
 */
public abstract class Options {

    public static final String USER_HOME = System.getProperty("user.home");

    public static final String DIRECTORY = "src/main/resources";
    public static final String DOT_JSON = ".json";
    public static final String DOT_CSV = ".csv";
    // Component cmd options
    public static final String DELRES = "delres";
    public static final String SAVERES = "saveres";
    public static final String ITERATIONS = "i";
    private static CommandLine cmd = null;

    public static void getCommandLine(String[] args) {
        org.apache.commons.cli.Options componentOptions = new org.apache.commons.cli.Options();

        Option componentOption = new Option(Options.DELRES, "Deletes the stored result");
        componentOptions.addOption(componentOption);

        componentOption = new Option(Options.SAVERES, "Saves the result");
        componentOptions.addOption(componentOption);

        componentOption = new Option(Options.ITERATIONS, true, "Iterations");
        componentOptions.addOption(componentOption);

        CommandLineParser parser = new DefaultParser();

        try {
            Options.cmd = parser.parse(componentOptions, args);
        } catch(ParseException e) {
            throw new RuntimeException("Could not parse the options you provided");
        }
    }

    public static void checkIfDeleteResult(File file) throws IOException {
        if(cmd.hasOption(Options.DELRES)) {
            if(file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    public static boolean checkIfSave() {
        return cmd.hasOption(Options.SAVERES);
    }

    public static boolean checkIfDeleteResult() {
        return cmd.hasOption(Options.DELRES);
    }

    public static int getIterations() {
        String iterations = cmd.getOptionValue(Options.ITERATIONS).trim();
        return Integer.parseInt(iterations);
    }
}
