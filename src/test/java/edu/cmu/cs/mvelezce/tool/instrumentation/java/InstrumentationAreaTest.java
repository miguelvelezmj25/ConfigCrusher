package edu.cmu.cs.mvelezce.tool.instrumentation;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.InstrumentationArea;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by mvelezce on 3/31/17.
 */
public class InstrumentationAreaTest {

    @Test
    public void testReadJar() throws IOException {
        String jarName = "org.sat4j.core.jar";
        List<String> classFiles = InstrumentationArea.readJar(jarName);
        Assert.assertFalse(classFiles.isEmpty());
    }

    @Test
    public void testReadFile() throws IOException {
        String fileName = "edu/cmu/cs/mvelezce/tool/instrumentation/java/DummyClass";
        List<MethodNode> methods = InstrumentationArea.readFile(fileName);
        Assert.assertFalse(methods.isEmpty());
    }

}