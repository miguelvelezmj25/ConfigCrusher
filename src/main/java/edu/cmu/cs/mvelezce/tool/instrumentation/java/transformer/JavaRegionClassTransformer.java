package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Adapter;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

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

        Set<JavaRegion> regionsInClass = JavaRegion.getRegionsInClass(classPackage, className);

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

            Set<JavaRegion> regionsInMethod = JavaRegion.getRegionsInMethod(classPackage, className, methodNode.name);

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
}
