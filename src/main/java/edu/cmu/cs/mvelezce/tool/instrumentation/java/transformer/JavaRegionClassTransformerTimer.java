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

    public JavaRegionClassTransformerTimer(String directory, Set<JavaRegion> regions) {
        super(directory, regions);
    }

    @Override
    public InsnList addInstructionsStartRegion(JavaRegion javaRegion) {
        InsnList instructionsStartRegion = new InsnList();
        instructionsStartRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsStartRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "enter", "(Ljava/lang/String;)V", false));

        return instructionsStartRegion;
    }

    @Override
    public InsnList addInstructionsEndRegion(JavaRegion javaRegion) {
        InsnList instructionsEndRegion = new InsnList();
        instructionsEndRegion.add(new LdcInsnNode(javaRegion.getRegionID()));
        instructionsEndRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/region/Regions", "exit", "(Ljava/lang/String;)V", false));

        return instructionsEndRegion;
    }

}
