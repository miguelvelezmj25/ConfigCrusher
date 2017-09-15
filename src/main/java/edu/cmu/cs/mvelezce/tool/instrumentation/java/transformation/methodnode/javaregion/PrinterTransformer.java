package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Set;

// TODO do i need to update the stack and frame?

/**
 * Created by mvelezce on 4/3/17.
 */
public class PrinterTransformer extends RegionTransformer {

    private String messageToPrint;

    public PrinterTransformer(String programName, String directory, Set<JavaRegion> regions) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, directory, regions);
    }

    public PrinterTransformer(String programName, ClassTransformer classTransformer, Set<JavaRegion> regions) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, classTransformer, regions);
    }

    @Override
    public InsnList getInstructionsStartRegion(JavaRegion javaRegion) {
        InsnList instructionsBeforeRegion = new InsnList();
        instructionsBeforeRegion.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        instructionsBeforeRegion.add(new LdcInsnNode(this.messageToPrint + " at start of region"));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        return instructionsBeforeRegion;
    }

    @Override
    public InsnList getInstructionsEndRegion(JavaRegion javaRegion) {
        InsnList instructionsAfterRegion = new InsnList();
        instructionsAfterRegion.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        instructionsAfterRegion.add(new LdcInsnNode(messageToPrint + " at end of region"));
        instructionsAfterRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        return instructionsAfterRegion;
    }

    @Override
    public void transformMethod(MethodNode methodNode) {
        throw new RuntimeException();
    }
}
