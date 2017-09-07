package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.TimerTransformer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class TimerRegionInstrumenter extends BaseRegionInstrumenter {

    // TODO is this needed?
    public static final String TARGET_DIRECTORY = "../performancemodel-mapper-evaluation/instrumented";

    public TimerRegionInstrumenter(String classDir, Set<JavaRegion> regions) {
        this(null, classDir, regions);
    }

    public TimerRegionInstrumenter(String srcDir, String classDir, Set<JavaRegion> regions) {
        super(srcDir, classDir, regions);
    }

    @Override
    public void instrument() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        MethodTransformer methodTransformer = new TimerTransformer(this.getClassDir(), this.getRegions());
        methodTransformer.transformMethods();
    }

    @Override
    public void compileFromSource() {
        // We do not compile since other transformation might have occured to make the instrumentation easier
        return;
    }

}
