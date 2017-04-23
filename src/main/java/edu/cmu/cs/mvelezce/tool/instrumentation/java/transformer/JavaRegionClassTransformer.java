package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
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

    @Override
    public void transform(ClassNode classNode) {
        String classCanonicalName = classNode.name;
        // TODO we might abstract this to a static method
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

            InsnList instructions = methodNode.instructions;
            InsnList newInstructions = new InsnList();
            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

            while(instructionsIterator.hasNext()) {
                AbstractInsnNode instruction = instructionsIterator.next();
                boolean instrumented = false;

                if(instruction.getOpcode() < 0) {
                    newInstructions.add(instruction);
                    continue;
                }

                int bytecodeIndex = instructions.indexOf(instruction);

                for(JavaRegion javaRegion : regionsInClass) {
                    if(javaRegion.getStartBytecodeIndex() == bytecodeIndex) {
                        newInstructions.add(this.addInstructionsBeforeRegion(javaRegion));
                        newInstructions.add(instruction);
                        instrumented = true;
                    }
                    else if(javaRegion.getEndBytecodeIndex() == bytecodeIndex) {
                        newInstructions.add(instruction);
                        newInstructions.add(this.addInstructionsAfterRegion(javaRegion));
                        instrumented = true;
                    }
                }

                if(!instrumented) {
                    newInstructions.add(instruction);
                }
            }

            methodNode.instructions.clear();
            methodNode.instructions.add(newInstructions);
        }
    }

    // TODO put this in java region class
    public static Set<JavaRegion> getRegionsInClass(String regionClass) {
        return JavaRegionClassTransformer.getRegionsInClass("", regionClass);
    }

    public static Set<JavaRegion> getRegionsInClass(String regionPackage, String regionClass) {
        Set<JavaRegion> javaRegions = new HashSet<>();

        for(Region region : Regions.getRegions()) {
            JavaRegion javaRegion = (JavaRegion) region;

            if(javaRegion.getRegionPackage().equals(regionPackage) && javaRegion.getRegionClass().equals(regionClass)) {
                javaRegions.add(javaRegion);
            }
        }

        return javaRegions;
    }

}
