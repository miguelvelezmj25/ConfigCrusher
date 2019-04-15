package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.callgraph.CallGraphBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.jimple.toolkits.callgraph.CallGraph;

public abstract class RegionTransformer<T> extends BaseMethodTransformer {

  private final String programName;
  private final Map<JavaRegion, T> regionsToData;
  private final CallGraph callGraph;

  public RegionTransformer(String programName, String entryPoint, ClassTransformer classTransformer,
      Map<JavaRegion, T> regionsToData, boolean debugInstrumentation) {
    super(classTransformer, debugInstrumentation);

    this.programName = programName;
    this.regionsToData = regionsToData;

    this.callGraph = CallGraphBuilder
        .buildCallGraph(entryPoint, classTransformer.getPathToClasses());
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    Set<MethodNode> methodsToInstrument = new HashSet<>();

    if (this.getRegionsInClass(classNode, this.regionsToData.keySet()).isEmpty()) {
      return methodsToInstrument;
    }

    for (MethodNode methodNode : classNode.methods) {
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
