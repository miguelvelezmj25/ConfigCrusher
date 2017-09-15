package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.BytecodeUtils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraphBuilder;
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

public class TimerTransformer extends RegionTransformer {

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

                MethodBlock end = TimerTransformer.getBlockToEndInstrumentingBeforeIt(graph, block);
                end = graph.getMethodBlock(end.getID());

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
        MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
        MethodGraph graph = builder.build();

        if(graph.getBlocks().size() <= 3) {
//            continue;
//            // TODO this happened in an enum method in which there were two labels in the graph and the first one had the return statement
            throw new RuntimeException("Check this case");
        }

        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);
        this.calculateASMStartIndex(regionsInMethod, methodNode);
        InsnList newInstructions;

        int startInstructionSize = methodNode.instructions.size();

        if(regionsInMethod.size() == 1) {
            newInstructions = this.instrumentEntireMethod(methodNode, regionsInMethod.get(0));
        }
        else {
            newInstructions = this.instrumentNormal(methodNode, regionsInMethod);
        }

        int end = methodNode.instructions.size();

        if(end != startInstructionSize) {
            throw new RuntimeException("We modified the instructions in the node itself instead of creating a new list");
        }

        methodNode.instructions.clear();
        methodNode.instructions.add(newInstructions);

        System.out.println("After transforming");
        builder = new MethodGraphBuilder(methodNode);
        builder.build();
        System.out.print("");
    }

    // TODO why dont we return a new list
    private InsnList instrumentStart(MethodBlock methodBlock, List<JavaRegion> regionsInMethod) {
        InsnList newInstructions = new InsnList();

        for(JavaRegion javaRegion : regionsInMethod) {
            if(javaRegion.getStartMethodBlock() != methodBlock) {
                continue;
            }


//            Label label = new Label();
//            label.info = labelNode.getLabel() + "000start";
//            LabelNode startRegionLabelNode = new LabelNode(label);
//
//            this.updateLabels(newInstructions, labelNode, startRegionLabelNode);

//            InsnList startRegionInstructions = new InsnList();
////            startRegionInstructions.add(startRegionLabelNode);
//            startRegionInstructions.add(this.getInstructionsStartRegion(javaRegion));
//            newInstructions.add(startRegionInstructions);

            newInstructions.add(this.getInstructionsStartRegion(javaRegion));


//            Label regionOriginalLabel = javaRegion.getStartMethodBlock().getOriginalLabel();
//            Label currentLabel = labelNode.getLabel();
//
//            if(!regionOriginalLabel.toString().equals(currentLabel.toString())) {
//                continue;
//            }
//
//            Label label = new Label();
//            label.info = labelNode.getLabel() + "000start";
//            LabelNode startRegionLabelNode = new LabelNode(label);
//
//            this.updateLabels(newInstructions, labelNode, startRegionLabelNode);
//
//            InsnList startRegionInstructions = new InsnList();
//            startRegionInstructions.add(startRegionLabelNode);
//            startRegionInstructions.add(this.getInstructionsStartRegion(javaRegion));
//            newInstructions.add(startRegionInstructions);
        }

        return newInstructions;
    }

    private Map<MethodBlock, List<JavaRegion>> addRegionsInBlocksWithReturn(MethodBlock methodBlock, List<JavaRegion> regionsInMethod) {
        Map<MethodBlock, List<JavaRegion>> blocksToRegions = new HashMap<>();

        for(JavaRegion javaRegion : regionsInMethod) {
            for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
                if(endMethodBlock != methodBlock) {
                    continue;
                }

                if(!blocksToRegions.containsKey(endMethodBlock)) {
                    blocksToRegions.put(endMethodBlock, new ArrayList<>());
                }

                blocksToRegions.get(endMethodBlock).add(javaRegion);
            }
        }

        return blocksToRegions;
    }

    // TODO why dont we return a new list
    private InsnList instrumentEnd(MethodBlock methodBlock, List<JavaRegion> regionsInMethod) {
        InsnList newInstructions = new InsnList();

        for(JavaRegion javaRegion : regionsInMethod) {
            for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
                if(endMethodBlock != methodBlock) {
                    continue;
                }

//                InsnList endRegionInstructions = new InsnList();
//                endRegionInstructions.add(this.getInstructionsEndRegion(javaRegion));
//                newInstructions.add(endRegionInstructions);
                newInstructions.add(this.getInstructionsEndRegion(javaRegion));
            }


//            for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
//                Label regionOriginalLabel = endMethodBlock.getOriginalLabel();
//                Label currentLabel = labelNode.getLabel();
//
//                if(!regionOriginalLabel.toString().equals(currentLabel.toString())) {
//                    continue;
//                }
//
//                Label label = new Label();
//                label.info = labelNode.getLabel() + "000end";
//                LabelNode endRegionLabelNode = new LabelNode(label);
//
//                this.updateLabels(newInstructions, labelNode, endRegionLabelNode);
//
//                InsnList endRegionInstructions = new InsnList();
//                endRegionInstructions.add(endRegionLabelNode);
//                endRegionInstructions.add(this.getInstructionsEndRegion(javaRegion));
//                newInstructions.add(endRegionInstructions);
//            }
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
        String methodName = methodNode.name;

        if(methodName.startsWith("<init>")) {
            // TODO check this
            methodName = this.getCurrentClassNode().name;
            methodName = methodName.replace("/", ".");
        }

        // Check if signature matches
        for(String outputLine : javapResult) {
            if(outputLine.contains(" " + methodName + "(")) {
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
    private InsnList instrumentNormal(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
        MethodGraph graph = builder.build();

        Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

        for(JavaRegion region : regionsInMethod) {
            instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
        }

        this.getStartAndEndBlocks(graph, instructionsToRegion);
        this.removeInnerRegions(regionsInMethod, graph);

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

            if(block.isWithReturn()) {
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
        Set<JavaRegion> innerRegionsToRemove = new HashSet<>();

        for(JavaRegion region : regionsInMethod) {
            for(JavaRegion possibleInnerRegion : regionsInMethod) {
                if(region == possibleInnerRegion) {
                    continue;
                }

                Set<MethodBlock> possibleInnerRegionEndBlocks = possibleInnerRegion.getEndMethodBlocks();
                if(possibleInnerRegionEndBlocks.size() > 1) {
                    continue;
                }

                MethodBlock possibleInnerRegionEndBlock = possibleInnerRegionEndBlocks.iterator().next();
                Set<MethodBlock> regionReachableBlocks = new HashSet<>();

                for(MethodBlock endBlock : region.getEndMethodBlocks()) {
                    Set<MethodBlock> reachableBlocks = graph.getReachableBlocks(region.getStartMethodBlock(), endBlock);
                    regionReachableBlocks.addAll(reachableBlocks);
                }

                if(regionReachableBlocks.contains(possibleInnerRegion.getStartMethodBlock())
                        && regionReachableBlocks.contains(possibleInnerRegionEndBlock)) {
                    innerRegionsToRemove.add(possibleInnerRegion);
                }
            }
        }

        regionsInMethod.removeAll(innerRegionsToRemove);

        if(regionsInMethod.isEmpty()) {
            throw new RuntimeException("The regions in a method cannot be empty after removing inner regions");
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

        MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
        MethodGraph graph = builder.build();
        Set<MethodBlock> endMethodBlocks = graph.getExitBlock().getPredecessors();

        // Instrument all end blocks
        for(MethodBlock endBlock : endMethodBlocks) {
            List<AbstractInsnNode> blockInstructions = endBlock.getInstructions();
            AbstractInsnNode lastInstruction = blockInstructions.get(blockInstructions.size() - 1);
            int opcodeLastInstruction = lastInstruction.getOpcode();

            if((opcodeLastInstruction < Opcodes.IRETURN || opcodeLastInstruction > Opcodes.RETURN)
                    && opcodeLastInstruction != Opcodes.RET) {
                throw new RuntimeException("The last instruction in a method with return is not a return instruction");
            }

            InsnList endInstructions = this.getInstructionsEndRegion(region);
            newInstructions.insertBefore(lastInstruction, endInstructions);
        }

        return newInstructions;
    }

//    /**
//     * This is done when we stop instrumenting in a basic block that has return statements
//     */
//    private InsnList instrumentEndWithReturn(MethodBlock blockWithReturn, List<JavaRegion> regions) {
//        InsnList newInstructions = new InsnList();
//
//        for(JavaRegion javaRegion : regions) {
//            for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
//                if(endMethodBlock != blockWithReturn) {
//                    continue;
//                }
//
//                List<AbstractInsnNode> blockInstructions = blockWithReturn.getInstructions();
//
//                for(AbstractInsnNode insnNode : blockInstructions) {
//                    newInstructions.add(insnNode);
//                }
//
//                AbstractInsnNode lastInstruction = blockInstructions.get(blockInstructions.size() - 1);
//                int opcodeLastInstruction = lastInstruction.getOpcode();
//
//                if((opcodeLastInstruction < Opcodes.IRETURN || opcodeLastInstruction > Opcodes.RETURN)
//                        && opcodeLastInstruction != Opcodes.RET) {
//                    throw new RuntimeException("The last instruction in a method with return is not a return instruction");
//                }
//
//                for(JavaRegion region : regions) {
//                    InsnList endInstructions = this.getInstructionsEndRegion(region);
//                    newInstructions.insertBefore(lastInstruction, endInstructions);
//                }
//            }
//        }
//
//        return newInstructions;
//    }
}
