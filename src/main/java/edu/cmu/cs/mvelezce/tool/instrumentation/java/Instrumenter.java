package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformerTimer;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Instrumenter {

    public static final String TARGET_DIRECTORY = "../performance-mapper-evaluation/instrumented";

    public static void instrument(String programName, String directory, String[] args, Set<JavaRegion> regions) throws IOException {
        // TODO do not delete folder with class files since we might delete files that do not need to be instrumented
        Options.getCommandLine(args);

//        File fileDirectory = new File(Instrumenter.TARGET_DIRECTORY + "/" + directory + "/" + mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/"));
//
//        for(String programFile : programFiles) {
//            File programFileToInstrument = new File(fileDirectory.getPath() + "/" + programFile.substring(programFile.lastIndexOf(".") + 1) + ".class");
//            Options.checkIfDeleteResult(programFileToInstrument);
//        }


//        Options.checkIfDeleteResult(fileDirectory);
//
//        if(fileDirectory.exists()) {
//            if(Instrumenter.checkAllFilesInstrumented("TODO", programFiles)) {
//                File[] files = fileDirectory.listFiles();
//
//                for(File file : files) {
//                    // TODO do not hardcode the ending
//                    if(!file.getName().endsWith(".class")) {
//                        continue;
//                    }
//
////                    String filePath = programName + "/" + mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/") + "/" + file.getName().substring(0, file.getName().length() - ".class".length());
//                    String filePath = mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/") + "/" + file.getName().substring(0, file.getName().length() - ".class".length());
//                    List<MethodNode> methods = Helper.readFile(filePath);
//
//                    System.out.println("INSTRUMENTED CLASSES");
//                    for (MethodNode method : methods) {
//                        MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
//                        System.out.println(methodGraph.toDotString(method.name));
//                    }
//
//                }
//
//                return;
//            }
//        }

//        Instrumenter.instrument(mainClass, directory, programFiles, regions);
    }

    public static void instrument(String directory, Set<JavaRegion> regions) throws IOException {
        JavaRegionClassTransformerTimer timer = new JavaRegionClassTransformerTimer(directory, regions);
        Set<ClassNode> classNodes = timer.transformClasses();



    }

    public static void instrument(String mainClass, String directory, String[] args, List<String> programFiles, Set<JavaRegion> regions) throws IOException {
        // TODO do not delete folder with class files since we might delete files that do not need to be instrumented
        Options.getCommandLine(args);

//        File fileDirectory = new File(Instrumenter.TARGET_DIRECTORY + "/" + directory + "/" + mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/"));
//
//        for(String programFile : programFiles) {
//            File programFileToInstrument = new File(fileDirectory.getPath() + "/" + programFile.substring(programFile.lastIndexOf(".") + 1) + ".class");
//            Options.checkIfDeleteResult(programFileToInstrument);
//        }


//        Options.checkIfDeleteResult(fileDirectory);
//
//        if(fileDirectory.exists()) {
//            if(Instrumenter.checkAllFilesInstrumented("TODO", programFiles)) {
//                File[] files = fileDirectory.listFiles();
//
//                for(File file : files) {
//                    // TODO do not hardcode the ending
//                    if(!file.getName().endsWith(".class")) {
//                        continue;
//                    }
//
////                    String filePath = programName + "/" + mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/") + "/" + file.getName().substring(0, file.getName().length() - ".class".length());
//                    String filePath = mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/") + "/" + file.getName().substring(0, file.getName().length() - ".class".length());
//                    List<MethodNode> methods = Helper.readFile(filePath);
//
//                    System.out.println("INSTRUMENTED CLASSES");
//                    for (MethodNode method : methods) {
//                        MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
//                        System.out.println(methodGraph.toDotString(method.name));
//                    }
//
//                }
//
//                return;
//            }
//        }

        Instrumenter.instrument(mainClass, directory, programFiles, regions);
    }


    public static void instrument(String mainClass, String directory, List<String> programFiles, Set<JavaRegion> regions) throws IOException {
//        JavaRegionClassTransformerTimer timer = new JavaRegionClassTransformerTimer(programFiles, regions);
//        Set<ClassNode> classNodes = timer.transformClasses();
//
//        File fileDirectory = new File(Instrumenter.TARGET_DIRECTORY + "/" + directory + "/" + mainClass.substring(0, mainClass.lastIndexOf(".")).replace(".", "/"));
//
//        if(!fileDirectory.exists()) {
//            fileDirectory.mkdirs();
//        }
//
//        for(ClassNode classNode : classNodes) {
////            List<MethodNode> methods = classNode.methods;
////
////            System.out.println("INSTRUMENTED CLASSES");
////            for(MethodNode method : methods) {
////                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
////                System.out.println(methodGraph.toDotString(method.name));
////            }
//
//            String fileName = classNode.name;
//            fileName = fileName.substring(fileName.lastIndexOf("/"));
//            timer.writeClass(classNode, fileDirectory + fileName);
//        }
    }

    public static boolean checkAllFilesInstrumented(String directory, List<String> programFiles) throws IOException {
        for(String fileName : programFiles) {
            fileName = fileName.replace(".", "/");
            File file = new File(Instrumenter.TARGET_DIRECTORY + "/" + directory + "/" + fileName + ".class");

            if(!file.exists()) {
                return false;
            }
        }

        return true;
    }
}
