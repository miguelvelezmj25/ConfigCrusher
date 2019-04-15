package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;

public abstract class DynamicConfigCrusherRegionTransformer extends DynamicRegionTransformer {

  public DynamicConfigCrusherRegionTransformer(String programName, String entryPoint,
      String directory, Map<JavaRegion, InfluencingTaints> regionsToInfluencingTaints)
      throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
    super(programName, entryPoint, directory, regionsToInfluencingTaints);
  }

}
