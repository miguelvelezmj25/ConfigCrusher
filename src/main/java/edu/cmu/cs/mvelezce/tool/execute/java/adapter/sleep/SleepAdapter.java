package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepAdapter extends Adapter {

    public static final String TEST_DIRECTORY = "test/out/production/test";
    private static final String[] CONFIGURATIONS = {"A", "B", "C", "D", "IA", "DA"};

    public SleepAdapter(String programName, String mainClass, String directory) {
        super(programName, mainClass, directory);
    }

    public static String[] adaptConfigurationToProgram(Set<String> configuration) {
        String[] sleepConfiguration = new String[CONFIGURATIONS.length];

        for(int i = 0; i < sleepConfiguration.length; i++) {
            if(configuration.contains(SleepAdapter.CONFIGURATIONS[i])) {
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
                performanceConfiguration.add(SleepAdapter.CONFIGURATIONS[i]);
            }
        }

        return performanceConfiguration;
    }

    @Override
    public void execute(Set<String> configuration) {
        String[] argsArray = SleepAdapter.adaptConfigurationToProgram(configuration);
        StringBuilder args = new StringBuilder();

        for(String arg : argsArray) {
            args.append(arg);
            args.append(" ");
        }

        Adapter.executeJavaProgram(programName, SleepMain.SLEEP_MAIN, this.mainClass, this.directory, args.toString().trim());
    }

}
