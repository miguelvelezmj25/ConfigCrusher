package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.BasicClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.VariableBeforeReturnTransformer;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Formatter {

    public static final String TARGET_DIRECTORY = "../performance-mapper-evaluation/instrumented";

    public static void format(String[] args, String originalSrcDirectory, String originalClassDirectory, String instrumentedSrcDirectory, String instrumentedClassDirectory) throws IOException, InterruptedException {
//        args = new String[]{"-delres"};
        Options.getCommandLine(args);

        if(Options.checkIfDeleteResult()) {
            Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentedSrcDirectory, instrumentedClassDirectory);
        }
    }

    public static void compile(String srcDir, String classDir) throws IOException, InterruptedException {
        String[] command = {"find", srcDir.substring(0, srcDir.length() - 1), "-name", "*.java"};
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new FileWriter(srcDir + "sources.txt"));
        String line;

        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.write("\n");
        }

        writer.close();

        command = new String[]{"javac", "-cp", "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/lib/*",
                "-d", classDir, "@" + srcDir + "sources.txt"};
        System.out.println(Arrays.toString(command));
        process = Runtime.getRuntime().exec(command);
        process.waitFor();

        reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static Set<ClassNode> formatReturnWithMethod(String classDir) throws IOException {
        BasicClassTransformer timer = new VariableBeforeReturnTransformer(classDir);
        Set<ClassNode> classNodes = timer.transformClasses();

        for(ClassNode classNode : classNodes) {
            String fileName = classNode.name;
            System.out.println(classDir + fileName);
            timer.writeClass(classNode, classDir + fileName);
        }

        return classNodes;
    }

    public static void format(String originalSrcDirectory, String originalClassDirectory, String instrumentedSrcDirectory, String instrumentedClassDirectory) throws IOException, InterruptedException {
        List<String> args = new ArrayList<>();
        args.add(originalSrcDirectory);
        args.add(originalClassDirectory);
        args.add(instrumentedSrcDirectory);
        args.add(instrumentedClassDirectory);

        for(int i = 0; i < 2; i++) {
            String srcDir = args.get(i * 2);
            String classDir = args.get((i * 2) + 1);
            Formatter.compile(srcDir, classDir);
        }

        Set<ClassNode> classNodes = Formatter.formatReturnWithMethod(originalClassDirectory);

        for(ClassNode classNode : classNodes) {
            String fileName = classNode.name;
            String[] command = new String[]{"cp", originalClassDirectory + fileName + ".class", instrumentedClassDirectory + fileName + ".class"};
            System.out.println(Arrays.toString(command));
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

}
