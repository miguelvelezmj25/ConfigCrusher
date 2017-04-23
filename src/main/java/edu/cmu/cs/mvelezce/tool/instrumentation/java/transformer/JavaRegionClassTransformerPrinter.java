package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.ListIterator;
import java.util.Set;

// TODO do i need to update the stack and frame?
/**
 * Created by mvelezce on 4/3/17.
 */
public class JavaRegionClassTransformerPrinter extends JavaRegionClassTransformer {

    private String messageToPrint;

    public JavaRegionClassTransformerPrinter(String fileName, String messageToPrint) {
        super(fileName);
        this.messageToPrint = messageToPrint;
    }

    @Override
    public void transform(ClassNode classNode) {
        String classCanonicalName = classNode.name;
        // TODO we might abstract this to a static method
        String classPackage = classCanonicalName.substring(0, classCanonicalName.lastIndexOf("/"));
        String className = classCanonicalName.substring(classCanonicalName.lastIndexOf("/") + 1);
        classPackage = classPackage.replaceAll("/", ".");
        className = className.replaceAll("/", ".");

        Set<JavaRegion> regionsInClass = JavaRegionClassTransformer.getRegionInClass(classPackage, className);

        for(MethodNode methodNode : classNode.methods) {
            boolean instrumentMethod = false;

            for(JavaRegion javaRegion : regionsInClass) {
                if(javaRegion.getRegionMethod().equals(methodNode.name)) {
                    instrumentMethod = true;
                    break;
                }
            }

            if(!instrumentMethod) {
                continue;
            }

            InsnList instructions = methodNode.instructions;
            InsnList newInstructions = new InsnList();
            ListIterator<AbstractInsnNode> instructionsIterator = instructions.iterator();

            while(instructionsIterator.hasNext()) {
                AbstractInsnNode instruction = instructionsIterator.next();
                boolean instrumented = false;

                if(instruction.getOpcode() < 0) {
                    newInstructions.add(instruction);
                    continue;
                }

                int bytecodeIndex = instructions.indexOf(instruction);

                for(JavaRegion javaRegion : regionsInClass) {
                    if(javaRegion.getStartBytecodeIndex() == bytecodeIndex) {
                        newInstructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                        newInstructions.add(new LdcInsnNode(this.messageToPrint + " at start of region"));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
                        newInstructions.add(instruction);
                        instrumented = true;
                    }
                    else if(javaRegion.getEndBytecodeIndex() == bytecodeIndex) {
                        newInstructions.add(instruction);
                        newInstructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                        newInstructions.add(new LdcInsnNode(this.messageToPrint + " at end of region"));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
                        instrumented = true;
                    }
                }

                if(!instrumented) {
                    newInstructions.add(instruction);
                }
            }

            methodNode.instructions.clear();
            methodNode.instructions.add(newInstructions);
        }
    }
}
