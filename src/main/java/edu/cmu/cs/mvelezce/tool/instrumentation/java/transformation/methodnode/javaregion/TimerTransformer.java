package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.BytecodeUtils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.DefaultMethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.function.Predicate;

public class TimerTransformer extends RegionTransformer {

    private Set<MethodBlock> blocksToInstrumentBeforeReturn = new HashSet<>();

    public TimerTransformer(String programName, String directory, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, directory, regionsToOptionSet);
    }

    public TimerTransformer(String programName, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, classTransformer, regionsToOptionSet);
    }

    public static MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
        return immediatePostDominator;
    }

    public static MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        MethodBlock id = methodGraph.getImmediateDominator(start);

        if(id != methodGraph.getEntryBlock() && id.getSuccessors().size() == 1 && id.getSuccessors().contains(start)) {
            return id;
        }

        return start;
    }

//    public static MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
//        methodGraph.getDominators();
//        MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
//        return immediatePostDominator;
//    }
//
//    public static MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
//        return start;
//    }

    // TODO why dont we return a new map. If I do not return a new map, it sugguest to users that the passed map will be
    // transformed. If I return a new map, it sugguest to users that the passed map will remain the same and a new map
    // is generated
    private void getStartAndEndBlocks(MethodGraph graph, Map<AbstractInsnNode, JavaRegion> instructionsToRegion) {
        for(MethodBlock block : graph.getBlocks()) {
            List<AbstractInsnNode> blockInstructions = block.getInstructions();

            for(AbstractInsnNode instructionToStartInstrumenting : instructionsToRegion.keySet()) {
                if(!blockInstructions.contains(instructionToStartInstrumenting)) {
                    continue;
                }

                MethodBlock start = TimerTransformer.getBlockToStartInstrumentingBeforeIt(graph, block);
                start = graph.getMethodBlock(start.getID());

                if(start == null) {
                    throw new RuntimeException();
                }

                MethodBlock end = TimerTransformer.getBlockToEndInstrumentingBeforeIt(graph, block);
                end = graph.getMethodBlock(end.getID());

                if(end == null) {
                    throw new RuntimeException();
                }

                Set<MethodBlock> endMethodBlocks = new HashSet<>();

                if(start == end) {
                    throw new RuntimeException("Start and end equal");
                }
                else if(start.getSuccessors().size() == 1 && start.getSuccessors().iterator().next().equals(end)) {
                    // TODO test
                    throw new RuntimeException("Happens when a control flow decision only has 1 successor??????");
//                        regionsToRemove.add(instructionsToRegion.get(instructionToStartInstrumenting));
                }
                else if(graph.getExitBlock() == end) {
                    this.blocksToInstrumentBeforeReturn.addAll(end.getPredecessors());
                    endMethodBlocks.addAll(end.getPredecessors());
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

    @Override
    public void transformMethod(MethodNode methodNode) {
        System.out.println("Before transforming");
        DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder(methodNode);
        MethodGraph graph = builder.build();

        if(graph.getBlocks().size() <= 3) {
//            continue;
//            // TODO this happened in an enum method in which there were two labels in the graph and the first one had the return statement
            throw new RuntimeException("Check this case");
        }

        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);
        this.calculateASMStartIndex(regionsInMethod, methodNode);

        InsnList newInstructions;
        int startInstructionCount = methodNode.instructions.size();

        if(regionsInMethod.size() == 1) {
            newInstructions = this.instrumentEntireMethod(methodNode, regionsInMethod.get(0));
        }
        else {
            Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

            for(JavaRegion region : regionsInMethod) {
                instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
            }

            this.getStartAndEndBlocks(graph, instructionsToRegion);
            this.removeInnerRegions(regionsInMethod, graph);
            this.checkSameOptionsInRegions(regionsInMethod);

            if(regionsInMethod.size() == 1) {
                newInstructions = this.instrumentEntireMethod(methodNode, regionsInMethod.get(0));
            }
            else {
                newInstructions = this.instrumentNormal(methodNode, graph, regionsInMethod);
            }
        }

        int endInstructionCount = methodNode.instructions.size();

        if(endInstructionCount != startInstructionCount) {
            throw new RuntimeException("We modified the instructions in the node itself instead of creating a new list");
        }

        methodNode.instructions.clear();
        methodNode.instructions.add(newInstructions);

        int afterInstrumentationInstructionCount = methodNode.instructions.size();

        if(afterInstrumentationInstructionCount <= endInstructionCount) {
            throw new RuntimeException("We apparently did not add instrumentation");
        }

        System.out.println("After transforming");
        builder = new DefaultMethodGraphBuilder(methodNode);
        builder.build();
        System.out.print("");
    }

    private void checkSameOptionsInRegions(List<JavaRegion> regionsInMethod) {
        Iterator<JavaRegion> regionsIterator = regionsInMethod.iterator();
        JavaRegion region = regionsIterator.next();
        Map<JavaRegion, Set<Set<String>>> regionsToOptions = this.getRegionsToOptionSet();
        Set<Set<String>> regionOptions = regionsToOptions.get(region);
        boolean oneRegion = true;

        while (regionsIterator.hasNext()) {
            region = regionsIterator.next();

            if(!regionsToOptions.get(region).equals(regionOptions)) {
                oneRegion = false;
            }
        }

        if(!oneRegion) {
            return;
        }

        JavaRegion first = regionsInMethod.iterator().next();
        Predicate<JavaRegion> regionPredicate = r -> r != first;
        regionsInMethod.removeIf(regionPredicate);

        if(regionsInMethod.size() != 1) {
            throw new RuntimeException("This is a method that has a single region, but did not end up with 1 region");
        }
    }

    private InsnList instrumentStart(MethodBlock methodBlock, List<JavaRegion> regionsInMethod) {
        InsnList newInstructions = new InsnList();

        for(JavaRegion javaRegion : regionsInMethod) {
            if(javaRegion.getStartMethodBlock() != methodBlock) {
                continue;
            }

            newInstructions.add(this.getInstructionsStartRegion(javaRegion));
        }

        return newInstructions;
    }

//    private Map<MethodBlock, List<JavaRegion>> addRegionsInBlocksWithReturn(MethodBlock methodBlock, List<JavaRegion> regionsInMethod) {
//        Map<MethodBlock, List<JavaRegion>> blocksToRegions = new HashMap<>();
//
//        for(JavaRegion javaRegion : regionsInMethod) {
//            for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
//                if(endMethodBlock != methodBlock) {
//                    continue;
//                }
//
//                if(!blocksToRegions.containsKey(endMethodBlock)) {
//                    blocksToRegions.put(endMethodBlock, new ArrayList<>());
//                }
//
//                blocksToRegions.get(endMethodBlock).add(javaRegion);
//            }
//        }
//
//        return blocksToRegions;
//    }

    private InsnList instrumentEnd(MethodBlock methodBlock, List<JavaRegion> regionsInMethod) {
        InsnList newInstructions = new InsnList();

        for(JavaRegion javaRegion : regionsInMethod) {
            for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
                if(endMethodBlock != methodBlock) {
                    continue;
                }

                newInstructions.add(this.getInstructionsEndRegion(javaRegion));
            }

        }

        return newInstructions;
    }

//    /**
//     * TODO this might execute extra iterations that are not needed since the LabelNodes are the same for both old and new labels, but the info is different
//     *
//     * @param instructions
//     */
//    // TODO why dont we return a new list
//    private void updateLabels(InsnList instructions, LabelNode oldLabel, LabelNode newLabel) {
//        System.out.println(oldLabel.getLabel() + " -> " + newLabel.getLabel().info);
//        int numberOfInstructions = instructions.size();
//        AbstractInsnNode instruction = instructions.getFirst();
//
//        for(int i = 0; i < numberOfInstructions; i++) {
//            if(instruction.getType() == AbstractInsnNode.JUMP_INSN) {
//                JumpInsnNode jumpInsnNode = (JumpInsnNode) instruction;
//
//                if(jumpInsnNode.label == oldLabel) {
//                    jumpInsnNode.label = newLabel;
//                    System.out.println("CHANGE");
//                }
//            }
//
//            instruction = instruction.getNext();
//        }
//    }

    private List<String> getJavapResult() {
        String classPackage = this.getCurrentClassNode().name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = this.getCurrentClassNode().name;
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

    // TODO why dont we return a new list
    private void calculateASMStartIndex(List<JavaRegion> regionsInMethod, MethodNode methodNode) {
        List<String> javapResult = this.getJavapResult();
        int methodStartIndex = 0;
        String methodNameInJavap = methodNode.name;

        if(methodNameInJavap.startsWith("<init>")) {
            // TODO check this
            methodNameInJavap = this.getCurrentClassNode().name;
            methodNameInJavap = methodNameInJavap.replace("/", ".");
        }

        if(methodNameInJavap.startsWith("<clinit>")) {
            methodNameInJavap = "  static {};";
        }
        else {
            methodNameInJavap += "(";
        }

        // Check if signature matches
        for(String outputLine : javapResult) {
            if(outputLine.equals(methodNameInJavap)) {
                break;
            }
            else if(outputLine.contains(" " + methodNameInJavap)) {
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

            String outputCommand = outputLine.substring(outputLine.indexOf(":") + 1).trim();

            if(StringUtils.isNumeric(outputCommand)) {
                continue;
            }

            int outputLineBytecodeIndex = -1;
            String outputLineBytecodeIndexString = outputLine.substring(0, outputLine.indexOf(":")).trim();

            if(StringUtils.isNumeric(outputLineBytecodeIndexString)) {
                outputLineBytecodeIndex = Integer.valueOf(outputLineBytecodeIndexString);
            }

            if(outputLineBytecodeIndex > currentBytecodeIndex) {
                instructionNumber++;
                currentBytecodeIndex = outputLineBytecodeIndex;
            }

            if(updatedRegions.size() == regionsInMethod.size()) {
                break;
            }
        }

        if(updatedRegions.size() != regionsInMethod.size()) {
            throw new RuntimeException("Did not update some regions");
        }
    }

    @Override
    public InsnList getInstructionsStartRegion(JavaRegion javaRegion) {
        InsnList instructionsStartRegion = new InsnList();
        instructionsStartRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsStartRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "enter", "(Ljava/lang/String;)V", false));

        return instructionsStartRegion;
    }

    @Override
    public InsnList getInstructionsEndRegion(JavaRegion javaRegion) {
        InsnList instructionsEndRegion = new InsnList();
        instructionsEndRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsEndRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "exit", "(Ljava/lang/String;)V", false));

        return instructionsEndRegion;
    }

    /**
     * Loop through the instructions to check where to instrument
     *
     * @param methodNode
     * @param regionsInMethod
     */
    private InsnList instrumentNormal(MethodNode methodNode, MethodGraph graph, List<JavaRegion> regionsInMethod) {
        System.out.println("########### " + this.getCurrentClassNode().name + " " + methodNode.name);
        List<JavaRegion> regionsInMethodReversed = new ArrayList<>(regionsInMethod);
        Collections.reverse(regionsInMethodReversed);

        InsnList newInstructions = new InsnList();
        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        while (instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();
            newInstructions.add(instruction);

            MethodBlock block = graph.getMethodBlock(instruction);

            if(block == null) {
                continue;
            }

            if(this.blocksToInstrumentBeforeReturn.contains(block)) {
                instruction = instructionsIterator.next();
                int opcode = instruction.getOpcode();

                while ((opcode < Opcodes.IRETURN || opcode > Opcodes.RETURN) && opcode != Opcodes.RET) {
                    newInstructions.add(instruction);
                    instruction = instructionsIterator.next();
                    opcode = instruction.getOpcode();
                }

                InsnList endInstructions = this.instrumentEnd(block, regionsInMethodReversed);
                newInstructions.add(endInstructions);
                newInstructions.add(instruction);
            }
            else {
                InsnList endInstructions = this.instrumentEnd(block, regionsInMethodReversed);
                newInstructions.add(endInstructions);
                InsnList startInstructions = this.instrumentStart(block, regionsInMethod);
                newInstructions.add(startInstructions);
            }
        }

        return newInstructions;
    }

    private void removeInnerRegions(List<JavaRegion> regionsInMethod, MethodGraph graph) {
        Map<JavaRegion, Set<JavaRegion>> regionsToInnerRegionSet = new LinkedHashMap<>();
        graph.getDominators();

        // Calculate possible inner regions
        for(JavaRegion region : regionsInMethod) {
            for(JavaRegion possibleInnerRegion : regionsInMethod) {
                if(region == possibleInnerRegion) {
                    continue;
                }

                MethodBlock regionStartBlock = region.getStartMethodBlock();
                MethodBlock possibleRegionStartBlock = possibleInnerRegion.getStartMethodBlock();

                if(graph.getDominators().get(regionStartBlock).contains(possibleRegionStartBlock)) {
                    continue;
                }

                Set<MethodBlock> possibleInnerRegionEndBlocks = possibleInnerRegion.getEndMethodBlocks();
                Set<MethodBlock> regionReachableBlocks = new HashSet<>();

                for(MethodBlock endBlock : region.getEndMethodBlocks()) {
                    Set<MethodBlock> reachableBlocks = graph.getReachableBlocks(region.getStartMethodBlock(), endBlock);
                    regionReachableBlocks.addAll(reachableBlocks);
                }

                if(regionReachableBlocks.contains(possibleInnerRegion.getStartMethodBlock())
                        && regionReachableBlocks.containsAll(possibleInnerRegionEndBlocks)) {
                    if(!regionsToInnerRegionSet.containsKey(region)) {
                        regionsToInnerRegionSet.put(region, new HashSet<>());
                    }

                    regionsToInnerRegionSet.get(region).add(possibleInnerRegion);
                }
            }
        }

        Map<JavaRegion, Set<Set<String>>> regionsToOptions = this.getRegionsToOptionSet();
        Set<JavaRegion> unableToRemoveRegions = new HashSet<>();

        // Check if remove
        for(Map.Entry<JavaRegion, Set<JavaRegion>> entry : regionsToInnerRegionSet.entrySet()) {
            Set<Set<String>> regionOptionSet = regionsToOptions.get(entry.getKey());
            Set<String> currentOptions = new HashSet<>();

            for(Set<String> options : regionOptionSet) {
                currentOptions.addAll(options);
            }

            Set<JavaRegion> innerRegions = entry.getValue();

            for(JavaRegion innerRegion : innerRegions) {
                Set<Set<String>> innerRegionOptionSet = regionsToOptions.get(innerRegion);
                Set<String> innerRegionOptions = new HashSet<>();

                for(Set<String> options : innerRegionOptionSet) {
                    innerRegionOptions.addAll(options);
                }

                if(currentOptions.equals(innerRegionOptions) || currentOptions.containsAll(innerRegionOptions)
                        || innerRegionOptions.containsAll(currentOptions)) {
                    currentOptions = new HashSet<>(innerRegionOptions);
                }
                else {
                    unableToRemoveRegions.add(entry.getKey());
                }
            }
        }

        for(JavaRegion unableToRemoveRegion : unableToRemoveRegions) {
            regionsToInnerRegionSet.remove(unableToRemoveRegion);
        }

        for(Set<JavaRegion> innerRegionsToRemove : regionsToInnerRegionSet.values()) {
            regionsInMethod.removeAll(innerRegionsToRemove);
        }

        if(regionsInMethod.isEmpty()) {
            throw new RuntimeException("The regions in a method cannot be empty after removing inner regions");
        }

        this.updateRegionsToOptions(regionsToInnerRegionSet);

        for(Set<JavaRegion> innerRegionsToRemove : regionsToInnerRegionSet.values()) {
            for(JavaRegion innerRegionToRemove : innerRegionsToRemove) {
                regionsToOptions.remove(innerRegionToRemove);
            }
        }

        if(regionsToOptions.isEmpty()) {
            throw new RuntimeException("The regions in a method cannot be empty after removing inner regions");
        }
    }

//    private Set<String> optionsInMethod() {
//
//    }

    private void updateRegionsToOptions(Map<JavaRegion, Set<JavaRegion>> regionsToInnerRegions) {
        Map<JavaRegion, Set<Set<String>>> regionsToOptionSet = this.getRegionsToOptionSet();

        for(Map.Entry<JavaRegion, Set<JavaRegion>> entry : regionsToInnerRegions.entrySet()) {
            Set<String> newOptions = new HashSet<>();

            for(JavaRegion innerRegion : entry.getValue()) {
                Set<Set<String>> innerOptionSet = regionsToOptionSet.get(innerRegion);
                Set<String> innerOptions = new HashSet<>();

                for(Set<String> options : innerOptionSet) {
                    innerOptions.addAll(options);
                }

                newOptions.addAll(innerOptions);
            }

            Set<Set<String>> regionOptionSet = regionsToOptionSet.get(entry.getKey());
            Set<String> regionOptions = new HashSet<>();

            for(Set<String> options : regionOptionSet) {
                regionOptions.addAll(options);
            }

            newOptions.addAll(regionOptions);

            Set<Set<String>> oldOptionSet = regionsToOptionSet.get(entry.getKey());
            oldOptionSet.clear();
            oldOptionSet.add(newOptions);
        }

    }

    /**
     * This can be done when there is a single region in a method
     *
     * @param methodNode
     */
    private InsnList instrumentEntireMethod(MethodNode methodNode, JavaRegion region) {
        InsnList newInstructions = new InsnList();
        ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

        // Have to loop through the instructions to avoid changing the current instructions in the method node
        while (iterator.hasNext()) {
            newInstructions.add(iterator.next());
        }

        // Instrument start
        AbstractInsnNode firstInstruction = newInstructions.getFirst();
        InsnList startInstructions = this.getInstructionsStartRegion(region);
        newInstructions.insertBefore(firstInstruction.getNext(), startInstructions);

        DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder(methodNode);
        MethodGraph graph = builder.build();
        Set<MethodBlock> endMethodBlocks = graph.getExitBlock().getPredecessors();

        // Instrument all end blocks
        for(MethodBlock endBlock : endMethodBlocks) {
            List<AbstractInsnNode> blockInstructions = endBlock.getInstructions();
            AbstractInsnNode lastInstruction = blockInstructions.get(blockInstructions.size() - 1);
            int opcodeLastInstruction = lastInstruction.getOpcode();

            if((opcodeLastInstruction < Opcodes.IRETURN || opcodeLastInstruction > Opcodes.RETURN)
                    && opcodeLastInstruction != Opcodes.RET) {
                lastInstruction = blockInstructions.get(blockInstructions.size() - 2);
                opcodeLastInstruction = lastInstruction.getOpcode();

                if((opcodeLastInstruction < Opcodes.IRETURN || opcodeLastInstruction > Opcodes.RETURN)
                        && opcodeLastInstruction != Opcodes.RET && opcodeLastInstruction != Opcodes.ATHROW) {
                    throw new RuntimeException("The last instruction in a method with return is not a return instruction");
                }
            }

            InsnList endInstructions = this.getInstructionsEndRegion(region);
            newInstructions.insertBefore(lastInstruction, endInstructions);
        }

        return newInstructions;
    }
}
