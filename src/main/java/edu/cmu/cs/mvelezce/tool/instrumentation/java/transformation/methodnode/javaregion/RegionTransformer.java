package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.BytecodeUtils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.DefaultMethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.config.SootConfig;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.apache.commons.lang3.StringUtils;
import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import soot.util.queue.QueueReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

// TODO heuristics
// Remove inner regions if outer regions have the same set or subset of options as the inner regions
// Combine consecutive regions with the same set of options
//
//
//
// TODO should this be abstract?
public abstract class RegionTransformer extends BaseMethodTransformer {

    private String entryPoint;
    private String rootPackage;
    private Map<JavaRegion, Set<Set<String>>> regionsToOptionSet;
    private ClassNode currentClassNode;
    private Set<MethodNode> methodsWithUpdatedIndexes = new HashSet<>();
    private Map<String, List<String>> classToJavapResult = new HashMap<>();
    private CallGraph callGraph;

    public RegionTransformer(String programName, String entryPoint, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) {
        super(programName, classTransformer);

        this.entryPoint = entryPoint;
        this.rootPackage = entryPoint.substring(0, entryPoint.indexOf("."));
        this.regionsToOptionSet = regionsToOptionSet;
        this.callGraph = this.buildCallGraph();
    }

    public RegionTransformer(String programName, String entryPoint, String directory, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        this(programName, entryPoint, new DefaultBaseClassTransformer(directory), regionsToOptionSet);
    }
//
//    // TODO should this be abstract?
//    public abstract InsnList getInstructionsStartRegion(JavaRegion javaRegion);
//
//    // TODO should this be abstract?
//    public abstract InsnList getInstructionsEndRegion(JavaRegion javaRegion);


    // TODO which ones to make abstract?
    public abstract MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start);

    // TODO which ones to make abstract?
    public abstract MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start);

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

    @Override
    public void transformMethods(Set<ClassNode> classNodes) throws IOException {
//        for(ClassNode classNode : classNodes) {
//            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);
//
//            if(methodsToInstrument.isEmpty()) {
//                continue;
//            }
//
//            System.out.println("Transforming class " + classNode.name);
//
//            for(MethodNode methodToInstrument : methodsToInstrument) {
//                this.transformMethod(methodToInstrument);
//            }
//
//            this.getClassTransformer().writeClass(classNode, this.getClassTransformer().getPath() + "/" + classNode.name);
//
//            // TODO if debug
//            TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
//            MethodTracer tracer = classInspector.visitClass();
//
//            for(MethodNode methodNode : methodsToInstrument) {
//                Printer printer = tracer.getPrinterForMethodSignature(methodNode.name + methodNode.desc);
//                PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
//                PrettyMethodGraph prettyGraph = prettyBuilder.build();
//                prettyGraph.saveDotFile(this.getProgramName(), classNode.name, methodNode.name);
//
//                try {
//                    prettyGraph.savePdfFile(this.getProgramName(), classNode.name, methodNode.name);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        Set<SootMethod> methods = this.getSystemMethods();

        boolean updatedRegions = true;

        while (updatedRegions) {
            updatedRegions = this.processMethodsInClasses(classNodes);
            updatedRegions = updatedRegions | this.processGraph(methods);
        }

        System.out.println(this.regionsToOptionSet.size());
        System.out.println();

//        this.transformMethods();

    }

    /**
     * Process the methods to find where the regions are in each of them
     *
     * @param classNodes
     * @return
     * @throws IOException
     */
    private boolean processMethodsInClasses(Set<ClassNode> classNodes) throws IOException {
        boolean updatedMethods = false;

        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            System.out.println("Transforming class " + classNode.name);

            for(MethodNode methodNode : methodsToInstrument) {
                updatedMethods = updatedMethods || this.processRegionsInMethod(methodNode);
            }

//            this.getClassTransformer().writeClass(classNode, this.getClassTransformer().getPath() + "/" + classNode.name);
//
//            // TODO if debug
//            TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
//            MethodTracer tracer = classInspector.visitClass();
//
//            for(MethodNode methodNode : methodsToInstrument) {
//                Printer printer = tracer.getPrinterForMethodSignature(methodNode.name + methodNode.desc);
//                PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
//                PrettyMethodGraph prettyGraph = prettyBuilder.build();
//                prettyGraph.saveDotFile(this.getProgramName(), classNode.name, methodNode.name);
//
//                try {
//                    prettyGraph.savePdfFile(this.getProgramName(), classNode.name, methodNode.name);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        return updatedMethods;
    }

    /**
     * Process the regions inside the methods. This entails removing inner regions and joining regions with the same
     * options.
     *
     * @param methodNode
     */
    private boolean processRegionsInMethod(MethodNode methodNode) {
        DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder(methodNode);
        MethodGraph graph = builder.build();

        if(graph.getBlocks().size() <= 3) {
            // TODO this happened in an enum method in which there were two labels in the graph and the first one had the return statement
            throw new RuntimeException("Check this case");
        }

        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

        if(!this.methodsWithUpdatedIndexes.contains(methodNode)) {
            this.calculateASMStartIndex(regionsInMethod, methodNode);
        }

        if(regionsInMethod.size() == 1) {
            return false;
        }

        boolean updatedRegions;
        Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

        for(JavaRegion region : regionsInMethod) {
            instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
        }

        this.setStartAndEndBlocks(graph, instructionsToRegion);
        updatedRegions = this.removeInnerRegionsInMethod(regionsInMethod, graph);

        if(regionsInMethod.size() == 1) {
            return updatedRegions;
        }

        updatedRegions = updatedRegions | this.leaveOneRegionInMethod(regionsInMethod);

        if(regionsInMethod.size() == 1) {
            return updatedRegions;
        }

        updatedRegions = updatedRegions | this.joinConsecutiveRegions(regionsInMethod);

        return updatedRegions;
    }

    /**
     * Remove all regions, but 1, in a method if they have the same options.
     *
     * @param regionsInMethod
     * @return
     */
    private boolean leaveOneRegionInMethod(List<JavaRegion> regionsInMethod) {
        JavaRegion region = regionsInMethod.get(0);
        Set<Set<String>> regionOptions = this.regionsToOptionSet.get(region);
        boolean oneRegion = true;

        for(int i = 1; i < regionsInMethod.size(); i++) {
            region = regionsInMethod.get(i);

            if(!this.regionsToOptionSet.get(region).equals(regionOptions)) {
                oneRegion = false;
                break;
            }
        }

        if(!oneRegion) {
            return false;
        }

        Set<JavaRegion> regionsToRemove = new HashSet<>();

        for(int i = 1; i < regionsInMethod.size(); i++) {
            regionsToRemove.add(regionsInMethod.get(i));
        }

        regionsInMethod.removeAll(regionsToRemove);
//        for(int i = 1; i < regionsInMethod.size(); i++) {
//            regionsInMethod.remove(i);
//        }

        if(regionsInMethod.size() != 1) {
            throw new RuntimeException("This is a method that has a single region, but did not end up with 1 region");
        }

        this.removeRegionsInRegionsToOptions(regionsToRemove);

        return true;
    }

    private boolean joinConsecutiveRegions(List<JavaRegion> regionsInMethod) {
        JavaRegion region = regionsInMethod.get(0);
        Set<Set<String>> regionOptionSet = this.regionsToOptionSet.get(region);
        Set<String> currentOptions = new HashSet<>();

        for(Set<String> options : regionOptionSet) {
            currentOptions.addAll(options);
        }

        Set<JavaRegion> regionsToRemove = new HashSet<>();

        for(int i = 1; i < regionsInMethod.size(); i++) {
            JavaRegion nextRegion = regionsInMethod.get(i);
            Set<Set<String>> nextRegionOptionSet = this.regionsToOptionSet.get(nextRegion);

            Set<String> nextOptions = new HashSet<>();

            for(Set<String> options : nextRegionOptionSet) {
                nextOptions.addAll(options);
            }

            if(currentOptions.equals(nextOptions) || currentOptions.containsAll(nextOptions)
                    || nextOptions.containsAll(currentOptions)) {
                if(region.getEndMethodBlocks().size() != 1) {
                    throw new RuntimeException("How to handle multiple returns");
                }

                if(nextRegion.getEndMethodBlocks().size() != 1) {
                    throw new RuntimeException("How to handle multiple returns");
                }

                region.setEndMethodBlocks(nextRegion.getEndMethodBlocks());
                regionsToRemove.add(nextRegion);
            }
            else {
                region = nextRegion;
                currentOptions = nextOptions;
            }
        }

        if(regionsToRemove.isEmpty()) {
            return false;
        }

        regionsInMethod.removeAll(regionsToRemove);
        this.removeRegionsInRegionsToOptions(regionsToRemove);

        return true;
    }

    /**
     * Remove inner regions in a method
     *
     * @param regionsInMethod
     * @param graph
     */
    private boolean removeInnerRegionsInMethod(List<JavaRegion> regionsInMethod, MethodGraph graph) {
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

                // Continue if the possible inner region is an outer region of the current region
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


        // Validate options in inner regions and check that we can remove all inner regions within a region
        Set<JavaRegion> regionsWithInnerRegionsWithDifferentOptionSet = new HashSet<>();

        for(Map.Entry<JavaRegion, Set<JavaRegion>> regionToInnerRegions : regionsToInnerRegionSet.entrySet()) {
            JavaRegion region = regionToInnerRegions.getKey();

            Set<Set<String>> regionOptionSet = this.regionsToOptionSet.get(region);
            Set<String> regionOptions = new HashSet<>();

            for(Set<String> options : regionOptionSet) {
                regionOptions.addAll(options);
            }

            if(regionToInnerRegions.getValue().size() == 1) {
                JavaRegion innerRegion = regionToInnerRegions.getValue().iterator().next();

                Set<Set<String>> innerRegionOptionSet = this.regionsToOptionSet.get(innerRegion);
                Set<String> innerRegionOptions = new HashSet<>();

                for(Set<String> options : innerRegionOptionSet) {
                    innerRegionOptions.addAll(options);
                }

                if(!regionOptions.equals(innerRegionOptions) && !innerRegionOptions.containsAll(regionOptions)) {
                    throw new RuntimeException("The region " + region.getStartMethodBlock().getID() + " has 1" +
                            " inner region " + innerRegion.getStartMethodBlock().getID() + ", but the outer options are not a set or" +
                            " a subset of the inner options");
                }
            }
            else {
                Set<String> currentOptions = new HashSet<>();
                currentOptions.addAll(regionOptions);

                Set<JavaRegion> innerRegions = regionToInnerRegions.getValue();

                for(JavaRegion innerRegion : innerRegions) {
                    Set<Set<String>> innerRegionOptionSet = this.regionsToOptionSet.get(innerRegion);
                    Set<String> innerRegionOptions = new HashSet<>();

                    for(Set<String> options : innerRegionOptionSet) {
                        innerRegionOptions.addAll(options);
                    }

                    if(currentOptions.equals(innerRegionOptions) || innerRegionOptions.containsAll(currentOptions)
                            || currentOptions.containsAll(innerRegionOptions)) {
                        currentOptions = new HashSet<>(innerRegionOptions);
                    }
                    else {
                        // If there are multiple inner regions with different options, we cannot remove the inner regions
                        regionsWithInnerRegionsWithDifferentOptionSet.add(region);
                    }
                }

            }
        }

        // Remove the regions that have inner regions with different option sets that cannot be removed
        for(JavaRegion region : regionsWithInnerRegionsWithDifferentOptionSet) {
            regionsToInnerRegionSet.remove(region);
        }

        if(regionsToInnerRegionSet.isEmpty()) {
            return false;
        }

        Set<JavaRegion> regionsToRemove = new HashSet<>();

        for(Set<JavaRegion> innerRegionsToRemove : regionsToInnerRegionSet.values()) {
            regionsToRemove.addAll(innerRegionsToRemove);
        }

        regionsInMethod.removeAll(regionsToRemove);

        if(regionsInMethod.isEmpty()) {
            throw new RuntimeException("The regions in a method cannot be empty after removing inner regions");
        }

        this.updateRegionsToOptionsWithOptionsInInnerRegions(regionsToInnerRegionSet);
        this.removeRegionsInRegionsToOptions(regionsToRemove);

        return true;
    }

    private void removeRegionsInRegionsToOptions(Set<JavaRegion> regionsToRemove) {
        for(JavaRegion region : regionsToRemove) {
            this.regionsToOptionSet.remove(region);
        }

        if(this.regionsToOptionSet.isEmpty()) {
            throw new RuntimeException("The regions in a method cannot be empty after removing inner regions");
        }
    }

    /**
     * Update the regions with all options from the inner regions
     *
     * @param regionsToInnerRegions
     */
    private void updateRegionsToOptionsWithOptionsInInnerRegions(Map<JavaRegion, Set<JavaRegion>> regionsToInnerRegions) {
        Map<JavaRegion, Set<Set<String>>> regionsToOptionSet = this.regionsToOptionSet;

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

    // TODO why dont we return a new map. If I do not return a new map, it sugguest to users that the passed map will be
    // transformed. If I return a new map, it sugguest to users that the passed map will remain the same and a new map
    // is generated
    private void setStartAndEndBlocks(MethodGraph graph, Map<AbstractInsnNode, JavaRegion> instructionsToRegion) {
        for(MethodBlock block : graph.getBlocks()) {
            List<AbstractInsnNode> blockInstructions = block.getInstructions();

            for(AbstractInsnNode instructionToStartInstrumenting : instructionsToRegion.keySet()) {
                if(!blockInstructions.contains(instructionToStartInstrumenting)) {
                    continue;
                }

                MethodBlock start = this.getBlockToStartInstrumentingBeforeIt(graph, block);
                start = graph.getMethodBlock(start.getID());

                if(start == null) {
                    throw new RuntimeException();
                }

                MethodBlock end = this.getBlockToEndInstrumentingBeforeIt(graph, block);
                end = graph.getMethodBlock(end.getID());

                if(end == null) {
                    throw new RuntimeException();
                }

                Set<MethodBlock> endMethodBlocks = new HashSet<>();

                if(start == end) {
                    throw new RuntimeException("Start and end equal");
                }
                else if(start.getSuccessors().size() == 1 && start.getSuccessors().iterator().next().equals(end)) {
                    throw new RuntimeException("Happens when a control flow decision only has 1 successor??????");
                }
                else if(graph.getExitBlock() == end) {
//                    this.blocksToInstrumentBeforeReturn.addAll(end.getPredecessors());
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

    private List<String> getJavapResult() {
        String classPackage = this.getCurrentClassNode().name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = this.getCurrentClassNode().name;
        className = className.substring(className.lastIndexOf("/") + 1);

        List<String> javapResult = this.classToJavapResult.get(className);

        if(javapResult != null) {
            return javapResult;
        }

        javapResult = new ArrayList<>();

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

        this.classToJavapResult.put(className, javapResult);

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

        this.methodsWithUpdatedIndexes.add(methodNode);
    }

//    private boolean processMethodsInClasses(Set<ClassNode> classNodes) throws IOException {
//        for(ClassNode classNode : classNodes) {
//            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);
//
//            if(methodsToInstrument.isEmpty()) {
//                continue;
//            }
//
//            System.out.println("Transforming class " + classNode.name);
//
//            for(MethodNode methodToInstrument : methodsToInstrument) {
//                this.transformMethod(methodToInstrument);
//            }
//
////            this.getClassTransformer().writeClass(classNode, this.getClassTransformer().getPath() + "/" + classNode.name);
////
////            // TODO if debug
////            TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
////            MethodTracer tracer = classInspector.visitClass();
////
////            for(MethodNode methodNode : methodsToInstrument) {
////                Printer printer = tracer.getPrinterForMethodSignature(methodNode.name + methodNode.desc);
////                PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
////                PrettyMethodGraph prettyGraph = prettyBuilder.build();
////                prettyGraph.saveDotFile(this.getProgramName(), classNode.name, methodNode.name);
////
////                try {
////                    prettyGraph.savePdfFile(this.getProgramName(), classNode.name, methodNode.name);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
//        }
//
//        return false;
//    }

    private boolean processGraph(Set<SootMethod> methods) {
        boolean updated = false;
        List<SootMethod> worklist = new LinkedList<>();
        worklist.addAll(methods);

        while (!worklist.isEmpty()) {
            SootMethod method = worklist.remove(0);

            List<JavaRegion> regions = this.getRegionsInMethod(method);

            if(regions.size() != 1) {
                continue;
            }

            List<Edge> callerEdges = this.getCallerEdges(method);

            // Find the first callers that make a call to this method inside a region. This means that methods might have
            // regions that do not call this method. In those case, we need to keep exploring up the call stack.
            while (!callerEdges.isEmpty()) {
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();

                List<JavaRegion> regionsInCaller = this.getRegionsInMethod(callerMethod);

                if(regionsInCaller.isEmpty()) {
                    List<Edge> edges = this.getCallerEdges(callerMethod);
                    int indexToInsert = Math.max(0, callerEdges.size() - 1);
                    callerEdges.addAll(indexToInsert, edges);
                }
                else if(regionsInCaller.size() == 1) {
                    this.removeRegionInMethod(method, callerMethod);
                    worklist.add(callerMethod);
                    updated = true;
                    break;
                }
                else {
                    for(JavaRegion region : regionsInCaller) {
                        throw new RuntimeException("Process");


                        // TODO transform unit to asm instruction
                        // TODO get all method blocks between the start and end blocks
                        // TODO search if the instruction is within the regions
                    }
                }
            }
        }

        return updated;
    }

    /**
     * Remove a region in the first method that is contained in a region in the other method
     *
     * @param method
     * @param callerMethod
     */
    private void removeRegionInMethod(SootMethod method, SootMethod callerMethod) {
        // Get regions in methods
        List<JavaRegion> regionInMethod = this.getRegionsInMethod(method);

        if(regionInMethod.size() != 1) {
            throw new RuntimeException("The inner method does no have 1 region");
        }

        List<JavaRegion> regionInCallerMethod = this.getRegionsInMethod(callerMethod);

        if(regionInCallerMethod.size() != 1) {
            throw new RuntimeException("The outer method does no have 1 region");
        }

        // Get options
        JavaRegion innerRegion = regionInMethod.get(0);
        Set<Set<String>> innerOptionSet = this.regionsToOptionSet.get(innerRegion);

        Set<String> innerOptions = new HashSet<>();

        for(Set<String> options : innerOptionSet) {
            innerOptions.addAll(options);
        }

        JavaRegion outerRegion = regionInCallerMethod.get(0);
        Set<Set<String>> outerOptionSet = this.regionsToOptionSet.get(outerRegion);

        Set<String> outerOptions = new HashSet<>();

        for(Set<String> options : outerOptionSet) {
            outerOptions.addAll(options);
        }

        // validate
        if(!innerOptions.containsAll(outerOptions)) {
            throw new RuntimeException(outerOptions + " is not a set or subset of " + innerOptions);
        }

        Set<JavaRegion> innerRegions = new HashSet<>();
        innerRegions.add(innerRegion);

        Map<JavaRegion, Set<JavaRegion>> regionsToInnerRegions = new HashMap<>();
        regionsToInnerRegions.put(outerRegion, innerRegions);

        this.updateRegionsToOptionsWithOptionsInInnerRegions(regionsToInnerRegions);
        this.removeRegionsInRegionsToOptions(innerRegions);

//        outerOptions.addAll(innerOptions);
//
//        // Update regions to options set
//        this.regionsToOptionSet.remove(innerRegion);
//
//        this.regionsToOptionSet.get(outerRegion).clear();
//        this.regionsToOptionSet.get(outerRegion).add(outerOptions);
    }

    private List<Edge> getCallerEdges(SootMethod method) {
        Iterator<Edge> outEdges = this.callGraph.edgesInto(method);
        List<Edge> callerEdges = new LinkedList<>();

        while (outEdges.hasNext()) {
            Edge edge = outEdges.next();
            MethodOrMethodContext src = edge.getSrc();

            if(!src.method().getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                continue;
            }

            callerEdges.add(edge);
        }

        return callerEdges;
    }

//    private Set<SootMethod> getCallerMethods(SootMethod method) {
//        Set<SootMethod> methods = new HashSet<>();
//        Iterator<Edge> outEdges = this.callGraph.edgesInto(method);
//
//        while(outEdges.hasNext()) {
//            Edge edge = outEdges.next();
//            MethodOrMethodContext src = edge.getSrc();
//
//            if(!src.method().getDeclaringClass().getPackageName().contains(this.rootPackage)) {
//                continue;
//            }
//
//            methods.add(src.method());
//        }
//
//        return methods;
//    }

    /**
     * Get the methods that are used in the system
     *
     * @return
     */
    private Set<SootMethod> getSystemMethods() {
        Set<SootMethod> methods = new HashSet<>();
        QueueReader<Edge> edges = this.callGraph.listener();

        while (edges.hasNext()) {
            Edge edge = edges.next();
            MethodOrMethodContext src = edge.getSrc();

            if(!src.method().getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                continue;
            }

            methods.add(src.method());

//            MethodOrMethodContext tgt = edge.getTgt();
//
//            if(!tgt.method().getDeclaringClass().getPackageName().contains(this.rootPackage)) {
//                continue;
//            }

        }

        return methods;
    }


    private CallGraph buildCallGraph() {
        initializeSoot();
        PackManager.v().getPack("wjpp").apply();
        PackManager.v().getPack("cg").apply();

        return Scene.v().getCallGraph();
    }

    private void initializeSoot() {
        String libPath = "/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/jce.jar";

        soot.G.reset();

        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_soot_classpath(appendClasspath(this.getClassTransformer().getPath(), libPath));

        // Configure the callgraph algorithm
        Options.v().setPhaseOption("cg.spark", "on");
        Options.v().setPhaseOption("cg.spark", "string-constants:true");

        // Specify additional options required for the callgraph
        Options.v().set_whole_program(true);
        Options.v().setPhaseOption("cg", "trim-clinit:false");

        // do not merge variables (causes problems with PointsToSets)
        Options.v().setPhaseOption("jb.ulp", "off");

        Options.v().set_src_prec(Options.src_prec_java);

        //at the end of setting: load user settings:
        new SootConfig().setSootOptions(Options.v());

        // load all entryPoint classes with their bodies
        Scene.v().addBasicClass(this.entryPoint, SootClass.BODIES);
        Scene.v().loadNecessaryClasses();

        boolean hasClasses = false;
//        for(String className : classes) {
        SootClass c = Scene.v().forceResolve(this.entryPoint, SootClass.BODIES);
        if(c != null) {
            c.setApplicationClass();
            if(!c.isPhantomClass() && !c.isPhantom()) {
                hasClasses = true;
            }
//            }
        }

        if(!hasClasses) {
            throw new RuntimeException("Only phantom classes loaded, skipping analysis...");
        }
    }

    private String appendClasspath(String appPath, String libPath) {
        String s = (appPath != null && !appPath.isEmpty()) ? appPath : "";

        if(libPath != null && !libPath.isEmpty()) {
            if(!s.isEmpty()) {
                s += File.pathSeparator;
            }
            s += libPath;
        }
        return s;
    }

    private List<JavaRegion> getRegionsInMethod(SootMethod sootMethod) {
        String classPackage = this.currentClassNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = this.currentClassNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        String methodName = sootMethod.getBytecodeSignature();
        methodName = methodName.substring(methodName.indexOf(" "), methodName.length() - 1).trim();

        List<JavaRegion> javaRegions = new ArrayList<>();

        for(JavaRegion javaRegion : this.regionsToOptionSet.keySet()) {
            if(javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass().equals(className)
                    && javaRegion.getRegionMethod().equals(methodName)) {
                javaRegions.add(javaRegion);
            }
        }

        javaRegions.sort(Comparator.comparingInt(JavaRegion::getStartBytecodeIndex));

        return javaRegions;
    }

    private List<JavaRegion> getRegionsInMethod(MethodNode methodNode) {
        String classPackage = this.currentClassNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = this.currentClassNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        String methodName = methodNode.name + methodNode.desc;

        List<JavaRegion> javaRegions = new ArrayList<>();

        for(JavaRegion javaRegion : this.regionsToOptionSet.keySet()) {
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

        for(JavaRegion javaRegion : this.regionsToOptionSet.keySet()) {
            if(javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass().equals(className)) {
                regionsInClass.add(javaRegion);
            }
        }

        return regionsInClass;
    }

    public ClassNode getCurrentClassNode() {
        return currentClassNode;
    }
}
