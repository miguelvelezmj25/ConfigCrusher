package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodTracer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.TraceClassInspector;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.*;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.config.SootConfig;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
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
    private Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> cachedMethodsToBlocksDecisions = new HashMap<>();
    private Map<JavaRegion, Set<Set<String>>> cachedRegionsToOptionSet = new HashMap<>();

    public RegionTransformer(String programName, String entryPoint, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) {
        super(programName, classTransformer);

        this.entryPoint = entryPoint;
        this.rootPackage = entryPoint.substring(0, entryPoint.indexOf("."));
        this.regionsToOptionSet = regionsToOptionSet;
        this.callGraph = this.buildCallGraph();

//        System.out.println("Call graph size: " + this.callGraph.size());
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
            if(!this.methodNodeToClassNode.containsValue(classNode)) {
                continue;
            }

            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            for(MethodNode methodNode : methodsToInstrument) {
                List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

                if(!this.methodsWithUpdatedIndexes.contains(methodNode)) {
                    this.calculateASMSIndexes(regionsInMethod, methodNode);
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
        }

        this.cachedRegionsToOptions();
        this.cacheMethodsToBlockDecisions();

        this.instrument(classNodes);

        System.out.println("# of regions before optimizing: " + initialRegionCount);
        System.out.println("# of regions after optimizing: " + this.regionsToOptionSet.size());
        System.out.println("");
    }

    private void cachedRegionsToOptions() {
        for(Map.Entry<JavaRegion, Set<Set<String>>> entry : this.regionsToOptionSet.entrySet()) {
            this.cachedRegionsToOptionSet.put(entry.getKey(), entry.getValue());
        }
    }

    private void cacheMethodsToBlockDecisions() {
        for(Map.Entry<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> entry : this.methodsToBlocksDecisions.entrySet()) {
            this.cachedMethodsToBlocksDecisions.put(entry.getKey(), new LinkedHashMap<>(entry.getValue()));
        }
    }

    private void setBlocksToDecisions(Set<ClassNode> classNodes) {
        for(ClassNode classNode : classNodes) {
//            System.out.println("Setting blocks to decisions in class " + classNode.name);

//            if(classNode.name.contains("Regions25")) {
//                System.out.println();
//            }

            for(MethodNode methodNode : classNode.methods) {
//                System.out.println("Setting blocks to decisions in method " + methodNode.name);
                List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);
                LinkedHashMap<MethodBlock, JavaRegion> blocksToRegionSet = this.matchBlocksToRegion(methodNode, regionsInMethod);
                this.methodsToBlocksDecisions.put(methodNode, blocksToRegionSet);

//                if(classNode.name.contains("Regions25")) {
//                    this.debugBlockDecisions(methodNode);
//                    System.out.println();
//                }
            }
        }
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

    /**
     * TODO
     *
     * @return
     */
    private boolean propagateUpAcrossMethods() {
        boolean updated = false;

        Set<SootMethod> methods = this.getSystemMethods();
        List<SootMethod> worklist = new ArrayList<>();
        worklist.addAll(methods);

        // Loop through all the methods until a fixed point
        while(!worklist.isEmpty()) {
            SootMethod method = worklist.remove(0);
//            System.out.println("Propagate regions across method " + a.getName());

            // Check not part of algorithm
            if(method.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
                continue;
            }

            List<JavaRegion> regionsInMethod = this.getRegionsInMethod(method);

            // Optimization nothing to push up
            if(regionsInMethod.isEmpty()) {
                continue;
            }

            boolean canPropagate = this.canPropagateRegion(method);

            if(!canPropagate) {
                continue;
            }

            List<SootMethod> methodsToAnalyze = this.propagateUpRegionInter(method);

            if(methodsToAnalyze.isEmpty()) {
                continue;
            }

            worklist.addAll(0, methodsToAnalyze);
            updated = true;
        }

        return updated;
    }

    private List<SootMethod> propagateUpRegionInter(SootMethod method) {
        List<SootMethod> methods = new ArrayList<>();
        MethodNode methodNode = this.sootMethodToMethodNode.get(method);
        Collection<JavaRegion> regions = this.methodsToBlocksDecisions.get(methodNode).values();
        Iterator<JavaRegion> regionsIter = regions.iterator();
        JavaRegion region = regionsIter.next();

        while(region == null) {
            region = regionsIter.next();
        }

        Set<String> decision = this.getDecision(region);

        if(decision.isEmpty()) {
            throw new RuntimeException("The first decision in " + methodNode.name + " cannot be null");
        }

        List<Edge> edges = this.getCallerEdges(method);

        for(Edge edge : edges) {
            SootMethod callerSootMethod = edge.src();
            MethodNode callerMethodNode = this.sootMethodToMethodNode.get(callerSootMethod);
            LinkedHashMap<MethodBlock, JavaRegion> callerBlocksToRegions = this.methodsToBlocksDecisions.get(callerMethodNode);

            MethodBlock callerBlock = this.getCallerBlock(edge);
            JavaRegion callerRegion = callerBlocksToRegions.get(callerBlock);
            Set<String> callerDecision = this.getDecision(callerRegion);

            if(!decision.containsAll(callerDecision) && !decision.equals(callerDecision) && !callerDecision.containsAll(decision)) {
                throw new RuntimeException("Cannot push up " + decision + " from " + methodNode.name + " to " + callerMethodNode.name + " at " + callerBlock.getID() + " because it has decision " + callerDecision);
            }

            if(!(decision.containsAll(callerDecision) && !decision.equals(callerDecision))) {
                continue;
            }

            JavaRegion newRegion;
            int index;

            if(callerRegion == null) {
                String classPackage = callerSootMethod.getDeclaringClass().getPackageName();
                String className = callerSootMethod.getDeclaringClass().getShortName();
                String methodName = callerSootMethod.getBytecodeSignature();
                methodName = methodName.substring(methodName.indexOf(" "), methodName.length() - 1).trim();
                index = callerMethodNode.instructions.indexOf(callerBlock.getInstructions().get(0));

                newRegion = new JavaRegion(classPackage, className, methodName, index);
                this.methodsWithUpdatedIndexes.add(callerMethodNode);
            }
            else {
                index = callerRegion.getStartBytecodeIndex();

                newRegion = new JavaRegion(callerRegion.getRegionPackage(), callerRegion.getRegionClass(), callerRegion.getRegionMethod(), index);
                this.regionsToOptionSet.remove(callerRegion);
            }

            callerBlocksToRegions.put(callerBlock, newRegion);

            Set<Set<String>> newOptionSet = new HashSet<>();
            newOptionSet.add(decision);

//            this.endRegionBlocksWithReturn.add(callerBlock);
            this.regionsToOptionSet.put(newRegion, newOptionSet);

            methods.add(edge.src());
            this.debugBlocksAndRegions(callerMethodNode);
        }

        return methods;
    }

    /**
     * Check with all callers if the region can be pushed up
     *
     * @param method
     * @return
     */
    private boolean canPropagateRegion(SootMethod method) {
        boolean canPush = true;

        MethodNode methodNode = this.sootMethodToMethodNode.get(method);
        Collection<JavaRegion> regions = this.methodsToBlocksDecisions.get(methodNode).values();
        Iterator<JavaRegion> regionsIter = regions.iterator();
        JavaRegion region = regionsIter.next();

        while(region == null) {
            region = regionsIter.next();
        }

        Set<String> decision = this.getDecision(region);

        if(decision.isEmpty()) {
            throw new RuntimeException("The first decision in " + methodNode.name + " cannot be null");
        }

        List<Edge> edges = this.getCallerEdges(method);

        for(Edge edge : edges) {
            SootMethod callerSootMethod = edge.src();
            MethodNode callerMethodNode = this.sootMethodToMethodNode.get(callerSootMethod);
            LinkedHashMap<MethodBlock, JavaRegion> callerBlocksToRegions = this.methodsToBlocksDecisions.get(callerMethodNode);

            MethodBlock callerBlock = this.getCallerBlock(edge);
            JavaRegion callerRegion = callerBlocksToRegions.get(callerBlock);
            Set<String> callerDecision = this.getDecision(callerRegion);

            if(!decision.containsAll(callerDecision) && !decision.equals(callerDecision) && !callerDecision.containsAll(decision)) {
//                this.debugBlockDecisions(callerMethodNode);
                System.out.println("Cannot push up " + decision + " from " + methodNode.name + " to " + callerMethodNode.name + " at " + callerBlock.getID() + " because it has decision " + callerDecision);
                canPush = false;
                break;
            }
        }

        return canPush;
    }

    private MethodBlock getCallerBlock(Edge edge) {
        MethodBlock block = null;

        SootMethod callerSootMethod = edge.src();
        MethodNode callerMethodNode = this.sootMethodToMethodNode.get(callerSootMethod);
        AbstractInsnNode inst = this.getASMInstFromCaller(edge);
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(callerMethodNode);

        for(MethodBlock entry : blocksToRegions.keySet()) {
            if(!entry.getInstructions().contains(inst)) {
                continue;
            }

            block = entry;

            break;
        }

        return block;
    }

    private AbstractInsnNode getASMInstFromCaller(Edge edge) {
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

        SootMethod src = edge.src();

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

//            System.out.println("Setting where to instrument class " + classNode.name);

            for(MethodNode methodToInstrument : methodsToInstrument) {
//                System.out.println("Setting where to instrument method " + methodToInstrument.name);
                this.setStartAndEndBlocks(methodToInstrument);
            }
        }

        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

//            System.out.println("Instrumenting class " + classNode.name);

            for(MethodNode methodToInstrument : methodsToInstrument) {
//                System.out.println("Instrumenting method " + methodToInstrument.name);
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

        this.debugRemovingRegions(classNodes);
    }

    private void debugRemovingRegions(Set<ClassNode> classNodes) {
        for(ClassNode classNode : classNodes) {
            for(MethodNode methodNode : classNode.methods) {
                List<JavaRegion> regions = this.getRegionsInMethod(methodNode);

                if(regions.size() == 1) {
                    continue;
                }

                for(JavaRegion r1 : regions) {
                    boolean diff = false;

                    for(JavaRegion r2 : regions) {
                        if(r1 == r2) {
                            continue;
                        }

                        Set<Set<String>> r1Decision = this.regionsToOptionSet.get(r1);
                        Set<Set<String>> r2Decision = this.regionsToOptionSet.get(r2);

                        if(!r1Decision.equals(r2Decision)) {
                            diff = true;
                            break;
                        }
                    }

                    if(!diff) {
//                        throw new RuntimeException("There are multiple regions in " + classNode.name + "-" + methodNode.name + " but all have the same options");
                        System.out.println("There are multiple regions in " + classNode.name + "-" + methodNode.name + " but all have the same options");
                    }
                }
            }
        }
    }

    /**
     * @param methodNode
     */
    private boolean propagateUpRegionsInMethod(MethodNode methodNode) {
        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

        if(!this.methodsWithUpdatedIndexes.contains(methodNode)) {
            this.calculateASMSIndexes(regionsInMethod, methodNode);
        }

        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegionSet = this.methodsToBlocksDecisions.get(methodNode);

        return this.propagateUpRegions(methodNode, blocksToRegionSet);
    }

    private boolean propagateUpRegions(MethodNode methodNode, Map<MethodBlock, JavaRegion> blocksToRegions) {
        this.debugBlockDecisions(methodNode);

        boolean updated = false;
        List<MethodBlock> worklist = new ArrayList<>();
        worklist.addAll(blocksToRegions.keySet());

        // Propagate up until a fixed point
        while(!worklist.isEmpty()) {
            MethodBlock block = worklist.remove(0);

            // Optimization
            if(blocksToRegions.get(block) == null) {
                continue;
            }

            // Special case
            if(block.isCatchWithImplicitThrow()) {
//                blocksToRegions.put(a, null);
//                this.regionsToOptionSet.remove(aRegion);
                continue;
            }

            List<MethodBlock> blocks = this.propagateUpRegionsInMethod(methodNode, block);

            if(blocks.isEmpty()) {
                continue;
            }

            worklist.addAll(0, blocks);
            updated = true;
        }

        worklist.addAll(blocksToRegions.keySet());

        // Fill the blocks that are guarded by decisions with those decisions
        while(!worklist.isEmpty()) {
            MethodBlock block = worklist.remove(0);

            // Optimization
            if(blocksToRegions.get(block) == null) {
                continue;
            }

            // Special case
            if(block.isCatchWithImplicitThrow()) {
//                blocksToRegions.put(a, null);
//                this.regionsToOptionSet.remove(aRegion);
                continue;
            }

            // Optimization
            if(block.isWithReturn()) {
                continue;
            }

            this.fillDownRegionsInMethod(methodNode, block);
        }

        this.debugBlockDecisions(methodNode);

        if(methodNode.name.equals("publish") && this.methodNodeToClassNode.get(methodNode).name.contains("CentralPublisher")) {
            System.out.println();
        }

        return updated;
    }

    private List<MethodBlock> propagateUpRegionsInMethod(MethodNode methodNode, MethodBlock block) {
        List<MethodBlock> blocks = new ArrayList<>();
        MethodGraph graph = this.getMethodGraph(methodNode);
        MethodBlock id = graph.getImmediateDominator(block);

        if(id == graph.getEntryBlock()) {
            return blocks;
        }

        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);
        JavaRegion blockRegion = blocksToRegions.get(block);
        Set<String> blockDecision = this.getDecision(blockRegion);

        JavaRegion idRegion = blocksToRegions.get(id);
        Set<String> idDecision = this.getDecision(idRegion);

        if(!(blockDecision.containsAll(idDecision) && !blockDecision.equals(idDecision))) {
//            this.debugBlockDecisions(methodNode);
//                System.out.println("Cannot push up to id in " + methodNode.name + " " + bDecision + " -> " + aDecision);
            return blocks;
        }

        // Check
        if(block.getPredecessors().isEmpty()) {
            throw new RuntimeException("The predecessors cannot be empty " + block.getID());
        }

        for(MethodBlock pred : block.getPredecessors()) {
            // A block might jump to itself
            if(block == pred) {
                continue;
            }

            JavaRegion predRegion = blocksToRegions.get(pred);
            Set<String> predDecision = this.getDecision(predRegion);

            if(!(blockDecision.containsAll(predDecision) || blockDecision.equals(predDecision))) {
                if(pred.isCatchWithImplicitThrow()) {
                    continue;
                }

                this.debugBlockDecisions(methodNode);
                throw new RuntimeException("Cannot push up decisions from " + block.getID() + " to " + pred.getID());


////                    System.out.println("Cannot push up to predecessor in " + methodNode.name + " " + bDecision + " -> " + aDecision);
//                    continue;
            }

            JavaRegion newRegion = new JavaRegion(blockRegion.getRegionPackage(), blockRegion.getRegionClass(), blockRegion.getRegionMethod());
            int index;

            this.debugBlocksAndRegions(methodNode);
//            this.debugBlockDecisions(methodNode);

            if(predRegion == null) {
                index = methodNode.instructions.indexOf(id.getInstructions().get(0));
            }
            else {
                index = predRegion.getStartBytecodeIndex();
                this.regionsToOptionSet.remove(predRegion);
            }

            newRegion.setStartBytecodeIndex(index);
            blocksToRegions.put(pred, newRegion);

            Set<Set<String>> newOptionSet = new HashSet<>();
            newOptionSet.add(blockDecision);
            this.regionsToOptionSet.put(newRegion, newOptionSet);

            this.debugBlocksAndRegions(methodNode);
//            this.debugBlockDecisions(methodNode);

            blocks.add(0, pred);
        }

        return blocks;
    }

    private void fillDownRegionsInMethod(MethodNode methodNode, MethodBlock block) {
        MethodGraph graph = this.getMethodGraph(methodNode);

        // Optimization
        if(!graph.isConnectedToExit(block)) {
            return;
        }

        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        JavaRegion blockRegion = blocksToRegions.get(block);
        Set<String> blockDecision = this.getDecision(blockRegion);

        MethodBlock beta = graph.getExitBlock();
        MethodBlock ipd = graph.getImmediatePostDominator(block);
        JavaRegion ipdRegion = blocksToRegions.get(ipd);
        Set<String> ipdDecision = this.getDecision(ipdRegion);

        while(ipd != beta && blockDecision.equals(ipdDecision)) {
//            MethodBlock temp = graph.getImmediatePostDominator(ipd);
            ipd = graph.getImmediatePostDominator(ipd);

//            if(temp == beta & ipd.getSuccessors().size() == 1 && ipd.getSuccessors().iterator().next() == beta) {
//                break;
//            }

//            ipd = temp;
            ipdRegion = blocksToRegions.get(ipd);
            ipdDecision = this.getDecision(ipdRegion);
        }

        Set<MethodBlock> reachables = graph.getReachableBlocks(block, ipd);
        reachables.remove(beta);
        List<MethodBlock> orderedReachables = new ArrayList<>();
        orderedReachables.addAll(reachables);

        orderedReachables.sort((o1, o2) -> {
            AbstractInsnNode o1Insts = o1.getInstructions().get(0);
            AbstractInsnNode o2Insts = o2.getInstructions().get(0);

            int o1Index = methodNode.instructions.indexOf(o1Insts);
            int o2Index = methodNode.instructions.indexOf(o2Insts);

            return Integer.compare(o1Index, o2Index);
        });

        Set<MethodBlock> skip = new HashSet<>();
        skip.add(block);
        skip.add(ipd);

        for(MethodBlock reach : orderedReachables) {
            if(skip.contains(reach)) {
                continue;
            }

            JavaRegion reachRegion = blocksToRegions.get(reach);
            Set<String> reachDecision = this.getDecision(reachRegion);

            Set<String> intersection = new HashSet<>();
            intersection.addAll(blockDecision);
            intersection.retainAll(reachDecision);

            if(!(intersection.equals(blockDecision) || intersection.equals(reachDecision)) && !blockDecision.equals(reachDecision)) {
                Set<MethodBlock> skipBlocks = graph.getReachableBlocks(reach, ipd);
                skipBlocks.remove(ipd);
                skip.addAll(skipBlocks);
                continue;
            }

            if(blockDecision.equals(reachDecision)) {
                continue;
            }

            this.debugBlocksAndRegions(methodNode);
//            this.debugBlockDecisions(methodNode);

            JavaRegion newRegion = new JavaRegion(blockRegion.getRegionPackage(), blockRegion.getRegionClass(), blockRegion.getRegionMethod());
            int index;

            if(reachRegion == null) {
                index = blockRegion.getStartBytecodeIndex();
            }
            else {
                index = reachRegion.getStartBytecodeIndex();
                this.regionsToOptionSet.remove(reachRegion);
            }

            newRegion.setStartBytecodeIndex(index);
            blocksToRegions.put(reach, newRegion);

            Set<Set<String>> newOptionSet = new HashSet<>();
            newOptionSet.add(blockDecision);
            this.regionsToOptionSet.put(newRegion, newOptionSet);

            this.debugBlocksAndRegions(methodNode);
            this.debugBlockDecisions(methodNode);
            System.out.println("");
        }
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

    private Set<String> getCachedDecision(JavaRegion region) {
        Set<String> decision = new HashSet<>();

        if(region == null) {
            return decision;
        }

        Set<Set<String>> optionSet = this.cachedRegionsToOptionSet.get(region);

        for(Set<String> options : optionSet) {
            decision.addAll(options);
        }

        return decision;
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
                    System.out.println(graph.toDotString("Error"));
                    System.out.println(block.getID());
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

    private void debugBlockDecisions(MethodNode methodNode) {
//        System.out.println("Debugging block decisions for " + methodNode.name);
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        MethodGraph graph = this.getMethodGraph(methodNode);
        Set<MethodBlock> blocks = graph.getBlocks();

//        if(methodNode.name.equals("init")) {
//            System.out.println();
//        }
//
//        if(methodNode.name.equals("<init>")) {
//            System.out.println();
//        }

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
//        System.out.println();
    }

    private void debugBlocksAndRegions(MethodNode methodNode) {
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);
        int blocksToDecisionCount = 0;

        for(JavaRegion r : blocksToRegions.values()) {
            if(r != null) {
                blocksToDecisionCount++;
            }
        }

        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);
        int regionsInMethodCount = regionsInMethod.size();

        if(blocksToDecisionCount != regionsInMethodCount) {
            throw new RuntimeException("The number of regions in " + methodNode.name + " does not match in the blocks to regions and methods to regions");
        }
    }

    /**
     * TODO
     *
     * @param methodNode
     */
    private void setStartAndEndBlocks(MethodNode methodNode) {
        // Special cases
        this.ignoreRegionsWithoutConnectionToExit(methodNode);
        this.ignoreCatchWithImplicitThrow(methodNode);

        MethodGraph graph = this.getMethodGraph(methodNode);
        MethodBlock beta = graph.getExitBlock();
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);
        LinkedHashMap<MethodBlock, JavaRegion> cachedBlocksToRegions = this.cachedMethodsToBlocksDecisions.get(methodNode);

        for(Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
            MethodBlock block = blockToRegion.getKey();

            // The entry block should be skipped
            if(graph.getEntryBlock() == block) {
                continue;
            }

            JavaRegion region = blockToRegion.getValue();

            // Optimization
            if(region == null) {
                continue;
            }

            region.setStartMethodBlock(block);
            Set<String> blockDecision = this.getDecision(region);

//            if(blockDecision.contains("B")) {
//                System.out.println();
//            }

            MethodBlock ipd = graph.getImmediatePostDominator(block);
            JavaRegion ipdRegion = cachedBlocksToRegions.get(ipd);
            Set<String> ipdDecision = this.getCachedDecision(ipdRegion);

            while(ipd != beta && (blockDecision.equals(ipdDecision) || blockDecision.containsAll(ipdDecision))) {
                MethodBlock temp = graph.getImmediatePostDominator(ipd);

//                // Optimization
//                if(temp == beta & ipd.getSuccessors().size() == 1 && ipd.getSuccessors().iterator().next() == beta) {
//                    break;
//                }

                ipd = temp;
                ipdRegion = cachedBlocksToRegions.get(ipd);
                ipdDecision = this.getCachedDecision(ipdRegion);
            }

            MethodBlock end = ipd;
            Set<MethodBlock> ends = new HashSet<>();

            if(block == end) {
                throw new RuntimeException("Start and end equal");
            }
            else if(block.getSuccessors().size() == 1 && block.getSuccessors().iterator().next().equals(end)) {
                ends.add(block);
//                throw new RuntimeException("A control flow decision only has 1 successor? " + block + " -> " + end);
            }
            else if(graph.getExitBlock() == end) {
                this.endRegionBlocksWithReturn.addAll(end.getPredecessors());
                ends.addAll(end.getPredecessors());
            }
            else {
                ends.add(end);
            }

            region.setEndMethodBlocks(ends);

            // Remove
            Set<MethodBlock> reachables = new HashSet<>();

            for(MethodBlock e : ends) {
                reachables.addAll(graph.getReachableBlocks(block, e));
            }

            reachables.removeAll(ends);
            reachables.add(block);

            // If the ends are connected to the exit node, we want to analyze them
            for(MethodBlock e : ends) {
                JavaRegion eRegion = cachedBlocksToRegions.get(e);
                Set<String> eDecision = this.getCachedDecision(eRegion);
                ipd = graph.getImmediatePostDominator(e);

                if(ipd == beta & e.getSuccessors().size() == 1 && e.getSuccessors().iterator().next() == beta
                        && (blockDecision.equals(eDecision) || blockDecision.containsAll(eDecision))) {
                    reachables.add(e);
                }
            }

            this.removeRegionsInCallees(methodNode, blockDecision, reachables);
            reachables.remove(block);
            this.removeRegionsInMethod(methodNode, blockDecision, reachables);
        }
    }

    /**
     * TODO
     *
     * @param methodNode
     */
    private void ignoreCatchWithImplicitThrow(MethodNode methodNode) {
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        // Check if there is a catch with an implicit flow
        for(Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
            if(blockToRegion.getValue() == null) {
                continue;
            }

            if(!blockToRegion.getKey().isCatchWithImplicitThrow()) {
                continue;
            }

            this.debugBlocksAndRegions(methodNode);
            this.regionsToOptionSet.remove(blockToRegion.getValue());
            blocksToRegions.put(blockToRegion.getKey(), null);
            this.debugBlocksAndRegions(methodNode);

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

            this.debugBlocksAndRegions(methodNode);
            this.regionsToOptionSet.remove(blockToRegion.getValue());
            blocksToRegions.put(blockToRegion.getKey(), null);
            this.debugBlocksAndRegions(methodNode);
        }
    }

    private void removeRegionsInMethod(MethodNode methodNode, Set<String> decision, Set<MethodBlock> reachables) {
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.methodsToBlocksDecisions.get(methodNode);

        for(MethodBlock block : reachables) {
            JavaRegion region = blocksToRegions.get(block);

            // Optimization
            if(region == null) {
                continue;
            }

            Set<String> blockDecision = this.getCachedDecision(region);
//            Set<String> bDecision = this.getDecision(bRegion);

            if(!(decision.equals(blockDecision) || decision.containsAll(blockDecision))) {
                continue;
            }

            this.debugBlocksAndRegions(methodNode);
            this.debugBlockDecisions(methodNode);

            blocksToRegions.put(block, null);
            this.regionsToOptionSet.remove(region);

            this.debugBlocksAndRegions(methodNode);
            this.debugBlockDecisions(methodNode);
        }
    }

    /**
     * TODO ignore specia blocks like catch with implicit throw
     *
     * @param methodNode
     * @param decision
     * @param reachables
     */
    private void removeRegionsInCallees(MethodNode methodNode, Set<String> decision, Set<MethodBlock> reachables) {
        Map<MethodBlock, SootMethod> blocksToMethods = new HashMap<>();
        SootMethod sootMethod = this.methodNodeToSootMethod.get(methodNode);

        for(MethodBlock block : reachables) {
            blocksToMethods.put(block, sootMethod);
        }

        Set<SootMethod> analyzedCallees = new HashSet<>();
        List<MethodBlock> worklist = new ArrayList<>();
        worklist.addAll(reachables);

        while(!worklist.isEmpty()) {
            MethodBlock reach = worklist.remove(0);
            sootMethod = blocksToMethods.get(reach);
            analyzedCallees.add(sootMethod);

            for(AbstractInsnNode inst : reach.getInstructions()) {
                // Optimization
                if(inst.getOpcode() < 0) {
                    continue;
                }

                // Optimization
                if(inst.getOpcode() < Opcodes.GETSTATIC || inst.getOpcode() > Opcodes.MONITOREXIT) {
                    continue;
                }

                // Get caller unit
                Unit unit = this.getUnit(inst, sootMethod);

                if(unit == null) {
                    continue;
                }

                List<Edge> calleeEdges = this.getCalleeEdges(unit);

                for(Edge edge : calleeEdges) {
                    SootMethod calleeSootMethod = edge.tgt();

                    if(analyzedCallees.contains(calleeSootMethod)) {
                        continue;
                    }

                    MethodNode calleeMethodNode = this.sootMethodToMethodNode.get(calleeSootMethod);
                    LinkedHashMap<MethodBlock, JavaRegion> calleeBlocksToRegions = this.methodsToBlocksDecisions.get(calleeMethodNode);

                    if(calleeBlocksToRegions == null) {
                        // TODO fix this by changing the package name
                        continue;
                    }

                    Set<MethodBlock> skip = new HashSet<>();

                    for(Map.Entry<MethodBlock, JavaRegion> entry : calleeBlocksToRegions.entrySet()) {
                        if(skip.contains(entry.getKey())) {
                            continue;
                        }

                        JavaRegion calleeRegion = entry.getValue();

                        // Optimization
                        if(calleeRegion == null) {
                            continue;
                        }

                        Set<String> calleeDecision = this.getCachedDecision(calleeRegion);

                        if(!(decision.equals(calleeDecision) || decision.containsAll(calleeDecision))) {
                            MethodGraph calleegraph = this.getMethodGraph(calleeMethodNode);
                            MethodBlock ipd = calleegraph.getImmediatePostDominator(entry.getKey());
                            Set<MethodBlock> rs = calleegraph.getReachableBlocks(entry.getKey(), ipd);
                            rs.remove(ipd);
                            skip.addAll(rs);
                            continue;
                        }

                        this.debugBlocksAndRegions(calleeMethodNode);
                        this.debugBlockDecisions(calleeMethodNode);

                        this.regionsToOptionSet.remove(calleeRegion);
                        calleeBlocksToRegions.put(entry.getKey(), null);

                        this.debugBlocksAndRegions(calleeMethodNode);
                        this.debugBlockDecisions(calleeMethodNode);
                    }

                    for(Map.Entry<MethodBlock, JavaRegion> entry : calleeBlocksToRegions.entrySet()) {
                        if(skip.contains(entry.getKey())) {
                            continue;
                        }

                        worklist.add(0, entry.getKey());
                        blocksToMethods.put(entry.getKey(), calleeSootMethod);
                    }
                }
            }
        }
    }

    /**
     * TODO
     *
     * @param inst
     * @param sootMethod
     * @return
     */
    private Unit getUnit(AbstractInsnNode inst, SootMethod sootMethod) {
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

        if(match == null && inst instanceof MethodInsnNode) {
            throw new RuntimeException("There has to be a instruciton that calls a method");
        }

//        if(match == null) {
//            throw new RuntimeException("Could not find the instruction in this method");
//        }

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
    private void calculateASMSIndexes(List<JavaRegion> regionsInMethod, MethodNode methodNode) {
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

            if(outputLine.contains(" Code:")) {
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

    private List<Edge> getCallerEdges(SootMethod method) {
        Iterator<Edge> inEdges = this.callGraph.edgesInto(method);
        List<Edge> worklist = new ArrayList<>();

        while(inEdges.hasNext()) {
            worklist.add(inEdges.next());
        }

        List<Edge> callerEdges = new ArrayList<>();

        while(!worklist.isEmpty()) {
            Edge edge = worklist.remove(0);
            SootMethod src = edge.src();

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
     * TODO
     *
     * @param unit
     * @return
     */
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
