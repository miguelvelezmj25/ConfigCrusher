package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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

    public abstract InsnList addInstructionsStartRegion(JavaRegion javaRegion);

    public abstract InsnList addInstructionsEndRegion(JavaRegion javaRegion);

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
            System.out.println(graph.toDotString(methodNode.name));
            // TODO have to call this since looping through the instructions seems to set the index to 0. WEIRD
            methodNode.instructions.toArray();
            Set<JavaRegion> regionsInMethod = this.getRegionsInMethod(classPackage, className, methodNode.name);
            InsnList newInstructions = new InsnList();

            InsnList instructions = methodNode.instructions;
            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

            LabelNode currentLabelNode = null;

            while(instructionsIterator.hasNext()) {
                AbstractInsnNode instruction = instructionsIterator.next();

                if(instruction.getType() == AbstractInsnNode.LABEL) {
                    currentLabelNode = (LabelNode) instruction;
                }

                if(instruction.getOpcode() < 0) {
                    newInstructions.add(instruction);
                    continue;
                }

                int bytecodeIndex = instructions.indexOf(instruction);

                boolean instrumentedEndWithThisIndex = false;
                for(JavaRegion javaRegion : regionsInMethod) {
                    if(javaRegion.getStartBytecodeIndex() == bytecodeIndex) {
                        Label label = new Label();
                        label.info = bytecodeIndex;
                        LabelNode startRegionLabelNode = new LabelNode(label);
                        this.updateLabels(newInstructions, currentLabelNode, startRegionLabelNode);

                        InsnList startRegionInstructions = new InsnList();
                        startRegionInstructions.add(startRegionLabelNode);
                        startRegionInstructions.add(this.addInstructionsStartRegion(javaRegion));

                        AbstractInsnNode labelInstruction = instruction;

                        while(labelInstruction.getType() != AbstractInsnNode.LABEL) {
                            labelInstruction = labelInstruction.getPrevious();
                        }

                        newInstructions.insertBefore(labelInstruction, startRegionInstructions);

                        MethodBlock currentMethodBlock = graph.getMethodBlock(currentLabelNode.getLabel());
                        MethodBlock blockToEndInstrumentation = graph.getWhereBranchesConverge(currentMethodBlock);
                        List<AbstractInsnNode> endBlockInstructions = blockToEndInstrumentation.getInstructions();

                        for(AbstractInsnNode endBlockInstruction : endBlockInstructions) {
                            if(endBlockInstruction.getType() != AbstractInsnNode.LABEL && endBlockInstruction.getType() != AbstractInsnNode.LINE && endBlockInstruction.getType() != AbstractInsnNode.FRAME) {
                                javaRegion.setEndBytecodeIndex(instructions.indexOf(endBlockInstruction));
                                break;
                            }
                        }
                    }
                    else if (javaRegion.getEndBytecodeIndex() == bytecodeIndex) {
                        InsnList endRegionInstructions = new InsnList();
                        if(!instrumentedEndWithThisIndex) {
                            Label label = new Label();
                            label.info = bytecodeIndex;
                            LabelNode endRegionLabelNode = new LabelNode(label);
                            this.updateLabels(newInstructions, currentLabelNode, endRegionLabelNode);
                            endRegionInstructions.add(endRegionLabelNode);
                        }

                        endRegionInstructions.add(this.addInstructionsEndRegion(javaRegion));

                        AbstractInsnNode labelInstruction = instruction;

                        while(labelInstruction.getType() != AbstractInsnNode.LABEL) {
                            labelInstruction = labelInstruction.getPrevious();
                        }

                        newInstructions.insertBefore(labelInstruction, endRegionInstructions);
                        instrumentedEndWithThisIndex = true;
                    }
                }

                newInstructions.add(instruction);
            }

            methodNode.instructions.clear();
            methodNode.instructions.add(newInstructions);
        }
    }

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

    private void updateLabels(InsnList newInstructions, LabelNode oldLabel, LabelNode newLabel) {
        int numberOfInstructions = newInstructions.size();
        AbstractInsnNode instruction = newInstructions.getFirst();

        for(int i = 0; i < numberOfInstructions; i++) {
            if(instruction.getType() == AbstractInsnNode.JUMP_INSN) {
                JumpInsnNode jumpInsnNode = (JumpInsnNode) instruction;

                if(jumpInsnNode.label == oldLabel) {
                    jumpInsnNode.label = newLabel;
                }
            }

            instruction = instruction.getNext();
        }
    }
}
