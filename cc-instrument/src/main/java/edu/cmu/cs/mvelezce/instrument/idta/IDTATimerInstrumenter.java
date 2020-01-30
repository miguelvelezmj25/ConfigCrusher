package edu.cmu.cs.mvelezce.instrument.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.idta.transform.IDTAMethodTransformer;
import edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.IDTAMethodInstrumenter;
import edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.time.IDTAExecutionTimeMethodInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.instrumenter.transform.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.utils.RegionsWithPartitionsUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IDTATimerInstrumenter extends BaseRegionInstrumenter<Partitioning> {

  private final IDTAMethodInstrumenter idtaMethodInstrumenter;

  public IDTATimerInstrumenter(String programName) {
    this(
        programName,
        "",
        "",
        "",
        new HashSet<>(),
        new HashMap<>(),
        new IDTAExecutionTimeMethodInstrumenter());
  }

  IDTATimerInstrumenter(
      String programName,
      String mainClass,
      String srcDir,
      String classDir,
      Set<String> options,
      Map<JavaRegion, Partitioning> regionsToPartitions,
      IDTAMethodInstrumenter idtaMethodInstrumenter) {
    super(programName, mainClass, srcDir, classDir, options, regionsToPartitions);
    System.err.println(
        "Remember that we are instrumenting blocks. Therefore, we might not need to know the start index of a region; only the start and end blocks");
    System.err.println("We are not deleting the .dot and .pdf files");
    System.err.println(
        "If we are using a profiler and not instrumenting for perf measurement, then we do not need to have this logic in instrumenting, but rather just propagation of regions intra.");

    this.idtaMethodInstrumenter = idtaMethodInstrumenter;
  }

  @Override
  public void instrument()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    MethodTransformer transformer =
        new IDTAMethodTransformer.Builder(
                this.getProgramName(),
                this.getMainClass(),
                this.getClassDir(),
                this.getOptions(),
                this.getRegionsToData(),
                this.idtaMethodInstrumenter)
            .setDebug(false)
            .build();
    transformer.transformMethods();
  }

  @Override
  public void compile() throws IOException, InterruptedException {
    //    System.err.println("Delete method once we are done with testing instrumenting");
    //    super.compile();
  }

  @Override
  protected void writeToFile(Map<JavaRegion, Partitioning> regionsToData) throws IOException {
    System.err.println(
        "The index of the regions might not be accurate. Not sure at the moment if we need the index for later analysis or understanding");
    RegionsWithPartitionsUtils.writeToFile(regionsToData, this.getOutputFile(), this.getOptions());
  }

  @Override
  protected Map<JavaRegion, Partitioning> readFromFile(File file) throws IOException {
    return RegionsWithPartitionsUtils.readFromFile(file);
  }

  @Override
  public Map<JavaRegion, Partitioning> getProcessedRegionsToData() throws IOException {
    String outputDir = this.getOutputFile();
    File outputFile = new File(outputDir);

    if (!outputFile.exists()) {
      throw new RuntimeException("There is no data for " + this.getProgramName());
    }

    return this.readFromFile(outputFile);
  }

  private String getOutputFile() {
    return IDTAMethodTransformer.DEBUG_DIR
        + "/"
        + this.getProgramName()
        + "/"
        + this.getProgramName()
        + Options.DOT_JSON;
  }
}
