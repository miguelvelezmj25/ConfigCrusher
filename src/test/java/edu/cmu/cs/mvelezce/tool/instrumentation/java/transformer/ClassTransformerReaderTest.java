package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.Example;
import edu.cmu.cs.mvelezce.Sleep1;
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
     *
     * @throws Exception
     */
    @Test
    public void testReadClass1() throws Exception {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Example.class.getCanonicalName());
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

    @Test
    public void testReadClass2() throws Exception {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes/edu/cmu/cs/mvelezce/Example");
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