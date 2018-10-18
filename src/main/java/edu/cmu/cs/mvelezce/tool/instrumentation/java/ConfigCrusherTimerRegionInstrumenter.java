package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.ConfigCrusherTimerTransformer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class ConfigCrusherTimerRegionInstrumenter extends BaseRegionInstrumenter {

    private String entryPoint;

    public ConfigCrusherTimerRegionInstrumenter(String programName, String entryPoint, String classDir, Map<JavaRegion, Set<Set<String>>> regionsToOptions) {
        super(programName, classDir, regionsToOptions);

        this.entryPoint = entryPoint;
    }

    public ConfigCrusherTimerRegionInstrumenter(String programName) {
        super(programName);
    }

    @Override
    public void instrument() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        MethodTransformer methodTransformer = new ConfigCrusherTimerTransformer(this.getProgramName(), this.entryPoint, this.getClassDir(), this.getRegionsToOptionSet());
        methodTransformer.transformMethods();
    }

    @Override
    public void compile() {
        // We do not compile since other transformation might have occured to make the instrumentation easier
        return;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
