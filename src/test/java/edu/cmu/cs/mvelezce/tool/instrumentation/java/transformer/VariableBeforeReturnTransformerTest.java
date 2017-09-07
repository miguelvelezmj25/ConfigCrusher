package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by mvelezce on 6/28/17.
 */
public class VariableBeforeReturnTransformerTest {

    @Test
    public void testTransform1() throws IOException {
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";

        BasicClassTransformer transformer = new VariableBeforeReturnTransformer(classDirectory);
        transformer.transformClasses();
    }

}