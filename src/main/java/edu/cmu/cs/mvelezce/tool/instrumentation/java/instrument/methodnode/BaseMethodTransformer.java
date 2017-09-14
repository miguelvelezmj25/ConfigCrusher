package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodTracer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.TraceClassInspector;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.PrettyMethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.PrettyMethodGraphBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

public abstract class BaseMethodTransformer implements MethodTransformer {

    private String programName;
    private ClassTransformer classTransformer;

    public BaseMethodTransformer(String programName, ClassTransformer classTransformer) {
        this.programName = programName;
        this.classTransformer = classTransformer;
    }

    @Override
    public void transformMethods() throws IOException {
        Set<ClassNode> classNodes = this.classTransformer.readClasses();
        this.transformMethods(classNodes);
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

            this.classTransformer.writeClass(classNode, this.classTransformer.getPath() + "/" + classNode.name);

            // TODO if debug
            TraceClassInspector classInspector = new TraceClassInspector(classNode.name);
            MethodTracer tracer = classInspector.visitClass();

            for(MethodNode methodNode : methodsToInstrument) {
                Printer printer = tracer.getPrinterForMethodSignature(methodNode.name + methodNode.desc);
                PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, printer);
                PrettyMethodGraph prettyGraph = prettyBuilder.build();
                prettyGraph.saveDotFile(this.programName, classNode.name, methodNode.name);

                try {
                    prettyGraph.savePdfFile(this.programName, classNode.name, methodNode.name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.out.println(prettyGraph.toDotStringVerbose(methodNode.name));
            }

        }
    }

    public String getProgramName() {
        return programName;
    }

    public ClassTransformer getClassTransformer() {
        return classTransformer;
    }
}
