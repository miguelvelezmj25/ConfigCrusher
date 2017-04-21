package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

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
