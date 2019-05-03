package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

public abstract class DynamicConfigCrusherRegionTransformer extends DynamicRegionTransformer {

  public DynamicConfigCrusherRegionTransformer(String programName, String entryPoint,
      String rootPackage, String directory,
      Map<JavaRegion, Set<Set<String>>> regionsToInfluencingTaints)
      throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
    super(programName, entryPoint, rootPackage, directory, regionsToInfluencingTaints);
  }

}
