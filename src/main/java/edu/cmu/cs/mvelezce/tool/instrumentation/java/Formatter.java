package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.BasicClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformerTimer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.VariableBeforeReturnTransformer;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Formatter {

    public static final String TARGET_DIRECTORY = "../performance-mapper-evaluation/instrumented";

    public static void format(String[] args, String srcDirectory, String classDirectory) throws IOException, InterruptedException {
//        args = new String[]{"-delres"};
        Options.getCommandLine(args);

        if(Options.checkIfDeleteResult()) {
            Formatter.format(srcDirectory, classDirectory);
        }
    }

    public static void format(String srcDirectory, String classDirectory) throws IOException, InterruptedException {
        String[] command = {"find", srcDirectory.substring(0, srcDirectory.length()-1), "-name", "*.java"};
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new FileWriter(srcDirectory + "sources.txt"));
        String line;

        while ((line= reader.readLine()) !=null) {
            writer.write(line);
            writer.write("\n");
        }

        writer.close();

        command = new String[]{"javac", "-cp", "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/lib/*", "-d", classDirectory, "@" + srcDirectory + "sources.txt"};
        System.out.println(Arrays.toString(command));
        process = Runtime.getRuntime().exec(command);
        process.waitFor();

        reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        while ((line= reader.readLine()) !=null) {
            System.out.println(line);
        }

        BasicClassTransformer timer = new VariableBeforeReturnTransformer(classDirectory);
        Set<ClassNode> classNodes = timer.transformClasses();

        for(ClassNode classNode : classNodes) {
            String fileName = classNode.name;
            System.out.println(fileName);
            timer.writeClass(classNode, classDirectory + fileName);
        }
    }

}
