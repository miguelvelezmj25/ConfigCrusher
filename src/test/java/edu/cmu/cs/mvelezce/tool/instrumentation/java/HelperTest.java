package edu.cmu.cs.mvelezce.tool.instrumentation.java;

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
        String jarName = "/lib/json-simple-1.1.1.jar";
        List<String> classFiles = Helper.readJar(jarName);
        Assert.assertFalse(classFiles.isEmpty());
    }

    @Test
    public void testReadFile() throws IOException {
        String fileName = "Sleep1/edu/cmu/cs/mvelezce/java/programs/Sleep1";
        List<MethodNode> methods = Helper.readFile(fileName);
        Assert.assertFalse(methods.isEmpty());
    }

}