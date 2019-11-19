package edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.inter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.analysis.utils.inter.BaseInterAnalysisUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.comparator.edge.EdgeComparator;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseRemoveNestedRegionsInter<T> extends BlockRegionAnalyzer<T> {

  private static final String ENUM_ORDINAL_SIGNATURE = "ordinal()I";

  private final CallGraph callGraph;
  private final SootAsmMethodMatcher sootAsmMethodMatcher;
  private final BaseInterAnalysisUtils<T> baseInterAnalysisUtils;

  public BaseRemoveNestedRegionsInter(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData,
      SootAsmMethodMatcher sootAsmMethodMatcher,
      CallGraph callGraph,
      BaseInterAnalysisUtils<T> baseInterAnalysisUtils) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.sootAsmMethodMatcher = sootAsmMethodMatcher;
    this.callGraph = callGraph;
    this.baseInterAnalysisUtils = baseInterAnalysisUtils;
  }

  protected abstract boolean coversAll(T coveringData, @Nullable T regionData);

  @Override
  protected String getAnalysisName() {
    return "removeNestedRegionsInter";
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    T callerData = this.getData(region);
    Queue<MethodBlock> worklist = new ArrayDeque<>();
    worklist.add(block);
    Set<MethodBlock> analyzedBlocks = new HashSet<>();

    while (!worklist.isEmpty()) {
      MethodBlock methodBlock = worklist.poll();

      if (analyzedBlocks.contains(methodBlock)) {
        continue;
      }

      analyzedBlocks.add(methodBlock);
      List<AbstractInsnNode> insnList = methodBlock.getInstructions();

      for (AbstractInsnNode insnNode : insnList) {
        int opcode = insnNode.getOpcode();

        if (opcode < Opcodes.GETSTATIC
            || opcode > Opcodes.ANEWARRAY
            || opcode == Opcodes.NEWARRAY) {
          continue;
        }

        String callingPackageName;

        if (insnNode instanceof MethodInsnNode) {
          MethodInsnNode methodInsnNode = ((MethodInsnNode) insnNode);
          callingPackageName = InstrumenterUtils.getClassNodePackage(methodInsnNode.owner);
        } else if (insnNode instanceof FieldInsnNode) {
          // Calling clinit
          FieldInsnNode fieldInsnNode = ((FieldInsnNode) insnNode);
          callingPackageName = InstrumenterUtils.getClassNodePackage(fieldInsnNode.owner);
        } else if (insnNode instanceof TypeInsnNode) {
          // Calling clinit
          TypeInsnNode typeInsnNode = ((TypeInsnNode) insnNode);
          callingPackageName = InstrumenterUtils.getClassNodePackage(typeInsnNode.desc);
        } else {
          throw new RuntimeException(
              "This seems to be an invoke instruction that we needs to handle " + insnNode);
        }

        if (!this.sootAsmMethodMatcher.getApplicationPackages().contains(callingPackageName)) {
          continue;
        }

        MethodNode methodNode = this.getBlockRegionMatcher().getMethodNode(methodBlock);

        if (methodNode == null) {
          throw new RuntimeException("Could not find the method node with a specific method block");
        }

        int methodCallIndex = this.getMethodCallIndex(insnNode, methodNode);
        Unit srcUnit = this.getSrcUnit(methodNode, insnNode, methodCallIndex);

        if (srcUnit == null) {
          continue;
        }

        List<Edge> callEdges = this.getCallerEdges(srcUnit);

        for (Edge callEdge : callEdges) {
          SootMethod targetSootMethod = callEdge.tgt();

          if (!this.canRemoveNestedRegion(targetSootMethod, callerData)) {
            continue;
          }

          MethodNode targetMethodNode = this.sootAsmMethodMatcher.getMethodNode(targetSootMethod);
          Set<MethodBlock> blocksWithUncoveredData =
              this.removeCoveredNestedRegions(targetMethodNode, callerData);

          Set<MethodBlock> targetBlocks =
              new HashSet<>(
                  this.getBlockRegionMatcher()
                      .getMethodNodesToRegionsInBlocks()
                      .get(targetMethodNode)
                      .keySet());

          targetBlocks.removeAll(blocksWithUncoveredData);
          worklist.addAll(targetBlocks);
        }
      }
    }

    return new HashSet<>();
  }

  private Set<MethodBlock> removeCoveredNestedRegions(MethodNode targetMethodNode, T callerData) {
    Set<MethodBlock> blocksWithUncoveredData = new HashSet<>();
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
        this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(targetMethodNode);

    for (Map.Entry<MethodBlock, JavaRegion> entry : blocksToRegions.entrySet()) {
      MethodBlock block = entry.getKey();
      if (block.getInstructions().isEmpty()) {
        blocksWithUncoveredData.add(block);

        continue;
      }

      JavaRegion region = entry.getValue();
      T regionData = this.getData(region);

      if (!this.coversAll(callerData, regionData)) {
        blocksWithUncoveredData.add(block);

        continue;
      }

      blocksToRegions.put(entry.getKey(), null);
      this.removeRegionToData(region);
    }

    return blocksWithUncoveredData;
  }

  private boolean canRemoveNestedRegion(SootMethod targetSootMethod, T callerData) {
    if (this.noRegionsInTargetMethod(targetSootMethod)) {
      return true;
    }

    Map<SootMethod, BaseInterAnalysisUtils.DetailedCallSites> callerSootMethodsToDetailedCallSites =
        this.baseInterAnalysisUtils.getCallerSootMethodsToDetailedCallSites(targetSootMethod);

    return this.baseInterAnalysisUtils.callerDataCriteriaCoversAllCallerDataOfCallee(
        callerData, callerSootMethodsToDetailedCallSites);
  }

  private boolean noRegionsInTargetMethod(SootMethod targetSootMethod) {
    MethodNode targetMethodNode = this.sootAsmMethodMatcher.getMethodNode(targetSootMethod);
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
        this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(targetMethodNode);
    Set<JavaRegion> regions = this.getRegionsInMethod(blocksToRegions);

    return regions.size() == 1 && regions.iterator().next() == null;
  }

  private Set<JavaRegion> getRegionsInMethod(
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    Set<JavaRegion> regions = new HashSet<>();

    for (Map.Entry<MethodBlock, JavaRegion> entry : blocksToRegions.entrySet()) {
      regions.add(entry.getValue());
    }

    return regions;
  }

  @Nullable
  private Unit getSrcUnit(MethodNode methodNode, AbstractInsnNode insnNode, int methodCallIndex) {
    SootMethod sootMethod = this.sootAsmMethodMatcher.getSootMethod(methodNode);

    if (sootMethod == null) {
      throw new RuntimeException("Could not find a soot method for " + methodNode.name);
    }

    int callIndex = 0;
    List<Edge> callerEdges = this.getCallerEdges(sootMethod);

    for (Edge edge : callerEdges) {
      SootClass tgtSootClass = this.baseInterAnalysisUtils.getSootClassOfInvokeStmt(edge);
      String tgtPackageName = tgtSootClass.getPackageName();
      String tgtClassName = tgtSootClass.getShortName();
      String tgtQualifiedClassName = tgtPackageName + "." + tgtClassName;

      SootMethod tgtSootMethod = this.baseInterAnalysisUtils.getSootMethodOfInvokeStmt(edge);
      String tgtSootMethodSignature = InstrumenterUtils.getSootMethodSignature(tgtSootMethod);

      if (!this.baseInterAnalysisUtils.matchesMethodInvocation(
          insnNode, tgtQualifiedClassName, tgtSootMethodSignature, edge)) {
        continue;
      }

      if (callIndex != methodCallIndex) {
        callIndex++;

        continue;
      }

      return edge.srcUnit();
    }

    if (insnNode instanceof MethodInsnNode) {
      MethodInsnNode methodInsnNode = ((MethodInsnNode) insnNode);

      if ((methodInsnNode.name + methodInsnNode.desc).equals(ENUM_ORDINAL_SIGNATURE)) {
        return null;
      }
    }

    //    System.err.println(
    //        "Could not find the source unit to call "
    //            + insnNode.getOpcode()
    //            + " in "
    //            + methodNode.name
    //            + ". Soot might be smart to know that those method will never be called");

    return null;
  }

  private List<Edge> getCallerEdges(Unit srcUnit) {
    Iterator<Edge> callerEdgesIter = this.callGraph.edgesOutOf(srcUnit);
    List<Edge> callerEdges = new ArrayList<>();

    while (callerEdgesIter.hasNext()) {
      Edge edge = callerEdgesIter.next();
      SootMethod tgtMethod = edge.tgt();
      SootClass tgtClass = tgtMethod.getDeclaringClass();
      String tgtPackageName = tgtClass.getPackageName();

      if (!this.sootAsmMethodMatcher.getApplicationPackages().contains(tgtPackageName)) {
        continue;
      }

      callerEdges.add(edge);
    }

    return callerEdges;
  }

  private List<Edge> getCallerEdges(SootMethod sootMethod) {
    Iterator<Edge> callerEdges = this.callGraph.edgesOutOf(sootMethod);
    List<Edge> orderedCallerEdges = new ArrayList<>();

    while (callerEdges.hasNext()) {
      Edge edge = callerEdges.next();
      SootMethod tgtMethod = edge.tgt();
      SootClass tgtClass = tgtMethod.getDeclaringClass();
      String tgtPackageName = tgtClass.getPackageName();

      if (!this.sootAsmMethodMatcher.getApplicationPackages().contains(tgtPackageName)) {
        continue;
      }

      orderedCallerEdges.add(edge);
    }

    orderedCallerEdges.sort(EdgeComparator.getInstance());

    return orderedCallerEdges;
  }

  private int getMethodCallIndex(AbstractInsnNode insnNode, MethodNode methodNode) {
    int methodCallIndex = 0;
    int opcode = insnNode.getOpcode();
    ListIterator<AbstractInsnNode> insnIter = methodNode.instructions.iterator();

    while (insnIter.hasNext()) {
      AbstractInsnNode currentInsnNode = insnIter.next();

      if (currentInsnNode.getOpcode() != opcode) {
        continue;
      }

      if (currentInsnNode instanceof MethodInsnNode && insnNode instanceof MethodInsnNode) {
        MethodInsnNode methodInsnNode = ((MethodInsnNode) insnNode);
        MethodInsnNode currentMethodInsnNode = ((MethodInsnNode) currentInsnNode);

        if (!methodInsnNode.owner.equals(currentMethodInsnNode.owner)
            || !methodInsnNode.name.equals(currentMethodInsnNode.name)
            || !methodInsnNode.desc.equals(currentMethodInsnNode.desc)) {
          continue;
        }

        if (methodInsnNode.equals(currentMethodInsnNode)) {
          return methodCallIndex;
        }
      } else if (currentInsnNode instanceof FieldInsnNode && insnNode instanceof FieldInsnNode) {
        FieldInsnNode fieldInsnNode = ((FieldInsnNode) insnNode);
        FieldInsnNode currentFieldInsnNode = ((FieldInsnNode) currentInsnNode);

        if (!fieldInsnNode.owner.equals(currentFieldInsnNode.owner)) {
          continue;
        }

        if (fieldInsnNode.equals(currentFieldInsnNode)) {
          return methodCallIndex;
        }
      } else if (currentInsnNode instanceof TypeInsnNode && insnNode instanceof TypeInsnNode) {
        TypeInsnNode typeInsnNode = ((TypeInsnNode) insnNode);
        TypeInsnNode currentTypeInsnNode = ((TypeInsnNode) currentInsnNode);

        if (!typeInsnNode.desc.equals(currentTypeInsnNode.desc)) {
          continue;
        }

        if (typeInsnNode.equals(currentTypeInsnNode)) {
          return methodCallIndex;
        }

      } else {
        throw new RuntimeException(
            "This seems to be an invoke instruction that we needs to handle " + currentInsnNode);
      }

      methodCallIndex++;
    }

    throw new RuntimeException(
        "Could not find the index of the method call "
            + InstrumenterUtils.getMethodSignature(methodNode));
  }
}
