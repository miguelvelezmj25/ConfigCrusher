package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.java.programs.Sleep1Dash1;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Test;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by mvelezce on 5/8/17.
 */
public class ClassTransformerReaderTest {

    /**
     * Helpful to find the indexes for regions when testing
     * @throws Exception
     */
    @Test
    public void testReadClass() throws Exception {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep1Dash1.FILENAME);
        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            System.out.println("Method: " + method.name);
            InsnList instructions = method.instructions;

            for(ListIterator<AbstractInsnNode> it = instructions.iterator(); it.hasNext(); ) {
                AbstractInsnNode instruction = it.next();
                System.out.println("Opcode: " + instruction.getOpcode() + " |Index: " + instructions.indexOf(instruction) + " |Instruction: " + instruction.getClass());
            }

            System.out.println();
        }

    }

}