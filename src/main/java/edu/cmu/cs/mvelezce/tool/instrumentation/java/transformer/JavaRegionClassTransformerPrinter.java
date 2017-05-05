package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;
import java.util.Set;

// TODO do i need to update the stack and frame?
/**
 * Created by mvelezce on 4/3/17.
 */
public class JavaRegionClassTransformerPrinter extends JavaRegionClassTransformer {

    private String messageToPrint;

    public JavaRegionClassTransformerPrinter(List<String> programFiles, Set<JavaRegion> regions, String messageToPrint) {
        super(programFiles, regions);

        this.messageToPrint = messageToPrint;
    }

    @Override
    public InsnList addInstructionsStartRegion(JavaRegion javaRegion) {
        InsnList instructionsBeforeRegion = new InsnList();
        instructionsBeforeRegion.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        instructionsBeforeRegion.add(new LdcInsnNode(this.messageToPrint + " at start of region"));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        return instructionsBeforeRegion;
    }

    @Override
    public InsnList addInstructionsEndRegion(JavaRegion javaRegion) {
        InsnList instructionsAfterRegion = new InsnList();
        instructionsAfterRegion.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        instructionsAfterRegion.add(new LdcInsnNode(messageToPrint + " at end of region"));
        instructionsAfterRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        return instructionsAfterRegion;
    }
}
