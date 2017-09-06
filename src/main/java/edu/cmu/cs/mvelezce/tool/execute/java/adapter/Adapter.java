package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public abstract class Adapter implements IAdapter {

    public static final String CLASS_CONTAINER = "target/classes/";

    private static final String JSON_SIMPLE_PATH = "lib/json-simple-1.1.1.jar";
    private static final String JACKSON_PATH = "/Users/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar:/Users/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.9/jackson-annotations-2.8.9.jar:/Users/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar";

    protected String programName;
    protected String mainClass;
    protected String directory;

    public Adapter(String programName, String mainClass, String directory) {
        this.programName = programName;
        this.mainClass = mainClass;
        this.directory = directory;
    }

    // TODO how to do this better?
    public static String executeJavaProgram(String programName, String mainAdapter, String mainClass, String directory, String args) {
        String command = "java -Xmx8G -cp " + Adapter.CLASS_CONTAINER + ":" + Adapter.JACKSON_PATH + ":" + Adapter.JSON_SIMPLE_PATH + ":" + directory + " " + mainAdapter + " " + programName + " " + mainClass + " " + args;
        System.out.println(command);
        StringBuilder output = new StringBuilder();

        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s;
            while ((s = stdInput.readLine()) != null) {
                if(!s.isEmpty()) {
                    output.append(s).append("\n");
                }
            }

            System.out.println(output);

            output = new StringBuilder();
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((s = stdError.readLine()) != null) {
                if(!s.isEmpty()) {
                    output.append(s).append("\n");
                }
            }

            System.out.println(output);
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        return null;
    }

//    public abstract String[] adaptConfigurationToProgram(Set<String> configuration);
//
//    public abstract Set<String> adaptConfigurationToPerformanceMeasurement(String[] configuration);

    public abstract void execute(Set<String> configuration);

}
