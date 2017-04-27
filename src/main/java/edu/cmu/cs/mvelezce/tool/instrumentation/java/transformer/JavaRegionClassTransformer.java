package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Adapter;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by mvelezce on 4/21/17.
 */
public abstract class JavaRegionClassTransformer extends ClassTransformerBase {

    public JavaRegionClassTransformer(String fileName) {
        super(fileName);
    }

    public abstract InsnList addInstructionsBeforeRegion(JavaRegion javaRegion);

    public abstract InsnList addInstructionsAfterRegion(JavaRegion javaRegion);

    // TODO there might be some programs that do not have a main method and we will have to create custom main classes for them
    public static void setMainClass(String mainClassFile) {
        JavaRegion program = new JavaRegion(mainClassFile, Adapter.MAIN);
        Regions.addProgram(program);
    }

    @Override
    public void transform(ClassNode classNode) {
        String classCanonicalName = classNode.name;
        String classPackage = classCanonicalName.substring(0, classCanonicalName.lastIndexOf("/"));
        String className = classCanonicalName.substring(classCanonicalName.lastIndexOf("/") + 1);
        classPackage = classPackage.replaceAll("/", ".");
        className = className.replaceAll("/", ".");

        Set<JavaRegion> regionsInClass = JavaRegionClassTransformer.getRegionsInClass(classPackage, className);

        for(MethodNode methodNode : classNode.methods) {
            boolean instrumentMethod = false;

            for(JavaRegion javaRegion : regionsInClass) {
                if(javaRegion.getRegionMethod().equals(methodNode.name)) {
                    instrumentMethod = true;
                    break;
                }
            }

            if(!instrumentMethod) {
                continue;
            }

            Set<JavaRegion> regionsInMethod = JavaRegionClassTransformer.getRegionsInMethod(classPackage, className, methodNode.name);

            InsnList instructions = methodNode.instructions;
            InsnList newInstructions = new InsnList();
            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

            while(instructionsIterator.hasNext()) {
                AbstractInsnNode instruction = instructionsIterator.next();
                boolean instrumented = false;

                if (instruction.getOpcode() < 0) {
                    newInstructions.add(instruction);
                    continue;
                }

                int bytecodeIndex = instructions.indexOf(instruction);

                for(JavaRegion javaRegion : regionsInMethod) {
                    if(javaRegion.getStartBytecodeIndex() == bytecodeIndex) {
                        newInstructions.add(this.addInstructionsBeforeRegion(javaRegion));
                        newInstructions.add(instruction);
                        instrumented = true;
                    }
                    else if (javaRegion.getEndBytecodeIndex() == bytecodeIndex) {
                        newInstructions.add(instruction);
                        newInstructions.add(this.addInstructionsAfterRegion(javaRegion));
                        instrumented = true;
                    }
                }

                if (!instrumented) {
                    newInstructions.add(instruction);
                }
            }

            methodNode.instructions.clear();
            methodNode.instructions.add(newInstructions);
        }
    }

    // Todo Seems weird to have this here
    private static Set<JavaRegion> getRegionsInClass(String regionPackage, String regionClass) {
        Set<JavaRegion> javaRegions = new HashSet<>();

        for(Region region : Regions.getRegions()) {
            JavaRegion javaRegion = (JavaRegion) region;

            if(javaRegion.getRegionPackage().equals(regionPackage) && javaRegion.getRegionClass().equals(regionClass)) {
                javaRegions.add(javaRegion);
            }
        }

        return javaRegions;
    }

    private static Set<JavaRegion> getRegionsInMethod(String regionPackage, String regionClass, String regionMethod) {
        Set<JavaRegion> javaRegions = new HashSet<>();

        for(Region region : Regions.getRegions()) {
            JavaRegion javaRegion = (JavaRegion) region;

            if(javaRegion.getRegionPackage().equals(regionPackage) && javaRegion.getRegionClass().equals(regionClass) && javaRegion.getRegionMethod().equals(regionMethod)) {
                javaRegions.add(javaRegion);
            }
        }

        return javaRegions;
    }
}
