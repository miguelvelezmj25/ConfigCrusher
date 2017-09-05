package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import org.apache.commons.io.FileUtils;

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

public abstract class BaseClassTransformer implements ClassTransformer {

    private String path;

    public BaseClassTransformer(String path) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        this.path = path;
        this.addToClassPath(path);
    }

    @Override
    public void addToClassPath(String pathToClass) throws NoSuchMethodException, MalformedURLException, InvocationTargetException, IllegalAccessException {
        File f = new File(pathToClass);
        URI u = f.toURI();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(urlClassLoader, u.toURL());

//        ClassLoader cl = ClassLoader.getSystemClassLoader();
//
//        URL[] urls = ((URLClassLoader)cl).getURLs();
//
//        for(URL url: urls){
//            System.out.println(url.getFile());
//        }
    }

    @Override
    public Set<ClassNode> readClasses() throws IOException {
        Set<ClassNode> classNodes = new HashSet<>();
        String[] extensions = {"class"};

        Collection<File> files = FileUtils.listFiles(new File(this.path), extensions, true);

        for(File file : files) {
            String fullPath = file.getPath();
            String path = fullPath.replace(".class", "");
            String relativePath = path.replace(this.path, "");
            String qualifiedName = relativePath.replace("/", ".");

            if(qualifiedName.startsWith(".")) {
                qualifiedName = qualifiedName.substring(1);
            }

            classNodes.add(this.readClass(qualifiedName));
        }

        return classNodes;
    }

    @Override
    public ClassNode readClass(String fileName) throws IOException {
//        System.out.println(fileName);
        ClassReader classReader = new ClassReader(fileName);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        return classNode;
    }

    @Override
    public void writeClass(ClassNode classNode, String fileName) throws IOException {
        ClassWriter classWriter = new CustomClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);

        DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(fileName + ".class")));
        output.write(classWriter.toByteArray());
        output.flush();
        output.close();
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
