package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.BranchCoverageInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Utils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.analysis.AnalyzerException;
import org.junit.Test;

public class CFGBuilderTest {

  @Test
  public void RunningExample()
      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException, AnalyzerException {

    Set<ClassNode> classNodes = this.getClassNodes();
    ClassNode classNode = this.getRunningExampleClassNode(classNodes);
    MethodNode methodNode = this.getMainMethod(classNode);
    String owner = classNode.name;

    CFGBuilder cfgBuilder = new CFGBuilder(owner, methodNode);
    MethodGraph graph = cfgBuilder.buildCFG();
    System.out.println(graph.toDotString(Utils.getClassName(classNode)));
  }

  private MethodNode getMainMethod(ClassNode classNode) {
    List<MethodNode> methodNodes = classNode.methods;

    for (MethodNode methodNode : methodNodes) {
      if (methodNode.name.equals("main") && methodNode.desc.equals("([Ljava/lang/String;)V")) {
        return methodNode;
      }
    }

    throw new RuntimeException("Could not find the main method");
  }

  private ClassNode getRunningExampleClassNode(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      if (classNode.name.contains("RunningExample")) {
        return classNode;
      }
    }

    throw new RuntimeException("Could not find a class node for Running Example");
  }

  private Set<ClassNode> getClassNodes()
      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException {
    String pathToClasses = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
    MethodTransformer transformer = new BranchCoverageInstrumenter(pathToClasses);
    return transformer.getClassTransformer().readClasses();
  }
}