package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

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
    public InsnList addInstructionsStartRegion(JavaRegion javaRegion) {
        InsnList instructionsStartRegion = new InsnList();
        instructionsStartRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsStartRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "enter", "(Ljava/lang/String;)V", false));
//        instructionsStartRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "getRegion", "(Ljava/lang/String;)Ledu/cmu/cs/mvelezce/tool/analysis/Region;", false));
//        instructionsStartRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "edu/cmu/cs/mvelezce/tool/analysis/Region", "enter", "()V", false));

        return instructionsStartRegion;
    }

    @Override
    public InsnList addInstructionsEndRegion(JavaRegion javaRegion) {
        InsnList instructionsEndRegion = new InsnList();
        instructionsEndRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsEndRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "exit", "(Ljava/lang/String;)V", false));
//        instructionsEndRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "getRegion", "(Ljava/lang/String;)Ledu/cmu/cs/mvelezce/tool/analysis/Region;", false));
//        instructionsEndRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "edu/cmu/cs/mvelezce/tool/analysis/Region", "exit", "()V", false));

        return instructionsEndRegion;
    }

}
