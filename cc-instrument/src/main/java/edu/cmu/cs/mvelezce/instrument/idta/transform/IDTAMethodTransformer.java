package edu.cmu.cs.mvelezce.instrument.idta.transform;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.RegionTransformer;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.instructionRegionMatcher.dynamic.DynamicInstructionRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.down.BaseDownExpander;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.down.idta.IDTADownExpander;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.up.BaseUpExpander;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.up.idta.IDTAUpExpander;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.utils.Options;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

public class IDTAMethodTransformer extends RegionTransformer<Set<FeatureExpr>> {

  private final BaseUpExpander<Set<FeatureExpr>> upExpander;
  private final BaseDownExpander<Set<FeatureExpr>> downExpander;

  private IDTAMethodTransformer(Builder builder)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException,
          InvocationTargetException {
    super(
        builder.programName,
        new DefaultClassTransformer(builder.classDir),
        builder.mainClass,
        builder.debug,
        builder.regionsToConstraints,
        new DynamicInstructionRegionMatcher());

    this.upExpander =
        new IDTAUpExpander(builder.options, this.getBlockRegionMatcher(), this.getRegionsToData());
    this.downExpander =
        new IDTADownExpander(
            builder.options, this.getBlockRegionMatcher(), this.getRegionsToData());
  }

  @Override
  protected String getDebugDir() {
    return Options.DIRECTORY + "/instrument/idta/java/programs";
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    super.transformMethod(methodNode, classNode);

    this.upExpander.processBlocks(methodNode, classNode);
    this.downExpander.processBlocks(methodNode, classNode);

    methodNode.visitMaxs(200, 200);
  }

  public static class Builder {

    private final String programName;
    private final String mainClass;
    private final String classDir;
    private final Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints;
    private final Set<String> options;

    private boolean debug = false;

    public Builder(
        String programName,
        String mainClass,
        String classDir,
        Set<String> options,
        Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints) {
      this.programName = programName;
      this.mainClass = mainClass;
      this.classDir = classDir;
      this.regionsToConstraints = regionsToConstraints;
      this.options = options;
    }

    public Builder setDebug(boolean debug) {
      this.debug = debug;
      return this;
    }

    public IDTAMethodTransformer build()
        throws InvocationTargetException, NoSuchMethodException, MalformedURLException,
            IllegalAccessException {
      return new IDTAMethodTransformer(this);
    }
  }
}
