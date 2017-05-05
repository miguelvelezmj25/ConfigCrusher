package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by mvelezce on 4/26/17.
 */
public class SleepDynamicAdapter extends DynamicAdapter {

    private static final String[] CONFIGURATIONS = {"A", "B", "C", "D"};

    private String mainClassFile;

    public SleepDynamicAdapter(String mainClassFile) {
        this.mainClassFile = mainClassFile;
    }

    // TODO pass the main class to execute
    public void execute(Set<String> configuration) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> mainClass = this.loadClass(this.mainClassFile);
        Method method = mainClass.getMethod(DynamicAdapter.MAIN, String[].class);

        try {
            Region program = Regions.getProgram();
            Regions.addExecutingRegion(program);

            program.startTime();
            method.invoke(null, (Object) this.adaptConfiguration(configuration));
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String[] adaptConfiguration(Set<String> configuration) {
        String[] sleepConfiguration = new String[4];

        for(int i = 0; i < sleepConfiguration.length; i++) {
            if(configuration.contains(SleepDynamicAdapter.CONFIGURATIONS[i])) {
                sleepConfiguration[i] = "true";
            }
            else {
                sleepConfiguration[i] = "false";
            }
        }

        return sleepConfiguration;
    }
}
