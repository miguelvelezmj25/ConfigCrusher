package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.java.programs.*;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class ClassTransformerBaseTest {

    private static final String SLEEP1 = Sleep1.FILENAME;
    private static final String SLEEP2 = Sleep2.FILENAME;
    private static final String SLEEP3 = Sleep3.FILENAME;
    private static final String SLEEP4 = Sleep4.FILENAME;
    private static final String SLEEP5 = Sleep5.FILENAME;
    private static final String SLEEP6 = Sleep6.FILENAME;
    private static final String SLEEP8 = Sleep8.FILENAME;
    private static final String SLEEP9 = Sleep9.FILENAME;
    private static final String SLEEP10 = Sleep10.FILENAME;
    private static final String SLEEP11 = Sleep11.FILENAME;

    protected static final String CLASS_CONTAINER = "target/classes/";


    /**
     * Helpful to find the indexes for regions when testing
     * @throws Exception
     */
    @Test
    public void testReadClass() throws Exception {
        ClassTransformerBase base = new ClassTransformerBase() {
            @Override
            public Set<ClassNode> transformClasses() throws IOException {
                return null;
            }

            @Override
            public void transform(ClassNode classNode) {

            }
        };
        ClassNode classNode = base.readClass(ClassTransformerBaseTest.SLEEP2);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            System.out.println("Method: " + method.name);
            InsnList instructions = method.instructions;

            for(ListIterator<AbstractInsnNode> it = instructions.iterator(); it.hasNext(); ) {
                AbstractInsnNode instruction = it.next();
                if(instruction.getOpcode() >= 0) {
                    System.out.println("Opcode: " + instruction.getOpcode() + " |Index: " + instructions.indexOf(instruction) + " |Instruction: " + instruction.getClass());
                }
            }
            System.out.println();
        }

    }

}