package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformerTimer;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Instrumenter {

    public static final String TARGET_DIRECTORY = "../performance-mapper-evaluation/instrumented";

    public static void instrument(String directory, Set<JavaRegion> regions) throws IOException {
        JavaRegionClassTransformerTimer timer = new JavaRegionClassTransformerTimer(directory, regions);
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
            timer.writeClass(classNode, directory + fileName);
        }
    }

}
