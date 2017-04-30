package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformerTimer;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

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

    // JSON strings
    public static final String INSTRUMENTED = "instrumented";
    public static final String CLASS = "class";

    public static void instrument(String programName, String[] args, List<String> programFiles, Set<JavaRegion> regions) throws IOException {
        Options.getCommandLine(args);

        File directory = new File(Instrumenter.DIRECTORY + "/" + programName.replace(".", "/"));

        Options.checkIfDeleteResult(directory);

        if(directory.exists()) {
            if(Instrumenter.checkAllFilesInstrumented(programFiles)) {
                return;
            }
        }

        Instrumenter.instrument(programName, programFiles, regions);
    }


    public static void instrument(String programName, List<String> programFiles, Set<JavaRegion> regions) throws IOException {
        JavaRegionClassTransformerTimer timer = new JavaRegionClassTransformerTimer(programFiles, regions);
        Set<ClassNode> classNodes = timer.transformClasses();

        File directory = new File(Instrumenter.DIRECTORY + "/" + programName.replace(".", "/"));

        if(!directory.exists()) {
            directory.mkdirs();
        }

        for(ClassNode classNode : classNodes) {
            String fileName = classNode.name;
            fileName = fileName.substring(fileName.lastIndexOf("/"));
            timer.writeClass(classNode, directory + fileName);
        }
    }

    public static boolean checkAllFilesInstrumented(List<String> programFiles) throws IOException {
        for(String fileName : programFiles) {
            fileName = fileName.replace(".", "/");
            File file = new File(Instrumenter.DIRECTORY + "/" + fileName);

            if(!file.exists()) {
                return false;
            }
        }

        return true;
    }
}
