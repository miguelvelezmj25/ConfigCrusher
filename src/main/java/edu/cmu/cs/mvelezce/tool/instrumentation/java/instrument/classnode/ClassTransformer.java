package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode;

import java.io.IOException;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

/**
 * Created by mvelezce on 4/3/17.
 */
public interface ClassTransformer {

  Set<ClassNode> readClasses() throws IOException;

  Set<ClassNode> getClassesToTransform(Set<ClassNode> classNodes);

  ClassNode readClass(String fileName) throws IOException;

  void writeClass(ClassNode classNode) throws IOException;

  String getPathToClasses();

  String getOutputDir();

}
