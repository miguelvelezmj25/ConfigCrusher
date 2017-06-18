package edu.cmu.cs.mvelezce.tool;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by mvelezce on 4/28/17.
 */
public abstract class Options {
    private static CommandLine cmd = null;

    public static final String DIRECTORY = "src/main/resources";
    public static final String DOT_JSON = ".json";

    // Component cmd options
    public static final String DELRES = "delres";
    public static final String SAVERES = "saveres";

    public static void getCommandLine(String[] args) {
        org.apache.commons.cli.Options componentOptions = new org.apache.commons.cli.Options();

        Option componentOption = new Option(Options.DELRES, "Deletes the stored result");
        componentOptions.addOption(componentOption);

        componentOption = new Option(Options.SAVERES, "Saves the result");
        componentOptions.addOption(componentOption);

        CommandLineParser parser = new DefaultParser();
        try {
            Options.cmd = parser.parse(componentOptions, args);
        } catch (ParseException e) {
            throw new RuntimeException("Could not parse the options you provided");
        }
    }

    public static void checkIfDeleteResult(File file) throws IOException {
        if(cmd.hasOption(Options.DELRES)) {
            if(file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            }
            else {
                file.delete();
            }
        }
    }

    public static boolean checkIfSave() {
        return cmd.hasOption(Options.SAVERES);
    }

    public static boolean checkIfDeleteResult() {
        return cmd.hasOption(Options.DELRES);
    }
}
