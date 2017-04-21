package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class ClassTransformerRegionTimerTest {

    @Test
    public void transform1() throws IOException {
        int methodNumber = 4;
        JavaRegion region = new JavaRegion(ClassTransformerBaseTest.PACKAGE, ClassTransformerBaseTest.CLASS, ClassTransformerBaseTest.METHOD + methodNumber);
        Map<String, JavaRegion> methodToRegionToInstrument = new HashedMap<>();
        methodToRegionToInstrument.put(region.getRegionMethod(), region);

        ClassTransformer classTransformer = new ClassTransformerRegionTimer(ClassTransformerBaseTest.FILE_TO_INSTRUMENT_NAME, methodToRegionToInstrument);
        ClassNode classNode = classTransformer.readClass();
        classTransformer.transform(classNode);
        classTransformer.writeClass(classNode, ClassTransformerBaseTest.CLASS_CONTAINER + ClassTransformerBaseTest.FILE_TO_INSTRUMENT_NAME);

        Map<String, Integer> methodToInstructionCount = new HashMap<>();

        for(MethodNode methodNode : classNode.methods) {
            methodToInstructionCount.put(methodNode.name, methodNode.instructions.size());
        }

        classTransformer = new ClassTransformerRegionTimer(ClassTransformerBaseTest.FILE_TO_INSTRUMENT_NAME, methodToRegionToInstrument);
        classNode = classTransformer.readClass();
        classTransformer.transform(classNode);

        boolean transformed = false;

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.instructions.size() != methodToInstructionCount.get(methodNode.name)) {
                transformed = true;
            }
        }

        Assert.assertTrue(transformed);

        String command = "java -cp " + ClassTransformerBaseTest.CLASS_CONTAINER + " " + ClassTransformerBaseTest.PACKAGE + "." + ClassTransformerBaseTest.CONTROLLER_CLASS + " " + methodNumber;
        String output = ClassTransformerBaseTest.executeCommand(command);
//        Assert.assertNotEquals(0, output.length());
    }

}