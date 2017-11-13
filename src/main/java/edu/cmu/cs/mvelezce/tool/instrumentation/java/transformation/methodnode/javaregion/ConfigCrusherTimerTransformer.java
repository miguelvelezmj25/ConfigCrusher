package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.DefaultMethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

public class ConfigCrusherTimerTransformer extends ConfigCrusherRegionTransformer {

    public ConfigCrusherTimerTransformer(String programName, String entryPoint, String directory, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, entryPoint, directory, regionsToOptionSet);
    }

    public ConfigCrusherTimerTransformer(String programName, String entryPoint, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, entryPoint, classTransformer, regionsToOptionSet);
    }

    @Override
    public void transformMethod(MethodNode methodNode) {
        InsnList newInstructions;
        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

        if(regionsInMethod.size() == 1) {
            newInstructions = this.instrumentEntireMethod(methodNode, regionsInMethod.get(0));
        }
        else {
            newInstructions = this.instrumentRegion(methodNode, regionsInMethod);
        }

        methodNode.instructions.clear();
        methodNode.instructions.add(newInstructions);

//        System.out.println("After transforming");
//        DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder(methodNode);
//        builder.build();
        System.out.println("");
    }

    private InsnList instrumentStart(MethodBlock methodBlock, List<JavaRegion> regionsInMethod) {
        InsnList newInstructions = new InsnList();

        for(JavaRegion javaRegion : regionsInMethod) {
            if(!javaRegion.getStartMethodBlock().equals(methodBlock)) {
                continue;
            }

            newInstructions.add(this.getInstructionsStartRegion(javaRegion));
        }

        return newInstructions;
    }


    private InsnList instrumentEnd(MethodBlock methodBlock, List<JavaRegion> regionsInMethod) {
        InsnList newInstructions = new InsnList();

        for(JavaRegion javaRegion : regionsInMethod) {
            for(MethodBlock endMethodBlock : javaRegion.getEndMethodBlocks()) {
                if(!endMethodBlock.equals(methodBlock)) {
                    continue;
                }

                newInstructions.add(this.getInstructionsEndRegion(javaRegion));
            }

        }

        return newInstructions;
    }

    @Override
    public InsnList getInstructionsStartRegion(JavaRegion javaRegion) {
        InsnList instructionsStartRegion = new InsnList();
        instructionsStartRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        // TODO make this prettier
        instructionsStartRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "enter", "(Ljava/lang/String;)V", false));
//        instructionsStartRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/RegionsCounter", "enter", "(Ljava/lang/String;)V", false));

        return instructionsStartRegion;
    }

    @Override
    public InsnList getInstructionsEndRegion(JavaRegion javaRegion) {
        InsnList instructionsEndRegion = new InsnList();
        instructionsEndRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        // TODO make this prettier
        instructionsEndRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "exit", "(Ljava/lang/String;)V", false));
//        instructionsEndRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/RegionsCounter", "exit", "(Ljava/lang/String;)V", false));

        return instructionsEndRegion;
    }

    /**
     * Loop through the instructions to check where to instrument
     *
     * @param methodNode
     * @param regionsInMethod
     */
    private InsnList instrumentRegion(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        System.out.println("########### " + this.getMethodNodeToClassNode().get(methodNode).name + " " + methodNode.name);

        InsnList newInstructions = this.instrumentRegionsStartAndEndSameBlock(methodNode, regionsInMethod);
        methodNode.instructions.clear();
        methodNode.instructions.add(newInstructions);

        newInstructions = this.instrumentNormal(methodNode, regionsInMethod);

        return newInstructions;
    }

    private InsnList instrumentRegionsStartAndEndSameBlock(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        InsnList newInstructions = new InsnList();
        ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

        // Have to loop through the instructions to avoid changing the current instructions in the method node
        while(iterator.hasNext()) {
            newInstructions.add(iterator.next());
        }

        List<JavaRegion> regionsInMethodReversed = new ArrayList<>(regionsInMethod);
        Collections.reverse(regionsInMethodReversed);

        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        MethodGraph graph = this.getMethodsToGraphs().get(methodNode);

        while(instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();
            MethodBlock block = graph.getMethodBlock(instruction);

            if(block == null) {
                continue;
            }

            for(JavaRegion region : regionsInMethod) {
                if(!this.startAndEndInSameBlock(region, block)) {
                    continue;
                }

                InsnList startInstructions = this.getInstructionsStartRegion(region);
                newInstructions.insertBefore(instruction.getNext(), startInstructions);

                if(this.getEndRegionBlocksWithReturn().contains(block)) {
                    instruction = instructionsIterator.next();
                    int opcode = instruction.getOpcode();

                    while((opcode < Opcodes.IRETURN || opcode > Opcodes.RETURN) && opcode != Opcodes.RET) {
                        instruction = instructionsIterator.next();
                        opcode = instruction.getOpcode();
                    }

                    InsnList endInstructions = this.instrumentEnd(block, regionsInMethodReversed);
                    newInstructions.add(endInstructions);
                }
                else {
                    AbstractInsnNode lastInst = block.getInstructions().get(block.getInstructions().size() - 1);

                    while(lastInst != instruction) {
                        instruction = instructionsIterator.next();
                    }

                    InsnList endInstructions = this.instrumentEnd(block, regionsInMethodReversed);
                    newInstructions.add(endInstructions);
                }
            }
        }

        return newInstructions;
    }

    private InsnList instrumentNormal(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        List<JavaRegion> regionsInMethodReversed = new ArrayList<>(regionsInMethod);
        Collections.reverse(regionsInMethodReversed);

        InsnList newInstructions = new InsnList();
        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        MethodGraph graph = this.getMethodsToGraphs().get(methodNode);

        while(instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();
            newInstructions.add(instruction);

            MethodBlock block = graph.getMethodBlock(instruction);

            if(block == null) {
                continue;
            }

            if(this.getEndRegionBlocksWithReturn().contains(block)) {
                instruction = instructionsIterator.next();
                int opcode = instruction.getOpcode();

                while((opcode < Opcodes.IRETURN || opcode > Opcodes.RETURN) && opcode != Opcodes.RET) {
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

    private boolean startAndEndInSameBlock(JavaRegion region, MethodBlock block) {
        if(region.getStartMethodBlock() != block) {
            return false;
        }

        if(region.getEndMethodBlocks().size() != 1) {
            return false;
        }

        if(region.getStartMethodBlock() != region.getEndMethodBlocks().iterator().next()) {
            return false;
        }

        return true;
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
        while(iterator.hasNext()) {
            newInstructions.add(iterator.next());
        }

        // Instrument start
        AbstractInsnNode firstInstruction = newInstructions.getFirst();
        InsnList startInstructions = this.getInstructionsStartRegion(region);
        newInstructions.insertBefore(firstInstruction.getNext(), startInstructions);

        MethodGraph graph = this.getMethodsToGraphs().get(methodNode);
        Set<MethodBlock> endMethodBlocks = graph.getExitBlock().getPredecessors();

        // Instrument all end blocks
        for(MethodBlock endBlock : endMethodBlocks) {
            List<AbstractInsnNode> blockInstructions = endBlock.getInstructions();
            AbstractInsnNode lastInstruction = blockInstructions.get(blockInstructions.size() - 1);
            int opcodeLastInstruction = lastInstruction.getOpcode();

            if((opcodeLastInstruction < Opcodes.IRETURN || opcodeLastInstruction > Opcodes.RETURN)
                    && opcodeLastInstruction != Opcodes.RET && opcodeLastInstruction != Opcodes.ATHROW) {
                lastInstruction = blockInstructions.get(blockInstructions.size() - 2);
                opcodeLastInstruction = lastInstruction.getOpcode();

                if((opcodeLastInstruction < Opcodes.IRETURN || opcodeLastInstruction > Opcodes.RETURN)
                        && opcodeLastInstruction != Opcodes.RET && opcodeLastInstruction != Opcodes.ATHROW) {
                    if(!this.getMethodsToGraphs().get(methodNode).isWithWhileTrue()) {
                        throw new RuntimeException("The last instruction in a method with return is not a return instruction");
                    }
                }
            }

            InsnList endInstructions = this.getInstructionsEndRegion(region);
            newInstructions.insertBefore(lastInstruction, endInstructions);
        }

        return newInstructions;
    }

}
