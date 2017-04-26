package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class JavaRegionClassTransformerTimer extends JavaRegionClassTransformer {

    public JavaRegionClassTransformerTimer(String fileName) {
        super(fileName);
    }

    @Override
    public InsnList addInstructionsBeforeRegion(JavaRegion javaRegion) {
        InsnList instructionsBeforeRegion = new InsnList();
        instructionsBeforeRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/Regions", "getRegion", "(Ljava/lang/String;)Ledu/cmu/cs/mvelezce/tool/analysis/Region;", false));
        instructionsBeforeRegion.add(new VarInsnNode(Opcodes.ASTORE, 3));
        instructionsBeforeRegion.add(new VarInsnNode(Opcodes.ALOAD, 3));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "edu/cmu/cs/mvelezce/tool/analysis/Region", "enter", "()V", false));

        return instructionsBeforeRegion;
    }

    @Override
    public InsnList addInstructionsAfterRegion(JavaRegion javaRegion) {
        InsnList instructionsAfterRegion = new InsnList();
        instructionsAfterRegion.add(new VarInsnNode(Opcodes.ALOAD, 3));
        instructionsAfterRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "edu/cmu/cs/mvelezce/tool/analysis/Region", "exit", "()V", false));

        return instructionsAfterRegion;
    }

}
