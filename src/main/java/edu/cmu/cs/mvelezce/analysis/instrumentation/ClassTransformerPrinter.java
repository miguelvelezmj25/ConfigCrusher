package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by mvelezce on 4/3/17.
 */
public class ClassTransformerPrinter extends ClassTransformerBase {

    private Set<String> methodsToInstrument;
    private String messageToPrint;

    public ClassTransformerPrinter(Set<String> methodsToInstrument, String messageToPrint) {
        this.methodsToInstrument = methodsToInstrument;
        this.messageToPrint = messageToPrint;
    }

    @Override
    public void transform(ClassNode classNode) {
        for(MethodNode methodNode : classNode.methods) {
            if(!this.methodsToInstrument.contains(methodNode.name)) {
                continue;
            }

            InsnList instructions = methodNode.instructions;

            InsnList newInstructions = new InsnList();
            newInstructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
            newInstructions.add(new LdcInsnNode(this.messageToPrint));
            newInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
            instructions.insert(newInstructions);
        }
    }
}
