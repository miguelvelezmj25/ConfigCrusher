package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.TimerTransformer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class TimerInstrumenter extends BaseInstrumenter {

    public static final String TARGET_DIRECTORY = "../performance-mapper-evaluation/instrumented";

    public TimerInstrumenter(String srcDir, String classDir, Set<JavaRegion> regions) {
        super(srcDir, classDir, regions);
    }

    @Override
    public void instrument() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        MethodTransformer methodTransformer = new TimerTransformer(this.getClassDir(), this.getRegions());
        methodTransformer.transformMethods();
    }

}
