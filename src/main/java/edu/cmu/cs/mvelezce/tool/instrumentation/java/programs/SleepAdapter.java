package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by mvelezce on 4/26/17.
 */
public class SleepAdapter extends Adapter {

    private static final String[] CONFIGURATIONS = {"A", "B", "C", "D"};

    private String mainClassFile;

    public SleepAdapter(String mainClassFile, Set<ClassNode> instrumentedClassNodes) {
        super(instrumentedClassNodes);
        this.mainClassFile = mainClassFile;
    }

    public void execute(Set<String> configuration) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> mainClass = this.loadClass(this.mainClassFile);
        Method method = mainClass.getMethod(Adapter.MAIN, String[].class);

        try {
            JavaRegion program = new JavaRegion(this.mainClassFile, Adapter.MAIN);
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            method.invoke(null, (Object) this.adaptConfiguration(configuration));
            program.endTime();
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
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
