package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import org.apache.commons.io.FileUtils;

public abstract class BaseClassTransformer implements ClassTransformer {

  private final String pathToClasses;

  public BaseClassTransformer(String pathToClasses)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    this.pathToClasses = pathToClasses;
    this.addToClassPath(this.pathToClasses);
  }

  private void addToClassPath(String pathToClass)
      throws NoSuchMethodException, MalformedURLException, InvocationTargetException, IllegalAccessException {
    File file = new File(pathToClass);
    URI uri = file.toURI();

    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    Class<URLClassLoader> urlClass = URLClassLoader.class;
    Method method = urlClass.getDeclaredMethod("addURL", URL.class);
    method.setAccessible(true);
    method.invoke(urlClassLoader, uri.toURL());
  }

  @Override
  public Set<ClassNode> readClasses() throws IOException {
    Set<ClassNode> classNodes = new HashSet<>();
    String[] extensions = {"class"};

    Collection<File> files = FileUtils.listFiles(new File(this.pathToClasses), extensions, true);

    for (File file : files) {
      String fullPath = file.getPath();
      String path = fullPath.replace(".class", "");
      String relativePath = path.replace(this.pathToClasses, "");
      String qualifiedName = relativePath.replace("/", ".");

      if (qualifiedName.startsWith(".")) {
        qualifiedName = qualifiedName.substring(1);
      }

      classNodes.add(this.readClass(qualifiedName));
    }

    return classNodes;
  }

  @Override
  public ClassNode readClass(String fileName) throws IOException {
    ClassReader classReader = new ClassReader(fileName);
    ClassNode classNode = new ClassNode();
    classReader.accept(classNode, 0);

    return classNode;
  }

  @Override
  public void writeClass(ClassNode classNode, String fileName) throws IOException {
    ClassWriter classWriter = new CustomClassWriter(
        ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(0);
    classNode.accept(classWriter);

    DataOutputStream output = new DataOutputStream(
        new FileOutputStream(new File(fileName + ".class")));
    output.write(classWriter.toByteArray());
    output.flush();
    output.close();
  }

  @Override
  public String getPathToClasses() {
    return this.pathToClasses;
  }
}
