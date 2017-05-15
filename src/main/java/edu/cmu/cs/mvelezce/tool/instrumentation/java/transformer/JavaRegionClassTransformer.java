package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraphBuilder;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.*;

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

            System.out.println("Before transforming");

            for(MethodNode method : classNode.methods) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }

            this.transform(classNode);

            System.out.println("After transforming");

            for(MethodNode method : classNode.methods) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }

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
            List<JavaRegion> regionsInMethod = this.getRegionsInMethod(classPackage, className, methodNode.name);
            // TODO have to call this since looping through the instructions seems to set the index to 0. WEIRD
            methodNode.instructions.toArray();

            Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

            for(JavaRegion region : regionsInMethod) {
                instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
            }

            for(MethodBlock block : graph.getBlocks()) {
                List<AbstractInsnNode> blockInstructions = block.getInstructions();

                for(AbstractInsnNode instructionToStartInstrumenting : instructionsToRegion.keySet()) {
                    if(blockInstructions.contains(instructionToStartInstrumenting)) {
                        MethodBlock start = JavaRegionClassTransformer.getBlockToStartInstrumentingBeforeIt(graph, block);
                        MethodBlock end = JavaRegionClassTransformer.getBlockToEndInstrumentingBeforeIt(graph, block);
                        JavaRegion region = instructionsToRegion.get(instructionToStartInstrumenting);
                        region.setStartMethodBlock(start);
                        region.setEndMethodBlock(end);
                    }
                }
            }

            InsnList newInstructions = new InsnList();

            InsnList instructions = methodNode.instructions;
            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

            while(instructionsIterator.hasNext()) {
                AbstractInsnNode instruction = instructionsIterator.next();

                if(instruction.getType() == AbstractInsnNode.LABEL) {
                    LabelNode currentLabelNode = (LabelNode) instruction;

                    for(JavaRegion javaRegion : regionsInMethod) {
                        if (javaRegion.getEndMethodBlock().getID().equals(currentLabelNode.getLabel().toString())) {
                            Label label = new Label();
                            label.info = currentLabelNode.getLabel() + "000end";
                            LabelNode endRegionLabelNode = new LabelNode(label);
                            this.updateLabels(newInstructions, currentLabelNode, endRegionLabelNode);

                            InsnList endRegionInstructions = new InsnList();
                            endRegionInstructions.add(endRegionLabelNode);
                            endRegionInstructions.add(this.addInstructionsEndRegion(javaRegion));
                            newInstructions.add(endRegionInstructions);
                        }
                    }

                    for(JavaRegion javaRegion : regionsInMethod) {
                        if(javaRegion.getStartMethodBlock().getID().equals(currentLabelNode.getLabel().toString())) {
                            Label label = new Label();
                            label.info = currentLabelNode.getLabel() + "000start";
                            LabelNode startRegionLabelNode = new LabelNode(label);
                            this.updateLabels(newInstructions, currentLabelNode, startRegionLabelNode);

                            InsnList startRegionInstructions = new InsnList();
                            startRegionInstructions.add(startRegionLabelNode);
                            startRegionInstructions.add(this.addInstructionsStartRegion(javaRegion));
                            newInstructions.add(startRegionInstructions);
                        }
                    }
                }

                newInstructions.add(instruction);
            }

            methodNode.instructions.clear();
            methodNode.instructions.add(newInstructions);
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

//    public static MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
//        // Find post dominator
//        MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
//        Set<Set<MethodBlock>> stronglyConnectedComponents = methodGraph.getStronglyConnectedComponents(methodGraph.getEntryBlock());
//        Set<MethodBlock> problematicStronglyConnectedComponent = new HashSet<>();
//
//        for(Set<MethodBlock> stronglyConnectedComponent : stronglyConnectedComponents) {
//            if(stronglyConnectedComponent.size() > 1 && (stronglyConnectedComponent.contains(immediatePostDominator) || stronglyConnectedComponent.contains(start))) {
//                problematicStronglyConnectedComponent = new HashSet<>(stronglyConnectedComponent);
//                break;
//            }
//        }
//
//        // If post dominator is not part of a strongly connected component, return
//        if(problematicStronglyConnectedComponent.isEmpty()) {
//            return start;
//        }
//
//        // If post dominator is part of a strongly connected component, find all immediate dominators of cycle except for the immediate post dominator
//        Set<MethodBlock> immediateDominatorsOfProblematicStronglyConnectedComponent = new HashSet<>();
//
//        for(MethodBlock methodBlock : problematicStronglyConnectedComponent) {
//            if(methodBlock.equals(immediatePostDominator)/* || methodBlock.equals(start)*/) {
//                continue;
//            }
//
//            immediateDominatorsOfProblematicStronglyConnectedComponent.add(methodGraph.getImmediateDominator(methodBlock));
//        }
//
//        // If all immediate dominators are within a strongly connected component
//        if(problematicStronglyConnectedComponent.containsAll(immediateDominatorsOfProblematicStronglyConnectedComponent)) {
//            return start;
//        }
//
//        // Get the next post dominator of the strongly connected component that is not part of the strongly connected component
//        Set<MethodBlock> addedBlocksToInstrumentBeforeThem = new HashSet<>();
//        Set<MethodBlock> blocksToInstrumentBeforeThem = new HashSet<>(problematicStronglyConnectedComponent);
//        blocksToInstrumentBeforeThem.add(start);
//        Iterator<MethodBlock> methodBlockIterator = blocksToInstrumentBeforeThem.iterator();
//
//        while(methodBlockIterator.hasNext()) {
//            MethodBlock component = methodBlockIterator.next();
//            MethodBlock immediateDominator = methodGraph.getImmediateDominator(component);
//
////            if(immediateDominator.getSuccessors().size() > 1 && immediateDominator.getSuccessors().contains(component)) {
////                Set<MethodBlock> immediateDominatorStronglyConnectedComponent = new HashSet<>();
////
////                for(Set<MethodBlock> stronglyConnectedComponent : stronglyConnectedComponents) {
////                    if(stronglyConnectedComponent.contains(immediateDominator)) {
////                        immediateDominatorStronglyConnectedComponent = new HashSet<>(stronglyConnectedComponent);
////                        break;
////                    }
////                }
////
////                if(immediateDominatorStronglyConnectedComponent.size() > 1) {
////                    Set<MethodBlock> immediateDominatorsOfImmediateDominatorStronglyConnectedComponent = new HashSet<>();
////
////                    for(MethodBlock methodBlock : immediateDominatorStronglyConnectedComponent) {
////                        if(methodBlock.equals(immediatePostDominator)) {
////                            continue;
////                        }
////
////                        immediateDominatorsOfImmediateDominatorStronglyConnectedComponent.add(methodGraph.getImmediateDominator(methodBlock));
////                    }
////
////                    // If all immediate dominators are within a strongly connected component
////                    if(immediateDominatorStronglyConnectedComponent.containsAll(immediateDominatorsOfImmediateDominatorStronglyConnectedComponent)) {
////                        return component;
////                    }
////                }
////                else {
////                    MethodBlock immediatePostDominatorOfImmediateDominator = methodGraph.getImmediatePostDominator(immediateDominator);
////
////                    if (!blocksToInstrumentBeforeThem.contains(immediateDominator) && !addedBlocksToInstrumentBeforeThem.contains(immediatePostDominatorOfImmediateDominator)) {
////                        addedBlocksToInstrumentBeforeThem.add(immediateDominator);
////                        blocksToInstrumentBeforeThem.add(immediateDominator);
////                        methodBlockIterator = blocksToInstrumentBeforeThem.iterator();
////                        continue;
////                    }
////                }
////            }
//
//            if(!blocksToInstrumentBeforeThem.contains(immediateDominator)) {
//                return component;
//            }
//        }
//
//        throw new RuntimeException("Could not find out where to start instrumenting");
//    }

    public static MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        // Find post dominator
        MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
        Set<Set<MethodBlock>> stronglyConnectedComponents = methodGraph.getStronglyConnectedComponents(methodGraph.getEntryBlock());
        Set<MethodBlock> problematicStronglyConnectedComponent = new HashSet<>();

        for(Set<MethodBlock> stronglyConnectedComponent : stronglyConnectedComponents) {
            if(stronglyConnectedComponent.size() > 1 && (stronglyConnectedComponent.contains(immediatePostDominator) || stronglyConnectedComponent.contains(start))) {
                problematicStronglyConnectedComponent = new HashSet<>(stronglyConnectedComponent);
                break;
            }
        }

        // If post dominator is not part of a strongly connected component, return
        if(problematicStronglyConnectedComponent.isEmpty()) {
            return start;
        }

        // If post dominator is part of a strongly connected component, find all immediate dominators of cycle except for the immediate post dominator
        Set<MethodBlock> immediateDominatorsOfProblematicStronglyConnectedComponent = new HashSet<>();

        for(MethodBlock methodBlock : problematicStronglyConnectedComponent) {
            if(methodBlock.equals(immediatePostDominator) || methodBlock.equals(start)) {
                continue;
            }

            immediateDominatorsOfProblematicStronglyConnectedComponent.add(methodGraph.getImmediateDominator(methodBlock));
        }

        // If all immediate dominators are within a strongly connected component
        if(problematicStronglyConnectedComponent.containsAll(immediateDominatorsOfProblematicStronglyConnectedComponent)) {
            return start;
        }

        // Get the next post dominator of the strongly connected component that is not part of the strongly connected component
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
