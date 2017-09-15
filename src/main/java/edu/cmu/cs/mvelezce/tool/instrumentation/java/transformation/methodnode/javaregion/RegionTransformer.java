package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.BytecodeUtils;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodTracer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.TraceClassInspector;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.*;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.*;
import jdk.internal.org.objectweb.asm.util.Printer;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

public abstract class RegionTransformer extends BaseMethodTransformer {

    private Set<JavaRegion> regions;
    private ClassNode currentClassNode = null;

    public RegionTransformer(String programnName, ClassTransformer classTransformer, Set<JavaRegion> regions) {
        super(programnName, classTransformer);
        this.regions = regions;
    }

    public RegionTransformer(String programName, String directory, Set<JavaRegion> regions) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        this(programName, new DefaultBaseClassTransformer(directory), regions);
    }

    public abstract InsnList getInstructionsStartRegion(JavaRegion javaRegion);

    public abstract InsnList getInstructionsEndRegion(JavaRegion javaRegion);

    @Override
    public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
        Set<MethodNode> methodsToInstrument = new HashSet<>();

        if(this.getRegionsInClass(classNode).isEmpty()) {
            return methodsToInstrument;
        }

        this.currentClassNode = classNode;

        for(MethodNode methodNode : classNode.methods) {
            if(!this.getRegionsInMethod(methodNode).isEmpty()) {
                methodsToInstrument.add(methodNode);
            }
        }

        return methodsToInstrument;
    }

    @Override
    public void transformMethods(Set<ClassNode> classNodes) throws IOException {
        for(ClassNode classNode : classNodes) {
            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

            if(methodsToInstrument.isEmpty()) {
                continue;
            }

            System.out.println("Transforming class " + classNode.name);

            for(MethodNode methodToInstrument : methodsToInstrument) {
                this.transformMethod(methodToInstrument);
            }

            this.getClassTransformer().writeClass(classNode, this.getClassTransformer().getPath() + "/" + classNode.name);

            // TODO if debug
            TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
            MethodTracer tracer = classInspector.visitClass();

            for(MethodNode methodNode : methodsToInstrument) {
                Printer printer = tracer.getPrinterForMethodSignature(methodNode.name + methodNode.desc);
                PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
                PrettyMethodGraph prettyGraph = prettyBuilder.build();
                prettyGraph.saveDotFile(this.getProgramName(), classNode.name, methodNode.name);

                try {
                    prettyGraph.savePdfFile(this.getProgramName(), classNode.name, methodNode.name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected List<JavaRegion> getRegionsInMethod(MethodNode methodNode) {
        String classPackage = this.currentClassNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = this.currentClassNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        String methodName = methodNode.name + methodNode.desc;

        List<JavaRegion> javaRegions = new ArrayList<>();

        for(JavaRegion javaRegion : this.regions) {
            if(javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass().equals(className)
                    && javaRegion.getRegionMethod().equals(methodName)) {
                javaRegions.add(javaRegion);
            }
        }

        javaRegions.sort(Comparator.comparingInt(JavaRegion::getStartBytecodeIndex));

        return javaRegions;
    }

    private List<JavaRegion> getRegionsInClass(ClassNode classNode) {
        String classPackage = classNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = classNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        List<JavaRegion> regionsInClass = new ArrayList<>();

        for(JavaRegion javaRegion : this.regions) {
            if(javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass().equals(className)) {
                regionsInClass.add(javaRegion);
            }
        }

        return regionsInClass;
    }

    public ClassNode getCurrentClassNode() {
        return currentClassNode;
    }
}
