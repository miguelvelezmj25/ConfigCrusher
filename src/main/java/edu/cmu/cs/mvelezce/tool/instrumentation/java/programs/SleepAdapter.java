package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by mvelezce on 4/26/17.
 */
public class SleepAdapter extends Adapter {

    private String mainClassFile;

    public SleepAdapter(String mainClassFile, Set<ClassNode> instrumentedClassNodes, Set<String> configuration) {
        super(instrumentedClassNodes, configuration);
        this.mainClassFile = mainClassFile;
    }

    public void execute() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> mainClass = this.loadClass(this.mainClassFile);
        Method method = mainClass.getMethod(Adapter.MAIN, String[].class);

        try {
            JavaRegion program = new JavaRegion(this.mainClassFile, Adapter.MAIN);
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            String[] params = Arrays.copyOf(this.configuration.toArray(), this.configuration.size(), String[].class);
            program.startTime();
            method.invoke(null, (Object) params);
            program.endTime();
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
