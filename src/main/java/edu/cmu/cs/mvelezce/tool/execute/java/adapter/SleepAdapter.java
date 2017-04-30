package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;

import java.util.Arrays;
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
        Region program = Regions.getProgram();
        Regions.addExecutingRegion(program);

        String[] argsArray = this.adaptConfiguration(configuration);
        StringBuilder args = new StringBuilder();

        for(String arg : argsArray) {
            args.append(arg);
            args.append(" ");
        }

        program.startTime();
        Adapter.executeJavaProgram(this.mainClass, this.directory, args.toString().trim());
        program.endTime();

        Regions.removeExecutingRegion(program);
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

        return Arrays.copyOfRange(sleepConfiguration, 0, configuration.size());
    }


}
