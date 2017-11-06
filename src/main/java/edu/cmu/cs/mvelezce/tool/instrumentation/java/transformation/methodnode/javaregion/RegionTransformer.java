package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
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
    private Map<MethodNode, MethodGraph> methodsToGraphs = new HashMap<>();
    private Map<MethodNode, SootMethod> methodNodeToSootMethod = new HashMap<>();
    private Map<SootMethod, Set<Set<String>>> sootMethodToOptionSet = new HashMap<>();
    private Map<SootMethod, MethodNode> sootMethodToMethodNode = new HashMap<>();
    private Set<MethodBlock> endRegionBlocksWithReturn = new HashSet<>();

    private Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> methodsToBlocksDecisions = new HashMap<>();

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

    private void matchMethodToClassNodes(Set<ClassNode> classNodes) {
        for(ClassNode classNode : classNodes) {
            for(MethodNode methodNode : classNode.methods) {
                this.methodNodeToClassNode.put(methodNode, classNode);
            }
        }
    }

    private void matchMethodsToOptions() {
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
    }

    private void calculateASMIndexes(Set<ClassNode> classNodes) {
        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            for(MethodNode methodNode : methodsToInstrument) {
                List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

                if(!this.methodsWithUpdatedIndexes.contains(methodNode)) {
                    this.calculateASMStartIndex(regionsInMethod, methodNode);
                }
            }
        }
    }

    @Override
    public void transformMethods(Set<ClassNode> classNodes) throws IOException {
        int initialRegionCount = this.regionsToOptionSet.size();

        Set<SootMethod> methods = this.getSystemMethods();
        this.matchSootToASMMethods(methods, classNodes);
        this.matchMethodToClassNodes(classNodes);
        this.calculateASMIndexes(classNodes);
        this.matchMethodsToOptions();
        this.setBlocksToDecisions(classNodes);

        boolean updatedRegions = true;

        while(updatedRegions) {
            updatedRegions = this.propagateUpMethodsInClasses(classNodes);
            updatedRegions = updatedRegions | this.propagateUpAcrossMethods();
            System.out.print("");
        }

//        this.instrument(classNodes);

        System.out.println("# of regions before optimizing: " + initialRegionCount);
        System.out.println("# of regions after optimizing: " + this.regionsToOptionSet.size());
        System.out.println("");
    }

    private void setBlocksToDecisions(Set<ClassNode> classNodes) {
        for(ClassNode classNode : classNodes) {
            System.out.println("Setting blocks to decisions in class " + classNode.name);

            for(MethodNode methodNode : classNode.methods) {
                System.out.println("Setting blocks to decisions in method " + methodNode.name);
                List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);
                LinkedHashMap<MethodBlock, JavaRegion> blocksToRegionSet = this.matchBlocksToRegion(methodNode, regionsInMethod);
                this.methodsToBlocksDecisions.put(methodNode, blocksToRegionSet);
            }
        }
    }

//    @Override
//    public void transformMethods(Set<ClassNode> classNodes) throws IOException {
//        Set<SootMethod> methods = this.getSystemMethods();
//        this.matchSootToASMMethods(methods, classNodes);
//
//        for(ClassNode classNode : classNodes) {
//            for(MethodNode methodNode : classNode.methods) {
//                this.methodNodeToClassNode.put(methodNode, classNode);
//            }
//        }
//
//        for(Map.Entry<JavaRegion, Set<Set<String>>> entry : this.regionsToOptionSet.entrySet()) {
//            JavaRegion region = entry.getKey();
//            String bytecodeSignature = region.getRegionPackage() + "." + region.getRegionClass() + ": " + region.getRegionMethod();
//
//            for(SootMethod sootMethod : this.sootMethodToMethodNode.keySet()) {
//                if(!sootMethod.getBytecodeSignature().contains(bytecodeSignature)) {
//                    continue;
//                }
//
//                Set<Set<String>> optionSet = this.sootMethodToOptionSet.get(sootMethod);
//
//                if(optionSet == null) {
//                    Set<Set<String>> set = new HashSet<>();
//                    set.addAll(entry.getValue());
//                    this.sootMethodToOptionSet.put(sootMethod, set);
//                }
//                else {
//                    optionSet.addAll(entry.getValue());
//                }
//
//            }
//        }
//
//        int initialRegionCount = this.regionsToOptionSet.size();
//        this.preProcessMethodsInClasses(classNodes);
//
//
////        for(ClassNode classNode : classNodes) {
////            for(MethodNode methodNode : classNode.methods) {
////                up1(methodNode);
////            }
////        }
//
//        boolean updatedRegions = true;
//
//        while(updatedRegions) {
//            updatedRegions = this.propagateUpMethodsInClasses(classNodes);
////            updatedRegions = updatedRegions | this.processGraph();
////            updatedRegions = this.processMethodsInClasses(classNodes);
//        }
//
////        while(updatedRegions) {
////            updatedRegions = this.processMethodsInClasses(classNodes);
////            updatedRegions = updatedRegions | this.processGraph();
//////            updatedRegions = this.processMethodsInClasses(classNodes);
////        }
//
//        System.out.println("# of regions before optimizing: " + initialRegionCount);
//        System.out.println("# of regions after optimizing: " + this.regionsToOptionSet.size());
//
//        // Instrument
//        for(ClassNode classNode : classNodes) {
//            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);
//
//            if(methodsToInstrument.isEmpty()) {
//                continue;
//            }
//
//            System.out.println("Transforming class for instrumentation " + classNode.name);
//
//            for(MethodNode methodToInstrument : methodsToInstrument) {
//                this.buildMethodGraph(methodToInstrument);
//                this.transformMethod(methodToInstrument);
//            }
//
//            this.getClassTransformer().writeClass(classNode, this.getClassTransformer().getPath() + "/" + classNode.name);
//
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
//                } catch(InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        System.out.println();
//    }

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
    private boolean propagateUpMethodsInClasses(Set<ClassNode> classNodes) {
        boolean updatedMethods = false;

        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            System.out.println("Propagate regions up in class " + classNode.name);

            for(MethodNode methodNode : methodsToInstrument) {
                System.out.println("Propagate regions up in method " + methodNode.name);
                updatedMethods = updatedMethods | this.propagateUpRegionsInMethod(methodNode);
            }
        }

        return updatedMethods;
    }

    private boolean propagateUpAcrossMethods() {
        boolean updated = false;

        Set<SootMethod> methods = this.getSystemMethods();
        List<SootMethod> worklist = new ArrayList<>();
        worklist.addAll(methods);

        while(!worklist.isEmpty()) {
            SootMethod a = worklist.remove(0);

            if(a.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
                continue;
            }

            List<JavaRegion> regionsInMethod = this.getRegionsInMethod(a);

            if(regionsInMethod.isEmpty()) {
                continue;
            }

            MethodNode methodNode = this.sootMethodToMethodNode.get(a);
            Collection<JavaRegion> regions = this.methodsToBlocksDecisions.get(methodNode).values();
            Iterator<JavaRegion> regionsIter = regions.iterator();
            JavaRegion aRegion = regionsIter.next();

            while(aRegion == null) {
                aRegion = regionsIter.next();
            }

            Set<String> aDecision = this.getDecision(aRegion);

            // Nothing to push up
            if(aDecision.isEmpty()) {
                continue;
            }

            boolean canPush = true;
            List<Edge> edges = this.getCallerEdges(a);

            for(Edge edge : edges) {
                SootMethod bSootMethod = edge.src();
                MethodNode bMethodNode = this.sootMethodToMethodNode.get(bSootMethod);
                LinkedHashMap<MethodBlock, JavaRegion> bBlocksToRegions = this.methodsToBlocksDecisions.get(bMethodNode);

                AbstractInsnNode inst = this.getASMInstruction(edge);
                MethodBlock bBlock = this.getMethodBlockInCallerMethod(edge, inst);
                JavaRegion bRegion = bBlocksToRegions.get(bBlock);
                Set<String> bDecision = this.getDecision(bRegion);

                if(!aDecision.containsAll(bDecision) || aDecision.equals(bDecision)) {
                    canPush = false;
                    break;
                }
            }

            if(!canPush) {
                continue;
            }

            edges = this.getCallerEdges(a);

            for(Edge edge : edges) {
                SootMethod bSootMethod = edge.src();
                MethodNode bMethodNode = this.sootMethodToMethodNode.get(bSootMethod);
                LinkedHashMap<MethodBlock, JavaRegion> bBlocksToRegions = this.methodsToBlocksDecisions.get(bMethodNode);

                AbstractInsnNode inst = this.getASMInstruction(edge);
                MethodBlock bBlock = this.getMethodBlockInCallerMethod(edge, inst);
                JavaRegion bRegion = bBlocksToRegions.get(bBlock);

                JavaRegion newRegion;
                int index;

                if(bRegion == null) {
                    String classPackage = bSootMethod.getDeclaringClass().getPackageName();
                    String className = bSootMethod.getDeclaringClass().getShortName();
                    String methodName = bSootMethod.getBytecodeSignature();
                    methodName = methodName.substring(methodName.indexOf(" "), methodName.length() - 1).trim();
                    index = bMethodNode.instructions.indexOf(inst);

                    newRegion = new JavaRegion(classPackage, className, methodName, index);
                    this.methodsWithUpdatedIndexes.add(bMethodNode);
                }
                else {
                    index = bRegion.getStartBytecodeIndex();

                    newRegion = new JavaRegion(bRegion.getRegionPackage(), bRegion.getRegionClass(), bRegion.getRegionMethod(), index);
                    this.regionsToOptionSet.remove(bRegion);
                }

                bBlocksToRegions.put(bBlock, newRegion);

                Set<Set<String>> newOptionSet = new HashSet<>();
                newOptionSet.add(aDecision);
                this.regionsToOptionSet.put(newRegion, newOptionSet);
            }

            edges = this.getCallerEdges(a);

            for(Edge edge : edges) {
                worklist.add(0, edge.src());
            }

            updated = true;
        }

        return updated;
    }

    private MethodBlock getMethodBlockInCallerMethod(Edge edge, AbstractInsnNode inst) {
        MethodBlock block = null;
        SootMethod sootMethod = edge.src();
        MethodNode methodNode = this.sootMethodToMethodNode.get(sootMethod);
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        for(MethodBlock entry : blocksToRegions.keySet()) {
            if(!entry.getInstructions().contains(inst)) {
                continue;
            }

            block = entry;

            break;
        }

        return block;
    }

    private AbstractInsnNode getASMInstruction(Edge edge) {
        SootMethod src = edge.src();
        Unit unit = edge.srcUnit();

        List<Integer> bytecodeIndexes = new ArrayList<>();

        for(Tag tag : unit.getTags()) {
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

        return this.getASMInstruction(src, bytecodeIndex);
    }

    /**
     * TODO
     */
    private void instrument(Set<ClassNode> classNodes) throws IOException {
        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            System.out.println("Instrumenting class " + classNode.name);

            for(MethodNode methodToInstrument : methodsToInstrument) {
                System.out.println("Instrumenting method " + methodToInstrument.name);
                this.setStartAndEndBlocks(methodToInstrument);
                this.transformMethod(methodToInstrument);
            }

            this.getClassTransformer().writeClass(classNode, this.getClassTransformer().getPath() + "/" + classNode.name);

            // Debugging
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
     * @param methodNode
     */
    private boolean propagateUpRegionsInMethod(MethodNode methodNode) {
        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

        if(!this.methodsWithUpdatedIndexes.contains(methodNode)) {
            this.calculateASMStartIndex(regionsInMethod, methodNode);
        }

        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegionSet = this.methodsToBlocksDecisions.get(methodNode);
        return this.propagateUpRegions(methodNode, blocksToRegionSet);
    }

    private boolean propagateUpRegions(MethodNode methodNode, Map<MethodBlock, JavaRegion> blocksToRegions) {
        this.debugBlockDecisions(methodNode);

        boolean updated = false;
        MethodGraph graph = this.getMethodGraph(methodNode);
        MethodBlock alpha = graph.getEntryBlock();
        List<MethodBlock> worklist = new ArrayList<>();
        worklist.addAll(blocksToRegions.keySet());

        while(!worklist.isEmpty()) {
            MethodBlock a = worklist.remove(0);

            if(blocksToRegions.get(a) == null) {
                continue;
            }

            JavaRegion aRegion = blocksToRegions.get(a);
            Set<String> aDecision = this.getDecision(aRegion);

            if(a.isCatchWithImplicitThrow()) {
//                blocksToRegions.put(a, null);
//                this.regionsToOptionSet.remove(aRegion);
                continue;
            }

            MethodBlock b = graph.getImmediateDominator(a);

            if(b == alpha) {
                continue;
            }

            JavaRegion bRegion = blocksToRegions.get(b);
            Set<String> bDecision = this.getDecision(bRegion);

            if(!aDecision.containsAll(bDecision) || aDecision.equals(bDecision)) {
                continue;
            }

            if(a.getPredecessors().isEmpty()) {
                throw new RuntimeException("The predecessors cannot be empty " + a.getID());
            }

            for(MethodBlock p : a.getPredecessors()) {
                JavaRegion pRegion = blocksToRegions.get(p);
                JavaRegion newRegion = new JavaRegion(aRegion.getRegionPackage(), aRegion.getRegionClass(), aRegion.getRegionMethod());
                int index;

                if(pRegion == null) {
                    index = methodNode.instructions.indexOf(b.getInstructions().get(0));
                }
                else {
                    index = pRegion.getStartBytecodeIndex();
                    this.regionsToOptionSet.remove(pRegion);
                }

                newRegion.setStartBytecodeIndex(index);
                blocksToRegions.put(p, newRegion);

                Set<Set<String>> newOptionSet = new HashSet<>();
                newOptionSet.add(aDecision);
                this.regionsToOptionSet.put(newRegion, newOptionSet);

                worklist.add(0, p);
                updated = true;
            }
        }

        this.debugBlockDecisions(methodNode);

        return updated;
    }

    private Set<String> getDecision(JavaRegion region) {
        Set<String> decision = new HashSet<>();

        if(region == null) {
            return decision;
        }

        Set<Set<String>> optionSet = this.regionsToOptionSet.get(region);

        for(Set<String> options : optionSet) {
            decision.addAll(options);
        }

        return decision;
    }

    private void updateDecision() {

    }

    /**
     * TODO
     *
     * @param methodNode
     * @param
     * @param regionsInMethod
     * @return
     */
    private LinkedHashMap<MethodBlock, JavaRegion> matchBlocksToRegion(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        // Initialize
        MethodGraph graph = this.buildMethodGraph(methodNode);
        InsnList instructions = methodNode.instructions;

        List<MethodBlock> blocks = new ArrayList<>();
        blocks.addAll(graph.getBlocks());
        blocks.remove(graph.getEntryBlock());
        blocks.remove(graph.getExitBlock());

        blocks.sort((o1, o2) -> {
            int o1Index = instructions.indexOf(o1.getInstructions().get(0));
            int o2Index = instructions.indexOf(o2.getInstructions().get(0));

            return Integer.compare(o1Index, o2Index);
        });

        blocks.add(0, graph.getEntryBlock());
        blocks.add(graph.getExitBlock());

        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = new LinkedHashMap<>();

        for(MethodBlock block : blocks) {
            blocksToRegions.put(block, null);
        }

        // Match instructions to regions
        Map<AbstractInsnNode, JavaRegion> instructionsToRegions = this.matchInstructionToRegion(methodNode, regionsInMethod);

        // Match blocks to region set.
        for(MethodBlock block : graph.getBlocks()) {
            List<AbstractInsnNode> blockInstructions = block.getInstructions();

            for(Map.Entry<AbstractInsnNode, JavaRegion> instructionToRegion : instructionsToRegions.entrySet()) {
                if(!blockInstructions.contains(instructionToRegion.getKey())) {
                    continue;
                }

                JavaRegion region = blocksToRegions.get(block);

                if(region != null) {
                    throw new RuntimeException("The region is not null");
                }

                blocksToRegions.put(block, instructionToRegion.getValue());
            }
        }

        return blocksToRegions;
    }

    /**
     * TODO
     *
     * @param methodNode
     * @param regionsInMethod
     * @return
     */
    private Map<AbstractInsnNode, JavaRegion> matchInstructionToRegion(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        InsnList instructions = methodNode.instructions;
        Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

        for(JavaRegion region : regionsInMethod) {
            instructionsToRegion.put(instructions.get(region.getStartBytecodeIndex()), region);
        }

        return instructionsToRegion;
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

        this.getMethodGraph(methodNode);
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

        MethodGraph graph = this.methodsToGraphs.get(methodNode);

        if(graph == null) {
            throw new RuntimeException("The graph cannot be null");
        }

        Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

        for(JavaRegion region : regionsInMethod) {
            instructionsToRegion.put(methodNode.instructions.get(region.getStartBytecodeIndex()), region);
        }

        this.setStartAndEndBlocks(graph, instructionsToRegion);
    }

    private MethodGraph getMethodGraph(MethodNode methodNode) {
        MethodGraph graph = this.methodsToGraphs.get(methodNode);

        if(graph == null) {
            DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder(methodNode);
            graph = builder.build();
            this.methodsToGraphs.put(methodNode, graph);
        }

        return graph;
    }

    private MethodGraph buildMethodGraph(MethodNode methodNode) {
        DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder(methodNode);
        MethodGraph graph = builder.build();
        this.methodsToGraphs.put(methodNode, graph);

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
                MethodGraph graph = this.methodsToGraphs.get(methodNode);
                MethodBlock start = region.getStartMethodBlock();
                Set<MethodBlock> reachableBlocks = new HashSet<>();

                for(MethodBlock exit : region.getEndMethodBlocks()) {
                    reachableBlocks.addAll(graph.getReachableBlocks(start, exit));
                }

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

//    public boolean up1(MethodNode methodNode) {
//        boolean updated = false;
//        MethodGraph graph = this.methodsToGraphs.get(methodNode);
//
//        if(graph == null) {
//            graph = this.buildMethodGraph(methodNode);
//        }
//
//        MethodBlock entry = graph.getEntryBlock();
//        MethodBlock exit = graph.getExitBlock();
//
//        for(MethodBlock block : graph.getBlocks()) {
//            if(block == entry || block == exit) {
//                continue;
//            }
//
//            if(block.getSuccessors().size() == 1) {
//                MethodBlock succ = block.getSuccessors().iterator().next();
//                String packageName = this.methodNodeToClassNode.get(methodNode).name;
//                String className = packageName;
//                String methodName = methodNode.name;
//                Set<String> blockDec = getDecision(this.methodNodeToClassNode.get(methodNode), block);
//                Set<String> succDec = getDecision(succ);
//
//                if(!blockDec.equals(succDec) && succDec.containsAll(blockDec)) {
//                    setDecision(block, succDec);
//                    updated = true;
//                }
//            }
//            else {
//                for(MethodBlock succ : block.getSuccessors()) {
//                    Set<String> blockDec = getDecision(block);
//                    Set<String> succDec = getDecision(succ);
//
//                    if(!blockDec.equals(succDec) && succDec.containsAll(blockDec)) {
//                        setDecision(block, succDec);
//                        updated = true;
//                    }
//                }
//
//            }
//        }
//
//        return updated;
//    }
//
//    public Set<String> getDecision(String packageName, String className, String methodName) {
//        Set<String> decision = new HashSet<>();
//
//        for(Map.Entry<JavaRegion, Set<Set<String>>> entry : this.regionsToOptionSet.entrySet()) {
//            JavaRegion region = entry.getKey();
//
//            if(region.getRegionPackage().equals(packageName) && region.getRegionClass().equals(className)
//                    && region.getRegionMethod().equals(methodName)) {
//                for(Set<String> options : entry.getValue()) {
//                    decision.addAll(options);
//                }
//            }
//
//        }
//
//        return decision;
//    }
//
//    public void setDecision(MethodBlock block, Set<String> descision) {
//
//    }

    /**
     * Remove inner regions in a method  TODO there might be the case where a region has multiple inner regions and
     * some of those inner regions can be removed
     *
     * @param regionsInMethod
     */
    private boolean removeInnerRegionsInMethod(MethodNode methodNode, List<JavaRegion> regionsInMethod) {
        MethodGraph graph = this.getMethodGraph(methodNode);
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

                if(!regionOptions.equals(innerRegionOptions)) {
                    System.out.println("check");
                }

                if(!regionOptions.containsAll(innerRegionOptions)) {
                    System.out.println("check");
                }

                if(!innerRegionOptions.containsAll(regionOptions)) {
                    System.out.println("check");
                }

                // TODO check if the interaction does occur in the global set of interactions
                if(!regionOptions.equals(innerRegionOptions) && !innerRegionOptions.containsAll(regionOptions)
                        && !regionOptions.containsAll(innerRegionOptions)) {
                    regionsWithInnerRegionsWithDifferentOptionSet.add(region);
//                    throw new RuntimeException("The region " + region.getStartMethodBlock().getID() + " has 1" +
//                            " inner region " + innerRegion.getStartMethodBlock().getID() + ", but the outer options are not a set or" +
//                            " a subset of the inner options");
                }

//                if(!regionOptions.equals(innerRegionOptions) && !innerRegionOptions.containsAll(regionOptions)
//                        ) {
//                    regionsWithInnerRegionsWithDifferentOptionSet.add(region);
////                    throw new RuntimeException("The region " + region.getStartMethodBlock().getID() + " has 1" +
////                            " inner region " + innerRegion.getStartMethodBlock().getID() + ", but the outer options are not a set or" +
////                            " a subset of the inner options");
//                }
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
        for(Map.Entry<JavaRegion, Set<JavaRegion>> entry : regionsToInnerRegions.entrySet()) {
            Set<String> newOptions = new HashSet<>();

            for(JavaRegion innerRegion : entry.getValue()) {
                Set<Set<String>> innerOptionSet = this.regionsToOptionSet.get(innerRegion);
                Set<String> innerOptions = new HashSet<>();

                for(Set<String> options : innerOptionSet) {
                    innerOptions.addAll(options);
                }

                newOptions.addAll(innerOptions);
            }

            if(!this.regionsToOptionSet.containsKey(entry.getKey())) {
                throw new RuntimeException("There is no region that will have its option set modified");
            }

            Set<Set<String>> regionOptionSet = this.regionsToOptionSet.get(entry.getKey());
            Set<String> regionOptions = new HashSet<>();

            for(Set<String> options : regionOptionSet) {
                regionOptions.addAll(options);
            }

            newOptions.addAll(regionOptions);

            Set<Set<String>> oldOptionSet = this.regionsToOptionSet.get(entry.getKey());
            oldOptionSet.clear();
            oldOptionSet.add(newOptions);
        }

    }

    private void debugBlockDecisions(MethodNode methodNode) {
        System.out.println("Debugging block decisions for " + methodNode.name);
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        MethodGraph graph = this.getMethodGraph(methodNode);
        Set<MethodBlock> blocks = graph.getBlocks();

        StringBuilder dotString = new StringBuilder("digraph " + methodNode.name + " {\n");
        dotString.append("node [shape=record];\n");

        for(MethodBlock block : blocks) {
            dotString.append(block.getID());
            dotString.append(" [label=\"");
            dotString.append(block.getID());
            dotString.append(" - ");

            JavaRegion region = blocksToRegions.get(block);

            if(region == null) {
                dotString.append("[]");
            }
            else {
                Set<String> decision = this.getDecision(region);
                dotString.append(decision);
            }

            dotString.append("\"];\n");
        }

        dotString.append(graph.getEntryBlock().getID());
        dotString.append(";\n");
        dotString.append(graph.getExitBlock().getID());
        dotString.append(";\n");

        for(MethodBlock methodBlock : graph.getBlocks()) {
            for(MethodBlock successor : methodBlock.getSuccessors()) {
                dotString.append(methodBlock.getID());
                dotString.append(" -> ");
                dotString.append(successor.getID());
                dotString.append(";\n");
            }
        }

        dotString.append("}");

        System.out.println(dotString);
    }

    private void debugBlocksAndRegions(MethodNode methodNode) {
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);
        int blocksToDecisionCount = 0;

        for(JavaRegion r : blocksToRegions.values()) {
            if(r != null) {
                blocksToDecisionCount++;
            }
        }

        int regionsInMethodCount = this.getRegionsInMethod(methodNode).size();

        if(blocksToDecisionCount != regionsInMethodCount) {
            throw new RuntimeException("The number of regions in a method does not match in the blocks to regions and methods to regions");
        }
    }

    /**
     * TODO
     * @param methodNode
     */
    private void setStartAndEndBlocks(MethodNode methodNode) {
        this.debugBlocksAndRegions(methodNode);
        this.debugBlockDecisions(methodNode);

        MethodGraph graph = this.getMethodGraph(methodNode);
        MethodBlock ro = graph.getExitBlock();
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        this.ignoreRegionsWithoutConnectionToExit(methodNode);
        this.ignoreCatchWithImplicitThrow(methodNode);

        // The entry block should be skipped
        Iterator<Map.Entry<MethodBlock, JavaRegion>> blocksToRegionsIterator = blocksToRegions.entrySet().iterator();
        blocksToRegionsIterator.next();

        for(int i = 0; blocksToRegionsIterator.hasNext(); i++) {
            Map.Entry<MethodBlock, JavaRegion> blockToRegion = blocksToRegionsIterator.next();

            if(blockToRegion.getValue() == null) {
                continue;
            }

            MethodBlock start = blockToRegion.getKey();
            JavaRegion aRegion = blocksToRegions.get(start);
            Set<String> aDecision = this.getDecision(aRegion);
            MethodBlock end;

            // TODO this is an optimization and not part of the original algorithm
            // TODO optimize for regions15
            if(i == 0) {
                end = graph.getExitBlock();
            }
            else {
                MethodBlock ipd = graph.getImmediatePostDominator(start);
                JavaRegion ipdRegion = blocksToRegions.get(ipd);
                Set<String> ipdDecision = this.getDecision(ipdRegion);

                while(ipd != ro && (aDecision.equals(ipdDecision) || aDecision.containsAll(ipdDecision))) {
                    MethodBlock temp = graph.getImmediatePostDominator(ipd);

                    if(temp == ro & ipd.getSuccessors().size() == 1 && ipd.getSuccessors().iterator().next() == ro) {
                        break;
                    }

                    ipd = temp;
                    ipdRegion = blocksToRegions.get(ipd);
                    ipdDecision = this.getDecision(ipdRegion);
                }

                end = ipd;

//                if(start.getSuccessors().size() < 2) {
//                    System.out.println("THERE WAS A PROBLEM");
//                    this.debugBlockDecisions(methodNode);
//                    throw new RuntimeException("A control flow decision has less than 2 successors " + start.getID());
//                }
            }

            Set<MethodBlock> ends = new HashSet<>();

            if(start == end) {
                throw new RuntimeException("Start and end equal");
            }
            else if(start.getSuccessors().size() == 1 && start.getSuccessors().iterator().next().equals(end)) {
                throw new RuntimeException("A control flow decision only has 1 successor? " + start + " -> " + end);
            }
            else if(graph.getExitBlock() == end) {
                this.endRegionBlocksWithReturn.addAll(end.getPredecessors());
                ends.addAll(end.getPredecessors());
            }
            else {
                ends.add(end);
            }

            JavaRegion region = blockToRegion.getValue();
            region.setStartMethodBlock(start);
            region.setEndMethodBlocks(ends);

            // Remove
            Set<MethodBlock> reachables = new HashSet<>();

            for(MethodBlock e : ends) {
                reachables.addAll(graph.getReachableBlocks(start, e));
            }

            reachables.remove(start);
//            this.removeRegionsInCallees(methodNode, aDecision, reachables);
            this.removeRegionsInMethod(methodNode, aDecision, reachables);
        }

        this.debugBlocksAndRegions(methodNode);
    }

    /**
     * TODO
     * @param methodNode
     */
    private void ignoreCatchWithImplicitThrow(MethodNode methodNode) {
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        // Check if there is a catch with an implicit flow
        for(Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
            if(blockToRegion.getValue() == null) {
                continue;
            }

            if(blockToRegion.getKey().isCatchWithImplicitThrow()) {
                throw new RuntimeException("Check");
//                this.regionsToOptionSet.remove(blockToRegion.getValue());
            }
        }

    }

    /**
     * TODO
     * Check if there is a region that does not have a connection to the exit block. This happens when regions occur
     * in handlers not connected to the exit block
     */
    private void ignoreRegionsWithoutConnectionToExit(MethodNode methodNode) {
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);
        MethodGraph graph = this.getMethodGraph(methodNode);

        for(Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
            if(blockToRegion.getValue() == null) {
                continue;
            }

            if(graph.getReachableBlocks(blockToRegion.getKey(), graph.getExitBlock()).contains(graph.getExitBlock())) {
                continue;
            }

            this.regionsToOptionSet.remove(blockToRegion.getValue());
            blocksToRegions.put(blockToRegion.getKey(), null);
            this.debugBlocksAndRegions(methodNode);
        }
    }

    private void removeRegionsInMethod(MethodNode methodNode, Set<String> aDecision, Set<MethodBlock> reachables) {
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        for(MethodBlock b : reachables) {
            JavaRegion bRegion = blocksToRegions.get(b);

            if(bRegion == null) {
                continue;
            }

            Set<String> bDecision = this.getDecision(bRegion);

            if(!aDecision.equals(bDecision) && !aDecision.containsAll(bDecision)) {
                continue;
            }

            blocksToRegions.put(b, null);
            this.regionsToOptionSet.remove(bRegion);
            this.debugBlocksAndRegions(methodNode);
        }
    }

    private void removeRegionsInCallees(MethodNode methodNode, Set<String> aDecision, Set<MethodBlock> reachables) {
        Set<SootMethod> analyzedCallees = new HashSet<>();
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        // TODO put in method
        for(MethodBlock b : reachables) {
            JavaRegion bRegion = blocksToRegions.get(b);

            if(bRegion == null) {
                continue;
            }

            Set<String> bDecision = this.getDecision(bRegion);

            if(!aDecision.equals(bDecision) && !aDecision.containsAll(bDecision)) {
                continue;
            }

            SootMethod sootMethod = this.methodNodeToSootMethod.get(methodNode);

            for(AbstractInsnNode inst : b.getInstructions()) {
                if(inst.getOpcode() < 0) {
                    continue;
                }

                Unit unit = this.matchUnit(inst, sootMethod);
                List<Edge> calleeEdges = this.getCalleeEdges(unit);

                    while(!calleeEdges.isEmpty()) {
                        Edge outEdge = calleeEdges.remove(0);
                        SootMethod sSootMethod = outEdge.tgt();
                        SootMethod sSrc = outEdge.src();
                        analyzedCallees.add(sSrc);
                        MethodNode sMethodNode = this.sootMethodToMethodNode.get(sSootMethod);
                        LinkedHashMap<MethodBlock, JavaRegion> sBlocksToRegions = this.methodsToBlocksDecisions.get(sMethodNode);

                        if(sBlocksToRegions == null) {
                            List<Edge> moreCallees = this.getCalleeEdges(sSootMethod);
                            List<Edge> moreEdges = new ArrayList<>();

                            for(Edge anotherCallee : moreCallees) {
                                if(analyzedCallees.contains(anotherCallee.src())) {
                                    continue;
                                }

                                moreEdges.add(anotherCallee);
                            }

                            calleeEdges.addAll(moreEdges);

                            continue;
                        }

                        for(Map.Entry<MethodBlock, JavaRegion> entry : sBlocksToRegions.entrySet()) {
                            bRegion = entry.getValue();

                            if(bRegion == null) {
                                continue;
                            }

                            bDecision = this.getDecision(bRegion);

                            if(!aDecision.equals(bDecision) && !aDecision.containsAll(bDecision)) {
                                continue;
                            }

                            sBlocksToRegions.put(entry.getKey(), null);
                            this.regionsToOptionSet.remove(bRegion);
                            this.debugBlocksAndRegions(methodNode);
                        }
                    }
                }









//
//            List<Edge> calleeEdges = this.getCalleeEdges(sootMethod);
//
//            while(!calleeEdges.isEmpty()) {
//                Edge outEdge = calleeEdges.remove(0);
//                SootMethod sSootMethod = outEdge.tgt();
//                SootMethod sSrc = outEdge.src();
//                analyzedCallees.add(sSrc);
//                MethodNode sMethodNode = this.sootMethodToMethodNode.get(sSootMethod);
//                LinkedHashMap<MethodBlock, JavaRegion> sBlocksToRegions = this.methodsToBlocksDecisions.get(sMethodNode);
//
//                if(sBlocksToRegions == null) {
//                    List<Edge> moreCallees = this.getCalleeEdges(sSootMethod);
//                    List<Edge> moreEdges = new ArrayList<>();
//
//                    for(Edge anotherCallee : moreCallees) {
//                        if(analyzedCallees.contains(anotherCallee.src())) {
//                            continue;
//                        }
//
//                        moreEdges.add(anotherCallee);
//                    }
//
//                    calleeEdges.addAll(moreEdges);
//
//                    continue;
//                }
//
//                for(Map.Entry<MethodBlock, JavaRegion> entry : sBlocksToRegions.entrySet()) {
//                    bRegion = entry.getValue();
//
//                    if(bRegion == null) {
//                        continue;
//                    }
//
//                    bDecision = this.getDecision(bRegion);
//
//                    if(!aDecision.equals(bDecision) && !aDecision.containsAll(bDecision)) {
//                        continue;
//                    }
//
//                    sBlocksToRegions.put(entry.getKey(), null);
//                    this.regionsToOptionSet.remove(bRegion);
//                    this.debugBlocksAndRegions(methodNode);
//                }
//            }
        }
    }

    /**
     * TODO
     * @param inst
     * @param sootMethod
     * @return
     */
    private Unit matchUnit(AbstractInsnNode inst, SootMethod sootMethod) {
        Unit match = null;

        for(Unit unit : sootMethod.getActiveBody().getUnits()) {
            List<Integer> bytecodeIndexes = new ArrayList<>();

            for(Tag tag : unit.getTags()) {
                if(tag instanceof BytecodeOffsetTag) {
                    int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
                    bytecodeIndexes.add(bytecodeIndex);
                }
            }

            if(bytecodeIndexes.isEmpty()) {
                continue;
            }

            int bytecodeIndex;

            if(bytecodeIndexes.size() == 1) {
                bytecodeIndex = bytecodeIndexes.get(0);
            }
            else {
                int index = bytecodeIndexes.indexOf(Collections.min(bytecodeIndexes));
                bytecodeIndex = bytecodeIndexes.get(index);
            }

            AbstractInsnNode asmInst = this.getASMInstruction(sootMethod, bytecodeIndex);

            if(inst != asmInst) {
                continue;
            }

            match = unit;
            break;
        }

        if(match == null) {
            throw new RuntimeException("Could not find the instruction in this method");
        }

        return match;
    }

    public MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
        return immediatePostDominator;
    }

    public MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        MethodBlock id = methodGraph.getImmediateDominator(start);

        if(id != methodGraph.getEntryBlock() && id.getSuccessors().size() == 1 && id.getSuccessors().contains(start)) {
            return id;
        }

        return start;
    }

    // TODO why dont we return a new map. If I do not return a new map, it sugguest to users that the passed map will be
    // transformed. If I return a new map, it sugguest to users that the passed map will remain the same and a new map
    // is generated
    private void setStartAndEndBlocks(MethodGraph graph, Map<AbstractInsnNode, JavaRegion> instructionsToRegion) {
        Set<JavaRegion> regionsInCatchWithoutExplicitThrow = new HashSet<>();

        for(MethodBlock block : graph.getBlocks()) {
            List<AbstractInsnNode> blockInstructions = block.getInstructions();

            for(Map.Entry<AbstractInsnNode, JavaRegion> instructionToRegion : instructionsToRegion.entrySet()) {
                AbstractInsnNode instruction = instructionToRegion.getKey();
                JavaRegion region = instructionToRegion.getValue();

                if(!blockInstructions.contains(instruction)) {
                    continue;
                }

                if(block.isCatchWithImplicitThrow()) {
                    regionsInCatchWithoutExplicitThrow.add(region);
                    continue;
                }

                if(!graph.getReachableBlocks(block, graph.getExitBlock()).contains(graph.getExitBlock())) {
                    this.regionsToOptionSet.remove(region);
                    continue;
                }

                MethodBlock start = this.getBlockToStartInstrumentingBeforeIt(graph, block);

                if(start == null) {
                    throw new RuntimeException();
                }

                MethodBlock end = this.getBlockToEndInstrumentingBeforeIt(graph, block);

                if(end == null) {
                    throw new RuntimeException();
                }

                Set<MethodBlock> endMethodBlocks = new HashSet<>();

                if(start == end) {
                    throw new RuntimeException("Start and end equal");
                }
                else if(start.getSuccessors().size() == 1 && start.getSuccessors().iterator().next().equals(end)) {
                    throw new RuntimeException("Happens when a control flow decision only has 1 successor? " + start
                            + " -> " + end);
                }
                else if(graph.getExitBlock() == end) {
                    this.endRegionBlocksWithReturn.addAll(end.getPredecessors());
                    endMethodBlocks.addAll(end.getPredecessors());
                }
                else {
                    endMethodBlocks.add(end);
                }

                region.setStartMethodBlock(start);
                region.setEndMethodBlocks(endMethodBlocks);
            }
        }

        for(JavaRegion javaRegion : regionsInCatchWithoutExplicitThrow) {
            this.regionsToOptionSet.remove(javaRegion);
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
            String[] command = new String[]{"javap", "-classpath", this.getClassTransformer().getPath(), "-p", "-c", "-s",
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
        Set<JavaRegion> updatedRegions = new HashSet<>();

        for(int i = methodStartIndex; i < javapResult.size(); i++) {
            methodStartIndex++;
            String outputLine = javapResult.get(i);

            if(outputLine.contains(" Code:")) {
                break;
            }
        }

        for(int i = methodStartIndex; i < javapResult.size(); i++) {
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

            if(callerEdges.isEmpty()) {
                continue;
            }

            // Check other strategies for each caller
            while(!callerEdges.isEmpty()) {
                reachedMainWithoutRegion = false;
                regionsWithCallToMethods = new HashMap<>();
                Edge outEdge = callerEdges.remove(0);
                SootMethod callerMethod = outEdge.src();

                if(method == callerMethod) {
                    throw new RuntimeException(method.getName() + " " + method.getDeclaringClass().getName() + " " + "same method and caller method");
                }

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

                    for(Edge callerEdge : edges) {
                        SootMethod callerOfCallerMethod = callerEdge.src();

                        if(callerMethod == callerOfCallerMethod) {
                            continue;
                        }

                        int insertIndex = Math.max(0, callerEdges.size() - 1);
                        callerEdges.add(insertIndex, callerEdge);
                    }

//                    int indexToInsert = Math.max(0, callerEdges.size() - 1);
//                    callerEdges.addAll(indexToInsert, edges);
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
                        MethodGraph graph = this.methodsToGraphs.get(this.sootMethodToMethodNode.get(callerMethod));

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

                    for(Edge callerEdge : edges) {
                        SootMethod callerOfCallerMethod = callerEdge.src();

                        if(callerMethod == callerOfCallerMethod) {
                            continue;
                        }

                        int insertIndex = Math.max(0, callerEdges.size() - 1);
                        callerEdges.add(insertIndex, callerEdge);
                    }

//                    int indexToInsert = Math.max(0, callerEdges.size() - 1);
//                    callerEdges.addAll(indexToInsert, edges);
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

            // TODO test this logic since we want to continue searching up the call graph
            if(callerEdges.isEmpty()) {
                continue;
            }

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

            // Check if the region can be pushed if no interactions are created. Interactions can occur with the
            // current options in a method or the options that a method might have had
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

            // TODO test this logic since we do want to continue looking up the call hirarchy
            // If the method does not have callers, it might be part of a java interface (e.g., Runnable's run method)
            if(callerEdges.isEmpty()) {
                continue;
            }

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

            // Check if the region can be pushed if no interactions are created with the set of options that the caller
            // methods might have had
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
                if(!outputLine.equals("  static {};")) {
                    throw new RuntimeException("Check this case");
                }

                break;
            }
            else if(outputLine.contains(" " + methodNameInJavap)) {
                String javapDescriptor = javapResult.get(methodStartIndex + 1).trim();
                javapDescriptor = javapDescriptor.substring(javapDescriptor.indexOf(" ")).trim();

                if(javapDescriptor.equals(methodNode.desc)) {
                    break;
                }
            }

            methodStartIndex++;
        }

        if(methodStartIndex == javapResult.size()) {
            throw new RuntimeException("The start of the javap result cannot be the size of the result");
        }

        return methodStartIndex;
    }

    private AbstractInsnNode getASMInstruction(SootMethod callerMethod, int bytecodeIndex) {
        MethodNode methodNode = this.sootMethodToMethodNode.get(callerMethod);
        int methodStartIndex = this.getJavapStartIndex(methodNode);
        List<String> javapResult = this.getJavapResult(this.methodNodeToClassNode.get(methodNode));

        int instructionNumber = 0;
        int currentBytecodeIndex = -1;

        for(int i = methodStartIndex; i < javapResult.size(); i++) {
            methodStartIndex++;
            String outputLine = javapResult.get(i);

            if(outputLine.contains(" Code:")) {
                break;
            }
        }

        for(int i = methodStartIndex; i < javapResult.size(); i++) {
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
        Iterator<Edge> inEdges = this.callGraph.edgesInto(method);
        List<Edge> worklist = new ArrayList<>();

        while(inEdges.hasNext()) {
            worklist.add(inEdges.next());
        }

        List<Edge> callerEdges = new ArrayList<>();

        while(!worklist.isEmpty()) {
            Edge edge = worklist.remove(0);
            MethodOrMethodContext src = edge.getSrc();

            if(!src.method().getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                Iterator<Edge> edges = this.callGraph.edgesInto(src);
                List<Edge> moreEdges = new ArrayList<>();

                while(edges.hasNext()) {
                    moreEdges.add(edges.next());
                }

                int index = Math.max(0, worklist.size() - 1);
                worklist.addAll(index, moreEdges);
            }
            else {
                callerEdges.add(edge);
            }
        }

        return callerEdges;
    }

    private List<Edge> getCalleeEdges(SootMethod method) {
        Iterator<Edge> outEdges = this.callGraph.edgesOutOf(method);
        Set<SootMethod> analyzedMethods = new HashSet<>();
        List<Edge> worklist = new ArrayList<>();

        while(outEdges.hasNext()) {
            worklist.add(outEdges.next());
        }

        List<Edge> callerEdges = new ArrayList<>();

        while(!worklist.isEmpty()) {
            Edge edge = worklist.remove(0);
            SootMethod tgt = edge.tgt();
            SootMethod src = edge.src();
            analyzedMethods.add(src);

            if(!tgt.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                Iterator<Edge> edges = this.callGraph.edgesOutOf(tgt);
                List<Edge> moreEdges = new ArrayList<>();

                while(edges.hasNext()) {
                    Edge nextEdge = edges.next();

                    if(analyzedMethods.contains(nextEdge.tgt())) {
                        System.out.println();
                        continue;
                    }

                    moreEdges.add(nextEdge);
                }

                int index = Math.max(0, worklist.size() - 1);
                worklist.addAll(index, moreEdges);
            }
            else {
                callerEdges.add(edge);
            }
        }

        return callerEdges;
    }


    private List<Edge> getCalleeEdges(Unit unit) {
        Iterator<Edge> outEdges = this.callGraph.edgesOutOf(unit);
        Set<SootMethod> analyzedMethods = new HashSet<>();
        List<Edge> worklist = new ArrayList<>();

        while(outEdges.hasNext()) {
            worklist.add(outEdges.next());
        }

        List<Edge> callerEdges = new ArrayList<>();

        while(!worklist.isEmpty()) {
            Edge edge = worklist.remove(0);
            SootMethod tgt = edge.tgt();
            SootMethod src = edge.src();
            analyzedMethods.add(src);

            if(!tgt.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
                Iterator<Edge> edges = this.callGraph.edgesOutOf(tgt);
                List<Edge> moreEdges = new ArrayList<>();

                while(edges.hasNext()) {
                    Edge nextEdge = edges.next();

                    if(analyzedMethods.contains(nextEdge.tgt())) {
                        System.out.println();
                        continue;
                    }

                    moreEdges.add(nextEdge);
                }

                int index = Math.max(0, worklist.size() - 1);
                worklist.addAll(index, moreEdges);
            }
            else {
                callerEdges.add(edge);
            }
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

    public Map<MethodNode, MethodGraph> getMethodsToGraphs() {
        return methodsToGraphs;
    }
}
