package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class DefaultBaseClassTransformer extends BaseClassTransformer {

    public DefaultBaseClassTransformer(String path) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(path);
    }
}
