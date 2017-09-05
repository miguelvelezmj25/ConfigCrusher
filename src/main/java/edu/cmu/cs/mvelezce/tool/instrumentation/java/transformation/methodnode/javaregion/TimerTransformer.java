package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Set;

public class TimerTransformer extends JavaRegionTransformer {

    public TimerTransformer(String directory, Set<JavaRegion> regions) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(directory, regions);
    }

    public TimerTransformer(ClassTransformer classTransformer, String directory, Set<JavaRegion> regions) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(classTransformer, directory, regions);
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
