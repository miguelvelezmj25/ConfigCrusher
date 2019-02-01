package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
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
    this.instrumentCFD(methodNode, classNode);
  }

  private void instrumentCFD(MethodNode methodNode, ClassNode classNode) {
    InsnList insnList = methodNode.instructions;
    ListIterator<AbstractInsnNode> insnListIter = insnList.iterator();
    String labelPrefix = classNode.name + "." + methodNode.name + methodNode.desc;
    int decisionCount = 0;

    while (insnListIter.hasNext()) {
      AbstractInsnNode insnNode = insnListIter.next();

      if (!(insnNode instanceof JumpInsnNode)) {
        continue;
      }

      InsnList loggingInsnList = this.getLoggingInsnsList(labelPrefix, decisionCount);
      insnList.insertBefore(insnNode, loggingInsnList);

      decisionCount++;
    }

    System.out.println();
  }

  private InsnList getLoggingInsnsList(String labelPrefix, int decisionCount) {
    InsnList loggingInsnList = new InsnList();

    loggingInsnList.add(
        new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
    loggingInsnList.add(new LdcInsnNode(labelPrefix + "." + decisionCount));
    loggingInsnList.add(
        new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
            "(Ljava/lang/String;)V",
            false));

    return loggingInsnList;
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
