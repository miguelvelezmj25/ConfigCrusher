package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by mvelezce on 4/3/17.
 */
public class ClassTransformerTimer extends ClassTransformerBase {

    private Set<String> methodsToInstrument;

    public ClassTransformerTimer(String fileName, Set<String> methodsToInstrument) {
        super(fileName);
        this.methodsToInstrument = methodsToInstrument;
    }

    @Override
    public void transform(ClassNode classNode) {
        for(MethodNode methodNode : classNode.methods) {
            if(!this.methodsToInstrument.contains(methodNode.name)) {
                continue;
            }

            InsnList instructions = methodNode.instructions;

            if(instructions.size() == 0) {
                continue;
            }

            Iterator<AbstractInsnNode> instructionIterator = instructions.iterator();

            // Put code before the end of the method
            while(instructionIterator.hasNext()) {
                AbstractInsnNode instruction = instructionIterator.next();
                int opcode = instruction.getOpcode();

                if((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new LdcInsnNode("MiguelId"));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/instrumentation/Timer", "stopTimer", "(Ljava/lang/String;)V", false));
                    instructions.insert(instruction.getPrevious(), newInstructions);
                }
            }

            InsnList newInstructions = new InsnList();
            newInstructions.add(new LdcInsnNode("MiguelId"));
            newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/instrumentation/Timer", "startTimer", "(Ljava/lang/String;)V", false));
            instructions.insert(newInstructions);
        }
    }
}

/*
package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.Iterator;

public class TimerClassTransformer implements ClassTransformer {

    public TimerClassTransformer(String fileName) {
        super(fileName);
    }

    @Override
    public void transform(ClassNode classNode) {
        for(MethodNode methodNode : classNode.methods) {
            if(!methodNode.name.equals("inc")) {
                continue;
            }

            InsnList instructions = methodNode.instructions;

            if(instructions.size() == 0) {
                continue;
            }

            Iterator<AbstractInsnNode> instructionIterator = instructions.iterator();

            while(instructionIterator.hasNext()) {
                AbstractInsnNode instruction = instructionIterator.next();
                int opcode = instruction.getOpcode();

                if((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false));
                    newInstructions.add(new InsnNode(Opcodes.POP2));
                    instructions.insert(instruction.getPrevious(), newInstructions);
                }
            }

            InsnList newInstructions = new InsnList();
            newInstructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
            newInstructions.add(new LdcInsnNode("Mom"));
            newInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
            //            newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false));
//            newInstructions.add(new InsnNode(Opcodes.POP2));
            instructions.insert(newInstructions);
        }
    }

//    public TimerClassTransformer(ClassVisitor classVisitor) {
//        super(Opcodes.ASM4, classVisitor);
//    }
//
//    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//        MethodVisitor methodVisitor = this.cv.visitMethod(access, name, desc, signature, exceptions);
//        return new TimerAdapter(methodVisitor);
//    }
}

 */