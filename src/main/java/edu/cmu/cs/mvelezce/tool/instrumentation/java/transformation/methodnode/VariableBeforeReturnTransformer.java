package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.BaseMethodTransformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

public class VariableBeforeReturnTransformer extends BaseMethodTransformer {

    public VariableBeforeReturnTransformer(ClassTransformer classTransformer) {
        super(classTransformer);
    }

    @Override
    public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
        Set<MethodNode> methodsToInstrument = new HashSet<>();

        for(MethodNode methodNode : classNode.methods) {
            InsnList instructions = methodNode.instructions;
            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

            while (instructionsIterator.hasNext()) {
                AbstractInsnNode instruction = instructionsIterator.next();

                if(instruction.getOpcode() >= Opcodes.IRETURN && instruction.getOpcode() <= Opcodes.ARETURN) {
                    AbstractInsnNode previousInstruction = instruction.getPrevious();

//                        if(previousInstruction.getOpcode() >= Opcodes.INVOKEVIRTUAL && previousInstruction.getOpcode() <= Opcodes.INVOKEDYNAMIC) {
                    if(previousInstruction.getType() == AbstractInsnNode.METHOD_INSN) {
                        MethodInsnNode methodInstruction = (MethodInsnNode) previousInstruction;

                        if(!methodNode.name.equals("<init>") && !methodInstruction.owner.equals("java/lang/Object")) {
                            methodsToInstrument.add(methodNode);
                        }
                    }
                }
            }
        }

        return methodsToInstrument;
    }

    @Override
    public void transformMethod(MethodNode methodNode) {
        System.out.println("Transforming method " + methodNode.name);

        InsnList newInsts = new InsnList();
        InsnList instructions = methodNode.instructions;
        ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

        while (instructionsIterator.hasNext()) {
            AbstractInsnNode instruction = instructionsIterator.next();

            if(instruction.getOpcode() >= Opcodes.IRETURN && instruction.getOpcode() <= Opcodes.ARETURN) {
                AbstractInsnNode previousInstruction = instruction.getPrevious();

//                        if(previousInstruction.getOpcode() >= Opcodes.INVOKEVIRTUAL && previousInstruction.getOpcode() <= Opcodes.INVOKEDYNAMIC) {
                if(previousInstruction.getType() == AbstractInsnNode.METHOD_INSN) {
                    MethodInsnNode methodInstruction = (MethodInsnNode) previousInstruction;

                    if(!methodNode.name.equals("<init>") && !methodInstruction.owner.equals("java/lang/Object")) {
                        InsnList localVarInsts = this.addInstructionsBeforeReturn(instruction, methodNode.maxLocals);
                        newInsts.add(localVarInsts);
                        methodNode.maxLocals += 1;
                    }
                }
            }

            newInsts.add(instruction);
        }

        methodNode.instructions.clear();
        methodNode.instructions.add(newInsts);
    }

    private InsnList addInstructionsBeforeReturn(AbstractInsnNode returnInst, int maxLocals) {
        InsnList newInsts = new InsnList();

        if(returnInst.getOpcode() == Opcodes.IRETURN) {
            newInsts.add(new VarInsnNode(Opcodes.ISTORE, maxLocals + 1));
            newInsts.add(new InsnNode(Opcodes.NOP));
            LabelNode ln = new LabelNode();
            newInsts.add(ln);
            newInsts.add(new LineNumberNode(-1, ln));
            newInsts.add(new InsnNode(Opcodes.NOP));
            newInsts.add(new VarInsnNode(Opcodes.ILOAD, maxLocals + 1));
        }
        else if(returnInst.getOpcode() == Opcodes.LRETURN) {
            newInsts.add(new VarInsnNode(Opcodes.LSTORE, maxLocals + 1));
            newInsts.add(new InsnNode(Opcodes.NOP));
            LabelNode ln = new LabelNode();
            newInsts.add(ln);
            newInsts.add(new LineNumberNode(-1, ln));
            newInsts.add(new InsnNode(Opcodes.NOP));
            newInsts.add(new VarInsnNode(Opcodes.LLOAD, maxLocals + 1));
        }
        else if(returnInst.getOpcode() == Opcodes.FRETURN) {
            newInsts.add(new VarInsnNode(Opcodes.FSTORE, maxLocals + 1));
            newInsts.add(new InsnNode(Opcodes.NOP));
            LabelNode ln = new LabelNode();
            newInsts.add(ln);
            newInsts.add(new LineNumberNode(-1, ln));
            newInsts.add(new InsnNode(Opcodes.NOP));
            newInsts.add(new VarInsnNode(Opcodes.FLOAD, maxLocals + 1));
        }
        else if(returnInst.getOpcode() == Opcodes.DRETURN) {
            newInsts.add(new VarInsnNode(Opcodes.DSTORE, maxLocals + 1));
            newInsts.add(new InsnNode(Opcodes.NOP));
            LabelNode ln = new LabelNode();
            newInsts.add(ln);
            newInsts.add(new LineNumberNode(-1, ln));
            newInsts.add(new InsnNode(Opcodes.NOP));
            newInsts.add(new VarInsnNode(Opcodes.DLOAD, maxLocals + 1));
        }
        else if(returnInst.getOpcode() == Opcodes.ARETURN) {
            newInsts.add(new VarInsnNode(Opcodes.ASTORE, maxLocals + 1));
            newInsts.add(new InsnNode(Opcodes.NOP));
            LabelNode ln = new LabelNode();
            newInsts.add(ln);
            newInsts.add(new LineNumberNode(-1, ln));
            newInsts.add(new InsnNode(Opcodes.NOP));
            newInsts.add(new VarInsnNode(Opcodes.ALOAD, maxLocals + 1));
        }
        else {
            throw new IllegalArgumentException("The instruction node is not a return");
        }

        return newInsts;
    }
}
