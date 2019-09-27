package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.fixed;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.fixed.StaticRegionTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

// TODO do i need to update the stack and frame?

/**
 * Created by mvelezce on 4/3/17.
 */
public class StaticPrinterTransformer extends StaticRegionTransformer {

    private String messageToPrint;

    public StaticPrinterTransformer(String programName, String directory, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, "", directory, regionsToOptionSet);
        throw new RuntimeException("Implement");
    }

    public StaticPrinterTransformer(String programName, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, "", classTransformer, regionsToOptionSet);
        throw new RuntimeException("Implement");
    }

    @Override
    public MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        throw new RuntimeException("Implement");
    }

    @Override
    public MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        throw new RuntimeException("Implement");
    }


    public InsnList getInstructionsStartRegion(JavaRegion javaRegion) {
        InsnList instructionsBeforeRegion = new InsnList();
        instructionsBeforeRegion.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        instructionsBeforeRegion.add(new LdcInsnNode(this.messageToPrint + " at start of region"));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        return instructionsBeforeRegion;
    }

    public InsnList getInstructionsEndRegion(JavaRegion javaRegion) {
        InsnList instructionsAfterRegion = new InsnList();
        instructionsAfterRegion.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        instructionsAfterRegion.add(new LdcInsnNode(messageToPrint + " at end of region"));
        instructionsAfterRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        return instructionsAfterRegion;
    }

    @Override
    public void transformMethod(MethodNode methodNode, ClassNode classNode) {
        throw new RuntimeException();
    }

}
