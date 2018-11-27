package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.Utils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm.CFGBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.BaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class SpecificationInstrumenter extends BaseMethodTransformer {

  public SpecificationInstrumenter(String pathToClasses)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super(new DefaultClassTransformer(pathToClasses));
  }

  public SpecificationInstrumenter(String pathToClasses, Set<String> classesToAnalyze)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super(new BranchCoverageClassTransformer(pathToClasses, classesToAnalyze));
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    return new HashSet<>(classNode.methods);
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    this.addBranchCoverageLogging(methodNode, classNode);
    this.updateMaxs(methodNode, classNode);

    MethodGraph graph = this.getCFG(methodNode, classNode);

    this.addInsnsEndMainMethod(methodNode, graph);
    this.updateMaxs(methodNode, classNode);
  }

  private void addBranchCoverageLogging(MethodNode methodNode, ClassNode classNode) {
    String packageName = Utils.getPackageName(classNode);
    String className = Utils.getClassName(classNode);
    String methodNameAndSignature = methodNode.name + methodNode.desc;
    InsnList insnList = methodNode.instructions;

    Iterator<AbstractInsnNode> insnIter = insnList.iterator();
    int decisionCount = 0;

    while (insnIter.hasNext()) {
      AbstractInsnNode insnNode = insnIter.next();
      int opcode = insnNode.getOpcode();

      // TODO add table switch, lookup switch, ifnull, ifnonnull
      if (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) {
        decisionCount++;
        InsnList loggingInsnList;

        switch (opcode) {
          case Opcodes.IFEQ:
            loggingInsnList = this
                .getIFEQLoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IFNE:
            loggingInsnList = this
                .getIFNELoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IFLT:
            loggingInsnList = this
                .getIFLTLoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IFGE:
            loggingInsnList = this
                .getIFGELoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IFGT:
            throw new UnsupportedOperationException("Implement");
          case Opcodes.IFLE:
            loggingInsnList = this
                .getIFLELoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IF_ICMPEQ:
            loggingInsnList = this
                .getICMPEQLoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IF_ICMPNE:
            loggingInsnList = this
                .getICMPNELoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IF_ICMPLT:
            loggingInsnList = this
                .getICMPLTLoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IF_ICMPGE:
            loggingInsnList = this
                .getICMPGELoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IF_ICMPGT:
            loggingInsnList = this
                .getIFICMPGTLoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IF_ICMPLE:
            loggingInsnList = this
                .getIFICMPLELoggingInsnList(packageName, className, methodNameAndSignature,
                    decisionCount);
            break;
          case Opcodes.IF_ACMPEQ:
//            throw new UnsupportedOperationException("Implement");
            loggingInsnList = new InsnList();
            break;
          case Opcodes.IF_ACMPNE:
//            throw new UnsupportedOperationException("Implement");
            loggingInsnList = new InsnList();
            break;
          default:
            throw new UnsupportedOperationException("Implement");
        }

        insnList.insertBefore(insnNode, loggingInsnList);
      }
    }
  }

  private void addInsnsEndMainMethod(MethodNode methodNode, MethodGraph graph) {
    if (!methodNode.name.equals("main") || !methodNode.desc.equals("([Ljava/lang/String;)V")) {
      return;
    }

    MethodBlock exitBlock = graph.getExitBlock();
    Set<MethodBlock> preds = exitBlock.getPredecessors();

    for (MethodBlock pred : preds) {
      List<AbstractInsnNode> predInsns = pred.getInstructions();

      for (int i = (predInsns.size() - 1); i >= 0; i--) {
        AbstractInsnNode insn = predInsns.get(i);
        int opcode = insn.getOpcode();

        if (opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
          InsnList savingInstructions = this.getSaveInsnList();
          methodNode.instructions.insertBefore(insn, savingInstructions);

          break;
        }
      }
    }
  }

  private InsnList getIFLELoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns
        .insert(this.getIFCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logIFLEDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getIFLTLoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns
        .insert(this.getIFCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logIFLTDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getIFGELoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns
        .insert(this.getIFCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logIFGEDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getIFNELoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns
        .insert(this.getIFCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logIFNEDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getIFEQLoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns
        .insert(this.getIFCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logIFEQDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getIFICMPLELoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns.insert(
        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logIFICMPLEDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getICMPEQLoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns.insert(
        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logICMPEQDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getICMPNELoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns.insert(
        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logICMPNEDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getICMPLTLoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns.insert(
        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logICMPLTDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getICMPGELoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns.insert(
        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logICMPGEDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getIFICMPGTLoggingInsnList(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList loggingInsns = new InsnList();
    loggingInsns.insert(
        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
            decisionCount));

    String methodName = "logIFICMPGTDecision";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    return loggingInsns;
  }

  private InsnList getInsnListForMethodCall(String methodName, String methodDescriptor) {
    InsnList insnList = new InsnList();

    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, SpecificationLogger.INTERNAL_NAME,
        methodName, methodDescriptor, false));

    return insnList;
  }

  private InsnList getIFCONDInsnListBeforeMethod(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList insnList = new InsnList();

    insnList.add(new InsnNode(Opcodes.DUP));
    insnList.add(new LdcInsnNode(packageName + "/" + className + "." + methodNameAndSignature));
    insnList.add(new IntInsnNode(Opcodes.SIPUSH, decisionCount));

    return insnList;
  }

  private InsnList getIFICMPCONDInsnListBeforeMethod(String packageName, String className,
      String methodNameAndSignature, int decisionCount) {
    InsnList insnList = new InsnList();

    insnList.add(new InsnNode(Opcodes.DUP2));
    insnList.add(new LdcInsnNode(packageName + "/" + className + "." + methodNameAndSignature));
    insnList.add(new IntInsnNode(Opcodes.SIPUSH, decisionCount));

    return insnList;
  }

  private InsnList getSaveInsnList() {
    InsnList saveInsnList = new InsnList();

    String methodName = "saveExecutedDecisions";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);

    saveInsnList
        .add(new MethodInsnNode(Opcodes.INVOKESTATIC, SpecificationLogger.INTERNAL_NAME,
            methodName, methodDescriptor, false));

    return saveInsnList;
  }

  private MethodGraph getCFG(MethodNode methodNode, ClassNode classNode) {
    MethodGraphBuilder cfgBuilder = new CFGBuilder(classNode.name);
    return cfgBuilder.build(methodNode);
  }

  private static class BranchCoverageClassTransformer extends BaseClassTransformer {

    BranchCoverageClassTransformer(String pathToClasses,
        Set<String> classesToTransform)
        throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
      super(pathToClasses, classesToTransform);
    }
  }
}