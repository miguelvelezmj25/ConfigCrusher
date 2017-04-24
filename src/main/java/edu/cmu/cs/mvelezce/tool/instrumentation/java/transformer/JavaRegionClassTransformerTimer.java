package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.*;

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
        // Creates an uninitialized object in the operand stack
        instructionsBeforeRegion.add(new TypeInsnNode(Opcodes.NEW, "edu/cmu/cs/mvelezce/tool/pipeline/java/JavaRegion"));
        // Since INVOKESPECIAL will consume the value created by NEW from the operand stack, I need to DUP since I may need to use the object reference later; e.g. for returning it
        instructionsBeforeRegion.add(new InsnNode(Opcodes.DUP));
        instructionsBeforeRegion.add(new LdcInsnNode(Type.getType("Ledu/cmu/cs/mvelezce/tool/instrumentation/java/programs/Sleep;")));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getPackage", "()Ljava/lang/Package;", false));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Package", "getName", "()Ljava/lang/String;", false));
        instructionsBeforeRegion.add(new LdcInsnNode(Type.getType("Ledu/cmu/cs/mvelezce/tool/instrumentation/java/programs/Sleep;")));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getSimpleName", "()Ljava/lang/String;", false));
        instructionsBeforeRegion.add(new LdcInsnNode(javaRegion.getRegionMethod()));
        instructionsBeforeRegion.add(new IntInsnNode(Opcodes.SIPUSH, javaRegion.getStartBytecodeIndex()));
        instructionsBeforeRegion.add(new IntInsnNode(Opcodes.SIPUSH, javaRegion.getEndBytecodeIndex()));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "edu/cmu/cs/mvelezce/tool/pipeline/java/JavaRegion", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;II)V", false));
        instructionsBeforeRegion.add(new VarInsnNode(Opcodes.ASTORE, 2));
        instructionsBeforeRegion.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instructionsBeforeRegion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/tool/analysis/Regions", "getRegion", "(Ledu/cmu/cs/mvelezce/tool/analysis/Region;)Ledu/cmu/cs/mvelezce/tool/analysis/Region;", false));
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
