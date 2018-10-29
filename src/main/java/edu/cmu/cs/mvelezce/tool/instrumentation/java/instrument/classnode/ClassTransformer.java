package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

/**
 * Created by mvelezce on 4/3/17.
 */
public interface ClassTransformer {

  String getPathToClasses();

  String getOutputDir();

  Set<ClassNode> readClasses() throws IOException;

  ClassNode readClass(String fileName) throws IOException;

  void writeClass(ClassNode classNode) throws IOException;

}
