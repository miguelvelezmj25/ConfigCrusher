package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepAdapter extends Adapter {

    private static final String[] CONFIGURATIONS = {"A", "B", "C", "D"};

    private String programName;
    private String mainClass;
    private String directory;

    public SleepAdapter(String programName, String mainClass, String directory) {
        this.programName = programName;
        this.mainClass = mainClass;
        this.directory = directory;
    }

    @Override
    public void execute(Set<String> configuration) {
        String[] argsArray = SleepAdapter.adaptConfigurationToSleepProgram(configuration);
        StringBuilder args = new StringBuilder();

        for(String arg : argsArray) {
            args.append(arg);
            args.append(" ");
        }

        Adapter.executeJavaProgram(programName, SleepMain.SLEEP_MAIN, this.mainClass, this.directory, args.toString().trim());
    }

    public static String[] adaptConfigurationToSleepProgram(Set<String> configuration) {
        String[] sleepConfiguration = new String[4];

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

}
