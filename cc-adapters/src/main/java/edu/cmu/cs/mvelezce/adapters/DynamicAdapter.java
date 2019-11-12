package edu.cmu.cs.mvelezce.adapters;

/** Created by mvelezce on 4/26/17. */
public abstract class DynamicAdapter extends ClassLoader {
  //  public static final String MAIN = "main";
  //
  //  protected static Set<ClassNode> instrumentedClassNodes = new HashSet<>();
  //
  //  public DynamicAdapter() {}
  //
  //  public static Set<ClassNode> getInstrumentedClassNodes() {
  //    return DynamicAdapter.instrumentedClassNodes;
  //  }
  //
  //  public static void setInstrumentedClassNodes(Set<ClassNode> instrumentedClassNodes) {
  //    DynamicAdapter.instrumentedClassNodes = instrumentedClassNodes;
  //  }
  //
  //  public abstract void execute(Set<String> configuration)
  //      throws ClassNotFoundException, NoSuchMethodException;
  //
  //  @Override
  //  public Class<?> loadClass(String name) throws ClassNotFoundException {
  //    for (ClassNode classNode : instrumentedClassNodes) {
  //      if (classNode.name.replace("/", ".").equals(name)) {
  //        return this.findClass(name);
  //      }
  //    }
  //
  //    return super.loadClass(name);
  //  }
  //
  //  @Override
  //  protected Class<?> findClass(String name) throws ClassNotFoundException {
  //    for (ClassNode classNode : instrumentedClassNodes) {
  //      if (classNode.name.replace("/", ".").equals(name)) {
  //        ClassWriter classWriter =
  //            new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
  //        classNode.accept(classWriter);
  //
  //        byte[] bytes = classWriter.toByteArray();
  //        return defineClass(name, bytes, 0, bytes.length);
  //      }
  //    }
  //
  //    throw new RuntimeException("The class node was not found for this name");
  //  }
}
