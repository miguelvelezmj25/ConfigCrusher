package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm.CFGBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class SubtracesMethodTransformer extends BaseMethodTransformer {

  private final String programName;

  private SubtracesMethodTransformer(Builder builder)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super(new DefaultClassTransformer(builder.classDir), builder.debug);
    this.programName = builder.programName;
  }

  @Override
  protected String getProgramName() {
    return this.programName;
  }

  @Override
  protected String getDebugDir() {
    return SubtracesInstrumenter.DIRECTORY;
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    Set<MethodNode> methodsToInstrument = new HashSet<>();

    for (MethodNode methodNode : classNode.methods) {
      InsnList insnList = methodNode.instructions;
      ListIterator<AbstractInsnNode> insnListIter = insnList.iterator();

      while (insnListIter.hasNext()) {
        AbstractInsnNode insnNode = insnListIter.next();

        if (insnNode instanceof JumpInsnNode) {
          methodsToInstrument.add(methodNode);
          break;
        }
      }
    }

    return methodsToInstrument;
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    String labelPrefix = classNode.name + "." + methodNode.name + methodNode.desc;
    this.instrumentCFDs(methodNode, labelPrefix);
    methodNode.visitMaxs(200, 200);
    this.instrumentIPDs(methodNode, classNode, labelPrefix);
  }

  private void instrumentIPDs(MethodNode methodNode, ClassNode classNode, String labelPrefix) {
    MethodGraph cfg = this.getCFG(methodNode, classNode);
    InsnList insnList = methodNode.instructions;
    ListIterator<AbstractInsnNode> insnListIter = insnList.iterator();
    int decisionCount = 0;
    boolean instrumentedIpdExitBlock = false;

    while (insnListIter.hasNext()) {
      AbstractInsnNode insnNode = insnListIter.next();

      if (!this.isCFD(insnNode.getOpcode())) {
        continue;
      }

      MethodBlock methodBlockWithJumpInsn = this.getMethodBlockWithJumpInsn(insnNode, cfg);
      Set<MethodBlock> succs = methodBlockWithJumpInsn.getSuccessors();

      if (succs.size() < 2) {
        throw new UnsupportedOperationException(
            "The method block with the jump instruction does not have at least 2 successors. "
                + "Possibly, the control-flow decision has an empty body");
      }

      MethodBlock exitBlock = cfg.getExitBlock();

      if (succs.contains(exitBlock)) {
        throw new UnsupportedOperationException("How can the successor be the exit node?");
      }

      MethodBlock ipd = cfg.getImmediatePostDominator(methodBlockWithJumpInsn);

      if (ipd.equals(exitBlock)) {
        if (instrumentedIpdExitBlock) {
          continue;
        }
//        throw new UnsupportedOperationException("Handle the cases where the exit block is the ipd");
        this.instrumentIPDExitNode(cfg, insnList);
        instrumentedIpdExitBlock = true;
      }
      else {
        this.instrumentNormalIPD(methodBlockWithJumpInsn, ipd, cfg, labelPrefix, decisionCount,
            insnList);
      }

      decisionCount++;
      cfg = this.getCFG(methodNode, classNode);
    }
  }

  private void instrumentNormalIPD(MethodBlock methodBlockWithJumpInsn, MethodBlock ipd,
      MethodGraph cfg, String labelPrefix, int decisionCount, InsnList insnList) {
    AbstractInsnNode ipdLabelInsn = ipd.getInstructions().get(0);
    Set<MethodBlock> reachables = cfg.getReachableBlocks(methodBlockWithJumpInsn, ipd);
    reachables.remove(ipd);

    LabelNode newIPDLabelNode = this.getLabelNode();
    InsnList newIPDLoggingInsnList = this
        .getNewIPDLoggingInsnList(newIPDLabelNode, labelPrefix, decisionCount);
    insnList.insertBefore(ipd.getInstructions().get(0), newIPDLoggingInsnList);

    for (MethodBlock reachable : reachables) {
      List<AbstractInsnNode> reachableInstructions = reachable.getInstructions();
      AbstractInsnNode reachableLastInstruction = reachableInstructions
          .get(reachableInstructions.size() - 1);

      if (!(reachableLastInstruction instanceof JumpInsnNode)) {
        continue;
      }

      if (!((JumpInsnNode) reachableLastInstruction).label.equals(ipdLabelInsn)) {
        continue;
      }

      JumpInsnNode newJumpInstruction = new JumpInsnNode(reachableLastInstruction.getOpcode(),
          newIPDLabelNode);
      insnList.insertBefore(reachableLastInstruction, newJumpInstruction);
      insnList.remove(reachableLastInstruction);
    }
  }

  private void instrumentIPDExitNode(MethodGraph cfg, InsnList insnList) {
    Set<MethodBlock> exitPreds = cfg.getExitBlock().getPredecessors();

    for (MethodBlock pred : exitPreds) {
      List<AbstractInsnNode> instructions = pred.getInstructions();
      AbstractInsnNode returnInsnNode = this.getReturnInsnNode(instructions);
      InsnList loggingInsnList = this.getIPDExitNodeLoggingInsnList();
      insnList.insertBefore(returnInsnNode, loggingInsnList);
    }
  }

  private AbstractInsnNode getReturnInsnNode(List<AbstractInsnNode> instructions) {
    for (int i = (instructions.size() - 1); i >= 0; i--) {
      AbstractInsnNode insnNode = instructions.get(i);
      int opcode = insnNode.getOpcode();

      if (opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
        return insnNode;
      }
    }

    throw new RuntimeException(
        "The predecessor of the exit node did not have a return instruction");
  }

  private boolean isCFD(int opcode) {
    return (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) || opcode == Opcodes.IFNULL
        || opcode == Opcodes.IFNONNULL;
  }

  private LabelNode getLabelNode() {
    Label label = new Label();
    return new LabelNode(label);
  }

  private InsnList getIPDExitNodeLoggingInsnList() {
    InsnList loggingInsnList = new InsnList();

    loggingInsnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
        "edu/cmu/cs/mvelezce/tool/analysis/taint/java/groundtruth/SubtracesLogging",
        "exitAtReturn", "()V", false));

    return loggingInsnList;
  }

  private InsnList getNewIPDLoggingInsnList(LabelNode labelNode, String labelPrefix,
      int decisionCount) {
    InsnList loggingInsnList = new InsnList();
    loggingInsnList.add(labelNode);
    loggingInsnList.add(new LdcInsnNode(labelPrefix));
    loggingInsnList.add(new IntInsnNode(Opcodes.BIPUSH, decisionCount));
    loggingInsnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
        "edu/cmu/cs/mvelezce/tool/analysis/taint/java/groundtruth/SubtracesLogging",
        "exitDecision", "(Ljava/lang/String;I)V", false));

    return loggingInsnList;
  }

  private MethodBlock getMethodBlockWithJumpInsn(AbstractInsnNode insnNode, MethodGraph cfg) {
    MethodBlock entryBlock = cfg.getEntryBlock();
    MethodBlock exitBlock = cfg.getExitBlock();
    Set<MethodBlock> methodBlocks = cfg.getBlocks();

    for (MethodBlock methodBlock : methodBlocks) {
      if (methodBlock.equals(entryBlock) || methodBlock.equals(exitBlock)) {
        continue;
      }

      List<AbstractInsnNode> instructions = methodBlock.getInstructions();
      AbstractInsnNode instruction = instructions.get(instructions.size() - 1);

      if (insnNode.equals(instruction)) {
        return methodBlock;
      }
    }

    throw new RuntimeException(
        "Could not fine the jump instruction as the last instruction of a method block. Possibly, "
            + "the instructions in not in the last position of the method block, which might be "
            + "that the control-flow decision does not have a body.");
  }

  private void instrumentCFDs(MethodNode methodNode, String labelPrefix) {
    InsnList insnList = methodNode.instructions;
    ListIterator<AbstractInsnNode> insnListIter = insnList.iterator();

    int decisionCount = 0;

    while (insnListIter.hasNext()) {
      AbstractInsnNode insnNode = insnListIter.next();

      if (!this.isCFD(insnNode.getOpcode())) {
        continue;
      }

      InsnList loggingInsnList = this.getCFDLoggingInsnList(labelPrefix, decisionCount);
      insnList.insertBefore(insnNode, loggingInsnList);

      decisionCount++;
    }
  }

  private InsnList getCFDLoggingInsnList(String labelPrefix, int decisionCount) {
    InsnList loggingInsnList = new InsnList();

    loggingInsnList.add(new LdcInsnNode(labelPrefix));
    loggingInsnList.add(new IntInsnNode(Opcodes.BIPUSH, decisionCount));
    loggingInsnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
        "edu/cmu/cs/mvelezce/tool/analysis/taint/java/groundtruth/SubtracesLogging",
        "enterDecision", "(Ljava/lang/String;I)V", false));

    return loggingInsnList;
  }

  private MethodGraph getCFG(MethodNode methodNode, ClassNode classNode) {
    MethodGraphBuilder cfgBuilder = new CFGBuilder(classNode.name);
    return cfgBuilder.build(methodNode);
  }

  public static class Builder {

    private final String programName;
    private final String classDir;

    private boolean debug = true;

    public Builder(String programName, String classDir) {
      this.programName = programName;
      this.classDir = classDir;
    }

    public Builder setDebug(boolean debug) {
      this.debug = debug;
      return this;
    }

    public SubtracesMethodTransformer build()
        throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
      return new SubtracesMethodTransformer(this);
    }
  }
}
