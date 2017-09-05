package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class DefaultBaseClassTransformer extends BaseClassTransformer {

    public DefaultBaseClassTransformer(String path) throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        super(path);
    }
}
