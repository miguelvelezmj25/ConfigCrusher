package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class BytecodePrettyPrinter {

    private String path;
    private Map<String, TraceMethodGetter> classesToTracers = new HashMap<>();

    public BytecodePrettyPrinter(String path) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        this.path = path;
        this.addToClassPath(path);
    }

    public void addToClassPath(String pathToClass) throws NoSuchMethodException, MalformedURLException, InvocationTargetException, IllegalAccessException {
        File f = new File(pathToClass);
        URI u = f.toURI();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(urlClassLoader, u.toURL());
    }

    public void readClasses() throws IOException {
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

            this.readMethodsInClass(qualifiedName);
        }

    }

    public void readMethodsInClass(String fileName) throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(printWriter);
        TraceMethodGetter traceMethodGetter = new TraceMethodGetter(Opcodes.ASM5, traceClassVisitor);
        ClassNode classNode = this.readClass(fileName);

        this.classesToTracers.put(classNode.name, traceMethodGetter);
        ClassReader reader = new ClassReader(fileName);
        reader.accept(traceMethodGetter, 0);
    }

    public ClassNode readClass(String fileName) throws IOException {
        ClassReader classReader = new ClassReader(fileName);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        return classNode;
    }

    public Map<String, TraceMethodGetter> getClassesToTracers() {
        return this.classesToTracers;
    }
}
