package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.RegionTransformer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

public abstract class DynamicRegionTransformer extends RegionTransformer<InfluencingTaints> {

  // TODO delete programName
  private DynamicRegionTransformer(String programName, String entryPoint,
      ClassTransformer classTransformer,
      Map<JavaRegion, InfluencingTaints> regionsToInfluencingTaints) {
    super(programName, entryPoint, classTransformer, regionsToInfluencingTaints, true,
        new DynamicInstructionRegionMatcher());
  }

  DynamicRegionTransformer(String programName, String entryPoint, String directory,
      Map<JavaRegion, InfluencingTaints> regionsToInfluencingTaints)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    this(programName, entryPoint, new DefaultClassTransformer(directory),
        regionsToInfluencingTaints);
  }

  @Override
  public void transformMethods(Set<ClassNode> classNodes) {
    this.setBlocksToDecisions(classNodes);
  }

}
