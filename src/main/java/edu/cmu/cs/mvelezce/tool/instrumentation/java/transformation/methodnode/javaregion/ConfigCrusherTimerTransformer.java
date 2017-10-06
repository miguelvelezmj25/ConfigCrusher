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

// TODO this class should only add the instrumentation code
public class ConfigCrusherTimerTransformer extends ConfigCrusherRegionTransformer {

    private Set<MethodBlock> blocksToInstrumentBeforeReturn = new HashSet<>();
    private boolean updatedRegions = false;

    public ConfigCrusherTimerTransformer(String programName, String entryPoint, String directory, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, entryPoint, directory, regionsToOptionSet);
    }

    public ConfigCrusherTimerTransformer(String programName, String entryPoint, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, entryPoint, classTransformer, regionsToOptionSet);
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

//    // TODO why dont we return a new map. If I do not return a new map, it sugguest to users that the passed map will be
//    // transformed. If I return a new map, it sugguest to users that the passed map will remain the same and a new map
//    // is generated
//    private void getStartAndEndBlocks(MethodGraph graph, Map<AbstractInsnNode, JavaRegion> instructionsToRegion) {
//        for(MethodBlock block : graph.getBlocks()) {
//            List<AbstractInsnNode> blockInstructions = block.getInstructions();
//
//            for(AbstractInsnNode instructionToStartInstrumenting : instructionsToRegion.keySet()) {
//                if(!blockInstructions.contains(instructionToStartInstrumenting)) {
//                    continue;
//                }
//
//                MethodBlock start = ConfigCrusherTimerTransformer.getBlockToStartInstrumentingBeforeIt(graph, block);
//                start = graph.getMethodBlock(start.getID());
//
//                if(start == null) {
//                    throw new RuntimeException();
//                }
//
//                MethodBlock end = ConfigCrusherTimerTransformer.getBlockToEndInstrumentingBeforeIt(graph, block);
//                end = graph.getMethodBlock(end.getID());
//
//                if(end == null) {
//                    throw new RuntimeException();
//                }
//
//                Set<MethodBlock> endMethodBlocks = new HashSet<>();
//
//                if(start == end) {
//                    throw new RuntimeException("Start and end equal");
//                }
//                else if(start.getSuccessors().size() == 1 && start.getSuccessors().iterator().next().equals(end)) {
//                    // TODO test
//                    throw new RuntimeException("Happens when a control flow decision only has 1 successor??????");
////                        regionsToRemove.add(instructionsToRegion.get(instructionToStartInstrumenting));
//                }
//                else if(graph.getExitBlock() == end) {
//                    this.blocksToInstrumentBeforeReturn.addAll(end.getPredecessors());
//                    endMethodBlocks.addAll(end.getPredecessors());
//                }
//                else {
//                    endMethodBlocks.add(end);
//                }
//
//                JavaRegion region = instructionsToRegion.get(instructionToStartInstrumenting);
//                region.setStartMethodBlock(start);
//                region.setEndMethodBlocks(endMethodBlocks);
//            }
//        }
//
//    }

    @Override
    public void transformMethod(MethodNode methodNode) {
//        System.out.println("Before transforming");
//        DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder(methodNode);
//        MethodGraph graph = builder.build();
//
//        if(graph.getBlocks().size() <= 3) {
////            continue;
////            // TODO this happened in an enum method in which there were two labels in the graph and the first one had the return statement
//            throw new RuntimeException("Check this case");
//        }
//
//        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);
//
//        if(!this.updatedIndexMethods.contains(methodNode)) {
//            this.calculateASMStartIndex(regionsInMethod, methodNode);
//        }
//
//        InsnList newInstructions;
//        int startInstructionCount = methodNode.instructions.size();
//
//        if(regionsInMethod.size() == 1) {
//            newInstructions = this.instrumentEntireMethod(methodNode, regionsInMethod.get(0));
//        }
//        else {
//            Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();
//
//            for(JavaRegion region : regionsInMethod) {
//                instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
//            }
//
//            this.getStartAndEndBlocks(graph, instructionsToRegion);
//            this.removeInnerRegions(regionsInMethod, graph);
//            this.checkSameOptionsInRegions(regionsInMethod);
//
//            if(regionsInMethod.size() == 1) {
//                newInstructions = this.instrumentEntireMethod(methodNode, regionsInMethod.get(0));
//            }
//            else {
//                newInstructions = this.instrumentNormal(methodNode, graph, regionsInMethod);
//            }
//        }
//
//        int endInstructionCount = methodNode.instructions.size();
//
//        if(endInstructionCount != startInstructionCount) {
//            throw new RuntimeException("We modified the instructions in the node itself instead of creating a new list");
//        }
//
//        methodNode.instructions.clear();
//        methodNode.instructions.add(newInstructions);
//
//        int afterInstrumentationInstructionCount = methodNode.instructions.size();
//
//        if(afterInstrumentationInstructionCount <= endInstructionCount) {
//            throw new RuntimeException("We apparently did not add instrumentation");
//        }
//
//        System.out.println("After transforming");
//        builder = new DefaultMethodGraphBuilder(methodNode);
//        builder.build();
//        System.out.print("");
    }

//    private void updateRegions() {
//
//    }
//
//    private void instrument() {
//
//    }

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



//    @Override
    public InsnList getInstructionsStartRegion(JavaRegion javaRegion) {
        InsnList instructionsStartRegion = new InsnList();
        instructionsStartRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsStartRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "enter", "(Ljava/lang/String;)V", false));

        return instructionsStartRegion;
    }

//    @Override
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

//    private Set<String> optionsInMethod() {
//
//    }

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

    public boolean isUpdatedRegions() {
        return updatedRegions;
    }
}
