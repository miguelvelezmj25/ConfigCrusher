package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.BytecodeUtils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraphBuilder;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by mvelezce on 4/21/17.
 */
public abstract class JavaRegionClassTransformer extends ClassTransformerBase {

    protected String directory;
    protected Set<JavaRegion> regions;
    private List<String> javapResult = new ArrayList<>();

    public JavaRegionClassTransformer(String directory, Set<JavaRegion> regions) {
        this.directory = directory;
        this.regions = regions;
    }

    public abstract InsnList addInstructionsStartRegion(JavaRegion javaRegion);

    public abstract InsnList addInstructionsEndRegion(JavaRegion javaRegion);

    @Override
    public Set<ClassNode> transformClasses() throws IOException {
        Set<ClassNode> classNodes = new HashSet<>();
        String[] extensions = {"class"};

        Collection<File> files = FileUtils.listFiles(new File(this.directory), extensions, true);

        for(File file : files) {
            String filePackage = file.getAbsolutePath().replace(this.directory, "");
            filePackage = filePackage.replace(".class", "");
            filePackage = filePackage.replace("/", ".");
            String fileClass = filePackage.substring(filePackage.lastIndexOf(".") + 1);
            int indexOfFilePackage = filePackage.lastIndexOf("." + fileClass);
            filePackage = filePackage.substring(0, indexOfFilePackage);
            boolean transform = false;

            for(JavaRegion javaRegion : this.regions) {
                if(javaRegion.getRegionPackage().equals(filePackage) && javaRegion.getRegionClass().equals(fileClass)) {
                    transform = true;
                    break;
                }
            }

            if(!transform) {
                continue;
            }

            ClassNode classNode = this.readClass(filePackage + "." + fileClass);

            String command = "javap -classpath " + this.directory + " -p -c "+ filePackage + "." + fileClass;
            System.out.println(command);

            try {
                Process p = Runtime.getRuntime().exec(command);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String s;
                while ((s = stdInput.readLine()) != null) {
                    if(!s.isEmpty()) {
                        this.javapResult.add(s);
                    }
                }
            }
            catch(IOException ie) {
                ie.printStackTrace();
            }

//            System.out.println("Before transforming");
//
//            for(MethodNode method : classNode.methods) {
//                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }

            if(fileClass.equals("Main")) {
                int i = 0;
            }

            this.transform(classNode);

//            System.out.println("After transforming");
//
//            for(MethodNode method : classNode.methods) {
//                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }

            classNodes.add(classNode);
        }

        return classNodes;
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
            if(methodNode.name.equals("<init>")) {
                int i = 0;
            }

            boolean instrumentMethod = false;

            for(JavaRegion javaRegion : regionsInClass) {
                if(javaRegion.getRegionMethod().equals(methodNode.name + methodNode.desc)) {
                    instrumentMethod = true;
                    break;
                }
            }

            if(!instrumentMethod) {
                continue;
            }

            if(methodNode.name.equals("main")) {
                int i = 0;
            }

            MethodGraph graph = MethodGraphBuilder.buildMethodGraph(methodNode);
            System.out.println("Before transforming");
            System.out.println(graph.toDotString(methodNode.name));

            if(graph.getBlocks().size() <= 3) {
                System.out.println("Special method that is not instrumented");
                continue;
                // TODO this happened in an enum method in which there were two labels in the bytecode and the first one had the return statement
            }

            // TODO have to call this since looping through the instructions seems to set the index to 0. WEIRD
            methodNode.instructions.toArray();
            List<JavaRegion> regionsInMethod = this.getRegionsInMethod(classPackage, className, methodNode.name + methodNode.desc);
            List<JavaRegion> regionsInMethodReversed = new ArrayList<>(regionsInMethod);
            Collections.reverse(regionsInMethodReversed);

            if(methodNode.name.equals("main")) {
                int i = 0;
            }

            this.calculateASMStartIndex(regionsInMethod, methodNode);

            if(methodNode.name.equals("main")) {
                int i = 0;
            }
            Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

            for(JavaRegion region : regionsInMethod) {
//                System.out.println(region.getStartBytecodeIndex());
                if(methodNode.name.equals("main")) {
                    int i = 0;
                }

                instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
            }

            Set<Set<MethodBlock>> stronglyConnectedComponents = graph.getStronglyConnectedComponents(graph.getEntryBlock());
            Map<MethodBlock, JavaRegion> specialBlocksToRegions = new HashMap<>();

            for(MethodBlock block : graph.getBlocks()) {
                List<AbstractInsnNode> blockInstructions = block.getInstructions();

                for(AbstractInsnNode instructionToStartInstrumenting : instructionsToRegion.keySet()) {
                    if(blockInstructions.contains(instructionToStartInstrumenting)) {
                        if(methodNode.name.equals("bftNodeSearch")) {
                            int i = 0;
                        }

                        MethodBlock start = JavaRegionClassTransformer.getBlockToStartInstrumentingBeforeIt(graph, block);
                        start = graph.getMethodBlock(start.getID());
                        MethodBlock end = JavaRegionClassTransformer.getBlockToEndInstrumentingBeforeIt(graph, block);
                        end = graph.getMethodBlock(end.getID());
                        Set<MethodBlock> endMethodBlocks = new HashSet<>();

                        if(graph.getExitBlock().equals(end)) {
                            endMethodBlocks.addAll(graph.getExitBlock().getPredecessors());
                        }
                        else {
                            endMethodBlocks.add(end);
                        }

                        JavaRegion region = instructionsToRegion.get(instructionToStartInstrumenting);
                        region.setStartMethodBlock(start);
                        region.setEndMethodBlocks(endMethodBlocks);


                        // TODO optimize
//                        boolean inProblematicStronglyConnectedComponent = false;
//
//                        for(Set<MethodBlock> stronglyConnectedComponent : stronglyConnectedComponents) {
//                            if(stronglyConnectedComponent.size() > 1 && stronglyConnectedComponent.contains(start)) {
//                                // If post dominator is part of a strongly connected component, find all immediate dominators of cycle except for the immediate post dominator
//                                Set<MethodBlock> immediateDominators = new HashSet<>();
//
//                                for(MethodBlock methodBlock : stronglyConnectedComponent) {
//                                    if(methodBlock.equals(start)) {
//                                        continue;
//                                    }
//
//                                    immediateDominators.add(graph.getImmediateDominator(methodBlock));
//                                }
//
//                                // If all immediate dominators are within a strongly connected component
//                                if(!stronglyConnectedComponent.containsAll(immediateDominators)) {
//                                    inProblematicStronglyConnectedComponent = true;
//                                    break;
//                                }
//                            }
//                        }

                        // TODO test
                        boolean inProblematicStronglyConnectedComponent = false;

                        for(Set<MethodBlock> stronglyConnectedComponent : stronglyConnectedComponents) {
                            if(stronglyConnectedComponent.size() > 1 && stronglyConnectedComponent.contains(start) && !stronglyConnectedComponent.containsAll(block.getPredecessors())) {
                                inProblematicStronglyConnectedComponent = true;
                                break;
                            }
                        }

                        MethodBlock immediateDominator = graph.getImmediateDominator(start);

                        if(immediateDominator.getSuccessors().size() > 1 && immediateDominator.getSuccessors().contains(start) && inProblematicStronglyConnectedComponent) {
                            specialBlocksToRegions.put(graph.getImmediateDominator(start), region);
                            throw new RuntimeException("Special case");
                        }

//                        MethodBlock immediateDominator = graph.getImmediateDominator(start);
//
//                        if(/*inProblematicStronglyConnectedComponent &&*/ immediateDominator.getSuccessors().size() > 1 && immediateDominator.getSuccessors().contains(start)) {
//                            specialBlocksToRegions.put(graph.getImmediateDominator(start), region);
//                        }

                    }
                }
            }

            if(methodNode.name.equals("bftNodeSearch")) {
                int i = 0;
            }

            InsnList newInstructions = new InsnList();
            InsnList instructions = methodNode.instructions;
            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();
            LabelNode currentLabelNode = null;
//            Set<Map.Entry<LabelNode, LabelNode>> labelUpdates = new LinkedHashSet<>();

            while(instructionsIterator.hasNext()) {
                AbstractInsnNode instruction = instructionsIterator.next();

                if(instruction.getType() == AbstractInsnNode.LABEL) {
                    // TODO test special nodes
                    if(currentLabelNode != null) {
                        MethodBlock methodBlock = graph.getMethodBlock(currentLabelNode.getLabel());

                        if(specialBlocksToRegions.containsKey(methodBlock)) {
                            Label label = new Label();
                            label.info = currentLabelNode.getLabel() + "000specialstart";
                            LabelNode startRegionLabelNode = new LabelNode(label);

                            InsnList startRegionInstructions = new InsnList();
                            startRegionInstructions.add(startRegionLabelNode);
                            startRegionInstructions.add(this.addInstructionsStartRegion(specialBlocksToRegions.get(methodBlock)));

                            if(newInstructions.getLast().getType() != AbstractInsnNode.JUMP_INSN) {
                                throw new RuntimeException("The last instruction is not a jump");
                            }

                            newInstructions.insertBefore(newInstructions.getLast(), startRegionInstructions);
                        }
                    }

                    currentLabelNode = (LabelNode) instruction;

                    for(JavaRegion javaRegion : regionsInMethod) {
                        for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
                            if (endMethodBlock.getOriginalLabel().toString().equals(currentLabelNode.getLabel().toString())) {
                                if(methodNode.name.equals("bftNodeSearch")) {
                                    int i = 0;
                                }

                                Label label = new Label();
                                label.info = currentLabelNode.getLabel() + "000end";
                                LabelNode endRegionLabelNode = new LabelNode(label);
                                JavaRegionClassTransformer.updateLabels(newInstructions, currentLabelNode, endRegionLabelNode);
//                            labelUpdates = new LinkedHashSet<>();
//                            labelUpdates.add(new AbstractMap.SimpleEntry<>(currentLabelNode, endRegionLabelNode));
//                            JavaRegionClassTransformer.updateLabels(newInstructions, labelUpdates);

                                InsnList endRegionInstructions = new InsnList();
                                endRegionInstructions.add(endRegionLabelNode);
                                endRegionInstructions.add(this.addInstructionsEndRegion(javaRegion));
                                newInstructions.add(endRegionInstructions);
                            }
                        }
                    }

                    for(JavaRegion javaRegion : regionsInMethodReversed) {
                        if(javaRegion.getRegionID().equals("e99f8442-6e09-4b9f-af2e-5156c2e47c00")) {
                            int i = 0;
                        }


//                        if(javaRegion.getStartMethodBlock().getID().equals(currentLabelNode.getLabel().toString())) {
                        if(javaRegion.getStartMethodBlock().getOriginalLabel().toString().equals(currentLabelNode.getLabel().toString())) {
                            if(methodNode.name.equals("bftNodeSearch")) {
                                int i = 0;
                            }

                            if(specialBlocksToRegions.containsValue(javaRegion)) {
                                continue;
                            }

                            Label label = new Label();
                            label.info = currentLabelNode.getLabel() + "000start";
                            LabelNode startRegionLabelNode = new LabelNode(label);
                            JavaRegionClassTransformer.updateLabels(newInstructions, currentLabelNode, startRegionLabelNode);
//                            labelUpdates = new LinkedHashSet<>();
//                            labelUpdates.add(new AbstractMap.SimpleEntry<>(currentLabelNode, startRegionLabelNode));
//                            JavaRegionClassTransformer.updateLabels(newInstructions, labelUpdates);

                            InsnList startRegionInstructions = new InsnList();
                            startRegionInstructions.add(startRegionLabelNode);
                            startRegionInstructions.add(this.addInstructionsStartRegion(javaRegion));
                            newInstructions.add(startRegionInstructions);
                        }
                    }
                }

                newInstructions.add(instruction);
            }

//            JavaRegionClassTransformer.updateLabels(newInstructions, labelUpdates);

            methodNode.instructions.clear();
            methodNode.instructions.add(newInstructions);

            graph = MethodGraphBuilder.buildMethodGraph(methodNode);
            System.out.println("After transforming");
            System.out.println(graph.toDotString(methodNode.name));
            System.out.print("");
        }

    }

    public void calculateASMStartIndex(List<JavaRegion> regionsInMethod, MethodNode methodNode) {
        JavaRegion tempRegion = regionsInMethod.get(0);
        int methodStartIndex = 0;
        String method = tempRegion.getRegionMethod();

        if(method.startsWith("<init>")) {
            method = method.replace("<init>", "");
            method = tempRegion.getRegionPackage() + "." + tempRegion.getRegionClass() + method;
        }

        method = method.substring(0, method.lastIndexOf("("));

        for(String outputLine : this.javapResult) {
            if(outputLine.contains(" " + method + "(")) {
                if(tempRegion.getRegionMethod().contains("isBlocked")) {
                    int z = 0;
                }

                String formalParametersString = outputLine.substring(outputLine.indexOf("(") + 1, outputLine.indexOf(")"));
                List<String> formalParameters = Arrays.asList(formalParametersString.split(","));
                StringBuilder methodDescriptors = new StringBuilder();

                for(String formalParameter : formalParameters) {
                    String methodDescriptor = BytecodeUtils.toBytecodeDescriptor(formalParameter.trim());
                    methodDescriptors.append(methodDescriptor);
                }

                String regionMethod = tempRegion.getRegionMethod();
                String regionFormalParameters = regionMethod.substring(regionMethod.indexOf("(") + 1, regionMethod.indexOf(")"));

                if(methodDescriptors.toString().equals(regionFormalParameters)) {
                    break;
                }
            }

            methodStartIndex++;
        }

        int instructionNumber = 0;
        int currentBytecodeIndex = -1;
        Set<JavaRegion> updatedRegions = new HashSet<>();

        // 2 are the lines before the actual code in a method
        for(int i = (methodStartIndex + 2); i < this.javapResult.size(); i++) {
            String outputLine = this.javapResult.get(i);

            if(!outputLine.contains(":")) {
                continue;
            }

            int outputLineBytecodeIndex = -1;
            String outputLineBytecodeIndexString = outputLine.substring(0, outputLine.indexOf(":")).trim();

            if(StringUtils.isNumeric(outputLineBytecodeIndexString)) {
                outputLineBytecodeIndex = Integer.valueOf(outputLineBytecodeIndexString);
            }

            for(JavaRegion region : regionsInMethod) {
                if(updatedRegions.contains(region)) {
                    continue;
                }

                if(outputLine.contains(region.getStartBytecodeIndex() + ":")) {
//                if(instructionNumber == region.getStartBytecodeIndex()) {
                    InsnList instructionsList = methodNode.instructions;
                    ListIterator<AbstractInsnNode> instructions = instructionsList.iterator();
                    int instructionCounter = -1;

                    while(instructions.hasNext()) {
                        AbstractInsnNode instruction = instructions.next();

                        if(methodNode.name.equals("main") && !instructions.hasNext()) {
                            int iasdfas = 0;
                        }

                        if(instruction.getOpcode() >= 0) {
                            instructionCounter++;
                        }
                        else {
                            continue;
                        }

                        if(instructionCounter == instructionNumber) {
                            if(methodNode.name.equals("main")) {
                                int fsddf = 0;
                            }

                            region.setStartBytecodeIndex(instructionsList.indexOf(instruction));
                            updatedRegions.add(region);
                            break;
                        }
                    }
                }

                if(updatedRegions.size() == regionsInMethod.size()) {
                    if(methodNode.name.equals("main")) {
                        int fsddf = 0;
                    }
                    break;
                }
            }

            if(outputLineBytecodeIndex > currentBytecodeIndex) {
                instructionNumber++;
                currentBytecodeIndex = outputLineBytecodeIndex;
            }

            if(updatedRegions.size() == regionsInMethod.size()) {
                if(methodNode.name.equals("main")) {
                    int fsddf = 0;
                }
                break;
            }
        }

        if(updatedRegions.size() != regionsInMethod.size()) {
            throw new RuntimeException("Did not update some regions");
        }
    }

    public static MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        // Find post dominator
        MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
        Set<Set<MethodBlock>> stronglyConnectedComponents = methodGraph.getStronglyConnectedComponents(methodGraph.getEntryBlock());
        Set<MethodBlock> problematicStronglyConnectedComponent = new HashSet<>();

        for(Set<MethodBlock> stronglyConnectedComponent : stronglyConnectedComponents) {
            if(stronglyConnectedComponent.size() > 1 && stronglyConnectedComponent.contains(immediatePostDominator)) {
                problematicStronglyConnectedComponent = new HashSet<>(stronglyConnectedComponent);
                break;
            }
        }

        // If post dominator is not part of a strongly connected component, return
        if(problematicStronglyConnectedComponent.isEmpty()) {
            return immediatePostDominator;
        }

        // If post dominator is part of a strongly connected component, find all immediate dominators of cycle except for the immediate post dominator
        Set<MethodBlock> immediateDominatorsOfProblematicStronglyConnectedComponent = new HashSet<>();

        for(MethodBlock methodBlock : problematicStronglyConnectedComponent) {
            if(methodBlock.equals(immediatePostDominator)) {
                continue;
            }

            immediateDominatorsOfProblematicStronglyConnectedComponent.add(methodGraph.getImmediateDominator(methodBlock));
        }

        // If all immediate dominators are within a strongly connected component
        if(problematicStronglyConnectedComponent.containsAll(immediateDominatorsOfProblematicStronglyConnectedComponent)) {
            return immediatePostDominator;
        }

        // Get the next post dominator of the strongly connected component that is not part of the strongly connected component
        for(MethodBlock methodBlock : problematicStronglyConnectedComponent) {
            immediatePostDominator = methodGraph.getImmediatePostDominator(methodBlock);

            if(!problematicStronglyConnectedComponent.contains(immediatePostDominator)) {
                return immediatePostDominator;
            }
        }

        throw new RuntimeException("Could not find out where to start instrumenting");
    }

    public static MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        Set<Set<MethodBlock>> stronglyConnectedComponents = methodGraph.getStronglyConnectedComponents(methodGraph.getEntryBlock());
        Set<MethodBlock> problematicStronglyConnectedComponent = new HashSet<>();

        for(Set<MethodBlock> stronglyConnectedComponent : stronglyConnectedComponents) {
            if(stronglyConnectedComponent.size() > 1 && stronglyConnectedComponent.contains(start)) {
                problematicStronglyConnectedComponent = new HashSet<>(stronglyConnectedComponent);
                break;
            }
        }

        // If start is not part of a strongly connected component, return
        if(problematicStronglyConnectedComponent.isEmpty()) {
            return start;
        }

        // If start is part of a strongly connected component, find all immediate dominators of cycle except for the immediate start
        Set<MethodBlock> immediateDominatorsOfProblematicStronglyConnectedComponent = new HashSet<>();

        for(MethodBlock methodBlock : problematicStronglyConnectedComponent) {
            if(methodBlock.equals(start)) {
                continue;
            }

            immediateDominatorsOfProblematicStronglyConnectedComponent.add(methodGraph.getImmediateDominator(methodBlock));
        }

        // If all immediate dominators are within a strongly connected component
        if(problematicStronglyConnectedComponent.containsAll(immediateDominatorsOfProblematicStronglyConnectedComponent)) {
            return start;
        }

        // Get the next start of the strongly connected component that is not part of the strongly connected component
        Set<MethodBlock> blocksToInstrumentBeforeThem = new HashSet<>(problematicStronglyConnectedComponent);
        blocksToInstrumentBeforeThem.add(start);

        for(MethodBlock methodBlock : blocksToInstrumentBeforeThem) {
            MethodBlock immediateDominator = methodGraph.getImmediateDominator(methodBlock);

            if(!blocksToInstrumentBeforeThem.contains(immediateDominator)) {
                return methodBlock;
            }
        }

        throw new RuntimeException("Could not find out where to start instrumenting");
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

    private List<JavaRegion> getRegionsInMethod(String regionPackage, String regionClass, String regionMethod) {
        List<JavaRegion> javaRegions = new ArrayList<>();

        for(JavaRegion javaRegion : this.regions) {
            if(javaRegion.getRegionPackage().equals(regionPackage) && javaRegion.getRegionClass().equals(regionClass) && javaRegion.getRegionMethod().equals(regionMethod)) {
                javaRegions.add(javaRegion);
            }
        }

        javaRegions.sort((region1, region2) -> region2.getStartBytecodeIndex() - region1.getStartBytecodeIndex());

        return javaRegions;
    }

    /**
     * TODO this might execute extra iterations that are not needed since the LabelNodes are the same for both old and new labels, but the info is different
     * @param instructions
     * @param changes
     */
    private static void updateLabels(InsnList instructions, Set<Map.Entry<LabelNode, LabelNode>> changes) {
//        for(Map.Entry<LabelNode, LabelNode> change : changes) {
//            System.out.println(change.getKey().getLabel() + " -> " + change.getValue().getLabel().info);
//            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();
//
//            while (instructionsIterator.hasNext()) {
//                AbstractInsnNode instruction = instructionsIterator.next();
//
//                if (instruction.getType() == AbstractInsnNode.JUMP_INSN) {
//                    JumpInsnNode jumpInsnNode = (JumpInsnNode) instruction;
//
//                    if (jumpInsnNode.label == change.getKey()) {
//                        jumpInsnNode.label = change.getValue();
//                        System.out.println("CHANGE");
//                    }
//                }
//            }
//        }

        for(Map.Entry<LabelNode, LabelNode> change : changes) {
            System.out.println(change.getKey().getLabel() + " -> " + change.getValue().getLabel().info);
            int numberOfInstructions = instructions.size();
            AbstractInsnNode instruction = instructions.getFirst();

            for(int i = 0; i < numberOfInstructions; i++) {
//                if(instruction.getType() == AbstractInsnNode.LABEL) {
//                    System.out.println(((LabelNode) instruction).getLabel());
//                }

                if(instruction.getType() == AbstractInsnNode.JUMP_INSN) {
                    JumpInsnNode jumpInsnNode = (JumpInsnNode) instruction;

                    if(jumpInsnNode.label == change.getKey()) {
                        jumpInsnNode.label = change.getValue();
                        System.out.println("CHANGE");
                    }
                }

                instruction = instruction.getNext();
            }
        }
    }

    /**
     * TODO this might execute extra iterations that are not needed since the LabelNodes are the same for both old and new labels, but the info is different
     * @param instructions
     */
    private static void updateLabels(InsnList instructions, LabelNode oldLabel, LabelNode newLabel) {
        System.out.println(oldLabel.getLabel() + " -> " + newLabel.getLabel().info);
        int numberOfInstructions = instructions.size();
        AbstractInsnNode instruction = instructions.getFirst();

        for(int i = 0; i < numberOfInstructions; i++) {
//                if(instruction.getType() == AbstractInsnNode.LABEL) {
//                    System.out.println(((LabelNode) instruction).getLabel());
//                }

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




}
