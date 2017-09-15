package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.TimerTransformer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
// TODO should we save the files that we instrumented for debugging?
public class TimerRegionInstrumenter extends BaseRegionInstrumenter {

    // TODO is this needed?
    public static final String TARGET_DIRECTORY = "../performance-mapper-evaluation/instrumented";

    public TimerRegionInstrumenter(String programName, String classDir, Map<JavaRegion, Set<Set<String>>> regionsToOptions) {
        super(programName, null, classDir, regionsToOptions);
    }

    @Override
    public void instrument() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        MethodTransformer methodTransformer = new TimerTransformer(this.getProgramName(), this.getClassDir(), this.getRegionsToOptionSet());
        methodTransformer.transformMethods();
    }

    @Override
    public void compileFromSource() {
        // We do not compile since other transformation might have occured to make the instrumentation easier
        return;
    }

}
