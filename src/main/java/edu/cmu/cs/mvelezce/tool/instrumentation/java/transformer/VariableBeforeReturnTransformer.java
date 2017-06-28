package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

/**
 * Created by mvelezce on 6/28/17.
 */
public class VariableBeforeReturnTransformer extends BasicClassTransformer {

    public VariableBeforeReturnTransformer(String directory) {
        super(directory);
    }

    @Override
    public InsnList addInstructionsBeforeReturn(AbstractInsnNode returnInst, int maxLocals) {
        InsnList newInsts = new InsnList();

        if(returnInst.getOpcode() == Opcodes.IRETURN) {
            newInsts.add(new VarInsnNode(Opcodes.ISTORE, maxLocals + 1));
            newInsts.add(new VarInsnNode(Opcodes.ILOAD, maxLocals + 1));
        }
        else if(returnInst.getOpcode() == Opcodes.LRETURN) {
            newInsts.add(new VarInsnNode(Opcodes.LSTORE, maxLocals + 1));
            newInsts.add(new VarInsnNode(Opcodes.LLOAD, maxLocals + 1));
        }
        else if(returnInst.getOpcode() == Opcodes.FRETURN) {
            newInsts.add(new VarInsnNode(Opcodes.FSTORE, maxLocals + 1));
            newInsts.add(new VarInsnNode(Opcodes.FLOAD, maxLocals + 1));
        }
        else if(returnInst.getOpcode() == Opcodes.DRETURN) {
            newInsts.add(new VarInsnNode(Opcodes.DSTORE, maxLocals + 1));
            newInsts.add(new VarInsnNode(Opcodes.DLOAD, maxLocals + 1));
        }
        else if(returnInst.getOpcode() == Opcodes.ARETURN) {
            newInsts.add(new VarInsnNode(Opcodes.ASTORE, maxLocals + 1));
            newInsts.add(new VarInsnNode(Opcodes.ALOAD, maxLocals + 1));
        }
        else {
            throw new IllegalArgumentException("The instruction node is not a return");
        }

        return newInsts;
    }
}
