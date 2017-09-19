package edu.cmu.cs.mvelezce.tool;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow.TaintFlowAnalysis;
import edu.cmu.cs.mvelezce.tool.compression.BaseCompression;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.simple.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.execute.java.DefaultExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.CompileInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Formatter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.TimerRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.performancemodel.DefaultPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceEntry2;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceModelBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class ConfigCrusher {

    private String programName;
    private String srcDir;
    private String classDir;
    private String entry;

    public ConfigCrusher(String programName, String classDir, String entry) {
        this(programName, "", classDir, entry);
    }

    public ConfigCrusher(String programName, String srcDir, String classDir, String entry) {
        this.programName = programName;
        this.srcDir = srcDir;
        this.classDir = classDir;
        this.entry = entry;
    }

    public PerformanceModel run(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InterruptedException {
        StaticAnalysis analysis = new TaintFlowAnalysis(this.programName);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = analysis.analyze();

        Set<Set<String>> options = BaseCompression.expandOptions(javaRegionsToOptionSet.values());
        Compression compressor = new SimpleCompression(this.programName, options);
        Set<Set<String>> configurations = compressor.compressConfigurations(args);

        Instrumenter instrumenter = new TimerRegionInstrumenter(this.programName, this.classDir, javaRegionsToOptionSet);
        instrumenter.instrument(args);

        Executor executor = new DefaultExecutor(this.programName, this.entry, this.classDir, configurations);
        Set<PerformanceEntry2> performanceEntries = executor.execute(args);

        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);
        PerformanceModelBuilder builder = new DefaultPerformanceModelBuilder(this.programName, performanceEntries, regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        return performanceModel;
    }

    public void compile() throws IOException, InterruptedException {
        Instrumenter compiler = new CompileInstrumenter(this.srcDir, this.classDir);
        compiler.compileFromSource();
    }

    public void format() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, InterruptedException {
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter compiler = new Formatter(this.srcDir, this.classDir);
        compiler.instrument(args);
    }
}
