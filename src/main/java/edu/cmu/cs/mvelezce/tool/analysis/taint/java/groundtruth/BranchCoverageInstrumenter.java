package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.Utils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm.CFGBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.BaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.analysis.AnalyzerException;

public class BranchCoverageInstrumenter extends BaseMethodTransformer {

  public BranchCoverageInstrumenter(String pathToClasses)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super(new DefaultClassTransformer(pathToClasses));
  }

  public BranchCoverageInstrumenter(String pathToClasses, Set<String> classesToAnalyze)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super(new BranchCoverageClassTransformer(pathToClasses, classesToAnalyze));
  }

  private InsnList getLoggingInstructions(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {

    InsnList loggingInsns = new InsnList();
    loggingInsns.add(new LdcInsnNode(packageName + "/" + className + "." + methodNameAndSignature));
    loggingInsns.add(new IntInsnNode(Opcodes.SIPUSH, decisionCount));
    loggingInsns.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
        "edu/cmu/cs/mvelezce/tool/analysis/taint/java/groundtruth/BranchCoverageLogger",
        "logExecutedDecision", "(Ljava/lang/String;I)V", false));

    return loggingInsns;
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    Set<MethodNode> methodsToInstrument = new HashSet<>();

    for (MethodNode methodNode : classNode.methods) {
      InsnList insnList = methodNode.instructions;
      Iterator<AbstractInsnNode> insnIter = insnList.iterator();

      while (insnIter.hasNext()) {
        AbstractInsnNode insnNode = insnIter.next();
        int opcode = insnNode.getOpcode();

        if (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) {
          methodsToInstrument.add(methodNode);
          break;
        }
      }
    }

    return methodsToInstrument;
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    MethodGraph graph = this.getCFG(methodNode, classNode);

    String packageName = Utils.getPackageName(classNode);
    String className = Utils.getClassName(classNode);
    String methodNameAndSignature = methodNode.name + methodNode.desc;

    InsnList insnList = methodNode.instructions;
    Iterator<AbstractInsnNode> insnIter = insnList.iterator();
    int decisionCount = 0;

//    System.out.println(graph.toDotString(methodNode.name));

    while (insnIter.hasNext()) {
      AbstractInsnNode insnNode = insnIter.next();
      int opcode = insnNode.getOpcode();

      if (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) {
        decisionCount++;
        MethodBlock block = graph.getMethodBlock(insnNode);
        MethodBlock immediatePostDominator = graph.getImmediatePostDominator(block);

        for (MethodBlock succ : block.getSuccessors()) {
          if (succ.equals(immediatePostDominator)) {
            continue;
          }

          AbstractInsnNode succInsn = succ.getInstructions().get(0);
          InsnList loggingInsnList = this
              .getLoggingInstructions(packageName, className, methodNameAndSignature,
                  decisionCount);

          if(succInsn.getType() == AbstractInsnNode.LABEL) {
            insnList.insert(succInsn, loggingInsnList);
          }
          else{
            insnList.insertBefore(succInsn, loggingInsnList);
          }
        }
      }
    }
  }

  private MethodGraph getCFG(MethodNode methodNode, ClassNode classNode) {
    CFGBuilder cfgBuilder = new CFGBuilder(classNode.name, methodNode);
    MethodGraph graph;

    try {
      graph = cfgBuilder.buildCFG();
    } catch (AnalyzerException ae) {
      throw new RuntimeException(
          "Could not build a control flow graph for method :" + methodNode.name, ae);
    }

    return graph;
  }

  private static class BranchCoverageClassTransformer extends BaseClassTransformer {

    BranchCoverageClassTransformer(String pathToClasses,
        Set<String> classesToTransform)
        throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
      super(pathToClasses, classesToTransform);
    }
  }
}
