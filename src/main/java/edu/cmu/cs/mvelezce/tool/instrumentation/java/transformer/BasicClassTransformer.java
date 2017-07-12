package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by mvelezce on 6/28/17.
 */
public abstract class BasicClassTransformer extends ClassTransformerBase {

    private String directory;
    private Set<MethodNode> methodsToInsrument = new HashSet<>();

    public BasicClassTransformer(String directory) {
        this.directory = directory;
    }

    public abstract InsnList addInstructionsBeforeReturn(AbstractInsnNode returnInst, int maxLocals);

    @Override
    public Set<ClassNode> transformClasses() throws IOException {
        Set<ClassNode> classNodes = new HashSet<>();
        String[] extensions = {"class"};

        Collection<File> files = FileUtils.listFiles(new File(this.directory), extensions, true);

        for(File file : files) {
            String filePackage = file.getAbsolutePath().replace(this.directory, "");
            filePackage = filePackage.replace(".class", "");
            filePackage = filePackage.replace("/", ".");
            String fileClass = filePackage.substring(filePackage.lastIndexOf(".") + 1);
            int indexOfFilePackage = filePackage.lastIndexOf("." + fileClass);
            filePackage = filePackage.substring(0, indexOfFilePackage);
            ClassNode classNode = this.readClass(filePackage + "." + fileClass);
//            ClassNode classNode = this.readClass("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/out/production/elevator/edu/cmu/cs/mvelezce/Actions");

            this.methodsToInsrument = new HashSet<>();

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
                                this.methodsToInsrument.add(methodNode);
                            }
                        }
                    }
                }
            }

            if(this.methodsToInsrument.isEmpty()) {
                continue;
            }

            this.transform(classNode);
            classNodes.add(classNode);
        }

        return classNodes;
    }

    @Override
    public void transform(ClassNode classNode) {
        for(MethodNode methodNode : classNode.methods) {
            if(!this.methodsToInsrument.contains(methodNode)) {
                continue;
            }

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
            int i = 0;
        }
    }
}
