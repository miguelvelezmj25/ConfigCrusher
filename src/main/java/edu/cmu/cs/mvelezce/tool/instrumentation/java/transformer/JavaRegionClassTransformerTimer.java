package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class JavaRegionClassTransformerTimer extends JavaRegionClassTransformer {

    public JavaRegionClassTransformerTimer(List<String> programFiles, Set<JavaRegion> regions) {
        super(programFiles, regions);
    }

    @Override
    public InsnList addInstructionsBeforeRegion(JavaRegion javaRegion, int maxLocals) {
        InsnList instructionsBeforeRegion = new InsnList();
        instructionsBeforeRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/Regions", "getRegion", "(Ljava/lang/String;)Ledu/cmu/cs/mvelezce/tool/analysis/Region;", false));
        instructionsBeforeRegion.add(new VarInsnNode(Opcodes.ASTORE, maxLocals));
        instructionsBeforeRegion.add(new VarInsnNode(Opcodes.ALOAD, maxLocals));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "edu/cmu/cs/mvelezce/tool/analysis/Region", "enter", "()V", false));

        return instructionsBeforeRegion;
    }

    @Override
    public InsnList addInstructionsAfterRegion(JavaRegion javaRegion, int maxLocals) {
        // TODO what about regions that return? They should be instrumented before the outer return
        InsnList instructionsAfterRegion = new InsnList();
        instructionsAfterRegion.add(new VarInsnNode(Opcodes.ALOAD, maxLocals));
        instructionsAfterRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "edu/cmu/cs/mvelezce/tool/analysis/Region", "exit", "()V", false));

        return instructionsAfterRegion;
    }

}
