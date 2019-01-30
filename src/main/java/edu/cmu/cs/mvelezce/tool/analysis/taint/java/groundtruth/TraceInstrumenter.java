package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.Options;
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
import java.util.ListIterator;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class TraceInstrumenter extends BaseMethodTransformer {

  private final String programName;

  public TraceInstrumenter(String programName, String pathToClasses)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super(new DefaultClassTransformer(pathToClasses));

    this.programName = programName;
  }

  TraceInstrumenter(String programName, String pathToClasses, Set<String> classesToAnalyze)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super(new TraceInstrumenterClassTransformer(pathToClasses, classesToAnalyze));

    this.programName = programName;
  }

  TraceInstrumenter(String programName, String pathToClasses, Set<String> classesToAnalyze,
      boolean debug)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super(new TraceInstrumenterClassTransformer(pathToClasses, classesToAnalyze), debug);

    this.programName = programName;
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    return new HashSet<>(classNode.methods);
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    this.addTraceLogging(methodNode, classNode);
    this.updateMaxs(methodNode, classNode);

//    throw new UnsupportedOperationException(
//        "Implement adding insns for IPD by updateding the jump of the"
//            + "blocks that have a direct path from the decision to the IPD, not all predecessors.");

    this.addIPDLogging(methodNode, classNode);
    this.updateMaxs(methodNode, classNode);
//
//    this.addInsnsEndMainMethod(methodNode, classNode);
//    this.updateMaxs(methodNode, classNode);
  }

  private void addIPDLogging(MethodNode methodNode, ClassNode classNode) {
    // TODO handle IPD = exit node

    MethodGraph cfg = this.getCFG(methodNode, classNode);

    InsnList instList = methodNode.instructions;
    ListIterator<AbstractInsnNode> instListIter = instList.iterator();

    while (instListIter.hasNext()) {
      AbstractInsnNode insnNode = instListIter.next();

      if (!(insnNode instanceof JumpInsnNode)) {
        continue;
      }

      System.out.println(cfg.toDotString("dsfd"));
      Set<MethodBlock> blocks = cfg.getBlocks();

      for (MethodBlock methodBlock : blocks) {
        if (!methodBlock.getInstructions().contains(insnNode)) {
          continue;
        }

        if (methodBlock.getSuccessors().size() < 2) {
          continue;
        }

        String methodName = "popFromIdStack";
        String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);

        InsnList newInsnList = new InsnList();
        Label label = new Label();
        LabelNode labelNode = new LabelNode(label);
        newInsnList.add(labelNode);
        newInsnList.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

        MethodBlock ipd = cfg.getImmediatePostDominator(methodBlock);
        Set<MethodBlock> reachables = cfg.getReachableBlocks(methodBlock, ipd);
        reachables.remove(ipd);

        AbstractInsnNode labelInsn = ipd.getInstructions().get(0);

        for (MethodBlock reachable : reachables) {
          List<AbstractInsnNode> insnList = reachable.getInstructions();
          AbstractInsnNode lastInsn = insnList.get(insnList.size() - 1);

          if (!(lastInsn instanceof JumpInsnNode)) {
            continue;
          }

          if (!((JumpInsnNode) lastInsn).label.equals(labelInsn)) {
            continue;
          }

          AbstractInsnNode newJumpInsnNode = new JumpInsnNode(lastInsn.getOpcode(), labelNode);

          methodNode.instructions.insertBefore(lastInsn, newJumpInsnNode);
          methodNode.instructions.remove(lastInsn);
//          methodNode.instructions.insertBefore(newJumpInsnNode, new InsnNode(Opcodes.NOP));
        }

//        System.out.println(labelInsn);

        methodNode.instructions.insertBefore(labelInsn, newInsnList);

        cfg = this.getCFG(methodNode, classNode);

        break;
      }
    }

//    Set<MethodBlock> blocksWithControlFlow = new HashSet<>();
//    MethodGraph cfg = this.getCFG(methodNode, classNode);
//
//    // TODO do analysis in order
//
//    for (MethodBlock methodBlock : cfg.getBlocks()) {
//      if (methodBlock.getSuccessors().size() > 1) {
//        blocksWithControlFlow.add(methodBlock);
//      }
//    }
//
//    for (MethodBlock methodBlock : blocksWithControlFlow) {
//      MethodBlock ipd = cfg.getImmediatePostDominator(methodBlock);
//
//      if (cfg.getExitBlock().equals(ipd)) {
//////        this.addExitBlockLogging(methodNode, ipd);
//      }
//      else {
//        this.addNormalLogging(methodNode, methodBlock, ipd, cfg);
//      }
//
//      cfg = this.getCFG(methodNode, classNode);
//    }

//    InsnList insnList = methodNode.instructions;
//
////    Iterator<AbstractInsnNode> insnIter = insnList.iterator();
//
//    while (insnIter.hasNext()) {
//      AbstractInsnNode insnNode = insnIter.next();
//      int opcode = insnNode.getOpcode();
//
//      // TODO add table switch, lookup switch, ifnull, ifnonnull
//      if (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) {
//        cfg.get
//
//
//        InsnList loggingInsnList;
//
//        insnList.insertBefore(insnNode, loggingInsnList);
//      }
//    }
  }

  private AbstractInsnNode getRetInsn(List<AbstractInsnNode> insnList) {
    AbstractInsnNode possibleRetInsn = insnList.get(insnList.size() - 1);

    if (this.isReturnInsn(possibleRetInsn)) {
      return possibleRetInsn;
    }

    possibleRetInsn = insnList.get(insnList.size() - 2);

    if (this.isReturnInsn(possibleRetInsn)) {
      return possibleRetInsn;
    }

    throw new RuntimeException(
        "Could not find the return instruction in the last two instructions of the block");
  }

  private boolean isReturnInsn(AbstractInsnNode possibleRetInsn) {
    int opcode = possibleRetInsn.getOpcode();

    if (opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
      return true;
    }

    return false;
  }

  private void addExitBlockLogging(MethodNode methodNode, MethodBlock ipd) {
    Set<MethodBlock> preds = ipd.getPredecessors();

    for (MethodBlock predMethodBlock : preds) {
      List<AbstractInsnNode> insnList = predMethodBlock.getInstructions();
      AbstractInsnNode retInsn = this.getRetInsn(insnList);

      String methodName = "popFromIdStack";
      String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);

      methodNode.instructions
          .insertBefore(retInsn, this.getInsnListForMethodCall(methodName, methodDescriptor));
    }

  }

  private void addNormalLogging(MethodNode methodNode, MethodBlock methodBlock, MethodBlock ipd,
      MethodGraph cfg) {

    System.out.println(cfg.toDotString("dfsd"));

    String methodName = "popFromIdStack";
    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);

    Set<MethodBlock> reachables = cfg.getReachableBlocks(methodBlock, ipd);
    reachables.remove(ipd);

    InsnList newInsnList = new InsnList();
    Label label = new Label();
    LabelNode labelNode = new LabelNode(label);
    newInsnList.add(labelNode);
    newInsnList.add(this.getInsnListForMethodCall(methodName, methodDescriptor));

    for (MethodBlock reachable : reachables) {
      List<AbstractInsnNode> insnList = reachable.getInstructions();
      AbstractInsnNode lastInsn = insnList.get(insnList.size() - 1);

      if (!(lastInsn instanceof JumpInsnNode)) {
        continue;
      }

      ((JumpInsnNode) lastInsn).label = labelNode;
    }

    AbstractInsnNode labelInsn = ipd.getInstructions().get(0);

    System.out.println(labelInsn);

    methodNode.instructions.insertBefore(labelInsn, newInsnList);
//
//    InsnList insnList = new InsnList();
//    Label label = new Label();
//    LabelNode labelNode = new LabelNode(label);
//    insnList.add(labelNode);
//    insnList.add(new InsnNode(Opcodes.NOP));
//    insnList.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//    insnList.add(new InsnNode(Opcodes.NOP));
//
//    Set<MethodBlock> preds = ipd.getPredecessors();
//
//    for (MethodBlock predMethodBlock : preds) {
//      List<AbstractInsnNode> il = predMethodBlock.getInstructions();
//      AbstractInsnNode lastInsn = il.get(il.size() - 1);
//
//      if (!(lastInsn instanceof JumpInsnNode)) {
//        continue;
//      }
//
//      ((JumpInsnNode) lastInsn).label = labelNode;
//      System.out.println();
//    }
//
//    AbstractInsnNode labelInsn = ipd.getInstructions().get(0);
//    methodNode.instructions.insertBefore(labelInsn, insnList);
  }


  private void addTraceLogging(MethodNode methodNode, ClassNode classNode) {
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
//          case Opcodes.IFLT:
//            loggingInsnList = this
//                .getIFLTLoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IFGE:
//            loggingInsnList = this
//                .getIFGELoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IFGT:
//            throw new UnsupportedOperationException("Implement");
//          case Opcodes.IFLE:
//            loggingInsnList = this
//                .getIFLELoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IF_ICMPEQ:
//            loggingInsnList = this
//                .getICMPEQLoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IF_ICMPNE:
//            loggingInsnList = this
//                .getICMPNELoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IF_ICMPLT:
//            loggingInsnList = this
//                .getICMPLTLoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IF_ICMPGE:
//            loggingInsnList = this
//                .getICMPGELoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IF_ICMPGT:
//            loggingInsnList = this
//                .getIFICMPGTLoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IF_ICMPLE:
//            loggingInsnList = this
//                .getIFICMPLELoggingInsnList(packageName, className, methodNameAndSignature,
//                    decisionCount);
//            break;
//          case Opcodes.IF_ACMPEQ:
////            throw new UnsupportedOperationException("Implement");
//            loggingInsnList = new InsnList();
//            break;
//          case Opcodes.IF_ACMPNE:
////            throw new UnsupportedOperationException("Implement");
//            loggingInsnList = new InsnList();
//            break;
          default:
//            throw new UnsupportedOperationException("Implement opcode: " + opcode);
            continue;
        }

        insnList.insertBefore(insnNode, loggingInsnList);
      }
    }
  }

  private void addInsnsEndMainMethod(MethodNode methodNode,
      ClassNode classNode) {
    if (!methodNode.name.equals("main") || !methodNode.desc.equals("([Ljava/lang/String;)V")) {
      return;
    }

    MethodGraph cfg = this.getCFG(methodNode, classNode);
    MethodBlock exitBlock = cfg.getExitBlock();
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

//  private InsnList getIFLELoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns
//        .insert(this.getIFCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logIFLEDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }
//
//  private InsnList getIFLTLoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns
//        .insert(this.getIFCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logIFLTDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }
//
//  private InsnList getIFGELoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns
//        .insert(this.getIFCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logIFGEDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }

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

//  private InsnList getIFICMPLELoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns.insert(
//        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logIFICMPLEDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }
//
//  private InsnList getICMPEQLoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns.insert(
//        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logICMPEQDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }
//
//  private InsnList getICMPNELoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns.insert(
//        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logICMPNEDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }
//
//  private InsnList getICMPLTLoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns.insert(
//        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logICMPLTDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }
//
//  private InsnList getICMPGELoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns.insert(
//        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logICMPGEDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }
//
//  private InsnList getIFICMPGTLoggingInsnList(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList loggingInsns = new InsnList();
//    loggingInsns.insert(
//        this.getIFICMPCONDInsnListBeforeMethod(packageName, className, methodNameAndSignature,
//            decisionCount));
//
//    String methodName = "logIFICMPGTDecision";
//    String methodDescriptor = SpecificationLogger.getMethodDescriptor(methodName);
//    loggingInsns.add(this.getInsnListForMethodCall(methodName, methodDescriptor));
//
//    return loggingInsns;
//  }

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

//  private InsnList getIFICMPCONDInsnListBeforeMethod(String packageName, String className,
//      String methodNameAndSignature, int decisionCount) {
//    InsnList insnList = new InsnList();
//
//    insnList.add(new InsnNode(Opcodes.DUP2));
//    insnList.add(new LdcInsnNode(packageName + "/" + className + "." + methodNameAndSignature));
//    insnList.add(new IntInsnNode(Opcodes.SIPUSH, decisionCount));
//
//    return insnList;
//  }

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

  @Override
  protected String getProgramName() {
    return this.programName;
  }

  @Override
  protected String getDebugDir() {
    return Options.DIRECTORY + "/analysis/spec/java/programs";
  }


  private static class TraceInstrumenterClassTransformer extends BaseClassTransformer {

    TraceInstrumenterClassTransformer(String pathToClasses, Set<String> classesToTransform)
        throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
      super(pathToClasses, classesToTransform);
    }
  }
}