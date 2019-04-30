package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.fixed;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodTracer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.TraceClassInspector;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.DefaultMethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.PrettyMethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.PrettyMethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.RegionTransformer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.util.Printer;
import org.apache.commons.lang3.StringUtils;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;

public abstract class StaticRegionTransformer extends RegionTransformer<Set<Set<String>>> {

  private static final String CLINIT_SIGNATURE = "void <clinit>()";

  private Set<MethodNode> methodsWithUpdatedIndexes = new HashSet<>();
  private Map<SootMethod, Set<Set<String>>> sootMethodToOptionSet = new HashMap<>();

  private Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> cachedMethodsToBlocksDecisions = new HashMap<>();
  private Map<JavaRegion, Set<Set<String>>> cachedRegionsToOptionSet = new HashMap<>();

  // TODO delete programName
  public StaticRegionTransformer(String programName, String entryPoint,
      ClassTransformer classTransformer,
      Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) {
    // TODO pass the root package in the constructor
    super(programName, entryPoint, entryPoint.substring(0, entryPoint.indexOf(".")),
        classTransformer, regionsToOptionSet, false, new StaticInstructionRegionMatcher());
  }

  public StaticRegionTransformer(String programName, String entryPoint, String directory,
      Map<JavaRegion, Set<Set<String>>> regionsToOptionSet)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    this(programName, entryPoint, new DefaultClassTransformer(directory), regionsToOptionSet);
  }

  private void matchMethodsToOptions() {
    for (Map.Entry<JavaRegion, Set<Set<String>>> entry : this.getRegionsToData().entrySet()) {
      JavaRegion region = entry.getKey();
      String bytecodeSignature =
          region.getRegionPackage() + "." + region.getRegionClass() + ": " + region
              .getRegionMethod();

      for (SootMethod sootMethod : this.getSootMethodToMethodNode().keySet()) {
        if (!sootMethod.getBytecodeSignature().contains(bytecodeSignature)) {
          continue;
        }

        Set<Set<String>> optionSet = this.sootMethodToOptionSet.get(sootMethod);

        if (optionSet == null) {
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
    for (ClassNode classNode : classNodes) {
      if (!this.getMethodNodeToClassNode().containsValue(classNode)) {
        continue;
      }

      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

      if (methodsToInstrument.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToInstrument) {
        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

        if (!this.methodsWithUpdatedIndexes.contains(methodNode)) {
          this.calculateASMSIndexes(regionsInMethod, methodNode);
        }
      }
    }
  }

  @Override
  public void transformMethods(Set<ClassNode> classNodes) throws IOException {
    super.transformMethods(classNodes);

    int initialRegionCount = this.getRegionsToData().size();

    this.calculateASMIndexes(classNodes);
    this.matchMethodsToOptions();
    this.setBlocksToRegions(classNodes);

    boolean updatedRegions = true;

    while (updatedRegions) {
      updatedRegions = this.propagateUpMethodsInClasses(classNodes);
      updatedRegions = updatedRegions | this.propagateUpAcrossMethods();
    }

    this.cachedRegionsToOptions();
    this.cacheMethodsToBlockDecisions();

    this.instrument(classNodes);

    System.out.println("# of regions before optimizing: " + initialRegionCount);
    System.out.println("# of regions after optimizing: " + this.getRegionsToData().size());
    System.out.println("");
  }

  private void cachedRegionsToOptions() {
    for (Map.Entry<JavaRegion, Set<Set<String>>> entry : this.getRegionsToData().entrySet()) {
      this.cachedRegionsToOptionSet.put(entry.getKey(), entry.getValue());
    }
  }

  private void cacheMethodsToBlockDecisions() {
    for (Map.Entry<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> entry : this
        .getMethodsToRegionsInBlocks()
        .entrySet()) {
      this.cachedMethodsToBlocksDecisions
          .put(entry.getKey(), new LinkedHashMap<>(entry.getValue()));
    }
  }

  /**
   * Process the methods to find where the regions are in each of them
   */
  private boolean propagateUpMethodsInClasses(Set<ClassNode> classNodes) {
    boolean updatedMethods = false;

    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

      if (methodsToInstrument.isEmpty()) {
        continue;
      }

      System.out.println("Propagate regions up in class " + classNode.name);

      for (MethodNode methodNode : methodsToInstrument) {
        System.out.println("Propagate regions up in method " + methodNode.name);
        updatedMethods = updatedMethods | this.propagateUpRegionsInMethod(methodNode);
      }
    }

    return updatedMethods;
  }

  /**
   * TODO
   */
  private boolean propagateUpAcrossMethods() {
    boolean updated = false;

    Set<SootMethod> methods = this.getApplicationSootMethods();
    List<SootMethod> worklist = new ArrayList<>();
    worklist.addAll(methods);

    // Loop through all the methods until a fixed point
    while (!worklist.isEmpty()) {
      SootMethod method = worklist.remove(0);
//            System.out.println("Propagate regions across method " + a.getName());

      // Check not part of algorithm
      if (method.getSubSignature().equals(StaticRegionTransformer.MAIN_SIGNATURE)) {
        continue;
      }

      List<JavaRegion> regionsInMethod = this.getRegionsInSootMethod(method);

      // Optimization nothing to push up
      if (regionsInMethod.isEmpty()) {
        continue;
      }

      boolean canPropagate = this.canPropagateRegion(method);

      if (!canPropagate) {
        continue;
      }

      List<SootMethod> methodsToAnalyze = this.propagateUpRegionInter(method);

      if (methodsToAnalyze.isEmpty()) {
        continue;
      }

      worklist.addAll(0, methodsToAnalyze);
      updated = true;
    }

    return updated;
  }

  private List<SootMethod> propagateUpRegionInter(SootMethod method) {
    List<SootMethod> methods = new ArrayList<>();
    MethodNode methodNode = this.getSootMethodToMethodNode().get(method);
    Collection<JavaRegion> regions = this.getMethodsToRegionsInBlocks().get(methodNode).values();
    Iterator<JavaRegion> regionsIter = regions.iterator();
    JavaRegion region = regionsIter.next();

    while (region == null) {
      region = regionsIter.next();
    }

    Set<String> decision = this.getSingleDecision(region);

    if (decision.isEmpty()) {
      throw new RuntimeException("The first decision in " + methodNode.name + " cannot be null");
    }

    List<Edge> edges = this.getCallerEdges(method);

    for (Edge edge : edges) {
      SootMethod callerSootMethod = edge.src();
      MethodNode callerMethodNode = this.getSootMethodToMethodNode().get(callerSootMethod);
      LinkedHashMap<MethodBlock, JavaRegion> callerBlocksToRegions = this
          .getMethodsToRegionsInBlocks()
          .get(callerMethodNode);

      MethodBlock callerBlock = this.getCallerBlock(edge);
      JavaRegion callerRegion = callerBlocksToRegions.get(callerBlock);
      Set<String> callerDecision = this.getSingleDecision(callerRegion);

      if (!decision.containsAll(callerDecision) && !decision.equals(callerDecision)
          && !callerDecision.containsAll(decision)) {
        throw new RuntimeException(
            "Cannot push up " + decision + " from " + methodNode.name + " to "
                + callerMethodNode.name + " at " + callerBlock.getID() + " because it has decision "
                + callerDecision);
      }

      if (!(decision.containsAll(callerDecision) && !decision.equals(callerDecision))) {
        continue;
      }

      JavaRegion newRegion;
      int index;

      if (callerRegion == null) {
        String classPackage = callerSootMethod.getDeclaringClass().getPackageName();
        String className = callerSootMethod.getDeclaringClass().getShortName();
        String methodName = callerSootMethod.getBytecodeSignature();
        methodName = methodName.substring(methodName.indexOf(" "), methodName.length() - 1).trim();
        index = callerMethodNode.instructions.indexOf(callerBlock.getInstructions().get(0));

        newRegion = new JavaRegion.Builder(classPackage, className, methodName)
            .startBytecodeIndex(index).build();
        this.methodsWithUpdatedIndexes.add(callerMethodNode);
      }
      else {
        index = callerRegion.getStartRegionIndex();

        newRegion = new JavaRegion.Builder(callerRegion.getRegionPackage(),
            callerRegion.getRegionClass(),
            callerRegion.getRegionMethod()).startBytecodeIndex(index).build();
        this.getRegionsToData().remove(callerRegion);
      }

      callerBlocksToRegions.put(callerBlock, newRegion);

      Set<Set<String>> newOptionSet = new HashSet<>();
      newOptionSet.add(decision);

//            this.endRegionBlocksWithReturn.add(callerBlock);
      this.getRegionsToData().put(newRegion, newOptionSet);

      methods.add(edge.src());
      this.debugBlocksAndRegions(callerMethodNode);
    }

    return methods;
  }

  /**
   * Check with all callers if the region can be pushed up
   */
  private boolean canPropagateRegion(SootMethod method) {
    boolean canPush = true;

    MethodNode methodNode = this.getSootMethodToMethodNode().get(method);
    Collection<JavaRegion> regions = this.getMethodsToRegionsInBlocks().get(methodNode).values();
    Iterator<JavaRegion> regionsIter = regions.iterator();
    JavaRegion region = regionsIter.next();

    while (region == null) {
      region = regionsIter.next();
    }

    Set<String> decision = this.getSingleDecision(region);

    if (decision.isEmpty()) {
      throw new RuntimeException("The first decision in " + methodNode.name + " cannot be null");
    }

    List<Edge> edges = this.getCallerEdges(method);

    for (Edge edge : edges) {
      SootMethod callerSootMethod = edge.src();
      MethodNode callerMethodNode = this.getSootMethodToMethodNode().get(callerSootMethod);
      LinkedHashMap<MethodBlock, JavaRegion> callerBlocksToRegions = this
          .getMethodsToRegionsInBlocks()
          .get(callerMethodNode);

      MethodBlock callerBlock = this.getCallerBlock(edge);
      JavaRegion callerRegion = callerBlocksToRegions.get(callerBlock);
      Set<String> callerDecision = this.getSingleDecision(callerRegion);

      if (!decision.containsAll(callerDecision) && !decision.equals(callerDecision)
          && !callerDecision.containsAll(decision)) {
//                this.debugBlockDecisions(callerMethodNode);
        System.out.println("Cannot push up " + decision + " from " + methodNode.name + " to "
            + callerMethodNode.name + " at " + callerBlock.getID() + " because it has decision "
            + callerDecision);
        canPush = false;
        break;
      }
    }

    return canPush;
  }

  /**
   * TODO
   */
  private void instrument(Set<ClassNode> classNodes) throws IOException {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);
      if (methodsToInstrument.isEmpty()) {
        continue;
      }

//            System.out.println("Setting where to instrument class " + classNode.name);

      for (MethodNode methodToInstrument : methodsToInstrument) {
//                System.out.println("Setting where to instrument method " + methodToInstrument.name);
        this.setStartAndEndBlocks(methodToInstrument);
      }
    }

    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

      if (methodsToInstrument.isEmpty()) {
        continue;
      }

//            System.out.println("Instrumenting class " + classNode.name);

      for (MethodNode methodToInstrument : methodsToInstrument) {
//                System.out.println("Instrumenting method " + methodToInstrument.name);
        this.transformMethod(methodToInstrument, classNode);
      }

      this.getClassTransformer().writeClass(classNode);

      // Debugging
      TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
      MethodTracer tracer = classInspector.visitClass();

      // TODO there is a bug in the pretty print since it is not showing the instructions that were added
      for (MethodNode methodNode : methodsToInstrument) {
        Printer printer = tracer
            .getPrinterForMethodSignature(RegionTransformer.getMethodName(methodNode));
        PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
        PrettyMethodGraph prettyGraph = prettyBuilder.build(methodNode);
        prettyGraph.saveDotFile(this.getProgramName(), classNode.name, methodNode.name);

        try {
          prettyGraph.savePdfFile(this.getProgramName(), classNode.name, methodNode.name);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    this.debugRemovingRegions(classNodes);
  }

  private void debugRemovingRegions(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      for (MethodNode methodNode : classNode.methods) {
        List<JavaRegion> regions = this.getRegionsInMethod(methodNode);

        if (regions.size() == 1) {
          continue;
        }

        for (JavaRegion r1 : regions) {
          boolean diff = false;

          for (JavaRegion r2 : regions) {
            if (r1 == r2) {
              continue;
            }

            Set<Set<String>> r1Decision = this.getRegionsToData().get(r1);
            Set<Set<String>> r2Decision = this.getRegionsToData().get(r2);

            if (!r1Decision.equals(r2Decision)) {
              diff = true;
              break;
            }
          }

          if (!diff) {
//                        throw new RuntimeException("There are multiple regions in " + classNode.name + "-" + methodNode.name + " but all have the same options");
            System.out.println(
                "There are multiple regions in " + classNode.name + "-" + methodNode.name
                    + " but all have the same options");
          }
        }
      }
    }
  }

  /**
   *
   */
  private boolean propagateUpRegionsInMethod(MethodNode methodNode) {
    List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);

    if (!this.methodsWithUpdatedIndexes.contains(methodNode)) {
      this.calculateASMSIndexes(regionsInMethod, methodNode);
    }

    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegionSet = this.getMethodsToRegionsInBlocks()
        .get(methodNode);

    return this.propagateUpRegions(methodNode, blocksToRegionSet);
  }

  private boolean propagateUpRegions(MethodNode methodNode,
      Map<MethodBlock, JavaRegion> blocksToRegions) {
    this.debugBlockDecisions(methodNode);

    boolean updated = false;
    List<MethodBlock> worklist = new ArrayList<>();
    worklist.addAll(blocksToRegions.keySet());

    // Propagate up until a fixed point
    while (!worklist.isEmpty()) {
      MethodBlock block = worklist.remove(0);

      // Optimization
      if (blocksToRegions.get(block) == null) {
        continue;
      }

      // Special case
      if (block.isCatchWithImplicitThrow()) {
//                blocksToRegions.put(a, null);
//                this.getRegionsToData().remove(aRegion);
        continue;
      }

      List<MethodBlock> updatedBlocks = this.propagateUpRegionsInMethod(methodNode, block);

      if (updatedBlocks.isEmpty()) {
        continue;
      }

      worklist.addAll(0, updatedBlocks);
      updated = true;
    }

    worklist.addAll(blocksToRegions.keySet());

    // Fill the blocks that are guarded by decisions with those decisions
    while (!worklist.isEmpty()) {
      MethodBlock block = worklist.remove(0);

      // Optimization
      if (blocksToRegions.get(block) == null) {
        continue;
      }

      // Special case
      if (block.isCatchWithImplicitThrow()) {
//                blocksToRegions.put(a, null);
//                this.getRegionsToData().remove(aRegion);
        continue;
      }

      // Optimization
      if (block.isWithReturn()) {
        continue;
      }

      this.fillDownRegionsInMethod(methodNode, block);
    }

    this.debugBlockDecisions(methodNode);

    if (methodNode.name.equals("publish") && this.getMethodNodeToClassNode().get(methodNode).name
        .contains("CentralPublisher")) {
      System.out.println();
    }

    return updated;
  }

  private List<MethodBlock> propagateUpRegionsInMethod(MethodNode methodNode, MethodBlock block) {
    List<MethodBlock> updatedBlocks = new ArrayList<>();
    MethodGraph graph = this.getMethodGraph(methodNode);
    MethodBlock id = graph.getImmediateDominator(block);

    if (id == null) {
      return updatedBlocks;
    }

    if (id == graph.getEntryBlock()) {
      return updatedBlocks;
    }

    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);
    JavaRegion blockRegion = blocksToRegions.get(block);
    Set<String> blockDecision = this.getSingleDecision(blockRegion);

    JavaRegion idRegion = blocksToRegions.get(id);
    Set<String> idDecision = this.getSingleDecision(idRegion);

    if (!(blockDecision.containsAll(idDecision) && !blockDecision.equals(idDecision))) {
//            this.debugBlockDecisions(methodNode);
//                System.out.println("Cannot push up to id in " + methodNode.name + " " + bDecision + " -> " + aDecision);
      return updatedBlocks;
    }

    // Check
    if (block.getPredecessors().isEmpty()) {
      throw new RuntimeException("The predecessors cannot be empty " + block.getID());
    }

    for (MethodBlock pred : block.getPredecessors()) {
      // A block might jump to itself
      if (block == pred) {
        continue;
      }

      JavaRegion predRegion = blocksToRegions.get(pred);
      Set<String> predDecision = this.getSingleDecision(predRegion);

      if (!(blockDecision.containsAll(predDecision) || blockDecision.equals(predDecision))) {
        if (pred.isCatchWithImplicitThrow()) {
          continue;
        }

        this.debugBlockDecisions(methodNode);
        throw new RuntimeException(
            "Cannot push up decisions from " + block.getID() + " to " + pred.getID());

////                    System.out.println("Cannot push up to predecessor in " + methodNode.name + " " + bDecision + " -> " + aDecision);
//                    continue;
      }

      JavaRegion newRegion = new JavaRegion.Builder(blockRegion.getRegionPackage(),
          blockRegion.getRegionClass(), blockRegion.getRegionMethod()).build();
      int index;

      this.debugBlocksAndRegions(methodNode);
//            this.debugBlockDecisions(methodNode);

      if (predRegion == null) {
        index = methodNode.instructions.indexOf(id.getInstructions().get(0));
      }
      else {
        index = predRegion.getStartRegionIndex();
        this.getRegionsToData().remove(predRegion);
      }

      newRegion.setStartRegionIndex(index);
      blocksToRegions.put(pred, newRegion);

      Set<Set<String>> newOptionSet = new HashSet<>();
      newOptionSet.add(blockDecision);
      this.getRegionsToData().put(newRegion, newOptionSet);

      this.debugBlocksAndRegions(methodNode);
//            this.debugBlockDecisions(methodNode);

      updatedBlocks.add(0, pred);
    }

    return updatedBlocks;
  }

  private void fillDownRegionsInMethod(MethodNode methodNode, MethodBlock block) {
    MethodGraph graph = this.getMethodGraph(methodNode);

    // Optimization
    if (!graph.isConnectedToExit(block)) {
      return;
    }

    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);

    JavaRegion blockRegion = blocksToRegions.get(block);
    Set<String> blockDecision = this.getSingleDecision(blockRegion);

    MethodBlock beta = graph.getExitBlock();
    MethodBlock ipd = graph.getImmediatePostDominator(block);
    JavaRegion ipdRegion = blocksToRegions.get(ipd);
    Set<String> ipdDecision = this.getSingleDecision(ipdRegion);

    while (ipd != beta && blockDecision.equals(ipdDecision)) {
//            MethodBlock temp = graph.getImmediatePostDominator(ipd);
      ipd = graph.getImmediatePostDominator(ipd);

//            if(temp == beta & ipd.getSuccessors().size() == 1 && ipd.getSuccessors().iterator().next() == beta) {
//                break;
//            }

//            ipd = temp;
      ipdRegion = blocksToRegions.get(ipd);
      ipdDecision = this.getSingleDecision(ipdRegion);
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

    for (MethodBlock reach : orderedReachables) {
      if (skip.contains(reach)) {
        continue;
      }

      JavaRegion reachRegion = blocksToRegions.get(reach);
      Set<String> reachDecision = this.getSingleDecision(reachRegion);

      Set<String> intersection = new HashSet<>();
      intersection.addAll(blockDecision);
      intersection.retainAll(reachDecision);

      if (!(intersection.equals(blockDecision) || intersection.equals(reachDecision))
          && !blockDecision.equals(reachDecision)) {
        Set<MethodBlock> skipBlocks = graph.getReachableBlocks(reach, ipd);
        skipBlocks.remove(ipd);
        skip.addAll(skipBlocks);
        continue;
      }

      if (blockDecision.equals(reachDecision)) {
        continue;
      }

      this.debugBlocksAndRegions(methodNode);
//            this.debugBlockDecisions(methodNode);

      JavaRegion newRegion = new JavaRegion.Builder(blockRegion.getRegionPackage(),
          blockRegion.getRegionClass(), blockRegion.getRegionMethod()).build();
      int index;

      if (reachRegion == null) {
        index = blockRegion.getStartRegionIndex();
      }
      else {
        index = reachRegion.getStartRegionIndex();
        this.getRegionsToData().remove(reachRegion);
      }

      newRegion.setStartRegionIndex(index);
      blocksToRegions.put(reach, newRegion);

      Set<Set<String>> newOptionSet = new HashSet<>();
      newOptionSet.add(blockDecision);
      this.getRegionsToData().put(newRegion, newOptionSet);

      this.debugBlocksAndRegions(methodNode);
      this.debugBlockDecisions(methodNode);
      System.out.println("");
    }
  }

  private Set<String> getSingleDecision(JavaRegion region) {
    Set<String> decision = new HashSet<>();

    if (region == null) {
      return decision;
    }

    Set<Set<String>> optionSet = this.getDecision(region);

    for (Set<String> options : optionSet) {
      decision.addAll(options);
    }

    return decision;
  }

  @Override
  protected Set<Set<String>> getDecision(JavaRegion region) {
    if (region == null) {
      return new HashSet<>();
    }

    return this.getRegionsToData().get(region);
  }


  private Set<String> getCachedDecision(JavaRegion region) {
    Set<String> decision = new HashSet<>();

    if (region == null) {
      return decision;
    }

    Set<Set<String>> optionSet = this.cachedRegionsToOptionSet.get(region);

    for (Set<String> options : optionSet) {
      decision.addAll(options);
    }

    return decision;
  }

  private MethodGraph getMethodGraph(MethodNode methodNode) {
    MethodGraph graph = this.getMethodsToGraphs().get(methodNode);

    if (graph == null) {
      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      graph = builder.build(methodNode);
      this.getMethodsToGraphs().put(methodNode, graph);
    }

    return graph;
  }

  private MethodGraph buildMethodGraph(MethodNode methodNode) {
    DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
    MethodGraph graph = builder.build(methodNode);
    this.getMethodsToGraphs().put(methodNode, graph);

    return graph;
  }

  private void debugBlockDecisions(MethodNode methodNode) {
//        System.out.println("Debugging block decisions for " + methodNode.name);
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);

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

    for (MethodBlock block : blocks) {
      dotString.append(block.getID());
      dotString.append(" [label=\"");
      dotString.append(block.getID());
      dotString.append(" - ");

      JavaRegion region = blocksToRegions.get(block);

      if (region == null) {
        dotString.append("[]");
      }
      else {
        Set<String> decision = this.getSingleDecision(region);
        dotString.append(decision);
      }

      dotString.append("\"];\n");
    }

    dotString.append(graph.getEntryBlock().getID());
    dotString.append(";\n");
    dotString.append(graph.getExitBlock().getID());
    dotString.append(";\n");

    for (MethodBlock methodBlock : graph.getBlocks()) {
      for (MethodBlock successor : methodBlock.getSuccessors()) {
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
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);
    int blocksToDecisionCount = 0;

    for (JavaRegion r : blocksToRegions.values()) {
      if (r != null) {
        blocksToDecisionCount++;
      }
    }

    List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode);
    int regionsInMethodCount = regionsInMethod.size();

    if (blocksToDecisionCount != regionsInMethodCount) {
      throw new RuntimeException(
          "The number of regions in " + methodNode.name + " does not match in the " +
              "blocks-to-regions map and methods-to-regions map");
    }
  }

  /**
   * TODO
   */
  private void setStartAndEndBlocks(MethodNode methodNode) {
    // Special cases
    this.ignoreRegionsWithoutConnectionToExit(methodNode);
    this.ignoreCatchWithImplicitThrow(methodNode);

    MethodGraph graph = this.getMethodGraph(methodNode);
    MethodBlock beta = graph.getExitBlock();
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);
    LinkedHashMap<MethodBlock, JavaRegion> cachedBlocksToRegions = this.cachedMethodsToBlocksDecisions
        .get(methodNode);

    for (Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
      MethodBlock block = blockToRegion.getKey();

      // The entry block should be skipped
      if (graph.getEntryBlock() == block) {
        continue;
      }

      JavaRegion region = blockToRegion.getValue();

      // Optimization
      if (region == null) {
        continue;
      }

      region.setStartMethodBlock(block);
      Set<String> blockDecision = this.getSingleDecision(region);

//            if(blockDecision.contains("B")) {
//                System.out.println();
//            }

      MethodBlock ipd = graph.getImmediatePostDominator(block);
      JavaRegion ipdRegion = cachedBlocksToRegions.get(ipd);
      Set<String> ipdDecision = this.getCachedDecision(ipdRegion);

      while (ipd != beta && (blockDecision.equals(ipdDecision) || blockDecision
          .containsAll(ipdDecision))) {
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

      if (block == end) {
        throw new RuntimeException("Start and end equal");
      }
      else if (block.getSuccessors().size() == 1 && block.getSuccessors().iterator().next()
          .equals(end)) {
        ends.add(block);

        if (graph.getExitBlock() == end) {
          this.getEndRegionBlocksWithReturn().add(block);
        }
      }
      else if (graph.getExitBlock() == end) {
        this.getEndRegionBlocksWithReturn().addAll(end.getPredecessors());
        ends.addAll(end.getPredecessors());
      }
      else {
        ends.add(end);
      }

      region.setEndMethodBlocks(ends);

      // Remove
      Set<MethodBlock> reachables = new HashSet<>();

      for (MethodBlock e : ends) {
        reachables.addAll(graph.getReachableBlocks(block, e));
      }

      reachables.removeAll(ends);
      reachables.add(block);

      // If the ends are connected to the exit node, we want to analyze them
      for (MethodBlock e : ends) {
        JavaRegion eRegion = cachedBlocksToRegions.get(e);
        Set<String> eDecision = this.getCachedDecision(eRegion);
        ipd = graph.getImmediatePostDominator(e);

        if (ipd == beta & e.getSuccessors().size() == 1
            && e.getSuccessors().iterator().next() == beta
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
   */
  private void ignoreCatchWithImplicitThrow(MethodNode methodNode) {
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);

    // Check if there is a catch with an implicit flow
    for (Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
      if (blockToRegion.getValue() == null) {
        continue;
      }

      if (!blockToRegion.getKey().isCatchWithImplicitThrow()) {
        continue;
      }

      this.debugBlocksAndRegions(methodNode);
      this.getRegionsToData().remove(blockToRegion.getValue());
      blocksToRegions.put(blockToRegion.getKey(), null);
      this.debugBlocksAndRegions(methodNode);

    }

  }

  /**
   * TODO Check if there is a region that does not have a connection to the exit block. This happens
   * when regions occur in handlers not connected to the exit block
   */
  private void ignoreRegionsWithoutConnectionToExit(MethodNode methodNode) {
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);
    MethodGraph graph = this.getMethodGraph(methodNode);

    for (Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
      if (blockToRegion.getValue() == null) {
        continue;
      }

      if (graph.getReachableBlocks(blockToRegion.getKey(), graph.getExitBlock())
          .contains(graph.getExitBlock())) {
        continue;
      }

      this.debugBlocksAndRegions(methodNode);
      this.getRegionsToData().remove(blockToRegion.getValue());
      blocksToRegions.put(blockToRegion.getKey(), null);
      this.debugBlocksAndRegions(methodNode);
    }
  }

  private void removeRegionsInMethod(MethodNode methodNode, Set<String> decision,
      Set<MethodBlock> reachables) {
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);

    for (MethodBlock block : reachables) {
      JavaRegion region = blocksToRegions.get(block);

      // Optimization
      if (region == null) {
        continue;
      }

      Set<String> blockDecision = this.getCachedDecision(region);
//            Set<String> bDecision = this.getSingleDecision(bRegion);

      if (!(decision.equals(blockDecision) || decision.containsAll(blockDecision))) {
        continue;
      }

      this.debugBlocksAndRegions(methodNode);
      this.debugBlockDecisions(methodNode);

      blocksToRegions.put(block, null);
      this.getRegionsToData().remove(region);

      this.debugBlocksAndRegions(methodNode);
      this.debugBlockDecisions(methodNode);
    }
  }

  /**
   * TODO ignore specia blocks like catch with implicit throw
   */
  private void removeRegionsInCallees(MethodNode methodNode, Set<String> decision,
      Set<MethodBlock> reachables) {
    Map<MethodBlock, SootMethod> blocksToMethods = new HashMap<>();
    SootMethod sootMethod = this.getMethodNodeToSootMethod().get(methodNode);

    for (MethodBlock block : reachables) {
      blocksToMethods.put(block, sootMethod);
    }

    Set<SootMethod> analyzedCallees = new HashSet<>();
    List<MethodBlock> worklist = new ArrayList<>();
    worklist.addAll(reachables);

    while (!worklist.isEmpty()) {
      MethodBlock reach = worklist.remove(0);
      sootMethod = blocksToMethods.get(reach);
      analyzedCallees.add(sootMethod);

      for (AbstractInsnNode inst : reach.getInstructions()) {
        // Optimization
        if (inst.getOpcode() < 0) {
          continue;
        }

        // Optimization
        if (inst.getOpcode() < Opcodes.GETSTATIC || inst.getOpcode() > Opcodes.MONITOREXIT) {
          continue;
        }

        // Get caller unit
        Unit unit = this.getUnit(inst, sootMethod);

        if (unit == null) {
          continue;
        }

        List<Edge> calleeEdges = this.getCalleeEdges(unit);

        for (Edge edge : calleeEdges) {
          SootMethod calleeSootMethod = edge.tgt();

          if (analyzedCallees.contains(calleeSootMethod)) {
            continue;
          }

          if (calleeSootMethod.getDeclaringClass().getName().contains("Turn")) {
//                        this.debugBlockDecisions(ca);
            System.out.println();
          }

          List<Edge> callerEdges = this.getCallerEdges(calleeSootMethod);

//                    if(callerEdges.size() > 1) {
          boolean canRemove = this.checkIfCanRemove(decision, callerEdges);

          if (!canRemove) {
            continue;
          }
//                    }

          MethodNode calleeMethodNode = this.getSootMethodToMethodNode().get(calleeSootMethod);

          LinkedHashMap<MethodBlock, JavaRegion> calleeBlocksToRegions = this
              .getMethodsToRegionsInBlocks()
              .get(calleeMethodNode);

          if (calleeBlocksToRegions == null) {
            // TODO fix this by changing the package name
            continue;
          }

          Set<MethodBlock> skip = new HashSet<>();

          for (Map.Entry<MethodBlock, JavaRegion> entry : calleeBlocksToRegions.entrySet()) {
            if (skip.contains(entry.getKey())) {
              continue;
            }

            JavaRegion calleeRegion = entry.getValue();

            // Optimization
            if (calleeRegion == null) {
              continue;
            }

            Set<String> calleeDecision = this.getCachedDecision(calleeRegion);

            if (!(decision.equals(calleeDecision) || decision.containsAll(calleeDecision))) {
              MethodGraph calleegraph = this.getMethodGraph(calleeMethodNode);
              MethodBlock ipd = calleegraph.getImmediatePostDominator(entry.getKey());
              Set<MethodBlock> rs = calleegraph.getReachableBlocks(entry.getKey(), ipd);
              rs.remove(ipd);
              skip.addAll(rs);
              continue;
            }

            this.debugBlocksAndRegions(calleeMethodNode);
            this.debugBlockDecisions(calleeMethodNode);

            this.getRegionsToData().remove(calleeRegion);
            calleeBlocksToRegions.put(entry.getKey(), null);

            this.debugBlocksAndRegions(calleeMethodNode);
            this.debugBlockDecisions(calleeMethodNode);
          }

          for (Map.Entry<MethodBlock, JavaRegion> entry : calleeBlocksToRegions.entrySet()) {
            if (skip.contains(entry.getKey())) {
              continue;
            }

            worklist.add(0, entry.getKey());
            blocksToMethods.put(entry.getKey(), calleeSootMethod);
          }
        }
      }
    }
  }

  private boolean checkIfCanRemove(Set<String> decision, List<Edge> callerEdges) {
    Deque<Edge> worklist = new ArrayDeque<>(callerEdges);
    Set<Edge> analyzed = new HashSet<>();

    while (!worklist.isEmpty()) {
      Edge edge = worklist.pop();
      analyzed.add(edge);

      MethodBlock callerBlock = this.getCallerBlock(edge);
      SootMethod caller = edge.src();
      MethodNode method = this.getSootMethodToMethodNode().get(caller);
      LinkedHashMap<MethodBlock, JavaRegion> blockDecisions = this.cachedMethodsToBlocksDecisions
          .get(method);
      JavaRegion callerRegion = blockDecisions.get(callerBlock);
      Set<String> callerDecision = this.getCachedDecision(callerRegion);

      if (callerDecision.isEmpty()) {
        List<Edge> callers = this.getCallerEdges(caller);

        for (Edge e : callers) {
          if (analyzed.contains(e)) {
            continue;
          }

          worklist.add(e);
        }
      }
      else if (!(decision.equals(callerDecision) || decision.containsAll(callerDecision))) {
        return false;
      }
    }

    return true;
  }

  /**
   * TODO
   */
  private Unit getUnit(AbstractInsnNode inst, SootMethod sootMethod) {
    Unit match = null;

    for (Unit unit : sootMethod.getActiveBody().getUnits()) {
      List<Integer> bytecodeIndexes = new ArrayList<>();

      for (Tag tag : unit.getTags()) {
        if (tag instanceof BytecodeOffsetTag) {
          int bytecodeIndex = ((BytecodeOffsetTag) tag).getBytecodeOffset();
          bytecodeIndexes.add(bytecodeIndex);
        }
      }

      if (bytecodeIndexes.isEmpty()) {
        continue;
      }

      int bytecodeIndex;

      if (bytecodeIndexes.size() == 1) {
        bytecodeIndex = bytecodeIndexes.get(0);
      }
      else {
        int index = bytecodeIndexes.indexOf(Collections.min(bytecodeIndexes));
        bytecodeIndex = bytecodeIndexes.get(index);
      }

      AbstractInsnNode asmInst = this.getAsmBytecodeOffsetFinder()
          .getASMInstruction(this.getMethodNode(sootMethod), sootMethod, bytecodeIndex);

      if (inst != asmInst) {
        continue;
      }

      match = unit;
      break;
    }

    if (match == null && inst instanceof MethodInsnNode) {
      throw new RuntimeException("There has to be a instruction that calls a method");
    }

//        if(match == null) {
//            throw new RuntimeException("Could not find the instruction in this method");
//        }

    return match;
  }

  public MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph,
      MethodBlock start) {
    MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
    return immediatePostDominator;
  }

  public MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph,
      MethodBlock start) {
    MethodBlock id = methodGraph.getImmediateDominator(start);

    if (id != methodGraph.getEntryBlock() && id.getSuccessors().size() == 1 && id.getSuccessors()
        .contains(start)) {
      return id;
    }

    return start;
  }

  // TODO why dont we return a new list
  private void calculateASMSIndexes(List<JavaRegion> regionsInMethod, MethodNode methodNode) {
    int methodStartIndex = this.getAsmBytecodeOffsetFinder().getJavapStartIndex(methodNode);
    List<String> javapResult = this.getAsmBytecodeOffsetFinder()
        .getJavapResult(this.getMethodNodeToClassNode().get(methodNode));

    int instructionNumber = 0;
    int currentBytecodeIndex = -1;
    // 2 are the lines before the actual code in a method
    Set<JavaRegion> updatedRegions = new HashSet<>();

    for (int i = methodStartIndex; i < javapResult.size(); i++) {
      methodStartIndex++;
      String outputLine = javapResult.get(i);

      if (outputLine.contains(" Code:")) {
        break;
      }
    }

    for (int i = methodStartIndex; i < javapResult.size(); i++) {
      String outputLine = javapResult.get(i);

      if (outputLine.contains(" Code:")) {
        break;
      }

      if (!outputLine.contains(":")) {
        continue;
      }

      for (JavaRegion region : regionsInMethod) {
        if (updatedRegions.contains(region)) {
          continue;
        }

        if (!outputLine.contains(region.getStartRegionIndex() + ":")) {
          continue;
        }

        InsnList instructionsList = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructions = instructionsList.iterator();
        int instructionCounter = -1;

        while (instructions.hasNext()) {
          AbstractInsnNode instruction = instructions.next();

          if (instruction.getOpcode() >= 0) {
            instructionCounter++;
          }
          else {
            continue;
          }

          if (instructionCounter == instructionNumber) {
            region.setStartRegionIndex(instructionsList.indexOf(instruction));
            updatedRegions.add(region);
            break;
          }
        }

        if (updatedRegions.size() == regionsInMethod.size()) {
          break;
        }
      }

      String outputCommand = outputLine.substring(outputLine.indexOf(":") + 1).trim();

      if (StringUtils.isNumeric(outputCommand)) {
        continue;
      }

      int outputLineBytecodeIndex = -1;
      String outputLineBytecodeIndexString = outputLine.substring(0, outputLine.indexOf(":"))
          .trim();

      if (StringUtils.isNumeric(outputLineBytecodeIndexString)) {
        outputLineBytecodeIndex = Integer.valueOf(outputLineBytecodeIndexString);
      }

      if (outputLineBytecodeIndex > currentBytecodeIndex) {
        instructionNumber++;
        currentBytecodeIndex = outputLineBytecodeIndex;
      }

      if (updatedRegions.size() == regionsInMethod.size()) {
        break;
      }
    }

    if (updatedRegions.size() != regionsInMethod.size()) {
      throw new RuntimeException("Did not update some regions");
    }

    this.methodsWithUpdatedIndexes.add(methodNode);
  }

  private List<Edge> getCalleeEdges(SootMethod method) {
    Iterator<Edge> outEdges = this.getCallGraph().edgesOutOf(method);
    Set<SootMethod> analyzedMethods = new HashSet<>();
    List<Edge> worklist = new ArrayList<>();

    while (outEdges.hasNext()) {
      worklist.add(outEdges.next());
    }

    List<Edge> callerEdges = new ArrayList<>();

    while (!worklist.isEmpty()) {
      Edge edge = worklist.remove(0);
      SootMethod tgt = edge.tgt();
      SootMethod src = edge.src();
      analyzedMethods.add(src);

      if (!tgt.getDeclaringClass().getPackageName().contains(this.getRootPackage())) {
        Iterator<Edge> edges = this.getCallGraph().edgesOutOf(tgt);
        List<Edge> moreEdges = new ArrayList<>();

        while (edges.hasNext()) {
          Edge nextEdge = edges.next();

          if (analyzedMethods.contains(nextEdge.tgt())) {
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
   */
  private List<Edge> getCalleeEdges(Unit unit) {
    Iterator<Edge> outEdges = this.getCallGraph().edgesOutOf(unit);
    Set<SootMethod> analyzedMethods = new HashSet<>();
    List<Edge> worklist = new ArrayList<>();

    while (outEdges.hasNext()) {
      worklist.add(outEdges.next());
    }

    List<Edge> callerEdges = new ArrayList<>();

    while (!worklist.isEmpty()) {
      Edge edge = worklist.remove(0);
      SootMethod tgt = edge.tgt();
      SootMethod src = edge.src();
      analyzedMethods.add(src);

      if (!tgt.getDeclaringClass().getPackageName().contains(this.getRootPackage())) {
        Iterator<Edge> edges = this.getCallGraph().edgesOutOf(tgt);
        List<Edge> moreEdges = new ArrayList<>();

        while (edges.hasNext()) {
          Edge nextEdge = edges.next();

          if (analyzedMethods.contains(nextEdge.tgt())) {
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

  protected List<JavaRegion> getRegionsInMethod(MethodNode methodNode) {
    ClassNode classNode = this.getMethodNodeToClassNode().get(methodNode);
    List<JavaRegion> javaRegions = this.getRegionsInMethodNode(methodNode, classNode);
    javaRegions.sort(Comparator.comparingInt(JavaRegion::getStartRegionIndex));

    return javaRegions;
  }

}
