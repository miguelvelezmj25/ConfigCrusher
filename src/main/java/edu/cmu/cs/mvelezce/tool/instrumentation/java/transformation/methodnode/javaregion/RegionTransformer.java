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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.jimple.toolkits.callgraph.CallGraph;

public abstract class RegionTransformer<T> extends BaseMethodTransformer {

  private final String programName;
  private final String entryPoint;
  private final Map<JavaRegion, T> regionsToData;
  private final CallGraph callGraph;
  private final BlockRegionMatcher blockRegionMatcher;
  private final Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> methodsToDecisionsInBlocks = new HashMap<>();
  private final Map<MethodNode, MethodGraph> methodsToGraphs = new HashMap<>();

  public RegionTransformer(String programName, String entryPoint, ClassTransformer classTransformer,
      Map<JavaRegion, T> regionsToData, boolean debugInstrumentation,
      InstructionRegionMatcher instructionRegionMatcher) {
    super(classTransformer, debugInstrumentation);

    this.programName = programName;
    this.entryPoint = entryPoint;
    this.regionsToData = regionsToData;
    this.blockRegionMatcher = new BlockRegionMatcher(instructionRegionMatcher);

    this.callGraph = CallGraphBuilder
        .buildCallGraph(entryPoint, classTransformer.getPathToClasses());
  }

  protected abstract T getDecision(JavaRegion javaRegion);

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

      if (!this.getRegionsInMethod(methodNode, classNode).isEmpty()) {
        methodsToInstrument.add(methodNode);
      }
    }

    return methodsToInstrument;
  }

  protected List<JavaRegion> getRegionsInMethod(MethodNode methodNode, ClassNode classNode) {
    String classPackage = getClassPackage(classNode);
    String className = getClassName(classNode);
    String methodName = getMethodName(methodNode);

    return this.getRegionsWith(classPackage, className, methodName);
  }

  protected List<JavaRegion> getRegionsWith(String classPackage, String className,
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

  protected void setBlocksToDecisions(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      for (MethodNode methodNode : classNode.methods) {
        if (!this.analyzeMethod(methodNode, classNode)) {
          continue;
        }

//                System.out.println("Setting blocks to decisions in method " + methodNode.name);
        List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode, classNode);

        LinkedHashMap<MethodBlock, JavaRegion> blocksToRegionSet = new LinkedHashMap<>();

        try {
          MethodGraph graph = this.getMethodGraph(methodNode, classNode);
          blocksToRegionSet = this.blockRegionMatcher
              .matchBlocksToRegion(methodNode, graph, regionsInMethod);
        }
        catch (InvalidGraphException ignored) {
          // TODO is there a better way to implement this logic without ignoring the exception?
        }

        this.getMethodsToDecisionsInBlocks().put(methodNode, blocksToRegionSet);
      }
    }
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
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = this.getMethodsToDecisionsInBlocks()
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
    return classNode.name.equals(this.getEntryPoint());
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

  protected Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> getMethodsToDecisionsInBlocks() {
    return methodsToDecisionsInBlocks;
  }

  protected Map<MethodNode, MethodGraph> getMethodsToGraphs() {
    return methodsToGraphs;
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
}
