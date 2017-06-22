package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class GPLAdapter extends Adapter {

    private static final String[] CONFIGURATIONS = {"WEIGHTED", "SHORTEST"};

    public static final String TEST_DIRECTORY = "test/out/production/test";

    private String programName;
    private String mainClass;
    private String directory;

    public GPLAdapter(String programName, String mainClass, String directory) {
        this.programName = programName;
        this.mainClass = mainClass;
        this.directory = directory;
    }

    @Override
    public void execute(Set<String> configuration) {
        String[] argsArray = GPLAdapter.adaptConfigurationToGPLProgram(configuration);
        StringBuilder args = new StringBuilder();

        for(String arg : argsArray) {
            args.append(arg);
            args.append(" ");
        }

        Adapter.executeJavaProgram(programName, GPLMain.GPL_MAIN, this.mainClass, this.directory, args.toString().trim());
    }

    public static String[] adaptConfigurationToGPLProgram(Set<String> configuration) {
        String[] sleepConfiguration = new String[CONFIGURATIONS.length];

        for(int i = 0; i < sleepConfiguration.length; i++) {
            if(configuration.contains(GPLAdapter.CONFIGURATIONS[i])) {
                sleepConfiguration[i] = "true";
            }
            else {
                sleepConfiguration[i] = "false";
            }
        }

        return sleepConfiguration;
    }

    public static Set<String> adaptConfigurationToPerformanceMeasurement(String[] configuration) {
        Set<String> performanceConfiguration = new HashSet<>();

        for(int i = 0; i < configuration.length; i++) {
            if(configuration[i].equals("true")) {
                performanceConfiguration.add(GPLAdapter.CONFIGURATIONS[i]);
            }
        }

        return performanceConfiguration;
    }

}
