package edu.cmu.cs.mvelezce.instrument.idta;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.instrument.idta.transform.IDTAMethodTransformer;
import edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.IDTAMethodInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.utils.results.RegionConstraintPretty;
import edu.cmu.cs.mvelezce.instrumenter.transform.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.utils.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IDTATimerInstrumenter extends BaseRegionInstrumenter<Set<FeatureExpr>> {

  private final IDTAMethodInstrumenter idtaMethodInstrumenter;

  IDTATimerInstrumenter(
      String programName,
      String mainClass,
      String srcDir,
      String classDir,
      Set<String> options,
      Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints,
      IDTAMethodInstrumenter idtaMethodInstrumenter) {
    super(programName, mainClass, srcDir, classDir, options, regionsToConstraints);
    System.err.println(
        "Remember that we are instrumenting blocks. Therefore, we might not need to know the start index of a region; only the start and end blocks");
    System.err.println("We are not deleting the .dot and .pdf files");

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
            .setDebug(true)
            .build();
    transformer.transformMethods();
  }

  @Override
  public void compile() throws IOException, InterruptedException {
    System.err.println("Delete method once we are done with testing instrumenting");
    super.compile();
  }

  @Override
  protected void writeToFile(Map<JavaRegion, Set<FeatureExpr>> regionsToData) throws IOException {
    System.err.println(
        "The index of the regions might not be accurate. Not sure at the moment if we need the index for later analysis or understanding");
    String outputFile =
        IDTAMethodTransformer.DEBUG_DIR
            + "/"
            + this.getProgramName()
            + "/"
            + this.getProgramName()
            + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    Set<RegionConstraintPretty> regionsToPrettyConstraints = new HashSet<>();

    for (Map.Entry<JavaRegion, Set<FeatureExpr>> entry : regionsToData.entrySet()) {
      Set<String> prettyConstraints = new HashSet<>();
      Set<FeatureExpr> constraints = entry.getValue();

      for (FeatureExpr constraint : constraints) {
        String prettyConstraint =
            ConstraintUtils.prettyPrintFeatureExpr(constraint, this.getOptions());
        prettyConstraints.add(prettyConstraint);
      }

      JavaRegion region = entry.getKey();
      RegionConstraintPretty regionToPrettyConstraints =
          new RegionConstraintPretty(
              region.getRegionPackage(),
              region.getRegionClass(),
              region.getRegionMethod(),
              region.getStartIndex(),
              prettyConstraints,
              region.getId().toString());

      regionsToPrettyConstraints.add(regionToPrettyConstraints);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, regionsToPrettyConstraints);
  }

  @Override
  protected Map<JavaRegion, Set<FeatureExpr>> readFromFile(File file) {
    throw new UnsupportedOperationException("Implemenet");
  }
}
