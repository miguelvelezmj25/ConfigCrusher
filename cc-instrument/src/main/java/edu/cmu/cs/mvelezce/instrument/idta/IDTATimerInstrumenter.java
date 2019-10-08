package edu.cmu.cs.mvelezce.instrument.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.idta.transform.IDTAMethodTransformer;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.instrumenter.transform.methodnode.MethodTransformer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class IDTATimerInstrumenter extends BaseRegionInstrumenter<Set<FeatureExpr>> {

  IDTATimerInstrumenter(
      String programName,
      String mainClass,
      String srcDir,
      String classDir,
      Set<String> options,
      Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints) {
    super(programName, mainClass, srcDir, classDir, options, regionsToConstraints);
    System.err.println(
        "Remember that we are instrumenting blocks. Therefore, we might not need to know the start index of a region; only the start and end blocks");
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
                this.getRegionsToData())
            .setDebug(true)
            .build();
    transformer.transformMethods();
  }

  @Override
  public void compile() throws IOException, InterruptedException {
    System.err.println("Delete method once we are done with testing instrumenting");
    //    super.compile();
  }
}
