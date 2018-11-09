package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import java.io.IOException;
import java.util.Set;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public abstract class BaseMethodTransformer implements MethodTransformer {

  private final ClassTransformer classTransformer;

  public BaseMethodTransformer(ClassTransformer classTransformer) {
    this.classTransformer = classTransformer;
  }

  // TODO override transform method to call the updateMaxs method

  @Override
  public void transformMethods() throws IOException {
    Set<ClassNode> classNodes = this.classTransformer.readClasses();
    classNodes = this.classTransformer.getClassesToTransform(classNodes);
    this.transformMethods(classNodes);
  }

  @Override
  public void transformMethods(Set<ClassNode> classNodes) throws IOException {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

      if (methodsToInstrument.isEmpty()) {
        continue;
      }

      System.out.println("Transforming class " + classNode.name);

      for (MethodNode methodToInstrument : methodsToInstrument) {
        this.transformMethod(methodToInstrument, classNode);
      }

      this.classTransformer.writeClass(classNode);
    }
  }

  @Override
  public ClassTransformer getClassTransformer() {
    return classTransformer;
  }

  protected void updateMaxs(MethodNode methodNode, ClassNode classNode) {
    MethodNode tmpMethodNode = this.getModifiedMethodNode(methodNode, classNode);
    methodNode.visitMaxs(tmpMethodNode.maxStack, tmpMethodNode.maxLocals);
  }

  private MethodNode getModifiedMethodNode(MethodNode methodNode, ClassNode classNode) {
    ClassWriter classWriter = this.classTransformer.getClassWriter(classNode);
    ClassNode newClassNode = this.getNewClassNode(classWriter);

    for (MethodNode method : newClassNode.methods) {
      if (method.name.equals(methodNode.name) && method.desc.equals(methodNode.desc)) {
        return method;
      }
    }

    throw new RuntimeException("Did not find the method");
  }

  private ClassNode getNewClassNode(ClassWriter classWriter) {
    ClassReader classReader = new ClassReader(classWriter.toByteArray());
    ClassNode classNode = new ClassNode();
    classReader.accept(classNode, 0);

    return classNode;
  }

}
