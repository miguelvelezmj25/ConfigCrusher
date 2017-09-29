package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.soot.config.SootConfig;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import soot.util.queue.QueueReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;

public abstract class RegionTransformer extends BaseMethodTransformer {

    private Map<JavaRegion, Set<Set<String>>> regionsToOptionSet;
    private ClassNode currentClassNode = null;

    public RegionTransformer(String programName, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) {
        super(programName, classTransformer);
        this.regionsToOptionSet = regionsToOptionSet;
    }

    public RegionTransformer(String programName, String directory, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        this(programName, new DefaultBaseClassTransformer(directory), regionsToOptionSet);
    }

    // TODO should this be abstract?
    public abstract InsnList getInstructionsStartRegion(JavaRegion javaRegion);

    // TODO should this be abstract?
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
//        for(ClassNode classNode : classNodes) {
//            Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);
//
//            if(methodsToInstrument.isEmpty()) {
//                continue;
//            }
//
//            System.out.println("Transforming class " + classNode.name);
//
//            for(MethodNode methodToInstrument : methodsToInstrument) {
//                this.transformMethod(methodToInstrument);
//            }
//
//            this.getClassTransformer().writeClass(classNode, this.getClassTransformer().getPath() + "/" + classNode.name);
//
//            // TODO if debug
//            TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
//            MethodTracer tracer = classInspector.visitClass();
//
//            for(MethodNode methodNode : methodsToInstrument) {
//                Printer printer = tracer.getPrinterForMethodSignature(methodNode.name + methodNode.desc);
//                PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
//                PrettyMethodGraph prettyGraph = prettyBuilder.build();
//                prettyGraph.saveDotFile(this.getProgramName(), classNode.name, methodNode.name);
//
//                try {
//                    prettyGraph.savePdfFile(this.getProgramName(), classNode.name, methodNode.name);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        CallGraph callGraph = this.buildInfoflowCallGraph();
        this.process(callGraph);

//        this.getLeafNodes();
    }

    private void process(CallGraph callGraph) {
        QueueReader<Edge> edges = callGraph.listener();
        Set<SootMethod> methods = new HashSet<>();

        while (edges.hasNext()) {
            Edge edge = edges.next();
            MethodOrMethodContext src = edge.getSrc();

            if(!src.method().getDeclaringClass().getPackageName().contains("edu.")) {
                continue;
            }

            MethodOrMethodContext tgt = edge.getTgt();
            methods.add(src.method());

            if(!tgt.method().getDeclaringClass().getPackageName().contains("edu.")) {
                continue;
            }

        }

        for(SootMethod method : methods) {
            System.out.println(method.getName());
        }
    }


    private CallGraph buildInfoflowCallGraph() {
        initializeSoot();
        PackManager.v().getPack("wjpp").apply();
        PackManager.v().getPack("cg").apply();

        return Scene.v().getCallGraph();
    }

    private void initializeSoot() {
        String libPath = "/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/jce.jar";

        soot.G.reset();

        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_soot_classpath(appendClasspath(this.getClassTransformer().getPath(), libPath));

        // Configure the callgraph algorithm
        Options.v().setPhaseOption("cg.spark", "on");
        Options.v().setPhaseOption("cg.spark", "string-constants:true");

        // Specify additional options required for the callgraph
        Options.v().set_whole_program(true);
        Options.v().setPhaseOption("cg", "trim-clinit:false");

        // do not merge variables (causes problems with PointsToSets)
        Options.v().setPhaseOption("jb.ulp", "off");

        Options.v().set_src_prec(Options.src_prec_java);

        //at the end of setting: load user settings:
        new SootConfig().setSootOptions(Options.v());

        // load all entryPoint classes with their bodies
        Scene.v().addBasicClass("edu.cmu.cs.mvelezce.Example", SootClass.BODIES);
        Scene.v().loadNecessaryClasses();

        boolean hasClasses = false;
//        for(String className : classes) {
        SootClass c = Scene.v().forceResolve("edu.cmu.cs.mvelezce.Example", SootClass.BODIES);
        if(c != null) {
            c.setApplicationClass();
            if(!c.isPhantomClass() && !c.isPhantom()) {
                hasClasses = true;
            }
//            }
        }

        if(!hasClasses) {
            throw new RuntimeException("Only phantom classes loaded, skipping analysis...");
        }
    }

    private String appendClasspath(String appPath, String libPath) {
        String s = (appPath != null && !appPath.isEmpty()) ? appPath : "";

        if(libPath != null && !libPath.isEmpty()) {
            if(!s.isEmpty()) {
                s += File.pathSeparator;
            }
            s += libPath;
        }
        return s;
    }

    protected List<JavaRegion> getRegionsInMethod(MethodNode methodNode) {
        String classPackage = this.currentClassNode.name;
        classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
        classPackage = classPackage.replace("/", ".");

        String className = this.currentClassNode.name;
        className = className.substring(className.lastIndexOf("/") + 1);

        String methodName = methodNode.name + methodNode.desc;

        List<JavaRegion> javaRegions = new ArrayList<>();

        for(JavaRegion javaRegion : this.regionsToOptionSet.keySet()) {
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

        for(JavaRegion javaRegion : this.regionsToOptionSet.keySet()) {
            if(javaRegion.getRegionPackage().equals(classPackage) && javaRegion.getRegionClass().equals(className)) {
                regionsInClass.add(javaRegion);
            }
        }

        return regionsInClass;
    }

    public Map<JavaRegion, Set<Set<String>>> getRegionsToOptionSet() {
        return regionsToOptionSet;
    }

    public ClassNode getCurrentClassNode() {
        return currentClassNode;
    }
}
