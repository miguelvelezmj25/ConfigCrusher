package edu.cmu.cs.mvelezce.instrument.region.utils.analysis.utils.inter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.comparator.edge.EdgeComparator;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.tree.*;
import soot.*;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JNewArrayExpr;
import soot.jimple.internal.JNewExpr;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseInterAnalysisUtils<T> extends BlockRegionAnalyzer<T> {

  private final CallGraph callGraph;
  private final SootAsmMethodMatcher sootAsmMethodMatcher;

  public BaseInterAnalysisUtils(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData,
      CallGraph callGraph,
      SootAsmMethodMatcher sootAsmMethodMatcher) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.callGraph = callGraph;
    this.sootAsmMethodMatcher = sootAsmMethodMatcher;
  }

  @Override
  protected String debugFileName(String methodName) {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  protected abstract boolean canExpandDataUp(T firstRegionData, @Nullable T callerData);

  protected abstract boolean coversAll(T callerDataCriteriaToRemoveNestedData, T currentCallerData);

  public Map<SootMethod, DetailedCallSites> getCallerSootMethodsToDetailedCallSites(
      SootMethod sootMethod) {
    Map<SootMethod, DetailedCallSites> callerSootMethodsToDetailedCallSites = new HashMap<>();
    Iterator<Edge> edgesInto = this.callGraph.edgesInto(sootMethod);

    while (edgesInto.hasNext()) {
      Edge edge = edgesInto.next();
      SootMethod srcMethod = edge.src();

      if (srcMethod.equals(sootMethod)) {
        continue;
      }

      SootClass srcClass = srcMethod.getDeclaringClass();
      String packageName = srcClass.getPackageName();

      if (!this.sootAsmMethodMatcher.getApplicationPackages().contains(packageName)) {
        //        throw new RuntimeException(
        //            "Apparently, JRE methods could be callers to application methods. So, we used
        // to check the callers of those JRE methods to find application methods. Not sure if this
        // is still relevant now");
        continue;
      }

      DetailedCallSites detailedCallSites = new DetailedCallSites();
      callerSootMethodsToDetailedCallSites.putIfAbsent(srcMethod, detailedCallSites);
      detailedCallSites = callerSootMethodsToDetailedCallSites.get(srcMethod);

      int srcOpcode = this.getSrcOpcode(edge.srcUnit());
      detailedCallSites.opcodesToCallSites.putIfAbsent(srcOpcode, new HashMap<>());
      Map<SootClass, List<Edge>> callSites = detailedCallSites.opcodesToCallSites.get(srcOpcode);

      SootClass sootClassInvokeStmt = this.getSootClassOfInvokeStmt(edge);
      callSites.putIfAbsent(sootClassInvokeStmt, new ArrayList<>());
      List<Edge> edges = callSites.get(sootClassInvokeStmt);
      edges.add(edge);
    }

    for (Map.Entry<SootMethod, DetailedCallSites> entry :
        callerSootMethodsToDetailedCallSites.entrySet()) {
      Collection<Map<SootClass, List<Edge>>> callSites =
          entry.getValue().opcodesToCallSites.values();

      for (Map<SootClass, List<Edge>> callSite : callSites) {
        Collection<List<Edge>> allEdges = callSite.values();

        for (List<Edge> edges : allEdges) {
          edges.sort(EdgeComparator.getInstance());
        }
      }
    }

    return callerSootMethodsToDetailedCallSites;
  }

  public Map<MethodNode, Set<MethodBlock>> canPropagateUpToAllCallers(
      T firstRegionData, Map<SootMethod, DetailedCallSites> callerSootMethodsToDetailedCallSites) {
    Map<MethodNode, Set<MethodBlock>> methodsToBlocksToPropagate = new HashMap<>();

    for (Map.Entry<SootMethod, DetailedCallSites> entry :
        callerSootMethodsToDetailedCallSites.entrySet()) {
      SootMethod sootMethod = entry.getKey();
      MethodNode methodNode = this.sootAsmMethodMatcher.getMethodNode(sootMethod);
      LinkedHashMap<MethodBlock, JavaRegion> blocks =
          this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(methodNode);
      DetailedCallSites detailedCallSites = entry.getValue();

      for (Map.Entry<Integer, Map<SootClass, List<Edge>>> opcodesToCallSites :
          detailedCallSites.getOpcodesToCallSites().entrySet()) {
        int opcode = opcodesToCallSites.getKey();
        Map<SootClass, List<Edge>> callSites = opcodesToCallSites.getValue();

        for (Map.Entry<SootClass, List<Edge>> callSite : callSites.entrySet()) {
          SootClass tgtSootClass = callSite.getKey();
          List<Edge> edges = callSite.getValue();

          for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            AbstractInsnNode callerInsn = this.getCallerInsn(opcode, tgtSootClass, edge, i);
            MethodBlock callerBlock = this.getCallerBlock(blocks.keySet(), callerInsn);
            JavaRegion callerRegion = blocks.get(callerBlock);
            T callerData = this.getData(callerRegion);

            if (!this.canExpandDataUp(firstRegionData, callerData)) {
              methodsToBlocksToPropagate.clear();

              return methodsToBlocksToPropagate;
            }

            methodsToBlocksToPropagate.putIfAbsent(methodNode, new HashSet<>());
            Set<MethodBlock> blocksToPropagateTo = methodsToBlocksToPropagate.get(methodNode);
            blocksToPropagateTo.add(callerBlock);
          }
        }
      }
    }

    return methodsToBlocksToPropagate;
  }

  public boolean callerDataCriteriaCoversAllCallerDataOfCallee(
      T callerDataCriteriaToRemoveNestedData,
      Map<SootMethod, DetailedCallSites> callerSootMethodsToDetailedCallSites) {
    Queue<Map.Entry<SootMethod, DetailedCallSites>> worklist =
        new ArrayDeque<>(callerSootMethodsToDetailedCallSites.entrySet());
    // TODO might need to add analyzed edges or soot methods
    while (!worklist.isEmpty()) {
      Map.Entry<SootMethod, DetailedCallSites> entry = worklist.poll();
      SootMethod sootMethod = entry.getKey();
      MethodNode methodNode = this.sootAsmMethodMatcher.getMethodNode(sootMethod);
      LinkedHashMap<MethodBlock, JavaRegion> blocks =
          this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(methodNode);
      DetailedCallSites detailedCallSites = entry.getValue();

      for (Map.Entry<Integer, Map<SootClass, List<Edge>>> opcodesToCallSites :
          detailedCallSites.getOpcodesToCallSites().entrySet()) {
        int opcode = opcodesToCallSites.getKey();
        Map<SootClass, List<Edge>> callSites = opcodesToCallSites.getValue();

        for (Map.Entry<SootClass, List<Edge>> callSite : callSites.entrySet()) {
          SootClass tgtSootClass = callSite.getKey();
          List<Edge> edges = callSite.getValue();

          for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            AbstractInsnNode callerInsn = this.getCallerInsn(opcode, tgtSootClass, edge, i);
            MethodBlock callerBlock = this.getCallerBlock(blocks.keySet(), callerInsn);
            JavaRegion callerRegion = blocks.get(callerBlock);
            T currentCallerData = this.getData(callerRegion);

            if (currentCallerData == null) {
              Map<SootMethod, DetailedCallSites> sourceCallers =
                  this.getCallerSootMethodsToDetailedCallSites(edge.src());
              worklist.addAll(sourceCallers.entrySet());
            } else if (!this.coversAll(callerDataCriteriaToRemoveNestedData, currentCallerData)) {
              return false;
            }
          }
        }
      }
    }

    return true;
  }

  public SootClass getSootClassOfInvokeStmt(Edge edge) {
    Unit srcUnit = edge.srcUnit();

    if (srcUnit instanceof JInvokeStmt) {
      InvokeExpr invokeExpr = ((JInvokeStmt) srcUnit).getInvokeExpr();
      SootMethodRef tgtSootMethod = invokeExpr.getMethodRef();

      return tgtSootMethod.getDeclaringClass();
    } else if (srcUnit instanceof JAssignStmt) {
      JAssignStmt jAssignStmt = ((JAssignStmt) srcUnit);

      if (jAssignStmt.containsInvokeExpr()) {
        InvokeExpr invokeExpr = jAssignStmt.getInvokeExpr();
        SootMethodRef tgtSootMethod = invokeExpr.getMethodRef();

        return tgtSootMethod.getDeclaringClass();
      } else if (jAssignStmt.containsFieldRef()) {
        FieldRef fieldRef = jAssignStmt.getFieldRef();

        if (!(fieldRef instanceof StaticFieldRef)) {
          throw new RuntimeException(
              "There is a different invoke field ref type to handle" + jAssignStmt);
        }

        SootField sootField = fieldRef.getField();

        if (!edge.tgt().getDeclaringClass().equals(sootField.getDeclaringClass())) {
          throw new RuntimeException("The target class and the invoked class do not match");
        }

        return sootField.getDeclaringClass();
      } else {
        Value rightOp = jAssignStmt.getRightOp();

        if (rightOp instanceof JNewExpr) {
          return edge.tgt().getDeclaringClass();
        } else if (rightOp instanceof JNewArrayExpr) {
          return edge.tgt().getDeclaringClass();
        } else {
          throw new RuntimeException("Handle special case of JAssigStmt without an invoke");
        }
      }
    }

    throw new RuntimeException("Could not find the SootClass for this unit " + srcUnit);
  }

  private MethodBlock getCallerBlock(Set<MethodBlock> blocks, AbstractInsnNode callerInsn) {
    for (MethodBlock block : blocks) {
      if (block.getInstructions().contains(callerInsn)) {
        return block;
      }
    }

    throw new RuntimeException(
        "Could not find the block containing the instruction " + callerInsn.getOpcode());
  }

  private AbstractInsnNode getCallerInsn(
      int opcode, SootClass tgtSootClass, Edge edge, int invokeIndex) {
    SootMethod srcSootMethod = edge.src();
    MethodNode srcMethodNode = this.sootAsmMethodMatcher.getMethodNode(srcSootMethod);

    if (srcMethodNode == null) {
      throw new RuntimeException("Could not find a method node for " + srcSootMethod);
    }

    ListIterator<AbstractInsnNode> insnIter = srcMethodNode.instructions.iterator();

    int invokeCount = 0;
    String tgtPackageName = tgtSootClass.getPackageName();
    String tgtClassName = tgtSootClass.getShortName();
    String tgtQualifiedClassName = tgtPackageName + "." + tgtClassName;

    SootMethod tgtSootMethod = this.getSootMethodOfInvokeStmt(edge);
    String tgtSootMethodSignature = InstrumenterUtils.getSootMethodSignature(tgtSootMethod);

    while (insnIter.hasNext()) {
      AbstractInsnNode insn = insnIter.next();

      if (insn.getOpcode() != opcode) {
        continue;
      }

      if (!this.matchesMethodInvocation(
          insn, tgtQualifiedClassName, tgtSootMethodSignature, edge)) {
        continue;
      }

      if (invokeCount != invokeIndex) {
        invokeCount++;

        continue;
      }

      return insn;
    }

    throw new RuntimeException("Could not find the instruction " + edge);
  }

  public SootMethod getSootMethodOfInvokeStmt(Edge edge) {
    Unit srcUnit = edge.srcUnit();

    if (srcUnit instanceof JInvokeStmt) {
      InvokeExpr invokeExpr = ((JInvokeStmt) srcUnit).getInvokeExpr();

      return invokeExpr.getMethodRef().resolve();
    } else if (srcUnit instanceof JAssignStmt) {
      JAssignStmt jAssignStmt = ((JAssignStmt) srcUnit);

      if (jAssignStmt.containsInvokeExpr()) {
        InvokeExpr invokeExpr = jAssignStmt.getInvokeExpr();
        SootMethodRef tgtSootMethod = invokeExpr.getMethodRef();

        return tgtSootMethod.resolve();
      } else if (jAssignStmt.containsFieldRef()) {
        return edge.tgt();
      } else {
        Value rightOp = jAssignStmt.getRightOp();

        if (rightOp instanceof JNewExpr) {
          return edge.tgt();
        } else if (rightOp instanceof JNewArrayExpr) {
          return edge.tgt();
        } else {
          throw new RuntimeException(
              "Handle special case of JAssigStmt without an invoke " + srcUnit);
        }
      }
    }

    throw new RuntimeException("Count determine the SootMethod for this unit " + srcUnit);
  }

  private int getSrcOpcode(Unit srcUnit) {
    Integer opcode;

    if (srcUnit instanceof JInvokeStmt) {
      InvokeExpr invokeExpr = ((JInvokeStmt) srcUnit).getInvokeExpr();
      opcode = this.sootAsmMethodMatcher.getOpcode(invokeExpr.getClass());
    } else if (srcUnit instanceof JAssignStmt) {
      JAssignStmt jAssignStmt = ((JAssignStmt) srcUnit);

      if (jAssignStmt.containsInvokeExpr()) {
        InvokeExpr invokeExpr = jAssignStmt.getInvokeExpr();
        opcode = this.sootAsmMethodMatcher.getOpcode(invokeExpr.getClass());
      } else if (jAssignStmt.containsFieldRef()) {
        FieldRef fieldRef = jAssignStmt.getFieldRef();
        opcode =
            this.sootAsmMethodMatcher.getOpcodeStaticRef(
                fieldRef.getClass(), fieldRef.equals(jAssignStmt.getLeftOp()));
      } else {
        Value rightOp = jAssignStmt.getRightOp();

        if (rightOp instanceof JNewExpr) {
          opcode = this.sootAsmMethodMatcher.getOpcode(rightOp.getClass());
        } else if (rightOp instanceof JNewArrayExpr) {
          Type type = rightOp.getType();
          opcode =
              this.sootAsmMethodMatcher.getOpcodeArrayType(
                  rightOp.getClass(), type instanceof PrimType);
        } else {
          throw new RuntimeException("Handle special case for getting the opcode of " + srcUnit);
        }
      }
    } else {
      throw new RuntimeException(
          "This class type of src unit calls a method " + srcUnit.getClass());
    }

    if (opcode == null) {
      throw new RuntimeException("Could not find an opcode for the instruction in " + srcUnit);
    }

    return opcode;
  }

  public boolean matchesMethodInvocation(
      AbstractInsnNode invokeInsn,
      String tgtQualifiedClassName,
      String tgtMethodSignature,
      Edge edge) {

    String invokeInsnOwner;
    String invokeInsnSig;

    if (invokeInsn instanceof MethodInsnNode) {
      MethodInsnNode methodInsnNode = ((MethodInsnNode) invokeInsn);
      invokeInsnOwner = methodInsnNode.owner;
      invokeInsnSig = methodInsnNode.name + methodInsnNode.desc;
    } else if (invokeInsn instanceof FieldInsnNode) {
      // Calling clinit
      FieldInsnNode fieldInsnNode = ((FieldInsnNode) invokeInsn);
      invokeInsnOwner = fieldInsnNode.owner;
      invokeInsnSig = fieldInsnNode.name + fieldInsnNode.desc;
    } else if (invokeInsn instanceof TypeInsnNode) {
      // Calling clinit
      TypeInsnNode typeInsnNode = ((TypeInsnNode) invokeInsn);
      invokeInsnOwner = typeInsnNode.desc;
      invokeInsnSig = "";
    } else {
      throw new RuntimeException(
          "This seems to be an invoke instruction that we needs to handle " + invokeInsn);
    }

    if (!invokeInsnOwner.replace("/", ".").equals(tgtQualifiedClassName)) {
      return false;
    }

    if (edge.isClinit()) {
      return true;
    }

    return invokeInsnSig.equals(tgtMethodSignature);
  }

  public static class DetailedCallSites {
    private final Map<Integer, Map<SootClass, List<Edge>>> opcodesToCallSites = new HashMap<>();

    Map<Integer, Map<SootClass, List<Edge>>> getOpcodesToCallSites() {
      return opcodesToCallSites;
    }
  }
}
