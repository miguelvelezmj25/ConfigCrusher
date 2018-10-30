package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import java.io.IOException;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public abstract class BaseMethodTransformer implements MethodTransformer {

  private final ClassTransformer classTransformer;

  public BaseMethodTransformer(ClassTransformer classTransformer) {
    this.classTransformer = classTransformer;
  }

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
}
