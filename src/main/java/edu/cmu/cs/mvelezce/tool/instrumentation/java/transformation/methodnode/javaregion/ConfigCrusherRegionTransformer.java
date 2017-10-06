package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


// TODO this class figures out where to start and stop insrumenting
public class ConfigCrusherRegionTransformer extends RegionTransformer {

    private Set<MethodBlock> blocksToInstrumentBeforeReturn = new HashSet<>();
    private boolean updatedRegions = false;

    public ConfigCrusherRegionTransformer(String programName, String entryPoint, String directory, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, entryPoint, directory, regionsToOptionSet);
    }

    public ConfigCrusherRegionTransformer(String programName, String entryPoint, ClassTransformer classTransformer, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        super(programName, entryPoint, classTransformer, regionsToOptionSet);
    }

    public ConfigCrusherRegionTransformer() throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
        this(null, null, "", null);
    }

    @Override
    public void transformMethod(MethodNode methodNode) {
        throw new RuntimeException("Implement");
    }

    public MethodBlock getBlockToEndInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        MethodBlock immediatePostDominator = methodGraph.getImmediatePostDominator(start);
        return immediatePostDominator;
    }

    public MethodBlock getBlockToStartInstrumentingBeforeIt(MethodGraph methodGraph, MethodBlock start) {
        MethodBlock id = methodGraph.getImmediateDominator(start);

        if(id != methodGraph.getEntryBlock() && id.getSuccessors().size() == 1 && id.getSuccessors().contains(start)) {
            return id;
        }

        return start;
    }
}
