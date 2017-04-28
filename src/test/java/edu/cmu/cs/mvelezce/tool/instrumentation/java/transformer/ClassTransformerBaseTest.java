package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.*;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ListIterator;

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
        ClassTransformerBase base = new ClassTransformerBase(ClassTransformerBaseTest.SLEEP11) {
            @Override
            public void transform(ClassNode classNode) {

            }
        };
        ClassNode classNode = base.readClass();

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

    protected static String executeCommand(String command) {
        System.out.println(command);
        StringBuilder output = new StringBuilder();
        Process process;

        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(output);
        return output.toString();
    }

}