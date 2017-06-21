package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformerTimer;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Instrumenter {

    public static final String TARGET_DIRECTORY = "../performance-mapper-evaluation/instrumented";

    public static void instrument(String[] args, String srcDirectory, String classDirectory, Set<JavaRegion> regions) throws IOException, InterruptedException {
//        args = new String[]{"-delres"};
        Options.getCommandLine(args);

        if(Options.checkIfDeleteResult()) {
            Instrumenter.instrument(srcDirectory, classDirectory, regions);
        }
    }

    public static void instrument(String srcDirectory, String classDirectory, Set<JavaRegion> regions) throws IOException, InterruptedException {
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

        command = new String[]{"javac", "-d", classDirectory, "@" + srcDirectory + "sources.txt"};
//        process = Runtime.getRuntime().exec(command);
//        process.waitFor();

        reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        while ((line= reader.readLine()) !=null) {
            System.out.println(line);
        }

        JavaRegionClassTransformerTimer timer = new JavaRegionClassTransformerTimer(classDirectory, regions);
        Set<ClassNode> classNodes = timer.transformClasses();

        for(ClassNode classNode : classNodes) {
//            List<MethodNode> methods = classNode.methods;
//
//            System.out.println("INSTRUMENTED CLASSES");
//            for(MethodNode method : methods) {
//                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }

            String fileName = classNode.name;
//            System.out.println(fileName);
            timer.writeClass(classNode, classDirectory + fileName);
        }
    }

}
