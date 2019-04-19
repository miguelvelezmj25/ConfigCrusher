package edu.cmu.cs.mvelezce.tool.instrumentation.java.region.timer;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.DynamicBaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.dynamic.DynamicConfigCrusherTimerTransformer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DynamicConfigCrusherTimerRegionInstrumenter extends DynamicBaseRegionInstrumenter {

  private final String entryPoint;
  private final String rootPackage;

  public DynamicConfigCrusherTimerRegionInstrumenter(String programName, String entryPoint,
      String rootPackage, String classDir,
      Map<JavaRegion, InfluencingTaints> regionsToInfluencingTaints) {
    super(programName, classDir, regionsToInfluencingTaints);

    this.entryPoint = entryPoint;
    this.rootPackage = rootPackage;
  }

  @Override
  public void instrument()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    MethodTransformer methodTransformer = new DynamicConfigCrusherTimerTransformer(
        this.getProgramName(), this.entryPoint, this.rootPackage, this.getClassDir(),
        this.getRegionsToData());
    methodTransformer.transformMethods();
  }

  @Override
  public void compile() {
  }
}
