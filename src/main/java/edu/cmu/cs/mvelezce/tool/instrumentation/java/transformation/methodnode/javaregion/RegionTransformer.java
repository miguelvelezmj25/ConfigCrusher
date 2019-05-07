package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import com.sun.istack.internal.NotNull;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.InvalidGraphException;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm.CFGBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.callgraph.CallGraphBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InvokeDynamicInsnNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;
import soot.util.queue.QueueReader;

public abstract class RegionTransformer<T, S> extends BaseMethodTransformer {

  public static final String MAIN_SIGNATURE = "void main(java.lang.String[])";

  private final String programName;
  private final String entryPoint;
  private final Map<JavaRegion, T> regionsToData;
  private final BlockRegionMatcher blockRegionMatcher;
  private final String rootPackage;
  private final CallGraph callGraph;
  private final Set<SootMethod> applicationSootMethods;
  private final Map<MethodNode, ClassNode> methodNodeToClassNode = new HashMap<>();
  private final Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> methodsToRegionsInBlocks = new HashMap<>();
  private final Map<MethodNode, MethodGraph> methodsToGraphs = new HashMap<>();
  private final Map<SootMethod, MethodNode> sootMethodToMethodNode = new HashMap<>();
  private final Map<MethodNode, SootMethod> methodNodeToSootMethod = new HashMap<>();
  private final Set<MethodBlock> endRegionBlocksWithReturn = new HashSet<>();
  private final ASMBytecodeOffsetFinder asmBytecodeOffsetFinder;

  public RegionTransformer(String programName, String entryPoint, String rootPackage,
      ClassTransformer classTransformer, Map<JavaRegion, T> regionsToData,
      boolean debugInstrumentation, InstructionRegionMatcher instructionRegionMatcher) {
    super(classTransformer, debugInstrumentation);

    this.programName = programName;
    this.entryPoint = entryPoint;
    this.regionsToData = regionsToData;
    this.rootPackage = rootPackage;

    this.blockRegionMatcher = new BlockRegionMatcher(instructionRegionMatcher);
    this.asmBytecodeOffsetFinder = new ASMBytecodeOffsetFinder(classTransformer.getPathToClasses(),
        this.methodNodeToClassNode);
    this.callGraph = CallGraphBuilder
        .buildCallGraph(entryPoint, classTransformer.getPathToClasses());
    this.applicationSootMethods = this.calculateApplicationSootMethods();
  }

  protected abstract T getDecision(@Nullable JavaRegion javaRegion);

  protected abstract boolean canRemoveNestedRegions(S decision, List<Edge> callerEdges);

  protected abstract Set<MethodBlock> removeNestedRegions(S decision, SootMethod calleeSootMethod);

  @Override
  public void transformMethods(Set<ClassNode> classNodes) throws IOException {
    this.matchMethodNodesToClassNodes(classNodes);
    SootMethodsToMethodNodesMatcher
        .matchSootMethodsToMethodNodes(classNodes, this.applicationSootMethods,
            this.sootMethodToMethodNode, this.methodNodeToSootMethod);
  }

  private void matchMethodNodesToClassNodes(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      for (MethodNode methodNode : classNode.methods) {
        this.methodNodeToClassNode.put(methodNode, classNode);
      }
    }
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    Set<MethodNode> methodsToInstrument = new HashSet<>();

    if (this.getRegionsInClass(classNode, this.regionsToData.keySet()).isEmpty()) {
      return methodsToInstrument;
    }

    for (MethodNode methodNode : classNode.methods) {
      if (!this.analyzeMethod(methodNode, classNode)) {
        continue;
      }

      if (!this.getRegionsInMethodNode(methodNode, classNode).isEmpty()) {
        methodsToInstrument.add(methodNode);
      }
    }

    return methodsToInstrument;
  }

  protected List<JavaRegion> getRegionsInSootMethod(SootMethod sootMethod) {
    String classPackage = sootMethod.getDeclaringClass().getPackageName();
    String className = sootMethod.getDeclaringClass().getShortName();
    String methodName = sootMethod.getBytecodeSignature();
    methodName = methodName.substring(methodName.indexOf(" "), methodName.length() - 1).trim();

    List<JavaRegion> javaRegions = this.getRegionsWith(classPackage, className, methodName);
    javaRegions.sort(Comparator.comparingInt(JavaRegion::getStartRegionIndex));

    return javaRegions;
  }

  protected List<JavaRegion> getRegionsInMethodNode(MethodNode methodNode, ClassNode classNode) {
    String classPackage = getClassPackage(classNode);
    String className = getClassName(classNode);
    String methodName = getMethodName(methodNode);

    return this.getRegionsWith(classPackage, className, methodName);
  }

  private List<JavaRegion> getRegionsWith(String classPackage, String className,
      String methodName) {
    List<JavaRegion> javaRegions = new ArrayList<>();

    for (JavaRegion javaRegion : this.getRegionsToData().keySet()) {
      if (javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass()
          .equals(className)
          && javaRegion.getRegionMethod().equals(methodName)) {
        javaRegions.add(javaRegion);
      }
    }

    return javaRegions;
  }

  @Nullable
  protected MethodBlock getCallerBlock(Edge edge) {
    SootMethod callerSootMethod = edge.src();
    MethodNode callerMethodNode = this.getSootMethodToMethodNode().get(callerSootMethod);
    AbstractInsnNode instInCaller = this.asmBytecodeOffsetFinder
        .getASMInstFromCaller(edge, callerMethodNode);

    // TODO fix this hack once we can handle methods with special cases
    try {
      Set<MethodBlock> blocks = this.getMethodsToRegionsInBlocks().get(callerMethodNode).keySet();

      for (MethodBlock block : blocks) {
        if (!block.getInstructions().contains(instInCaller)) {
          continue;
        }

        return block;
      }
    }
    catch (NullPointerException npe) {
      return null;
    }

    throw new RuntimeException(
        "Could not find the instruction in the caller method blocks, however, before it seemed that a null block could be returned");
  }

  protected List<Edge> getCallerEdges(SootMethod method) {
    CallGraph callGraph = this.getCallGraph();
    Iterator<Edge> inEdges = callGraph.edgesInto(method);
    List<Edge> worklist = new ArrayList<>();

    while (inEdges.hasNext()) {
      worklist.add(inEdges.next());
    }

    List<Edge> callerEdges = new ArrayList<>();

    while (!worklist.isEmpty()) {
      Edge edge = worklist.remove(0);
      SootMethod src = edge.src();

      if (!src.method().getDeclaringClass().getPackageName().contains(this.getRootPackage())) {
        Iterator<Edge> edges = callGraph.edgesInto(src);
        List<Edge> moreEdges = new ArrayList<>();

        while (edges.hasNext()) {
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

  // TODO takes sometime to execute
  protected void setBlocksToRegions(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      for (MethodNode methodNode : classNode.methods) {
        if (!this.analyzeMethod(methodNode, classNode)) {
          continue;
        }

//                System.out.println("Setting blocks to decisions in method " + methodNode.name);
        List<JavaRegion> regionsInMethod = this.getRegionsInMethodNode(methodNode, classNode);
        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegionSet = new LinkedHashMap<>();

        try {
          MethodGraph graph = this.getMethodGraph(methodNode, classNode);
          blocksToRegionSet = this.blockRegionMatcher
              .matchBlocksToRegion(methodNode, graph, regionsInMethod);
        }
        catch (InvalidGraphException ignored) {
          // TODO is there a better way to implement this logic without ignoring the exception?
        }

        this.getMethodsToRegionsInBlocks().put(methodNode, blocksToRegionSet);
      }
    }
  }

  protected MethodNode getMethodNode(SootMethod sootMethod) {
    MethodNode methodNode = this.sootMethodToMethodNode.get(sootMethod);

    if (methodNode == null) {
      methodNode =
          this.sootMethodToMethodNode.put(sootMethod, methodNode);
      this.methodNodeToSootMethod.put(methodNode, sootMethod);
    }

    return methodNode;
  }

  protected MethodGraph getMethodGraph(MethodNode methodNode, ClassNode classNode) {
    MethodGraph graph = this.methodsToGraphs.get(methodNode);

    if (graph == null) {
      graph = CFGBuilder.getCfg(methodNode, classNode);
      this.methodsToGraphs.put(methodNode, graph);
    }

    return graph;
  }

  public static String getClassPackage(ClassNode classNode) {
    String classPackage = classNode.name;
    classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
    classPackage = classPackage.replace("/", ".");

    return classPackage;
  }

  public static String getClassName(ClassNode classNode) {
    String className = classNode.name;
    className = className.substring(className.lastIndexOf("/") + 1);

    return className;
  }

  public static String getMethodName(MethodNode methodNode) {
    return methodNode.name + methodNode.desc;
  }

  public InsnList getInstructionsStartRegion(JavaRegion javaRegion) {
    InsnList instructionsStartRegion = new InsnList();
    instructionsStartRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
    // TODO make this prettier
    instructionsStartRegion.add(
        new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions",
            "enter", "(Ljava/lang/String;)V", false));

    return instructionsStartRegion;
  }

  public InsnList getInstructionsEndRegion(JavaRegion javaRegion) {
    InsnList instructionsEndRegion = new InsnList();
    instructionsEndRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
    // TODO make this prettier
    instructionsEndRegion.add(
        new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions",
            "exit", "(Ljava/lang/String;)V", false));

    return instructionsEndRegion;
  }

  protected void debugBlockDecisions(MethodNode methodNode, ClassNode classNode) {
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToRegionsInBlocks()
        .get(methodNode);

    MethodGraph graph = this.getMethodGraph(methodNode, classNode);
    Set<MethodBlock> blocks = graph.getBlocks();

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
        T decision = this.getDecision(region);
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
  }

  // TODO maybe we could delete some classNodes that are never referenced?
  private Set<SootMethod> calculateApplicationSootMethods() {
    Set<SootMethod> methods = new HashSet<>();
    QueueReader<Edge> edges = this.getCallGraph().listener();

    while (edges.hasNext()) {
      Edge edge = edges.next();
      MethodOrMethodContext srcObject = edge.getSrc();
      SootMethod src = srcObject.method();

      if (!src.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
        continue;
      }

      methods.add(src);

      MethodOrMethodContext tgtObject = edge.getTgt();
      SootMethod tgt = tgtObject.method();

      if (!tgt.getDeclaringClass().getPackageName().contains(this.rootPackage)) {
        continue;
      }

      methods.add(tgt);
    }

    return methods;
  }

  // TODO temp method to avoid analyzing special methods
  private boolean analyzeMethod(MethodNode methodNode, ClassNode classNode) {
    if (this.isMainClass(classNode)) {
      MethodNode mainMethod = this.getMainMethod(classNode);

      if (methodNode.equals(mainMethod)) {
        return true;
      }
    }

    if (this.isSpecialBerkeleyDbMethod(methodNode, classNode)) {
      return false;
    }

    if (!methodNode.tryCatchBlocks.isEmpty()) {
      return false;
    }

    if (this.hasThrow(methodNode)) {
      return false;
    }

    return !this.hasSwitch(methodNode);
  }

  private boolean hasSwitch(MethodNode methodNode) {
    InsnList insnList = methodNode.instructions;
    ListIterator<AbstractInsnNode> insnListIter = insnList.iterator();

    while (insnListIter.hasNext()) {
      AbstractInsnNode insnNode = insnListIter.next();
      int opcode = insnNode.getOpcode();

      if (opcode == Opcodes.TABLESWITCH || opcode == Opcodes.LOOKUPSWITCH) {
        return true;
      }
    }

    return false;
  }

  private boolean hasThrow(MethodNode methodNode) {
    InsnList insnList = methodNode.instructions;
    ListIterator<AbstractInsnNode> insnListIter = insnList.iterator();

    while (insnListIter.hasNext()) {
      AbstractInsnNode insnNode = insnListIter.next();

      if (insnNode.getOpcode() == Opcodes.ATHROW) {
        return true;
      }
    }

    return false;
  }

  private boolean isSpecialBerkeleyDbMethod(@NotNull MethodNode methodNode, ClassNode classNode) {
    if (classNode.name.equals("com/sleepycat/je/tree/IN") && methodNode.name
        .equals("addToMainCache")) {
      return true;
    }

    return classNode.name.equals("com/sleepycat/je/evictor/Evictor") && methodNode.name
        .equals("getNextTarget");
  }

  private boolean isMainClass(ClassNode classNode) {
    return classNode.name.replace("/", ".").equals(this.getEntryPoint());
  }

  private MethodNode getMainMethod(ClassNode classNode) {
    for (MethodNode methodNode : classNode.methods) {
      if (methodNode.name.equals("main") && methodNode.desc.equals("([Ljava/lang/String;)V")) {
        return methodNode;
      }
    }

    throw new RuntimeException("Could not find main method in " + classNode.name);
  }

  @Override
  protected String getProgramName() {
    return this.programName;
  }

  @Override
  protected String getDebugDir() {
    throw new UnsupportedOperationException("Implement");
  }

  public Map<JavaRegion, T> getRegionsToData() {
    return regionsToData;
  }

  protected CallGraph getCallGraph() {
    return callGraph;
  }

  protected String getEntryPoint() {
    return entryPoint;
  }

  protected Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> getMethodsToRegionsInBlocks() {
    return methodsToRegionsInBlocks;
  }

  protected String getRootPackage() {
    return rootPackage;
  }

  protected Map<MethodNode, MethodGraph> getMethodsToGraphs() {
    return methodsToGraphs;
  }

  protected Map<SootMethod, MethodNode> getSootMethodToMethodNode() {
    return sootMethodToMethodNode;
  }

  protected Map<MethodNode, SootMethod> getMethodNodeToSootMethod() {
    return methodNodeToSootMethod;
  }

  protected Set<MethodBlock> getEndRegionBlocksWithReturn() {
    return endRegionBlocksWithReturn;
  }

  protected Set<SootMethod> getApplicationSootMethods() {
    return applicationSootMethods;
  }

  protected Map<MethodNode, ClassNode> getMethodNodeToClassNode() {
    return methodNodeToClassNode;
  }

  protected ASMBytecodeOffsetFinder getAsmBytecodeOffsetFinder() {
    return asmBytecodeOffsetFinder;
  }

  private List<JavaRegion> getRegionsInClass(ClassNode classNode,
      Set<JavaRegion> javaRegions) {
    String classPackage = getClassPackage(classNode);
    String className = getClassName(classNode);

    List<JavaRegion> regionsInClass = new ArrayList<>();

    for (JavaRegion javaRegion : javaRegions) {
      if (javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass()
          .equals(className)) {
        regionsInClass.add(javaRegion);
      }
    }

    return regionsInClass;
  }

  protected Set<AbstractInsnNode> getInsnThatCallMethods(MethodBlock reach) {
    Set<AbstractInsnNode> insnNodes = new HashSet<>();

    for (AbstractInsnNode inst : reach.getInstructions()) {
      // Optimization
      int opcode = inst.getOpcode();

      if (opcode < 0) {
        continue;
      }

      if (opcode < Opcodes.INVOKEVIRTUAL || opcode > Opcodes.INVOKEDYNAMIC) {
        if (inst instanceof InvokeDynamicInsnNode || inst instanceof MethodInsnNode) {
          throw new RuntimeException(
              "We want to find instructions that are calling methods. The instruction " + opcode
                  + " is of type " + inst.getClass());
        }

        continue;
      }

      insnNodes.add(inst);
    }

    return insnNodes;
  }

  protected Set<Unit> getCallingUnits(Set<AbstractInsnNode> insnNodes, MethodNode methodNode) {
    Set<Unit> callingUnits = new HashSet<>();

    for (AbstractInsnNode insnNode : insnNodes) {
      if (insnNode instanceof MethodInsnNode) {
        if (!((MethodInsnNode) insnNode).owner.replace("/", ".").contains(this.rootPackage)) {
          continue;
        }
      }
      else if (insnNode instanceof InvokeDynamicInsnNode) {
        throw new UnsupportedOperationException("Handle this case");
      }
      else {
        throw new RuntimeException(
            "Did not expect this type of node to call a method " + insnNode.getClass());
      }

      callingUnits.add(this.getUnit(insnNode, methodNode));
    }

    return callingUnits;
  }

  // TODO probably it should not be protected since we want to abstract behavior
  protected Unit getUnit(AbstractInsnNode inst, MethodNode methodNode) {
    Unit match = null;
    SootMethod sootMethod = this.getMethodNodeToSootMethod().get(methodNode);

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
          .getASMInstruction(methodNode, bytecodeIndex);

      if (inst != asmInst) {
        continue;
      }

      match = unit;
      break;
    }

//    if (match == null && inst instanceof MethodInsnNode) {
//      throw new RuntimeException("There has to be a instruction that calls a method");
//    }

    if (match == null) {
      throw new RuntimeException("There was no match");
    }

    return match;
  }

  protected List<Edge> getCalleeEdges(Set<Unit> callingUnits) {
    List<Edge> edges = new ArrayList<>();

    for (Unit unit : callingUnits) {
      edges.addAll(this.getCalleeEdges(unit));
    }

    return edges;
  }

  // TODO probably it should not be protected since we want to abstract behavior
  protected List<Edge> getCalleeEdges(Unit unit) {
    Iterator<Edge> outEdges = this.getCallGraph().edgesOutOf(unit);
    Set<SootMethod> methodsAnalyzed = new HashSet<>();
    List<Edge> worklist = new ArrayList<>();

    while (outEdges.hasNext()) {
      worklist.add(outEdges.next());
    }

    List<Edge> calleeEdges = new ArrayList<>();

    while (!worklist.isEmpty()) {
      Edge edge = worklist.remove(0);
      SootMethod tgt = edge.tgt();
      SootMethod src = edge.src();

      methodsAnalyzed.add(src);

      if (!tgt.getDeclaringClass().getPackageName().contains(this.getRootPackage())) {
        Iterator<Edge> edges = this.getCallGraph().edgesOutOf(tgt);
        List<Edge> moreEdges = new ArrayList<>();

        while (edges.hasNext()) {
          Edge nextEdge = edges.next();

          if (methodsAnalyzed.contains(nextEdge.tgt())) {
            continue;
          }

          moreEdges.add(nextEdge);
        }

        int index = Math.max(0, worklist.size() - 1);
        worklist.addAll(index, moreEdges);
      }
      else {
        calleeEdges.add(edge);
      }
    }

    return calleeEdges;
  }

  protected Set<MethodBlock> removeNestedRegions(Set<Edge> calleeEdges, S decision) {
    Set<MethodBlock> modifiedCalleeBlocks = new HashSet<>();

    for (Edge edge : calleeEdges) {
      SootMethod calleeSootMethod = edge.tgt();
//
//          if (analyzedCallees.contains(calleeSootMethod)) {
//            continue;
//          }
//

      // TODO really needed ?
//      List<Edge> callerEdges = this.getCallerEdges(calleeSootMethod);
//      boolean canRemove = this.canRemoveNestedRegions(decision, callerEdges);
//
//      if (!canRemove) {
//        continue;
//      }

      Set<MethodBlock> modifiedMethodBlocks = this.removeNestedRegions(decision, calleeSootMethod);
      modifiedCalleeBlocks.addAll(modifiedMethodBlocks);
    }

    return modifiedCalleeBlocks;
  }

  protected void instrument(Set<ClassNode> classNodes) throws IOException {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

      if (methodsToInstrument.isEmpty()) {
        continue;
      }

      for (MethodNode methodToInstrument : methodsToInstrument) {
        // TODO handle this case that is not being read by soot
        if (classNode.name.equals("com/sleepycat/je/recovery/RecoveryInfo")
            && methodToInstrument.name.equals("appendLsn")) {
          continue;
        }

        this.transformMethod(methodToInstrument, classNode);
      }

      this.getClassTransformer().writeClass(classNode);
      this.debugMethods(classNode, methodsToInstrument);
    }
  }

}
