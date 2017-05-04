package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mvelezce on 4/21/17.
 */
public abstract class JavaRegionClassTransformer extends ClassTransformerBase {

    protected List<String> fileNames;
    protected Set<JavaRegion> regions;

    public JavaRegionClassTransformer(List<String> fileNames, Set<JavaRegion> regions) {
        this.fileNames = fileNames;
        this.regions = regions;
    }

    public abstract InsnList addInstructionsBeforeRegion(JavaRegion javaRegion, int maxLocals);

    public abstract InsnList addInstructionsAfterRegion(JavaRegion javaRegion, int maxLocals);

    @Override
    public Set<ClassNode> transformClasses() throws IOException {
        Set<ClassNode> classNodes = new HashSet<>();

        for(String fileName : this.fileNames) {
            ClassNode classNode = this.readClass(fileName);
            this.transform(classNode);
            classNodes.add(classNode);
        }

        return classNodes;
    }

    // TODO there might be some programs that do not have a main method and we will have to create custom main classes for them
    public static void setMainClass(String mainClassFile) {
        // TODO I do not think this should be here
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

        Set<JavaRegion> regionsInClass = this.getRegionsInClass(classPackage, className);

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

            MethodGraph graph = MethodGraphBuilder.buildMethodGraph(methodNode);
            Set<JavaRegion> regionsInMethod = this.getRegionsInMethod(classPackage, className, methodNode.name);
            InsnList newInstructions = new InsnList();


//            Stack<Integer> localVariableIndexes = new Stack<>();
//            int nextLocalVariableIndex = methodNode.maxLocals;
//            InsnList instructions = methodNode.instructions;
//            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();
//
//            while(instructionsIterator.hasNext()) {
//                AbstractInsnNode instruction = instructionsIterator.next();
//                boolean instrumented = false;
//
//                if (instruction.getOpcode() < 0) {
//                    newInstructions.add(instruction);
//                    continue;
//                }
//
//                int bytecodeIndex = instructions.indexOf(instruction);
//
//                for(JavaRegion javaRegion : regionsInMethod) {
//                    if(javaRegion.getStartBytecodeIndex() == bytecodeIndex) {
//                        newInstructions.add(this.addInstructionsBeforeRegion(javaRegion, nextLocalVariableIndex));
//                        newInstructions.add(instruction);
//
//                        instrumented = true;
//                        localVariableIndexes.push(nextLocalVariableIndex);
//                        nextLocalVariableIndex++;
//                    }
//                    else if (javaRegion.getEndBytecodeIndex() == bytecodeIndex) {
//                        newInstructions.add(instruction);
//                        newInstructions.add(this.addInstructionsAfterRegion(javaRegion, localVariableIndexes.pop()));
//                        instrumented = true;
//                    }
//                }
//
//                if(!instrumented) {
//                    newInstructions.add(instruction);
//                }
//            }

            methodNode.instructions.clear();
            methodNode.instructions.add(newInstructions);
        }
    }

    // Todo Seems weird to have this here
    private Set<JavaRegion> getRegionsInClass(String regionPackage, String regionClass) {
        Set<JavaRegion> javaRegions = new HashSet<>();

        for(JavaRegion javaRegion : this.regions) {
            if(javaRegion.getRegionPackage().equals(regionPackage) && javaRegion.getRegionClass().equals(regionClass)) {
                javaRegions.add(javaRegion);
            }
        }

        return javaRegions;
    }

    private Set<JavaRegion> getRegionsInMethod(String regionPackage, String regionClass, String regionMethod) {
        Set<JavaRegion> javaRegions = new HashSet<>();

        for(JavaRegion javaRegion : this.regions) {
            if(javaRegion.getRegionPackage().equals(regionPackage) && javaRegion.getRegionClass().equals(regionClass) && javaRegion.getRegionMethod().equals(regionMethod)) {
                javaRegions.add(javaRegion);
            }
        }

        return javaRegions;
    }
}
