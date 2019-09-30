package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.format.VariableBeforeReturnTransformer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Formatter extends CompileInstrumenter {

    public Formatter(String srcDir, String classDir) {
        super(srcDir, classDir);
    }

    @Override
    public void instrument() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        // TODO create a list of transformers to execute similar to jimple manipulation

        MethodTransformer transformer = new VariableBeforeReturnTransformer(this.getClassDir());
        transformer.transformMethods();
    }
}
