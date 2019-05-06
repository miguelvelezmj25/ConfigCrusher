package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.RegionTransformer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.Edge;

// TODO have an method expander class and a method propagator class
public abstract class DynamicRegionTransformer extends
    RegionTransformer<Set<Set<String>>, InfluencingTaints> {

  // TODO delete programName
  private DynamicRegionTransformer(String programName, String entryPoint, String rootPackage,
      ClassTransformer classTransformer,
      Map<JavaRegion, Set<Set<String>>> regionsToInfluencingTaints) {
    super(programName, entryPoint, rootPackage, classTransformer, regionsToInfluencingTaints, true,
        new DynamicInstructionRegionMatcher());
  }

  DynamicRegionTransformer(String programName, String entryPoint, String rootPackage,
      String directory, Map<JavaRegion, Set<Set<String>>> regionsToInfluencingTaints)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    this(programName, entryPoint, rootPackage, new DefaultClassTransformer(directory),
        regionsToInfluencingTaints);
  }

  @Override
  public void transformMethods(Set<ClassNode> classNodes) throws IOException {
    super.transformMethods(classNodes);

    this.setBlocksToRegions(classNodes);

    boolean updatedMethods = true;

    while (updatedMethods) {
      updatedMethods = this.expandRegionsInMethods(classNodes);
//      updatedMethods = updatedMethods | this.propagateRegionsUpClasses();
    }

    this.setStartAndEndBlocks(classNodes);
  }

  // TODO can be pushed up
  private void setStartAndEndBlocks(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

      if (methodsToInstrument.isEmpty()) {
        continue;
      }

      for (MethodNode methodToInstrument : methodsToInstrument) {
        this.setStartAndEndBlocks(methodToInstrument);
      }
    }
  }

  private void setStartAndEndBlocks(MethodNode methodNode) {
    // TODO handle special cases
//    // Special cases
//    this.ignoreRegionsWithoutConnectionToExit(methodNode);
//    this.ignoreCatchWithImplicitThrow(methodNode);

    ClassNode classNode = this.getMethodNodeToClassNode().get(methodNode);
    MethodGraph graph = this.getMethodGraph(methodNode, classNode);
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
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

      Set<MethodBlock> ends = this.setStartAndEndBlocks(graph, block, region, blocksToRegions);
      this.removeNestedRegions(graph, block, region, ends, blocksToRegions, methodNode);
    }
  }

  // TODO can pull out to super class
  private void removeNestedRegions(MethodGraph graph, MethodBlock block, JavaRegion region,
      Set<MethodBlock> ends, LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions,
      MethodNode methodNode) {
    Set<MethodBlock> reachables = new HashSet<>();

    for (MethodBlock end : ends) {
      reachables.addAll(graph.getReachableBlocks(block, end));
    }

    reachables.removeAll(ends);
    reachables.add(block);

    // If the ends are connected to the exit node, we want to analyze them since we will be removing regions called within the blocks
    Set<MethodBlock> endRegionBlocksWithReturn = this.getEndRegionBlocksWithReturn();

    for (MethodBlock end : ends) {
      if (endRegionBlocksWithReturn.contains(end)) {
        reachables.add(end);
      }
    }

    Set<Set<String>> blockDecision = this.getDecision(region);

//    this.removeRegionsInCallees(blockDecision, reachables, methodNode);
    reachables.remove(block);
    this.removeRegionsInMethod(blockDecision, reachables, blocksToRegions);
  }

  /**
   * TODO ignore specia blocks like catch with implicit throw
   */
  private void removeRegionsInCallees(InfluencingTaints decision, Set<MethodBlock> reachables,
      MethodNode methodNode) {
//    Map<MethodBlock, SootMethod> blocksToMethods = new HashMap<>();
//    SootMethod sootMethod = this.getMethodNodeToSootMethod().get(methodNode);
//
//    for (MethodBlock block : reachables) {
//      blocksToMethods.put(block, sootMethod);
//    }
//
//    Set<SootMethod> analyzedCallees = new HashSet<>();
    List<MethodBlock> worklist = new ArrayList<>();
    worklist.addAll(reachables);
    // TODO maybe do not add methods that have already been analyzed or a re already in the worklist

    while (!worklist.isEmpty()) {
      MethodBlock reach = worklist.remove(0);
//      sootMethod = blocksToMethods.get(reach);
//      analyzedCallees.add(sootMethod);

      Set<AbstractInsnNode> insnNodes = this.getInsnThatCallMethods(reach);

      if (insnNodes.isEmpty()) {
        continue;
      }

      Set<Unit> callingUnits = this.getCallingUnits(insnNodes, methodNode);

      if (callingUnits.isEmpty() || insnNodes.size() != callingUnits.size()) {
        throw new RuntimeException("Could not find all calling units");
      }

      Set<Edge> calleeEdges = this.getCalleeEdges(callingUnits);

      if (calleeEdges.isEmpty()) {
        continue;
      }

      Set<MethodBlock> modifiedMethodBlocks = this.removeNestedRegions(calleeEdges, decision);
      worklist.addAll(0, modifiedMethodBlocks);
    }
  }

  private void removeRegionsInMethod(Set<Set<String>> blockDecision, Set<MethodBlock> reachables,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {

    for (MethodBlock block : reachables) {
      JavaRegion region = blocksToRegions.get(block);

      // Optimization
      if (region == null) {
        continue;
      }

      Set<Set<String>> decision = this.getDecision(region);

      // TODO check when to remove a region based on influencing taints
      if (!decision.equals(blockDecision)) {
        continue;
      }

      blocksToRegions.put(block, null);
      this.getRegionsToData().remove(region);
    }
  }

  private Set<MethodBlock> setStartAndEndBlocks(MethodGraph graph, MethodBlock block,
      JavaRegion region, LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    region.setStartMethodBlock(block);
    Set<Set<String>> blockDecision = this.getDecision(region);

    MethodBlock ipd = graph.getImmediatePostDominator(block);
    JavaRegion ipdRegion = blocksToRegions.get(ipd);
    Set<Set<String>> ipdDecision = this.getDecision(ipdRegion);

    MethodBlock beta = graph.getExitBlock();

    // TODO check when to finish looking for the next ipd
    while (ipd != beta && blockDecision.equals(ipdDecision)) {
      MethodBlock tmp = graph.getImmediatePostDominator(ipd);

////                // Optimization
////                if(temp == beta & ipd.getSuccessors().size() == 1 && ipd.getSuccessors().iterator().next() == beta) {
////                    break;
////                }
//
      ipd = tmp;
      ipdRegion = blocksToRegions.get(ipd);
      ipdDecision = this.getDecision(ipdRegion);
    }

    MethodBlock end = ipd;
    Set<MethodBlock> ends = new HashSet<>();

    if (block == end) {
      throw new RuntimeException("Start and end equal");
    }
    else if (block.getSuccessors().size() == 1 && block.getSuccessors().iterator().next()
        .equals(end)) {
//        ends.add(block);
//
//        if (graph.getExitBlock() == end) {
//          this.endRegionBlocksWithReturn.add(block);
//        }
      throw new UnsupportedOperationException("What is this case?");
    }
    else if (beta == end) {
      Set<MethodBlock> preds = end.getPredecessors();
      ends.addAll(preds);
      this.getEndRegionBlocksWithReturn().addAll(end.getPredecessors());
    }
    else {
      ends.add(end);
    }

    region.setEndMethodBlocks(ends);

    return ends;
  }

//  private void instrument(Set<ClassNode> classNodes) throws IOException {
//
////
////    for (ClassNode classNode : classNodes) {
////      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);
////
////      if (methodsToInstrument.isEmpty()) {
////        continue;
////      }
////
//////            System.out.println("Instrumenting class " + classNode.name);
////
////      for (MethodNode methodToInstrument : methodsToInstrument) {
//////                System.out.println("Instrumenting method " + methodToInstrument.name);
////        this.transformMethod(methodToInstrument, classNode);
////      }
////
////      this.getClassTransformer().writeClass(classNode);
////
////      // Debugging
////      TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
////      MethodTracer tracer = classInspector.visitClass();
////
////      // TODO there is a bug in the pretty print since it is not showing the instructions that were added
////      for (MethodNode methodNode : methodsToInstrument) {
////        Printer printer = tracer
////            .getPrinterForMethodSignature(RegionTransformer.getMethodName(methodNode));
////        PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
////        PrettyMethodGraph prettyGraph = prettyBuilder.build(methodNode);
////        prettyGraph.saveDotFile(this.getProgramName(), classNode.name, methodNode.name);
////
////        try {
////          prettyGraph.savePdfFile(this.getProgramName(), classNode.name, methodNode.name);
////        }
////        catch (InterruptedException e) {
////          e.printStackTrace();
////        }
////      }
//    }
//  }

  private boolean propagateRegionsUpClasses() {
    boolean propagated = false;
    Set<SootMethod> methodsInWorklist = this.getApplicationSootMethods();
    List<SootMethod> worklist = new ArrayList<>(methodsInWorklist);

    while (!worklist.isEmpty()) {
      SootMethod method = worklist.remove(0);
      methodsInWorklist.remove(method);

      if (method.getSubSignature().equals(RegionTransformer.MAIN_SIGNATURE)) {
        continue;
      }

      List<JavaRegion> regionsInMethod = this.getRegionsInSootMethod(method);

      if (regionsInMethod.isEmpty()) {
        continue;
      }

      boolean canPropagateFirstRegionToAllCallers = this
          .canPropagateFirstRegionToAllCallers(method);

      if (!canPropagateFirstRegionToAllCallers) {
        continue;
      }

      List<SootMethod> methodsToAnalyze = this.propagateInfluencingTaintsToCaller(method);

      if (methodsToAnalyze.isEmpty()) {
        continue;
      }

      for (SootMethod sootMethod : methodsToAnalyze) {
        if (!methodsInWorklist.contains(sootMethod)) {
          worklist.add(0, sootMethod);
          propagated = true;
        }
      }
    }

    return propagated;
  }

  private List<SootMethod> propagateInfluencingTaintsToCaller(SootMethod method) {
//    MethodNode methodNode = this.getMethodNode(method);
//    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
//        .get(methodNode);
//
//    // TODO this check is done since there are some regions in methods that we cannot currently handle in berkeley db
//    if (blocksToRegions == null) {
//      return new ArrayList<>();
//    }
//
//    Set<SootMethod> modifiedMethods = new HashSet<>();
//    JavaRegion firstRegion = this.getFirstRegionInMethod(blocksToRegions);
//    InfluencingTaints influencingTaints = this.getDecision(firstRegion);
//
//    if (influencingTaints.getContext().isEmpty() && influencingTaints.getCondition().isEmpty()) {
//      throw new RuntimeException(
//          "The first influencingTaints in " + methodNode.name + " with regions cannot be null");
//    }
//
//    List<Edge> edges = this.getCallerEdges(method);
//
//    for (Edge edge : edges) {
//      SootMethod callerSootMethod = edge.src();
//      MethodNode callerMethodNode = this.getSootMethodToMethodNode().get(callerSootMethod);
//      LinkedHashMap<MethodBlock, JavaRegion> callerBlocksToRegions = this
//          .getMethodsToRegionsInBlocks()
//          .get(callerMethodNode);
//
//      MethodBlock callerBlock = this.getCallerBlock(edge);
//      JavaRegion callerRegion = callerBlocksToRegions.get(callerBlock);
//      InfluencingTaints callerInfluencingTaints = this.getDecision(callerRegion);
//
//      if (!shouldPropagateTaintsToCaller(influencingTaints, callerInfluencingTaints)) {
//        continue;
//      }
//
//      JavaRegion callerNewRegion = this
//          .addNewCallerRegionToMappingOfBlocksToRegions(callerBlock, callerRegion,
//              callerBlocksToRegions);
//      this.addNewCallerRegionToMappingOfRegionsToData(callerNewRegion, callerInfluencingTaints,
//          influencingTaints);
//
//      modifiedMethods.add(callerSootMethod);
//    }
//
//    return new ArrayList<>(modifiedMethods);
    throw new UnsupportedOperationException("Implement");
  }

  private JavaRegion addNewCallerRegionToMappingOfBlocksToRegions(MethodBlock callerBlock,
      JavaRegion callerRegion, LinkedHashMap<MethodBlock, JavaRegion> callerBlocksToRegions) {
    JavaRegion callerNewRegion;

    if (callerRegion == null) {
      throw new UnsupportedOperationException("Implement");
//          String classPackage = callerSootMethod.getDeclaringClass().getPackageName();
//          String className = callerSootMethod.getDeclaringClass().getShortName();
//          String methodName = callerSootMethod.getBytecodeSignature();
//          methodName = methodName.substring(methodName.indexOf(" "), methodName.length() - 1).trim();
//          index = callerMethodNode.instructions.indexOf(callerBlock.getInstructions().get(0));
//
//          newRegion = new JavaRegion.Builder(classPackage, className, methodName)
//              .startBytecodeIndex(index).build();
//          this.methodsWithUpdatedIndexes.add(callerMethodNode);
    }
    else {
      int index = callerRegion.getStartRegionIndex();

      callerNewRegion = new JavaRegion.Builder(callerRegion.getRegionPackage(),
          callerRegion.getRegionClass(), callerRegion.getRegionMethod()).startBytecodeIndex(index)
          .build();
      this.getRegionsToData().remove(callerRegion);
    }

    callerBlocksToRegions.put(callerBlock, callerNewRegion);

    return callerNewRegion;
  }

  private boolean canPropagateFirstRegionToAllCallers(SootMethod method) {
//    MethodNode methodNode = this.getMethodNode(method);
//    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
//        .get(methodNode);
//
//    // TODO this check is done since there are some regions in methods that we cannot currently handle in berkeley db
//    if (blocksToRegions == null) {
//      return false;
//    }
//
//    JavaRegion firstRegion = this.getFirstRegionInMethod(blocksToRegions);
//    InfluencingTaints influencingTaints = this.getDecision(firstRegion);
//
//    if (influencingTaints.getContext().isEmpty() && influencingTaints.getCondition().isEmpty()) {
//      throw new RuntimeException(
//          "The first influencingTaints in " + methodNode.name + " with regions cannot be null");
//    }
//
//    List<Edge> edges = this.getCallerEdges(method);
//
//    boolean canPropagate = true;
//
//    for (Edge edge : edges) {
//      SootMethod callerSootMethod = edge.src();
//      MethodNode callerMethodNode = this.getSootMethodToMethodNode().get(callerSootMethod);
//      LinkedHashMap<MethodBlock, JavaRegion> callerBlocksToRegions = this
//          .getMethodsToRegionsInBlocks()
//          .get(callerMethodNode);
//
//      MethodBlock callerBlock = this.getCallerBlock(edge);
//      JavaRegion callerRegion = callerBlocksToRegions.get(callerBlock);
//      InfluencingTaints callerInfluencingTaints = this.getDecision(callerRegion);
//
//      if (!canPropagateTaintsToCaller(influencingTaints, callerInfluencingTaints)) {
//        canPropagate = false;
//        break;
//      }
//
//    }
//
//    return canPropagate;
    throw new UnsupportedOperationException("Implement");
  }

  private boolean shouldPropagateTaintsToCaller(InfluencingTaints influencingTaints,
      InfluencingTaints callerInfluencingTaints) {
    // TODO Check when are the cases that we should propagate influence to the caller and when we should not
    if (influencingTaints.equals(callerInfluencingTaints)) {
      return false;
    }

    Set<String> thisContextTaints = influencingTaints.getContext();
    Set<String> thisConditionTaints = influencingTaints.getCondition();

    Set<String> contextTaintsCaller = callerInfluencingTaints.getContext();
    Set<String> conditionTaintsCaller = callerInfluencingTaints.getCondition();

    if (!conditionTaintsCaller.equals(thisContextTaints)) {
      return false;
    }

    return true;
    // TODO Check when are the cases that we should propagate influence to the caller and when we should not
  }

  private boolean canPropagateTaintsToCaller(InfluencingTaints influencingTaints,
      InfluencingTaints callerInfluencingTaints) {
    // TODO Check when are the cases that we can propagate influence to the caller and when we cannot
    if (influencingTaints.equals(callerInfluencingTaints)) {
      return false;
    }

    Set<String> thisContextTaints = influencingTaints.getContext();
    Set<String> thisConditionTaints = influencingTaints.getCondition();

    Set<String> contextTaintsCaller = callerInfluencingTaints.getContext();
    Set<String> conditionTaintsCaller = callerInfluencingTaints.getCondition();

    if (!(contextTaintsCaller.containsAll(thisContextTaints)
        || conditionTaintsCaller.containsAll(thisContextTaints))) {
      return false;
    }

//    if (!(contextTaintsCaller.containsAll(thisContextTaints) || conditionTaintsCaller
//        .equals(thisContextTaints))) {
//      throw new UnsupportedOperationException("Handle case");
//
////      if (!contextTaintsCaller.containsAll(thisContextTaints) && !conditionTaintsCaller
////          .containsAll(thisConditionTaints)) {
////        canPropagate = false;
////        break;
//
//// OLD //      if (!influencingTaints.containsAll(callerInfluencingTaints) && !influencingTaints
//// OLD //          .equals(callerInfluencingTaints)
//// OLD //          && !callerInfluencingTaints.containsAll(influencingTaints)) {
////                this.debugBlockDecisions(callerMethodNode);
//    }

    return true;
    // TODO Check when are the cases that we can propagate influence to the caller and when we cannot
  }

  private JavaRegion getFirstRegionInMethod(
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    Collection<JavaRegion> methodRegions = blocksToRegions.values();
    Iterator<JavaRegion> methodRegionsIter = methodRegions.iterator();
    JavaRegion firstRegion = methodRegionsIter.next();

    while (firstRegion == null) {
      firstRegion = methodRegionsIter.next();
    }

    return firstRegion;
  }

  private boolean expandRegionsInMethods(Set<ClassNode> classNodes) {
    boolean updatedMethods = false;

    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

      if (methodsToInstrument.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToInstrument) {
        boolean updatedRegions = true;

        while (updatedRegions) {
          LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this
              .getMethodsToRegionsInBlocks().get(methodNode);
          updatedRegions = this.expandUpRegionsInMethod(methodNode, classNode, blocksToRegions);
          updatedRegions = updatedRegions
              | this.expandDownRegionsInMethod(methodNode, classNode, blocksToRegions);
          updatedMethods = updatedMethods | updatedRegions;
        }

//        this.debugBlockDecisions(methodNode, classNode);
//        System.out.println();
      }
    }

    return updatedMethods;
  }

  // TODO might be able to abstract this method and most callees to region transformer
  private boolean expandDownRegionsInMethod(MethodNode methodNode, ClassNode classNode,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    boolean updatedRegions = false;
    List<MethodBlock> worklist = new ArrayList<>(blocksToRegions.keySet());

    while (!worklist.isEmpty()) {
      MethodBlock block = worklist.remove(0);

      // Optimization
      if (blocksToRegions.get(block) == null) {
        continue;
      }

      // Special case
      if (block.isCatchWithImplicitThrow()) {
//        continue;
        throw new RuntimeException("Handle case");
      }

      // Nothing blocks to propagate influence down
      if (block.isWithReturn()) {
        continue;
      }

      updatedRegions = updatedRegions
          | this.expandDownRegionInMethod(methodNode, classNode, block, blocksToRegions);
    }

    return updatedRegions;
  }

  private boolean expandDownRegionInMethod(MethodNode methodNode, ClassNode classNode,
      MethodBlock block, LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    MethodGraph graph = this.getMethodGraph(methodNode, classNode);

    // Optimization
    if (!graph.isConnectedToExit(block)) {
//      return;
      throw new RuntimeException("Why is this an optimization?");
    }

    JavaRegion blockRegion = blocksToRegions.get(block);
    Set<Set<String>> blockInfluencingTaints = this.getDecision(blockRegion);

    MethodBlock beta = graph.getExitBlock();
    MethodBlock ipd = graph.getImmediatePostDominator(block);
    JavaRegion ipdRegion = blocksToRegions.get(ipd);
    Set<Set<String>> ipdInfluencingTaints = this.getDecision(ipdRegion);

    // We can expand up to the ipd. Checking if we can continue expanding down
    while (canExpandDownToIPD(ipd, beta, blockInfluencingTaints, ipdInfluencingTaints)) {
      ipd = graph.getImmediatePostDominator(ipd);
      ipdRegion = blocksToRegions.get(ipd);
      ipdInfluencingTaints = this.getDecision(ipdRegion);
    }

    return this.expandDownInfluencingTaintsInMethod(block, blockInfluencingTaints, ipd, graph,
        blocksToRegions);
  }

  private boolean expandDownInfluencingTaintsInMethod(MethodBlock block,
      Set<Set<String>> blockInfluencingTaints, MethodBlock ipd, MethodGraph graph,
      Map<MethodBlock, JavaRegion> blocksToRegions) {
    boolean updatedRegions = false;
    Set<MethodBlock> reachables = graph.getReachableBlocks(block, ipd);
    reachables.remove(graph.getExitBlock());

    // TODO why should the reachables be ordered?
//    List<MethodBlock> orderedReachables = new ArrayList<>();
//    orderedReachables.addAll(reachables);
//
//    orderedReachables.sort((o1, o2) -> {
//      AbstractInsnNode o1Insts = o1.getInstructions().get(0);
//      AbstractInsnNode o2Insts = o2.getInstructions().get(0);
//
//      int o1Index = methodNode.instructions.indexOf(o1Insts);
//      int o2Index = methodNode.instructions.indexOf(o2Insts);
//
//      return Integer.compare(o1Index, o2Index);
//    });
//
    Set<MethodBlock> skip = new HashSet<>();
    skip.add(block);
    skip.add(ipd);

    for (MethodBlock reach : reachables) {
      if (skip.contains(reach)) {
        continue;
      }

      JavaRegion reachRegion = blocksToRegions.get(reach);
      Set<Set<String>> reachInfluencingTaints = this.getDecision(reachRegion);

//// TODO is this code really needed?
////      Set<String> intersection = new HashSet<>();
////      intersection.addAll(blockInfluencingTaints);
////      intersection.retainAll(reachInfluencingTaints);
////
////      if (!(intersection.equals(blockInfluencingTaints) || intersection.equals(reachInfluencingTaints))
////          && !blockInfluencingTaints.equals(reachInfluencingTaints)) {
////        Set<MethodBlock> skipBlocks = graph.getReachableBlocks(reach, ipd);
////        skipBlocks.remove(ipd);
////        skip.addAll(skipBlocks);
////        continue;
////      }

      if (!canExpandDownToReach(blockInfluencingTaints, reachInfluencingTaints)) {
        continue;
      }

      updatedRegions = true;

      JavaRegion reachNewRegion = this
          .addNewReachRegionToMappingOfBlocksToRegions(block, reach, reachRegion, blocksToRegions);
      this.addNewRegionToMappingOfRegionsToData(reachNewRegion, blockInfluencingTaints);
    }

    return updatedRegions;
  }

  private boolean canExpandDownToReach(Set<Set<String>> blockInfluencingTaints,
      Set<Set<String>> reachInfluencingTaints) {
    if (blockInfluencingTaints.equals(reachInfluencingTaints)) {
      return false;
    }

    if (blockInfluencingTaints.containsAll(reachInfluencingTaints)) {
      return true;
    }

    if (blockInfluencingTaints.size() > 1 || reachInfluencingTaints.size() > 1) {
      throw new UnsupportedOperationException("Implement");
    }

    return false;
  }

  // TODO might be able to abstract this method and most callees to region transformer
  private boolean expandUpRegionsInMethod(MethodNode methodNode, ClassNode classNode,
      Map<MethodBlock, JavaRegion> blocksToRegions) {
    boolean updatedRegions = false;
    List<MethodBlock> worklist = new ArrayList<>(blocksToRegions.keySet());

    while (!worklist.isEmpty()) {
      MethodBlock block = worklist.remove(0);

      // Optimization
      if (blocksToRegions.get(block) == null) {
        continue;
      }

      List<MethodBlock> updatedBlocks = this
          .expandUpRegionInMethod(methodNode, classNode, block, blocksToRegions);

      if (updatedBlocks.isEmpty()) {
        continue;
      }

      updatedRegions = true;

      // Optimization
      worklist.addAll(0, updatedBlocks);
    }

    return updatedRegions;
  }

  private List<MethodBlock> expandUpRegionInMethod(MethodNode methodNode, ClassNode classNode,
      MethodBlock block, Map<MethodBlock, JavaRegion> blocksToRegions) {
    List<MethodBlock> updatedBlocks = new ArrayList<>();
    MethodGraph graph = this.getMethodGraph(methodNode, classNode);
    MethodBlock id = graph.getImmediateDominator(block);

    if (id == null || id == graph.getEntryBlock()) {
      return updatedBlocks;
    }

    JavaRegion blockRegion = blocksToRegions.get(block);
    Set<Set<String>> blockInfluencingTaints = this.getDecision(blockRegion);

    JavaRegion idRegion = blocksToRegions.get(id);
    Set<Set<String>> idInfluencingTaints = this.getDecision(idRegion);

    if (!this.canExpandUp(blockInfluencingTaints, idInfluencingTaints)) {
      return updatedBlocks;
    }

    return this.expandUpInfluencingTaintsInMethod(block, blockInfluencingTaints, blocksToRegions);
  }

  private List<MethodBlock> expandUpInfluencingTaintsInMethod(MethodBlock block,
      Set<Set<String>> blockInfluencingTaints, Map<MethodBlock, JavaRegion> blocksToRegions) {
    List<MethodBlock> updatedBlocks = new ArrayList<>();
    Set<MethodBlock> blockPreds = block.getPredecessors();

    if (blockPreds.isEmpty()) {
      throw new RuntimeException("The predecessors cannot be empty " + block.getID());
    }

    for (MethodBlock pred : blockPreds) {
      // A block might jump to itself
      if (block == pred) {
        continue;
      }

      JavaRegion predRegion = blocksToRegions.get(pred);
      Set<Set<String>> predInfluencingTaints = this.getDecision(predRegion);

      if (!canExpandUp(blockInfluencingTaints, predInfluencingTaints)) {
        // TODO this logic is not similar to the static version. Why is the static version different?
////        if (pred.isCatchWithImplicitThrow()) {
////          continue;
////        }
////
//
//        throw new RuntimeException(
//            "Cannot push up decisions from " + block.getID() + " to " + pred.getID());
        continue;
      }

      JavaRegion predNewRegion = this
          .addNewPredRegionToMappingOfBlocksToRegions(block, pred, predRegion, blocksToRegions);
      this.addNewRegionToMappingOfRegionsToData(predNewRegion, blockInfluencingTaints);

      updatedBlocks.add(0, pred);
    }

    return updatedBlocks;
  }

  private void addNewCallerRegionToMappingOfRegionsToData(JavaRegion newCallerRegion,
      InfluencingTaints callerInfluencingTaints, InfluencingTaints blockInfluencingTaints) {
//    Set<String> newConditionTaints = new HashSet<>();
//    newConditionTaints.addAll(blockInfluencingTaints.getCondition());
//    newConditionTaints.addAll(callerInfluencingTaints.getCondition());
//
//    InfluencingTaints newInfluencingTaints = new InfluencingTaints(
//        callerInfluencingTaints.getContext(), newConditionTaints);
//    this.getRegionsToData().put(newCallerRegion, newInfluencingTaints);
    throw new UnsupportedOperationException("Implement");
  }

  private void addNewRegionToMappingOfRegionsToData(JavaRegion newRegion,
      Set<Set<String>> blockInfluencingTaints) {
    this.getRegionsToData().put(newRegion, blockInfluencingTaints);
  }

  // TODO might be able to have only one method
  private JavaRegion addNewPredRegionToMappingOfBlocksToRegions(MethodBlock block, MethodBlock pred,
      JavaRegion predRegion, Map<MethodBlock, JavaRegion> blocksToRegions) {
    int index;

    if (predRegion == null) {
      index = -1;
    }
    else {
      index = predRegion.getStartRegionIndex();
      this.getRegionsToData().remove(predRegion);
    }

    JavaRegion blockRegion = blocksToRegions.get(block);
    JavaRegion newRegion = new JavaRegion.Builder(blockRegion.getRegionPackage(),
        blockRegion.getRegionClass(), blockRegion.getRegionMethod()).startBytecodeIndex(index)
        .build();

    blocksToRegions.put(pred, newRegion);

    return newRegion;
  }

  // TODO might be able to have only one method
  private JavaRegion addNewReachRegionToMappingOfBlocksToRegions(MethodBlock block,
      MethodBlock reach, JavaRegion reachRegion, Map<MethodBlock, JavaRegion> blocksToRegions) {
    JavaRegion blockRegion = blocksToRegions.get(block);
    int index;

    if (reachRegion == null) {
//      index = blockRegion.getStartRegionIndex();
      index = -1;
    }
    else {
      index = reachRegion.getStartRegionIndex();
      this.getRegionsToData().remove(reachRegion);
    }

    JavaRegion newRegion = new JavaRegion.Builder(blockRegion.getRegionPackage(),
        blockRegion.getRegionClass(), blockRegion.getRegionMethod()).startBytecodeIndex(index)
        .build();

    blocksToRegions.put(reach, newRegion);

    return newRegion;
  }

  private boolean canExpandDownToIPD(MethodBlock ipd, MethodBlock beta,
      Set<Set<String>> blockInfluencingTaints,
      Set<Set<String>> ipdInfluencingTaints) {

    if (ipd == beta) {
      return false;
    }

    if (blockInfluencingTaints.containsAll(ipdInfluencingTaints)) {
      return true;
    }

////    Set<String> thisContextTaints = thisInfluencingTaints.getContext();
////    Set<String> ipdContextTaints = ipdInfluencingTaints.getContext();
////
////    if (!thisContextTaints.equals(ipdContextTaints)) {
////      throw new RuntimeException("The blockContextTaints do not equal the ipdContextTaints");
////    }
//
//    return thisInfluencingTaints.getCondition().containsAll(ipdInfluencingTaints.getCondition());

    if (blockInfluencingTaints.size() > 1 || ipdInfluencingTaints.size() > 1) {
      throw new UnsupportedOperationException("Implement");
    }

    return false;
  }

  private boolean canExpandUp(Set<Set<String>> blockInfluencingTaints,
      Set<Set<String>> upInfluencingTaints) {
    // If they are the same, we do want to expand up since the taints are the same.
    if (blockInfluencingTaints.equals(upInfluencingTaints)) {
      return false;
    }

//    Set<String> thisContextTaints = thisInfluencingTaints.getContext();
//    Set<String> idContextTaints = idInfluencingTaints.getContext();
//
//    if (!thisContextTaints.equals(idContextTaints)) {
//      throw new RuntimeException("The blockContextTaints do not equal the idContextTaints");
//    }

//    !(blockDecision.containsAll(idDecision) && !blockDecision.equals(idDecision))

    for (Set<String> blockInfluencingTaint : blockInfluencingTaints) {
      for (Set<String> upInfluencingTaint : upInfluencingTaints) {
        if (!blockInfluencingTaint.containsAll(upInfluencingTaint)
            || blockInfluencingTaint.equals(upInfluencingTaint)) {
          return false;
        }
      }
    }

    if (upInfluencingTaints.size() <= 1) {
      return true;
    }
    else {
      throw new UnsupportedOperationException("Imple");
    }
  }

  @Override
  protected Set<Set<String>> getDecision(JavaRegion region) {
    if (region == null) {
      return new HashSet<>();
    }

    return this.getRegionsToData().get(region);
  }

  @Override
  protected boolean canRemoveNestedRegions(InfluencingTaints decision, List<Edge> callerEdges) {
//    Deque<Edge> worklist = new ArrayDeque<>(callerEdges);
////    Set<Edge> analyzed = new HashSet<>();
//
//    while (!worklist.isEmpty()) {
//      Edge edge = worklist.pop();
////      analyzed.add(edge);
////
//      SootMethod caller = edge.src();
//      MethodNode method = this.getSootMethodToMethodNode().get(caller);
//      LinkedHashMap<MethodBlock, JavaRegion> blockDecisions = this.getMethodsToRegionsInBlocks()
//          .get(method);
//
//      MethodBlock callerBlock = this.getCallerBlock(edge);
//      JavaRegion callerRegion = blockDecisions.get(callerBlock);
//      InfluencingTaints callerDecision = this.getDecision(callerRegion);
//
//      // TODO decise when to remove nested decisions
//      if (callerDecision.getContext().isEmpty() && callerDecision.getCondition().isEmpty()) {
//        throw new UnsupportedOperationException("Handle case");
////        List<Edge> callers = this.getCallerEdges(caller);
////
////        for (Edge e : callers) {
////          if (analyzed.contains(e)) {
////            continue;
////          }
////
////          worklist.add(e);
////        }
//      }
////      else if (!(decision.equals(callerDecision) || decision.containsAll(callerDecision))) {
//      else if (!decision.equals(callerDecision)) {
//        return false;
//      }
//    }
//
//    return true;
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  protected Set<MethodBlock> removeNestedRegions(InfluencingTaints decision,
      SootMethod calleeSootMethod) {
//    Set<MethodBlock> calleeModifiedBlocks = new HashSet<>();
//    MethodNode calleeMethodNode = this.getSootMethodToMethodNode().get(calleeSootMethod);
//    LinkedHashMap<MethodBlock, JavaRegion> calleeBlocksToRegions = this
//        .getMethodsToRegionsInBlocks().get(calleeMethodNode);
//
////          if (calleeBlocksToRegions == null) {
////            // TODO fix this by changing the package name
////            continue;
////          }
////
////          Set<MethodBlock> skip = new HashSet<>();
////
//    for (Map.Entry<MethodBlock, JavaRegion> entry : calleeBlocksToRegions.entrySet()) {
////            if (skip.contains(entry.getKey())) {
////              continue;
////            }
////
//      JavaRegion calleeRegion = entry.getValue();
//
//      // Optimization
//      if (calleeRegion == null) {
//        continue;
//      }
//
//      InfluencingTaints calleeDecision = this.getDecision(calleeRegion);
//
//      // TODO decide when not to remove nested regions
//      if (!(decision.getCondition().containsAll(calleeDecision.getContext()) && decision
//          .getCondition().containsAll(calleeDecision.getCondition()))) {
//        continue;
//      }
////            if (!(decision.equals(calleeDecision) || decision.containsAll(calleeDecision))) {
////              MethodGraph calleegraph = this.getMethodGraph(calleeMethodNode);
////              MethodBlock ipd = calleegraph.getImmediatePostDominator(entry.getKey());
////              Set<MethodBlock> rs = calleegraph.getReachableBlocks(entry.getKey(), ipd);
////              rs.remove(ipd);
////              skip.addAll(rs);
////              continue;
////            }
////
//      this.getRegionsToData().remove(calleeRegion);
//      MethodBlock calleeModifiedBlock = entry.getKey();
//      calleeBlocksToRegions.put(calleeModifiedBlock, null);
//      calleeModifiedBlocks.add(calleeModifiedBlock);
//    }
////
////          for (Map.Entry<MethodBlock, JavaRegion> entry : calleeBlocksToRegions.entrySet()) {
////            if (skip.contains(entry.getKey())) {
////              continue;
////            }
////
////            worklist.add(0, entry.getKey());
////            blocksToMethods.put(entry.getKey(), calleeSootMethod);
////          }
//
//    return calleeModifiedBlocks;
    throw new UnsupportedOperationException("Implement");
  }
}
