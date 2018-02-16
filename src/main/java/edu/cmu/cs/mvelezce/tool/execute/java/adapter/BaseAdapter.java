package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.CompileInstrumenter;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static edu.cmu.cs.mvelezce.tool.Options.USER_HOME;

/**
 * Created by miguelvelez on 4/30/17.
 */
public abstract class BaseAdapter implements Adapter {

    private static final String CLASS_CONTAINER = "target/classes/";
    private static final String JACKSON_PATH = USER_HOME + "/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar:" + USER_HOME + "/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.9/jackson-annotations-2.8.9.jar:" + USER_HOME + "/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar:/home/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar:/home/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.9/jackson-annotations-2.8.9.jar:/home/mvelezce/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar:" + USER_HOME + "/.m2/repository/commons-cli/commons-cli/1.4/commons-cli-1.4.jar:" + USER_HOME + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar";
    // TODO figure out what prevayler's path is
    private static final String PREVAYLER_PATH = USER_HOME + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar:" + USER_HOME + "/.m2/repository/log4j/log4j/1.2.15/log4j-1.2.15.jar:" + USER_HOME + "/.m2/repository/com/thoughtworks/xstream/xstream/1.4.5/xstream-1.4.5.jar";
    private static final String KANZI_PATH = USER_HOME + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar";
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
        File m2Dir = new File(CompileInstrumenter.M2_DIR);
        Collection<File> m2Files = FileUtils.listFiles(m2Dir, new String[]{"jar"}, true);

        StringBuilder cp = new StringBuilder();

        for(File jarFile : m2Files) {
            cp.append(jarFile);
            cp.append(CompileInstrumenter.sep);
        }

        cp.deleteCharAt(cp.length() - 1);

        StringBuilder output = new StringBuilder();
        List<String> commandList = new ArrayList<>();
        commandList.add("java");
//        commandList.add("-server");
        commandList.add("-Xms10G");
        commandList.add("-Xmx10G");
//        commandList.add("-XX:MetaspaceSize=10G");
//        commandList.add("-XX:MaxMetaspaceSize=10G");
//        commandList.add("-Xmn10G");
        commandList.add("-XX:+UseConcMarkSweepGC");
        commandList.add("-cp");
//        commandList.add(BaseAdapter.CLASS_CONTAINER + ":" + BaseAdapter.JACKSON_PATH + ":" + this.directory);

//        commandList.add(BaseAdapter.CLASS_CONTAINER + ":" + BaseAdapter.JACKSON_PATH + ":" + cp.toString() + ":" + this.directory);
        commandList.add(this.directory + ":" + BaseAdapter.CLASS_CONTAINER + ":" + BaseAdapter.JACKSON_PATH + ":" + cp.toString());


//        commandList.add(BaseAdapter.CLASS_CONTAINER + ":" + BaseAdapter.JACKSON_PATH + ":" + KANZI_PATH + ":" + this.directory);
//        commandList.add(BaseAdapter.CLASS_CONTAINER + ":" + BaseAdapter.JACKSON_PATH + ":" + PREVAYLER_PATH + ":" + this.directory);
        commandList.add(mainAdapter);
        commandList.add(this.programName);
        commandList.add(this.mainClass);
        commandList.addAll(Arrays.asList(args));

        String[] command = new String[commandList.size()];
        command = commandList.toArray(command);
        System.out.println(Arrays.toString(command));
        Process process = Runtime.getRuntime().exec(command);

        System.out.println("Output: ");
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String string;

        while((string = inputReader.readLine()) != null) {
            if(!string.isEmpty()) {
                System.out.println(string);
//                output.append(string).append("\n");
            }
        }

        System.out.println(output);

        System.out.println("Errors: ");
        output = new StringBuilder();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        while((string = errorReader.readLine()) != null) {
            if(!string.isEmpty()) {
                System.out.println(string);
//                output.append(string).append("\n");
            }
        }

        System.out.println(output);

        process.waitFor();

        if(!output.toString().isEmpty()) {
            throw new IOException();
        }
    }

}
