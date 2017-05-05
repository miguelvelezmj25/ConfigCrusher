package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformerTimer;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Instrumenter {

    // TODO change this other directory when testing programs
    public static final String DIRECTORY = "src/main/resources";

    public static void instrument(String programName, String mainClass, String[] args, List<String> programFiles, Set<JavaRegion> regions) throws IOException {
        Options.getCommandLine(args);

        File directory = new File(Instrumenter.DIRECTORY + "/" + programName + "/" + mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/"));

        Options.checkIfDeleteResult(directory);

        if(directory.exists()) {
            if(Instrumenter.checkAllFilesInstrumented(programName, programFiles)) {
                return;
            }
        }

        Instrumenter.instrument(programName, mainClass, programFiles, regions);
    }


    public static void instrument(String programName, String mainClass, List<String> programFiles, Set<JavaRegion> regions) throws IOException {
        JavaRegionClassTransformerTimer timer = new JavaRegionClassTransformerTimer(programFiles, regions);
        Set<ClassNode> classNodes = timer.transformClasses();

        File directory = new File(Instrumenter.DIRECTORY + "/" + programName + "/" + mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/"));

        if(!directory.exists()) {
            directory.mkdirs();
        }

        for(ClassNode classNode : classNodes) {
            List<MethodNode> methods = classNode.methods;

            System.out.println("INSTRUMENTED CLASSES");
            for(MethodNode method : methods) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }

            String fileName = classNode.name;
            fileName = fileName.substring(fileName.lastIndexOf("/"));
            timer.writeClass(classNode, directory + fileName);
        }
    }

    public static boolean checkAllFilesInstrumented(String programName, List<String> programFiles) throws IOException {
        for(String fileName : programFiles) {
            fileName = fileName.replace(".", "/");
            File file = new File(Instrumenter.DIRECTORY + "/" + programName + "/" + fileName + ".class");

            if(!file.exists()) {
                return false;
            }
        }

        return true;
    }
}
