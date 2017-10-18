package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.BytecodeUtils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodTracer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.TraceClassInspector;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.*;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.config.SootConfig;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.util.Printer;
import org.apache.commons.lang3.StringUtils;
import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;
import soot.util.queue.QueueReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

public abstract class RegionTransformer extends BaseMethodTransformer {

    private static final String CLINIT_SIGNATURE = "void <clinit>()";
    private static final String MAIN_SIGNATURE = "void main(java.lang.String[])";

    private String entryPoint;
    private String rootPackage;
    private Map<JavaRegion, Set<Set<String>>> regionsToOptionSet;

    private CallGraph callGraph;
    private Map<MethodNode, ClassNode> methodNodeToClassNode = new HashMap<>();
    private Set<MethodNode> methodsWithUpdatedIndexes = new HashSet<>();
    private Map<String, List<String>> classToJavapResult = new HashMap<>();
    private Map<MethodNode, MethodGraph> methodToGraph = new HashMap<>();
    private Map<MethodNode, SootMethod> methodNodeToSootMethod = new HashMap<>();
    private Map<SootMethod, MethodNode> sootMethodToMethodNode = new HashMap<>();
    private Map<SootMethod, Set<Set<String>>> sootMethodToOptionSet = new HashMap<>();
    private Set<MethodBlock> endRegionBlocksWithReturn = new HashSet<>();

    public RegionTransformer(String programName, String entryPoint, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) {
        super(programName, classTransformer);

        this.entryPoint = entryPoint;
        this.rootPackage = entryPoint.substring(0, entryPoint.indexOf("."));
        this.regionsToOptionSet = regionsToOptionSet;
        this.callGraph = this.buildCallGraph();

        System.out.println("Call graph size: " + this.callGraph.size());
    }

    public RegionTransformer(String programName, String entryPoint, String directory, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        this(programName, entryPoint, new DefaultBaseClassTransformer(directory), regionsToOptionSet);
    }

    public abstract MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start);

    public abstract MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start);

    @Override
    public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
        Set<MethodNode> methodsToInstrument = new HashSet<>();

        if(this.getRegionsInClass(classNode).isEmpty()) {
            return methodsToInstrument;
        }

        for(MethodNode methodNode : classNode.methods) {
            if(!this.getRegionsInMethod(methodNode).isEmpty()) {
                methodsToInstrument.add(methodNode);
            }
        }

        return methodsToInstrument;
    }

    @Override
    public void transformMethods(Set<ClassNode> classNodes) throws IOException {
        Set<SootMethod> methods = this.getSystemMethods();
        this.matchSootToASMMethods(methods, classNodes);

        for(ClassNode classNode : classNodes) {
            for(MethodNode methodNode : classNode.methods) {
                this.methodNodeToClassNode.put(methodNode, classNode);
            }
        }

        for(Map.Entry<JavaRegion, Set<Set<String>>> entry : this.regionsToOptionSet.entrySet()) {
            JavaRegion region = entry.getKey();
            String bytecodeSignature = region.getRegionPackage() + "." + region.getRegionClass() + ": " + region.getRegionMethod();

            for(SootMethod sootMethod : this.sootMethodToMethodNode.keySet()) {
                if(!sootMethod.getBytecodeSignature().contains(bytecodeSignature)) {
                    continue;
                }

                Set<Set<String>> optionSet = this.sootMethodToOptionSet.get(sootMethod);

                if(optionSet == null) {
                    Set<Set<String>> set = new HashSet<>();
                    set.addAll(entry.getValue());
                    this.sootMethodToOptionSet.put(sootMethod, set);
                }
                else {
                    optionSet.addAll(entry.getValue());
                }

            }
        }

        this.preProcessMethodsInClasses(classNodes);

        int initialRegionCount = this.regionsToOptionSet.size();
        boolean updatedRegions = true;
//
        while(updatedRegions) {
            updatedRegions = this.processGraph();
//            updatedRegions = this.processMethodsInClasses(classNodes);
            updatedRegions = updatedRegions | this.processMethodsInClasses(classNodes);
        }

        System.out.println("# of regions before optimizing: " + initialRegionCount);
        System.out.println("# of regions after optimizing: " + this.regionsToOptionSet.size());

        // Instrument
        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            System.out.println("Transforming class for instrumentation " + classNode.name);

            for(MethodNode methodToInstrument : methodsToInstrument) {
                this.transformMethod(methodToInstrument);
            }

            this.getClassTransformer().writeClass(classNode, this.getClassTransformer().getPath() + "/" + classNode.name);

            TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
            MethodTracer tracer = classInspector.visitClass();

            for(MethodNode methodNode : methodsToInstrument) {
                Printer printer = tracer.getPrinterForMethodSignature(methodNode.name + methodNode.desc);
                PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
                PrettyMethodGraph prettyGraph = prettyBuilder.build();
                prettyGraph.saveDotFile(this.getProgramName(), classNode.name, methodNode.name);

                try {
                    prettyGraph.savePdfFile(this.getProgramName(), classNode.name, methodNode.name);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println();
    }

    public void matchSootToASMMethods(Set<SootMethod> sootMethods, Set<ClassNode> classNodes) {
        for(ClassNode classNode : classNodes) {
            List<MethodNode> methodNodes = classNode.methods;

            for(MethodNode methodNode : methodNodes) {
                String classPackageNode = classNode.name;
                classPackageNode = classPackageNode.substring(0, classPackageNode.lastIndexOf("/"));
                classPackageNode = classPackageNode.replace("/", ".");

                String classNameNode = classNode.name;
                classNameNode = classNameNode.substring(classNameNode.lastIndexOf("/") + 1);

                String methodNameNode = methodNode.name + methodNode.desc;

                for(SootMethod sootMethod : sootMethods) {
                    String classPackageSoot = sootMethod.getDeclaringClass().getPackageName();
                    String classNameSoot = sootMethod.getDeclaringClass().getShortName();
                    String methodNameSoot = sootMethod.getBytecodeSignature();
                    methodNameSoot = methodNameSoot.substring(methodNameSoot.indexOf(" "), methodNameSoot.length() - 1).trim();

                    if(classPackageNode.equals(classPackageSoot) && classNameNode.equals(classNameSoot)
                            && methodNameNode.equals(methodNameSoot)) {
                        this.sootMethodToMethodNode.put(sootMethod, methodNode);
                        this.methodNodeToSootMethod.put(methodNode, sootMethod);

                        break;
                    }
                }
            }
        }
    }

    private void preProcessMethodsInClasses(Set<ClassNode> classNodes) throws IOException {
        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            System.out.println("Preprocessing methods in class " + classNode.name);

            for(MethodNode methodNode : methodsToInstrument) {
                MethodGraph graph = this.buildMethodGraph(methodNode);
                List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

                if(regionsInMethod.size() == 1) {
                    continue;
                }

                if(!this.methodsWithUpdatedIndexes.contains(methodNode)) {
                    this.calculateASMStartIndex(regionsInMethod, methodNode);
                }

                Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

                for(JavaRegion region : regionsInMethod) {
                    instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
                }

                this.setStartAndEndBlocks(graph, instructionsToRegion);
            }
        }
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

            System.out.println("Transforming class to remove regions " + classNode.name);

            for(MethodNode methodNode : methodsToInstrument) {
                updatedMethods = updatedMethods | this.processRegionsInMethod(methodNode);
            }
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
        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

        if(regionsInMethod.size() == 1) {
            return false;
        }

        if(!this.methodsWithUpdatedIndexes.contains(methodNode)) {
            this.calculateASMStartIndex(regionsInMethod, methodNode);
        }

        this.buildMethodGraph(methodNode);
        this.setStartAndEndBlocksIfAbsent(methodNode, regionsInMethod);

        boolean updatedRegions;
        updatedRegions = this.removeInnerRegionsInMethod(methodNode, regionsInMethod);

        if(regionsInMethod.size() == 1) {
            return updatedRegions;
        }

        updatedRegions = this.leaveOneRegionInMethod(regionsInMethod);

        if(regionsInMethod.size() == 1) {
            return updatedRegions;
        }

        updatedRegions = this.joinConsecutiveRegions(methodNode, regionsInMethod);

        if(regionsInMethod.size() == 1) {
            return updatedRegions;
        }

        return updatedRegions;
    }

    private void setStartAndEndBlocksIfAbsent(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        boolean update = false;

        for(JavaRegion region : regionsInMethod) {
            if(region.getStartMethodBlock() == null || region.getEndMethodBlocks() == null || region.getEndMethodBlocks().isEmpty()) {
                update = true;
                break;
            }
        }

        if(!update) {
            return;
        }

        MethodGraph graph = this.methodToGraph.get(methodNode);

        if(graph == null) {
            throw new RuntimeException("The graph cannot be null");
        }

        Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

        for(JavaRegion region : regionsInMethod) {
            instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
        }

        this.setStartAndEndBlocks(graph, instructionsToRegion);
    }

    private MethodGraph buildMethodGraph(MethodNode methodNode) {
        MethodGraph graph = this.methodToGraph.get(methodNode);

        if(graph == null) {
            DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder(methodNode);
            graph = builder.build();
            this.methodToGraph.put(methodNode, graph);

//            if(graph.getBlocks().size() <= 3) {
//                // TODO this happened in an enum method in which there were two labels in the graph and the first one had the return statement
//                throw new RuntimeException("Check this case");
//            }
        }

        return graph;
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

        if(regionsInMethod.size() != 1) {
            throw new RuntimeException("This is a method that has a single region, but did not end up with 1 region");
        }

        this.removeRegionsInRegionsToOptions(regionsToRemove);

        return true;
    }

    private boolean joinConsecutiveRegions(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
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

                MethodGraph graph = this.methodToGraph.get(methodNode);
                Set<MethodBlock> reachableBlocks = graph.getReachableBlocks(region.getStartMethodBlock(), region.getEndMethodBlocks().iterator().next());

                if(!reachableBlocks.contains(nextRegion.getStartMethodBlock()) || !reachableBlocks.containsAll(nextRegion.getEndMethodBlocks())) {
                    region.setEndMethodBlocks(nextRegion.getEndMethodBlocks());
                }

                regionsToRemove.add(nextRegion);

                Set<String> newOptions = new HashSet<>();
                newOptions.addAll(currentOptions);
                newOptions.addAll(nextOptions);

                Set<Set<String>> oldOptionSet = this.regionsToOptionSet.get(region);
                oldOptionSet.clear();
                oldOptionSet.add(newOptions);

                currentOptions = newOptions;
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
     */
    private boolean removeInnerRegionsInMethod(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        MethodGraph graph = this.methodToGraph.get(methodNode);

        if(graph == null) {
            graph = this.buildMethodGraph(methodNode);
        }

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
                    regionsWithInnerRegionsWithDifferentOptionSet.add(region);
//                    throw new RuntimeException("The region " + region.getStartMethodBlock().getID() + " has 1" +
//                            " inner region " + innerRegion.getStartMethodBlock().getID() + ", but the outer options are not a set or" +
//                            " a subset of the inner options");
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
                    this.endRegionBlocksWithReturn.addAll(end.getPredecessors());
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

    private List<String> getJavapResult(ClassNode classNode) {
        String classPackage = classNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = classNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        return this.getJavapResult(classPackage, className);
    }

    private List<String> getJavapResult(String classPackage, String className) {
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

            while((string = inputReader.readLine()) != null) {
                if(!string.isEmpty()) {
                    javapResult.add(string);
                }
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while((string = errorReader.readLine()) != null) {
                System.out.println(string);
            }

            process.waitFor();
        } catch(IOException | InterruptedException ie) {
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
        int methodStartIndex = this.getJavapStartIndex(methodNode);
        List<String> javapResult = this.getJavapResult(this.methodNodeToClassNode.get(methodNode));

        int instructionNumber = 0;
        int currentBytecodeIndex = -1;
        // 2 are the lines before the actual code in a method
        int javapOffset = 2;
        Set<JavaRegion> updatedRegions = new HashSet<>();

        for(int i = (methodStartIndex + javapOffset); i < javapResult.size(); i++) {
            String outputLine = javapResult.get(i);

            if(outputLine.contains(" Code:")) {
                break;
            }

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

                while(instructions.hasNext()) {
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

    private Set<SootMethod> getLeafMethods() {
        Set<SootMethod> leafMethods = new HashSet<>();
        Set<SootMethod> nonLeafMethods = new HashSet<>();
        QueueReader<Edge> callEdges = this.callGraph.listener();

        while(callEdges.hasNext()) {
            Edge edge = callEdges.next();
            MethodOrMethodContext srcObject = edge.getSrc();
            SootMethod src = srcObject.method();

            if(!src.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                continue;
            }

            MethodOrMethodContext tgtObject = edge.getTgt();
            SootMethod tgt = tgtObject.method();

            if(!tgt.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                if(!nonLeafMethods.contains(src)) {
                    leafMethods.add(src);
                }

                continue;
            }

            // Analyze the if the target is a leaf since we might not have an edge where the target is the source
            Iterator<Edge> tgtCallees = this.callGraph.edgesOutOf(tgt);
            boolean allJavaCalls = true;

            while(tgtCallees.hasNext()) {
                Edge callee = tgtCallees.next();

                // There is a call from the target to another method of this program.
                if(callee.getTgt().method().getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                    allJavaCalls = false;
                    break;
                }
            }

            if(!allJavaCalls) {
                nonLeafMethods.add(src);
                nonLeafMethods.add(tgt);
                leafMethods.remove(src);
                leafMethods.remove(tgt);
                continue;
            }

            if(!nonLeafMethods.contains(tgt)) {
                leafMethods.add(tgt);
            }
            else {
                throw new RuntimeException("How can it be that the target has all calls to java methods or no calls," +
                        " but it was added as a non leaf method?");
            }
        }

        if(leafMethods.isEmpty()) {
            throw new RuntimeException("There has to be leaf methods");
        }

        return leafMethods;
    }

    private Set<SootMethod> getLeafMethodsWithRegions() {
        Set<SootMethod> leafMethodsWithRegions = new HashSet<>();
        List<SootMethod> worklist = new ArrayList<>();
        worklist.addAll(this.getLeafMethods());

        while(!worklist.isEmpty()) {
            System.out.println(worklist.size());
            SootMethod sootMethod = worklist.remove(0);

            if(sootMethod.getSubSignature().equals(RegionTransformer.CLINIT_SIGNATURE)) {
                continue;
            }

            if(sootMethod.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
                continue;
            }

            List<JavaRegion> regions = this.getRegionsInMethod(sootMethod);

            if(!regions.isEmpty()) {
                leafMethodsWithRegions.add(sootMethod);
                continue;
            }

            List<Edge> callerEdges = this.getCallerEdges(sootMethod);

            for(Edge callerEdge : callerEdges) {
                SootMethod callerMethod = callerEdge.src();

                if(callerMethod.getSubSignature().equals(RegionTransformer.CLINIT_SIGNATURE)) {
                    continue;
                }

                if(callerMethod.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
                    continue;
                }

                if(worklist.contains(sootMethod)) {
                    continue;
                }

                if(sootMethod.equals(callerMethod)) {
                    continue;
                }

                int insertIndex = Math.max(0, worklist.size() - 1);
                worklist.add(insertIndex, callerMethod);
            }
        }

        return leafMethodsWithRegions;
    }

    private Set<SootMethod> getNonLeafMethods() {
        Set<SootMethod> nonLeafMethods = new HashSet<>();
        QueueReader<Edge> callEdges = this.callGraph.listener();

        while(callEdges.hasNext()) {
            Edge edge = callEdges.next();
            MethodOrMethodContext srcObject = edge.getSrc();
            SootMethod src = srcObject.method();

            if(!src.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                continue;
            }

            MethodOrMethodContext tgtObject = edge.getTgt();
            SootMethod tgt = tgtObject.method();

            if(!tgt.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                continue;
            }

            nonLeafMethods.add(src);
        }

        if(nonLeafMethods.isEmpty()) {
            throw new RuntimeException("There has to be non leaf methods");
        }

        return nonLeafMethods;
    }

    private boolean processGraph() {
        boolean updated = false;

        // First add the leaf methods
        Set<SootMethod> leafMethodsWithRegions = this.getLeafMethodsWithRegions();
        Set<SootMethod> nonLeafMethodsWithRegions = this.getSystemMethods();
        nonLeafMethodsWithRegions.removeAll(leafMethodsWithRegions);

        List<SootMethod> worklist = new ArrayList<>();
        worklist.addAll(leafMethodsWithRegions);

        // Push the regions up the call graph for the leaf methods
        while(!worklist.isEmpty()) {
            SootMethod method = worklist.remove(0);
            updated = updated | this.pushMethodsUpTheCallGraphToEmptyMethods(method);
            updated = updated | this.pushMethodsUpTheCallGraphToEmptyOrOneRegionMethods(method);
        }

        worklist.addAll(nonLeafMethodsWithRegions);

        // Push the regions up the call graph for the non leaf methods
        while(!worklist.isEmpty()) {
            SootMethod method = worklist.remove(0);
            updated = updated | this.pushMethodsUpTheCallGraphToEmptyMethods(method);
            updated = updated | this.pushMethodsUpTheCallGraphToEmptyOrOneRegionMethods(method);
        }

        // Process all methods
        worklist.addAll(leafMethodsWithRegions);
        worklist.addAll(nonLeafMethodsWithRegions);

        updated = updated | this.processGraphWorklistAlgorithm(worklist);

        return updated;
    }

    private boolean processGraphWorklistAlgorithm(List<SootMethod> worklist) {
        boolean updated = false;

        // Process
        while(!worklist.isEmpty()) {
            SootMethod method = worklist.remove(0);
            List<JavaRegion> regions = this.getRegionsInMethod(method);

            if(regions.size() != 1) {
                continue;
            }

            this.pushMethodsUpTheCallGraphToEmptyMethods(method);
            regions = this.getRegionsInMethod(method);

            if(regions.size() != 1) {
                continue;
            }

            boolean reachedMainWithoutRegion = false;
            Map<JavaRegion, SootMethod> regionsWithCallToMethods = new HashMap<>();
            List<Edge> callerEdges = this.getCallerEdges(method);

            // Check other strategies for each caller
            while(!callerEdges.isEmpty()) {
                reachedMainWithoutRegion = false;
                regionsWithCallToMethods = new HashMap<>();
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();
                List<JavaRegion> regionsInCaller = this.getRegionsInMethod(callerMethod);

                if(regionsInCaller.isEmpty()) {
                    // Find regions higher in the call stack
                    if(callerMethod.getSubSignature().equals(RegionTransformer.CLINIT_SIGNATURE)) {
                        continue;
                    }

                    List<Edge> edges = this.getCallerEdges(callerMethod);

                    if(edges.isEmpty() && callerMethod.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
                        reachedMainWithoutRegion = true;
                        break;
                    }

                    int indexToInsert = Math.max(0, callerEdges.size() - 1);
                    callerEdges.addAll(indexToInsert, edges);
                }
                else if(regionsInCaller.size() == 1) {
                    regionsWithCallToMethods.put(regionsInCaller.get(0), callerMethod);
                }
                else {
                    // Find if the call happens inside a region. If not, look higher in the call stack
                    List<Integer> bytecodeIndexes = new ArrayList<>();

                    for(Tag tag : outEdge.srcUnit().getTags()) {
                        if(tag instanceof BytecodeOffsetTag) {
                            int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
                            bytecodeIndexes.add(bytecodeIndex);
                        }
                    }

                    if(bytecodeIndexes.isEmpty()) {
                        throw new RuntimeException("There must be a bytecode index tag");
                    }

                    int bytecodeIndex;

                    if(bytecodeIndexes.size() == 1) {
                        bytecodeIndex = bytecodeIndexes.get(0);
                    }
                    else {
                        int index = bytecodeIndexes.indexOf(Collections.min(bytecodeIndexes));
                        bytecodeIndex = bytecodeIndexes.get(index);
                    }

                    AbstractInsnNode callerInstruction = this.getASMInstruction(callerMethod, bytecodeIndex);
                    boolean foundInRegion = false;

                    for(JavaRegion region : regionsInCaller) {
                        Set<MethodBlock> reachableBlocks = new HashSet<>();
                        MethodBlock start = region.getStartMethodBlock();
                        Set<MethodBlock> ends = region.getEndMethodBlocks();
                        MethodGraph graph = this.methodToGraph.get(this.sootMethodToMethodNode.get(callerMethod));

                        for(MethodBlock end : ends) {
                            Set<MethodBlock> blocks = graph.getReachableBlocks(start, end);
                            reachableBlocks.addAll(blocks);
                        }

                        reachableBlocks.remove(start);
                        reachableBlocks.removeAll(ends);

                        for(MethodBlock block : reachableBlocks) {
                            if(block.getInstructions().contains(callerInstruction)) {
                                regionsWithCallToMethods.put(region, callerMethod);
                                foundInRegion = true;
                            }
                        }
                    }

                    if(foundInRegion) {
                        continue;
                    }

                    // Find regions higher in the call stack
                    if(callerMethod.getSubSignature().equals(RegionTransformer.CLINIT_SIGNATURE)) {
                        continue;
                    }

                    List<Edge> edges = this.getCallerEdges(callerMethod);

                    if(edges.isEmpty() && callerMethod.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
                        reachedMainWithoutRegion = true;
                        break;
                    }

                    int indexToInsert = Math.max(0, callerEdges.size() - 1);
                    callerEdges.addAll(indexToInsert, edges);
                }

            }

            if(reachedMainWithoutRegion || regionsWithCallToMethods.isEmpty()) {
                continue;
            }

            // Check that the outer options are a set or subset of the inner options. Otherwise, we do not push up since
            // we do not want to create interactions
            Set<Set<String>> innerRegionOptionSet = this.regionsToOptionSet.get(regions.get(0));
            Set<String> innerRegionOptions = new HashSet<>();

            for(Set<String> set : innerRegionOptionSet) {
                innerRegionOptions.addAll(set);
            }

            Set<JavaRegion> regionsWithCallWithDifferentOptions = new HashSet<>();

            for(JavaRegion outerRegion : regionsWithCallToMethods.keySet()) {
                Set<Set<String>> outerRegionOptionSet = this.regionsToOptionSet.get(outerRegion);
                Set<String> outerRegionOptions = new HashSet<>();

                for(Set<String> set : outerRegionOptionSet) {
                    outerRegionOptions.addAll(set);
                }

                if(!outerRegionOptions.equals(innerRegionOptions) && !innerRegionOptions.containsAll(outerRegionOptions)
                        && !outerRegionOptions.containsAll(innerRegionOptions)) {
                    regionsWithCallWithDifferentOptions.add(outerRegion);
                }
            }

            for(JavaRegion region : regionsWithCallWithDifferentOptions) {
                regionsWithCallToMethods.remove(region);
            }

            if(regionsWithCallToMethods.isEmpty()) {
                continue;
            }

            // Update the regions
            Set<JavaRegion> innerRegions = new HashSet<>();
            innerRegions.add(regions.get(0));

            Map<JavaRegion, Set<JavaRegion>> regionsToInnerRegions = new HashMap<>();

            for(JavaRegion region : regionsWithCallToMethods.keySet()) {
                regionsToInnerRegions.put(region, innerRegions);
            }

            this.updateRegionsToOptionsWithOptionsInInnerRegions(regionsToInnerRegions);
            this.removeRegionsInRegionsToOptions(innerRegions);

            worklist.addAll(0, regionsWithCallToMethods.values());
            updated = true;
        }

        return updated;
    }

    private boolean pushMethodsUpTheCallGraphToEmptyOrOneRegionMethods(SootMethod method) {
        boolean updated = false;
        List<SootMethod> worklist = new ArrayList<>();
        worklist.add(method);

        while(!worklist.isEmpty()) {
            method = worklist.remove(0);
            List<JavaRegion> regions = this.getRegionsInMethod(method);

            if(regions.size() != 1) {
                continue;
            }

            if(method.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
                continue;
            }

            // Check if all the callers have at most one region to push regions up
            boolean allMethodWithOneRegionMax = true;
            List<Edge> callerEdges = this.getCallerEdges(method);

            while(!callerEdges.isEmpty()) {
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();
                List<JavaRegion> regionsInCaller = this.getRegionsInMethod(callerMethod);

                if(regionsInCaller.size() > 1) {
                    allMethodWithOneRegionMax = false;
                    break;
                }
            }

            if(!allMethodWithOneRegionMax) {
                continue;
            }

            // Check if the options in the caller can be merged with the ones in the callee
            JavaRegion region = regions.get(0);
            Set<Set<String>> innerOptionSet = this.regionsToOptionSet.get(region);
            Set<String> innerOptions = new HashSet<>();

            for(Set<String> set : innerOptionSet) {
                innerOptions.addAll(set);
            }

            boolean canMerge = true;
            callerEdges = this.getCallerEdges(method);

            while(!callerEdges.isEmpty()) {
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();
                List<JavaRegion> regionsInCaller = this.getRegionsInMethod(callerMethod);

                if(regionsInCaller.isEmpty()) {
                    continue;
                }

                Set<Set<String>> outerOptionSet = this.regionsToOptionSet.get(regionsInCaller.get(0));
                Set<String> outerOptions = new HashSet<>();

                for(Set<String> set : outerOptionSet) {
                    outerOptions.addAll(set);
                }

                if(!outerOptions.equals(innerOptions) && !outerOptions.containsAll(innerOptions)
                        && !innerOptions.containsAll(outerOptions)) {
                    canMerge = false;
                    break;
                }
            }

            if(!canMerge) {
                continue;
            }

            // Check if the region can be pushed if no interactions are created
            Set<Set<String>> optionSet = this.regionsToOptionSet.get(region);
            boolean canPush = true;
            callerEdges = this.getCallerEdges(method);

            while(!callerEdges.isEmpty()) {
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();

                Set<Set<String>> callerOptionSet = this.sootMethodToOptionSet.get(callerMethod);

                if(callerOptionSet != null && !optionSet.equals(callerOptionSet)
                        && !optionSet.containsAll(callerOptionSet) && !callerOptionSet.containsAll(optionSet)) {
                    canPush = false;
                    break;
                }
            }

            if(!canPush) {
                continue;
            }

            // First update the options
            updated = true;
            Set<JavaRegion> regionToRemove = new HashSet<>();
            regionToRemove.add(region);
            callerEdges = this.getCallerEdges(method);

            while(!callerEdges.isEmpty()) {
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();
                List<JavaRegion> regionsInCaller = this.getRegionsInMethod(callerMethod);

                if(regionsInCaller.isEmpty()) {
                    continue;
                }

                optionSet = this.sootMethodToOptionSet.get(callerMethod);

                if(optionSet == null) {
                    Set<Set<String>> set = new HashSet<>();
                    set.addAll(this.regionsToOptionSet.get(region));
                    this.sootMethodToOptionSet.put(callerMethod, set);
                }
                else {
                    optionSet.addAll(this.regionsToOptionSet.get(region));
                }

                Map<JavaRegion, Set<JavaRegion>> regionsToInnerRegions = new HashMap<>();
                regionsToInnerRegions.put(regionsInCaller.get(0), regionToRemove);
                this.updateRegionsToOptionsWithOptionsInInnerRegions(regionsToInnerRegions);

                if(this.getRegionsInMethod(callerMethod).size() != 1) {
                    throw new RuntimeException("Updating the options in the region should not create more regions");
                }
            }

            // Then create new regions in the callers with no regions
            List<Edge> callersWithNoRegions = new ArrayList<>();
            callerEdges = this.getCallerEdges(method);

            while(!callerEdges.isEmpty()) {
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();
                List<JavaRegion> regionsInCaller = this.getRegionsInMethod(callerMethod);

                if(!regionsInCaller.isEmpty()) {
                    continue;
                }

                callersWithNoRegions.add(outEdge);
            }

            this.createRegionsInCallers(region, callersWithNoRegions);

            callerEdges = this.getCallerEdges(method);

            for(Edge callerEdge : callerEdges) {
                worklist.add(callerEdge.getSrc().method());
            }
        }

        return updated;
    }

    private boolean pushMethodsUpTheCallGraphToEmptyMethods(SootMethod method) {
        boolean updated = false;
        List<SootMethod> worklist = new ArrayList<>();
        worklist.add(method);

        while(!worklist.isEmpty()) {
            method = worklist.remove(0);
            List<JavaRegion> regions = this.getRegionsInMethod(method);

            if(regions.size() != 1) {
                continue;
            }

            JavaRegion region = regions.get(0);

            if(method.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
                continue;
            }

            // Check if all the callers are empty to push regions up
            boolean allEmpty = true;
            List<Edge> callerEdges = this.getCallerEdges(method);

            while(!callerEdges.isEmpty()) {
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();
                List<JavaRegion> regionsInCaller = this.getRegionsInMethod(callerMethod);

                if(!regionsInCaller.isEmpty()) {
                    allEmpty = false;
                    break;
                }
            }

            if(!allEmpty) {
                continue;
            }

            // Check if the region can be pushed if no interactions are created
            Set<Set<String>> optionSet = this.regionsToOptionSet.get(region);
            boolean canPush = true;
            callerEdges = this.getCallerEdges(method);

            while(!callerEdges.isEmpty()) {
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();

                Set<Set<String>> callerOptionSet = this.sootMethodToOptionSet.get(callerMethod);

                if(callerOptionSet != null && !optionSet.equals(callerOptionSet)
                        && !optionSet.containsAll(callerOptionSet) && !callerOptionSet.containsAll(optionSet)) {
                    canPush = false;
                    break;
                }
            }

            if(!canPush) {
                continue;
            }

            // Can push up the regions and options
            updated = true;
            this.createRegionsInCallers(region, this.getCallerEdges(method));

            callerEdges = this.getCallerEdges(method);

            for(Edge callerEdge : callerEdges) {
                worklist.add(callerEdge.getSrc().method());
            }

        }

        return updated;
    }

    private void createRegionsInCallers(JavaRegion region, List<Edge> callerEdges) {
        Set<String> addedRegions = new HashSet<>();
        Map<JavaRegion, Set<JavaRegion>> regionsToInnerRegions = new HashMap<>();
        Set<JavaRegion> regionToRemove = new HashSet<>();
        regionToRemove.add(region);

        for(Edge edge : callerEdges) {
            SootMethod callerMethod = edge.src();

            String classPackage = callerMethod.getDeclaringClass().getPackageName();
            String className = callerMethod.getDeclaringClass().getShortName();
            String methodName = callerMethod.getBytecodeSignature();
            methodName = methodName.substring(methodName.indexOf(" "), methodName.length() - 1).trim();

            String addedRegionsKey = classPackage + " " + className + " " + methodName;

            if(addedRegions.contains(addedRegionsKey)) {
                continue;
            }

            Set<Set<String>> optionSet = this.sootMethodToOptionSet.get(callerMethod);

            if(optionSet == null) {
                Set<Set<String>> set = new HashSet<>();
                set.addAll(this.regionsToOptionSet.get(region));
                this.sootMethodToOptionSet.put(callerMethod, set);
            }
            else {
                optionSet.addAll(this.regionsToOptionSet.get(region));
            }

            addedRegions.add(addedRegionsKey);

            JavaRegion newRegion = new JavaRegion(classPackage, className, methodName);
            System.out.println("Created region: " + newRegion);
            this.regionsToOptionSet.put(newRegion, new HashSet<>());
            regionsToInnerRegions.put(newRegion, regionToRemove);
        }

        this.updateRegionsToOptionsWithOptionsInInnerRegions(regionsToInnerRegions);
        this.removeRegionsInRegionsToOptions(regionToRemove);
    }

    private int getJavapStartIndex(MethodNode methodNode) {
        ClassNode classNode = this.methodNodeToClassNode.get(methodNode);
        List<String> javapResult = this.getJavapResult(classNode);
        String methodNameInJavap = methodNode.name;

        if(methodNameInJavap.startsWith("<init>")) {
            methodNameInJavap = classNode.name;
            methodNameInJavap = methodNameInJavap.replace("/", ".");
        }

        if(methodNameInJavap.startsWith("<clinit>")) {
            methodNameInJavap = "  static {};";
        }
        else {
            methodNameInJavap += "(";
        }

        int methodStartIndex = 0;

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

        return methodStartIndex;
    }

    private AbstractInsnNode getASMInstruction(SootMethod callerMethod, int bytecodeIndex) {
        MethodNode methodNode = this.sootMethodToMethodNode.get(callerMethod);
        int methodStartIndex = this.getJavapStartIndex(methodNode);
        List<String> javapResult = this.getJavapResult(this.methodNodeToClassNode.get(methodNode));

        int instructionNumber = 0;
        int currentBytecodeIndex = -1;
        // 2 are the lines before the actual code in a method
        int javapOffset = 2;

        for(int i = (methodStartIndex + javapOffset); i < javapResult.size(); i++) {
            String outputLine = javapResult.get(i);

            if(outputLine.contains("Code:")) {
                break;
            }

            if(!outputLine.contains(":")) {
                continue;
            }

            if(outputLine.contains(bytecodeIndex + ":")) {
                break;
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
        }

        InsnList instructionsList = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructions = instructionsList.iterator();
        int instructionCounter = -1;

        while(instructions.hasNext()) {
            AbstractInsnNode instruction = instructions.next();

            if(instruction.getOpcode() >= 0) {
                instructionCounter++;
            }
            else {
                continue;
            }

            if(instructionCounter == instructionNumber) {
//                if(!(instruction instanceof MethodInsnNode)) {
//                    throw new RuntimeException("The instruction has to be a method call");
//                }

                return instruction;
            }
        }

        throw new RuntimeException("Could not find the instruction");
    }

    /**
     * Remove a region in the first method that is contained in a region in the other method
     *
     * @param method
     * @param callerMethod
     */
    private void removeRegionInMethod(SootMethod method, SootMethod callerMethod) {
        // Get regions in methods
        List<JavaRegion> regionInMethod = this.getRegionsInMethod(this.sootMethodToMethodNode.get(method));

        if(regionInMethod.size() != 1) {
            throw new RuntimeException("The inner method does no have 1 region");
        }

        List<JavaRegion> regionInCallerMethod = this.getRegionsInMethod(this.sootMethodToMethodNode.get(callerMethod));

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
    }

    private List<Edge> getCallerEdges(SootMethod method) {
        Iterator<Edge> outEdges = this.callGraph.edgesInto(method);
        List<Edge> callerEdges = new LinkedList<>();

        while(outEdges.hasNext()) {
            Edge edge = outEdges.next();
            MethodOrMethodContext src = edge.getSrc();

            if(!src.method().getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                continue;
            }

            callerEdges.add(edge);
        }

        return callerEdges;
    }

    /**
     * Get the methods that are used in the system
     *
     * @return
     */
    private Set<SootMethod> getSystemMethods() {
        Set<SootMethod> methods = new HashSet<>();
        QueueReader<Edge> edges = this.callGraph.listener();

        while(edges.hasNext()) {
            Edge edge = edges.next();
            MethodOrMethodContext srcObject = edge.getSrc();
            SootMethod src = srcObject.method();

            if(!src.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                continue;
            }

            methods.add(src);

            MethodOrMethodContext tgtObject = edge.getTgt();
            SootMethod tgt = tgtObject.method();

            if(!tgt.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                continue;
            }

            methods.add(tgt);
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
        String classPackage = sootMethod.getDeclaringClass().getPackageName();
        String className = sootMethod.getDeclaringClass().getShortName();
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

    protected List<JavaRegion> getRegionsInMethod(MethodNode methodNode) {
        ClassNode classNode = this.methodNodeToClassNode.get(methodNode);
        String classPackage = classNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = classNode.name;
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

    public Map<MethodNode, ClassNode> getMethodNodeToClassNode() {
        return methodNodeToClassNode;
    }

    public Set<MethodBlock> getEndRegionBlocksWithReturn() {
        return this.endRegionBlocksWithReturn;
    }
}
