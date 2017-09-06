package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.BytecodeUtils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

public abstract class RegionTransformer extends BaseMethodTransformer {

    private Set<JavaRegion> regions;
    private ClassNode currentClassNode = null;

    public RegionTransformer(ClassTransformer classTransformer, Set<JavaRegion> regions) {
        super(classTransformer);
        this.regions = regions;
    }

    public RegionTransformer(String directory, Set<JavaRegion> regions) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        this(new DefaultBaseClassTransformer(directory), regions);
    }

    public abstract InsnList addInstructionsStartRegion(JavaRegion javaRegion);

    public abstract InsnList addInstructionsEndRegion(JavaRegion javaRegion);

    @Override
    public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
        Set<MethodNode> methodsToInstrument = new HashSet<>();

        if(this.getRegionsInClass(classNode).isEmpty()) {
            return methodsToInstrument;
        }

        this.currentClassNode = classNode;

        for(MethodNode methodNode : classNode.methods) {
            if(!this.getRegionsInMethod(methodNode).isEmpty()) {
                methodsToInstrument.add(methodNode);
            }
        }

        return methodsToInstrument;
    }

    private List<JavaRegion> getRegionsInMethod(MethodNode methodNode) {
        String classPackage = this.currentClassNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = this.currentClassNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        String methodName = methodNode.name + methodNode.desc;

        List<JavaRegion> javaRegions = new ArrayList<>();

        for(JavaRegion javaRegion : this.regions) {
            if(javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass().equals(className)
                    && javaRegion.getRegionMethod().equals(methodName)) {
                javaRegions.add(javaRegion);
            }
        }

        javaRegions.sort(Comparator.comparingInt(JavaRegion::getStartBytecodeIndex));

        return javaRegions;
    }

    private List<JavaRegion> getRegionsInClass(ClassNode classNode) {
        String classPackage = classNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = classNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        List<JavaRegion> regionsInClass = new ArrayList<>();

        for(JavaRegion javaRegion : this.regions) {
            if(javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass().equals(className)) {
                regionsInClass.add(javaRegion);
            }
        }

        return regionsInClass;
    }

    private void getStartAndEndBlocks(MethodGraph graph, Map<AbstractInsnNode, JavaRegion> instructionsToRegion) {
        for(MethodBlock block : graph.getBlocks()) {
            List<AbstractInsnNode> blockInstructions = block.getInstructions();

            for(AbstractInsnNode instructionToStartInstrumenting : instructionsToRegion.keySet()) {
                if(blockInstructions.contains(instructionToStartInstrumenting)) {
                    MethodBlock start = this.getBlockToStartInstrumentingBeforeIt(graph, block);
                    start = graph.getMethodBlock(start.getID());

                    MethodBlock end = this.getBlockToEndInstrumentingBeforeIt(graph, block);
                    end = graph.getMethodBlock(end.getID());

                    Set<MethodBlock> endMethodBlocks = new HashSet<>();

                    if(start.getSuccessors().size() == 1 && start.getSuccessors().iterator().next().getLabel().equals(end.getLabel())) {
                        // TODO test
                        throw new RuntimeException("Somethign");
//                        regionsToRemove.add(instructionsToRegion.get(instructionToStartInstrumenting));
                    }
                    else {
                        if(graph.getExitBlock().equals(end)) {
                            for(MethodBlock predecessor : graph.getExitBlock().getPredecessors()) {
                                if(predecessor.isWithRet()) {
                                    endMethodBlocks.add(predecessor);
                                }
                            }
                        }
                        else {
                            endMethodBlocks.add(end);
                        }

                        JavaRegion region = instructionsToRegion.get(instructionToStartInstrumenting);
                        region.setStartMethodBlock(start);
                        region.setEndMethodBlocks(endMethodBlocks);
                    }
                }
            }
        }
    }

    // TODO move down the hierarchy
    private void instrumentStart(LabelNode labelNode, List<JavaRegion> regionsInMethod, InsnList newInstructions) {
        for(JavaRegion javaRegion : regionsInMethod) {
            Label regionOriginalLabel = javaRegion.getStartMethodBlock().getOriginalLabel();
            Label currentLabel = labelNode.getLabel();

            if(!regionOriginalLabel.toString().equals(currentLabel.toString())) {
                continue;
            }

            Label label = new Label();
            label.info = labelNode.getLabel() + "000start";
            LabelNode startRegionLabelNode = new LabelNode(label);

            this.updateLabels(newInstructions, labelNode, startRegionLabelNode);

            InsnList startRegionInstructions = new InsnList();
            startRegionInstructions.add(startRegionLabelNode);
            startRegionInstructions.add(this.addInstructionsStartRegion(javaRegion));
            newInstructions.add(startRegionInstructions);
        }
    }

    // TODO move down the hierarchy
    private void instrumentEnd(LabelNode labelNode, List<JavaRegion> regionsInMethod, InsnList newInstructions) {
        for(JavaRegion javaRegion : regionsInMethod) {
            for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
                Label regionOriginalLabel = endMethodBlock.getOriginalLabel();
                Label currentLabel = labelNode.getLabel();

                if(!regionOriginalLabel.toString().equals(currentLabel.toString())) {
                    continue;
                }

                Label label = new Label();
                label.info = labelNode.getLabel() + "000end";
                LabelNode endRegionLabelNode = new LabelNode(label);

                this.updateLabels(newInstructions, labelNode, endRegionLabelNode);

                InsnList endRegionInstructions = new InsnList();
                endRegionInstructions.add(endRegionLabelNode);
                endRegionInstructions.add(this.addInstructionsEndRegion(javaRegion));
                newInstructions.add(endRegionInstructions);

            }
        }
    }

    @Override
    public void transformMethod(MethodNode methodNode) {
        MethodGraph graph = MethodGraphBuilder.buildMethodGraph(methodNode);
        System.out.println("Before transforming");
        System.out.println(graph.toDotString(methodNode.name));

        if(graph.getBlocks().size() <= 3) {
//            System.out.println("Special method that is not instrumented");
//            continue;
//            // TODO this happened in an enum method in which there were two labels in the graph and the first one had the return statement
            throw new RuntimeException("Check this case");
        }

//        // TODO have to call this since looping through the instructions seems to set the index to 0. WEIRD
//        methodNode.instructions.toArray();
        // TODO maybe not anymore

        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);
        List<JavaRegion> regionsInMethodReversed = new ArrayList<>(regionsInMethod);
        Collections.reverse(regionsInMethodReversed);

        // TODO do we?
        this.calculateASMStartIndex(regionsInMethod, methodNode);

        Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

        for(JavaRegion region : regionsInMethod) {
            instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
        }

//        Set<JavaRegion> regionsToRemoves = new HashSet<>();
//        Map<MethodBlock, JavaRegion> specialBlocksToRegions = new HashMap<>();
//        Set<Set<MethodBlock>> stronglyConnectedComponents = graph.getStronglyConnectedComponents(graph.getEntryBlock());
//
//         TODO test
//        regionsInMethod.removeAll(regionsToRemove);
//        regionsInMethodReversed.removeAll(regionsToRemove);
        this.getStartAndEndBlocks(graph, instructionsToRegion);

        // TODO test
//        if(regionsInMethod.isEmpty()) {
//            return;
//        }

        LabelNode currentLabelNode;
        InsnList newInstructions = new InsnList();
        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        while (instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();

            if(instruction.getType() == AbstractInsnNode.LABEL) {
                currentLabelNode = (LabelNode) instruction;

                this.instrumentEnd(currentLabelNode, regionsInMethodReversed, newInstructions);
                this.instrumentStart(currentLabelNode, regionsInMethod, newInstructions);
            }

            newInstructions.add(instruction);
        }

        methodNode.instructions.clear();
        methodNode.instructions.add(newInstructions);

        graph = MethodGraphBuilder.buildMethodGraph(methodNode);
        System.out.println("After transforming");
        System.out.println(graph.toDotString(methodNode.name));
        System.out.print("");
    }

    public MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        methodGraph.getDominators();
        // Find post dominator
        MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
        return immediatePostDominator;
    }

    public MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        return start;
    }

    /**
     * TODO this might execute extra iterations that are not needed since the LabelNodes are the same for both old and new labels, but the info is different
     *
     * @param instructions
     */
    private void updateLabels(InsnList instructions, LabelNode oldLabel, LabelNode newLabel) {
        System.out.println(oldLabel.getLabel() + " -> " + newLabel.getLabel().info);
        int numberOfInstructions = instructions.size();
        AbstractInsnNode instruction = instructions.getFirst();

        for(int i = 0; i < numberOfInstructions; i++) {
            if(instruction.getType() == AbstractInsnNode.JUMP_INSN) {
                JumpInsnNode jumpInsnNode = (JumpInsnNode) instruction;

                if(jumpInsnNode.label == oldLabel) {
                    jumpInsnNode.label = newLabel;
                    System.out.println("CHANGE");
                }
            }

            instruction = instruction.getNext();
        }
    }

    private List<String> getJavapResult() {
        String classPackage = this.currentClassNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = this.currentClassNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        List<String> javapResult = new ArrayList<>();

        try {
            String[] command = new String[]{"javap", "-classpath", this.getClassTransformer().getPath(), "-p", "-c",
                    classPackage + "." + className};
            System.out.println(Arrays.toString(command));
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String string;

            while ((string = inputReader.readLine()) != null) {
                if(!string.isEmpty()) {
                    javapResult.add(string);
                }
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while ((string = errorReader.readLine()) != null) {
                System.out.println(string);
            }

            process.waitFor();
        } catch (IOException | InterruptedException ie) {
            ie.printStackTrace();
        }

        if(javapResult.size() < 3) {
            System.out.println(javapResult);
            throw new RuntimeException("The output of javap is not expected");
        }

        return javapResult;
    }


    public void calculateASMStartIndex(List<JavaRegion> regionsInMethod, MethodNode methodNode) {
        List<String> javapResult = this.getJavapResult();
        int methodStartIndex = 0;

        if(methodNode.name.startsWith("<init>")) {
            // TODO check this
            throw new RuntimeException();
//            method = method.replace("<init>", "");
//            method = tempRegion.getRegionPackage() + "." + tempRegion.getRegionClass() + method;
        }

        // Check if signature matches
        for(String outputLine : javapResult) {
            if(outputLine.contains(" " + methodNode.name + "(")) {
                String formalParametersString = outputLine.substring(outputLine.indexOf("(") + 1, outputLine.indexOf(")"));
                List<String> formalParameters = Arrays.asList(formalParametersString.split(","));
                StringBuilder methodDescriptors = new StringBuilder();

                for(String formalParameter : formalParameters) {
                    String methodDescriptor = BytecodeUtils.toBytecodeDescriptor(formalParameter.trim());
                    methodDescriptors.append(methodDescriptor);
                }

                String regionDesc = methodNode.desc;
                String regionFormalParameters = regionDesc.substring(regionDesc.indexOf("(") + 1, regionDesc.indexOf(")"));

                if(methodDescriptors.toString().equals(regionFormalParameters)) {
                    break;
                }
            }

            methodStartIndex++;
        }

        int instructionNumber = 0;
        int currentBytecodeIndex = -1;
        // 2 are the lines before the actual code in a method
        int javapOffset = 2;
        Set<JavaRegion> updatedRegions = new HashSet<>();

        for(int i = (methodStartIndex + javapOffset); i < javapResult.size(); i++) {
            String outputLine = javapResult.get(i);

            if(!outputLine.contains(":")) {
                continue;
            }

            for(JavaRegion region : regionsInMethod) {
                if(updatedRegions.contains(region)) {
                    continue;
                }

                if(!outputLine.contains(region.getStartBytecodeIndex() + ":")) {
                    continue;
                }

                InsnList instructionsList = methodNode.instructions;
                ListIterator<AbstractInsnNode> instructions = instructionsList.iterator();
                int instructionCounter = -1;

                while (instructions.hasNext()) {
                    AbstractInsnNode instruction = instructions.next();

                    if(instruction.getOpcode() >= 0) {
                        instructionCounter++;
                    }
                    else {
                        continue;
                    }

                    if(instructionCounter == instructionNumber) {
                        region.setStartBytecodeIndex(instructionsList.indexOf(instruction));
                        updatedRegions.add(region);
                        break;
                    }
                }

                if(updatedRegions.size() == regionsInMethod.size()) {
                    break;
                }
            }

            int outputLineBytecodeIndex;
            String outputLineBytecodeIndexString = outputLine.substring(0, outputLine.indexOf(":")).trim();

            if(StringUtils.isNumeric(outputLineBytecodeIndexString)) {
                outputLineBytecodeIndex = Integer.valueOf(outputLineBytecodeIndexString);
            }
            else {
                // TODO check this
                throw new RuntimeException("Something happened");
            }

            if(outputLineBytecodeIndex > currentBytecodeIndex) {
                instructionNumber++;
                currentBytecodeIndex = outputLineBytecodeIndex;
            }
            else {
                // TODO check this
                throw new RuntimeException("Something happened");
            }

            if(updatedRegions.size() == regionsInMethod.size()) {
                break;
            }
        }

        if(updatedRegions.size() != regionsInMethod.size()) {
            throw new RuntimeException("Did not update some regions");
        }
    }
}
