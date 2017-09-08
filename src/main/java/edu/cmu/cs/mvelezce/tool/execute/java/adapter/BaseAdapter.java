package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by miguelvelez on 4/30/17.
 */
public abstract class BaseAdapter implements Adapter {

    public static final String TEST_DIRECTORY = "test/out/production/test";
    private static final String CLASS_CONTAINER = "target/classes/";
    private static final String JACKSON_PATH = "/Users/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar:/Users/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.9/jackson-annotations-2.8.9.jar:/Users/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar";
    private String programName;
    private String mainClass;
    private String directory;
    private List<String> options;

    public BaseAdapter(String programName, String mainClass, String directory, List<String> options) {
        this.programName = programName;
        this.mainClass = mainClass;
        this.directory = directory;
        this.options = options;
    }

    @Override
    public Set<String> configurationAsSet(String[] configuration) {
        Set<String> performanceConfiguration = new HashSet<>();

        for(int i = 0; i < configuration.length; i++) {
            if(configuration[i].equals("true")) {
                performanceConfiguration.add(this.options.get(i));
            }
        }

        return performanceConfiguration;
    }

    @Override
    public String[] configurationAsMainArguments(Set<String> configuration) {
        String[] sleepConfiguration = new String[this.options.size()];

        for(int i = 0; i < sleepConfiguration.length; i++) {
            if(configuration.contains(this.options.get(i))) {
                sleepConfiguration[i] = "true";
            }
            else {
                sleepConfiguration[i] = "false";
            }
        }

        return sleepConfiguration;
    }

    public String getProgramName() {
        return programName;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getDirectory() {
        return directory;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public void execute(Set<String> configuration) throws IOException, InterruptedException {
        this.execute(configuration, 0);
    }

    @Override
    public void execute(String mainAdapter, String[] args) throws InterruptedException, IOException {
        StringBuilder output = new StringBuilder();
        List<String> commandList = new ArrayList<>();
        commandList.add("java");
        commandList.add("-Xmx8G");
        commandList.add("-cp");
        commandList.add(BaseAdapter.CLASS_CONTAINER + ":" + BaseAdapter.JACKSON_PATH + ":" + this.directory);
        commandList.add(mainAdapter);
        commandList.add(this.programName);
        commandList.add(this.mainClass);
        commandList.addAll(Arrays.asList(args));

        String[] command = new String[commandList.size()];
        command = commandList.toArray(command);
        System.out.println(Arrays.toString(command));
        Process process = Runtime.getRuntime().exec(command);

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String string;

        while ((string = inputReader.readLine()) != null) {
            if(!string.isEmpty()) {
                output.append(string).append("\n");
            }
        }

        System.out.println(output);

        output = new StringBuilder();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        while ((string = errorReader.readLine()) != null) {
            if(!string.isEmpty()) {
                output.append(string).append("\n");
            }
        }

        System.out.println(output);

        process.waitFor();

        if(!output.toString().isEmpty()) {
            throw new IOException();
        }
    }

}
