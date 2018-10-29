package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

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
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class ControlFlowDecisionInstrumenter extends BaseMethodTransformer {

  ControlFlowDecisionInstrumenter(String pathToClasses)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    super("", new DefaultClassTransformer(pathToClasses));
  }

  private InsnList getLoggingInstructions() {
    InsnList loggingInsns = new InsnList();
    loggingInsns.add(
        new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
    loggingInsns.add(new LdcInsnNode("If statement"));
    loggingInsns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
        "(Ljava/lang/String;)V", false));

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

        if (opcode >= Opcodes.LCMP && opcode <= Opcodes.IF_ACMPNE) {
          methodsToInstrument.add(methodNode);
          break;
        }
      }
    }

    return methodsToInstrument;
  }

  @Override
  public void transformMethod(MethodNode methodNode) {
    InsnList insnList = methodNode.instructions;
    Iterator<AbstractInsnNode> insnIter = insnList.iterator();

    while (insnIter.hasNext()) {
      AbstractInsnNode insnNode = insnIter.next();
      int opcode = insnNode.getOpcode();

      if (opcode >= Opcodes.LCMP && opcode <= Opcodes.IF_ACMPNE) {
        insnList.insertBefore(insnNode, this.getLoggingInstructions());
      }
    }
  }
}
