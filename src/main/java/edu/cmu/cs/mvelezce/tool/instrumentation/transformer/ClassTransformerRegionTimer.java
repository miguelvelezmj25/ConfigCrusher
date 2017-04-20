package edu.cmu.cs.mvelezce.tool.instrumentation.transformer;

import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class ClassTransformerRegionTimer extends ClassTransformerBase {

    private Map<String, JavaRegion> methodToRegionToInstrument;

    public ClassTransformerRegionTimer(String fileName, Map<String, JavaRegion> methodToRegionToInstrument) {
        super(fileName);
        this.methodToRegionToInstrument = methodToRegionToInstrument;
    }

    @Override
    public void transform(ClassNode classNode) {
        for(MethodNode methodNode : classNode.methods) {
            if(!this.methodToRegionToInstrument.containsKey(methodNode.name)) {
                continue;
            }

            JavaRegion currentRegion = this.methodToRegionToInstrument.get(methodNode.name);
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

                    newInstructions.add(new LdcInsnNode(currentRegion.getRegionPackage()));
                    newInstructions.add(new LdcInsnNode(currentRegion.getRegionClass()));
                    newInstructions.add(new LdcInsnNode(currentRegion.getRegionMethod()));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/taint/Regions", "getRegion", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ledu/cmu/cs/mvelezce/analysis/taint/Region;", false));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "edu/cmu/cs/mvelezce/analysis/taint/Region", "endTime", "()V", false));
                    instructions.insert(instruction.getPrevious(), newInstructions);
                }
            }

            InsnList newInstructions = new InsnList();
            newInstructions.add(new LdcInsnNode(currentRegion.getRegionPackage()));
            newInstructions.add(new LdcInsnNode(currentRegion.getRegionClass()));
            newInstructions.add(new LdcInsnNode(currentRegion.getRegionMethod()));
            newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "edu/cmu/cs/mvelezce/analysis/taint/Regions", "getRegion", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ledu/cmu/cs/mvelezce/analysis/taint/Region;", false));
            newInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "edu/cmu/cs/mvelezce/analysis/taint/Region", "startTime", "()V", false));
            instructions.insert(newInstructions);
        }
    }
}
