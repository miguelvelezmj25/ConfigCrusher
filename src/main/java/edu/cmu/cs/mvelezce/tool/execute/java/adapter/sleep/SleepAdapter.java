package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;

import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepAdapter extends Adapter {

    private static final String[] CONFIGURATIONS = {"A", "B", "C", "D"};

    private String mainClass;
    private String directory;

    public SleepAdapter(String mainClass, String directory) {
        this.mainClass = mainClass;
        this.directory = directory;
    }

    @Override
    public void execute(Set<String> configuration) {
        String[] argsArray = this.adaptConfiguration(configuration);
        StringBuilder args = new StringBuilder();

        for(String arg : argsArray) {
            args.append(arg);
            args.append(" ");
        }

        Adapter.executeJavaProgram(SleepMain.SLEEP_MAIN, this.mainClass, this.directory, args.toString().trim());
    }

    public String[] adaptConfiguration(Set<String> configuration) {
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


}
