package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.Helper;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by mvelezce on 3/31/17.
 */
public class HelperTest {

    @Test
    public void testReadJar() throws IOException {
        String jarName = "org.sat4j.core.jar";
        List<String> classFiles = Helper.readJar(jarName);
        Assert.assertFalse(classFiles.isEmpty());
    }

    @Test
    public void testReadFile() throws IOException {
        String fileName = "edu/cmu/cs/mvelezce/tool/instrumentation/java/programs/Dummy";
        List<MethodNode> methods = Helper.readFile(fileName);
        Assert.assertFalse(methods.isEmpty());
    }

}