package edu.cmu.cs.mvelezce.instrument.idta.transform;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.IDTAMethodInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.transformer.RegionTransformer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher.dynamic.DynamicInstructionRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.cg.SootCallGraphBuilder;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter.BaseInterExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter.idta.BaseIDTAInterExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.down.BaseDownIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.down.idta.IDTADownIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.up.BaseUpIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.up.idta.IDTAUpIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.inter.BaseRemoveNestedRegionsInter;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.inter.idta.IDTARemoveNestedRegionsInter;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.intra.BaseRemoveNestedRegionsIntra;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.intra.idta.IDTARemoveNestedRegionsIntra;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.startEndBlocksSetter.BaseStartEndRegionBlocksSetter;
import edu.cmu.cs.mvelezce.instrument.region.utils.startEndBlocksSetter.idta.IDTAStartEndRegionBlocksSetter;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.utils.Options;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class IDTAMethodTransformer extends RegionTransformer<Set<FeatureExpr>> {

  public static final String DEBUG_DIR = Options.DIRECTORY + "/instrument/idta/java/programs";

  private final BaseUpIntraExpander<Set<FeatureExpr>> upIntraExpander;
  private final BaseDownIntraExpander<Set<FeatureExpr>> downIntraExpander;
  private final BaseInterExpander<Set<FeatureExpr>> interExpander;
  private final BaseStartEndRegionBlocksSetter<Set<FeatureExpr>> startEndRegionBlocksSetter;
  private final BaseRemoveNestedRegionsInter<Set<FeatureExpr>> removeNestedRegionsInter;
  private final IDTAMethodInstrumenter idtaMethodInstrumenter;
  private final CallGraph callGraph;
  private final SootAsmMethodMatcher sootAsmMethodMatcher;

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

    BaseIDTAExpander baseIDTAExpander = BaseIDTAExpander.getInstance();
    baseIDTAExpander.init(this.getRegionsToData().values());

    this.upIntraExpander =
        new IDTAUpIntraExpander(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            baseIDTAExpander);
    this.downIntraExpander =
        new IDTADownIntraExpander(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            baseIDTAExpander);

    BaseRemoveNestedRegionsIntra<Set<FeatureExpr>> idtaRemoveNestedRegionsIntra =
        new IDTARemoveNestedRegionsIntra(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            baseIDTAExpander);
    this.startEndRegionBlocksSetter =
        new IDTAStartEndRegionBlocksSetter(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            idtaRemoveNestedRegionsIntra,
            baseIDTAExpander);

    this.sootAsmMethodMatcher = SootAsmMethodMatcher.getInstance();
    this.callGraph = SootCallGraphBuilder.buildCallGraph(builder.mainClass, builder.classDir);

    this.removeNestedRegionsInter =
        new IDTARemoveNestedRegionsInter(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            this.sootAsmMethodMatcher,
            this.callGraph,
            baseIDTAExpander);

    this.interExpander =
        new BaseIDTAInterExpander(
            builder.programName,
            DEBUG_DIR,
            builder.options,
            this.getBlockRegionMatcher(),
            this.getRegionsToData(),
            this.callGraph,
            this.sootAsmMethodMatcher,
            baseIDTAExpander);

    this.idtaMethodInstrumenter = builder.idtaMethodInstrumenter;
  }

  @Override
  protected String getDebugDir() {
    return DEBUG_DIR;
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
        this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(methodNode);
    this.idtaMethodInstrumenter.instrument(methodNode, classNode, blocksToRegions);
  }

  @Override
  protected void transformRegions(Set<ClassNode> classNodes) {
    this.sootAsmMethodMatcher.init(this.callGraph, classNodes);

    boolean propagatedRegions = true;

    while (propagatedRegions) {
      propagatedRegions = this.propagateRegionsIntra(classNodes);
      propagatedRegions = propagatedRegions | this.propagateRegionsInter(classNodes);
    }

    this.removeNestedRegionsInter(classNodes);
    this.setStartAndEndBlocks(classNodes);
  }

  private void removeNestedRegionsInter(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToProcess = this.getMethodsToInstrument(classNode);

      if (methodsToProcess.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToProcess) {
        this.removeNestedRegionsInter.processBlocks(methodNode, classNode);

        if (this.debug()) {
          this.removeNestedRegionsInter.debugBlockData(methodNode, classNode);
        }
      }
    }
  }

  private boolean propagateRegionsInter(Set<ClassNode> classNodes) {
    boolean propagatedRegions = false;

    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToProcess = this.getMethodsToInstrument(classNode);

      if (methodsToProcess.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToProcess) {
        propagatedRegions = propagatedRegions | this.expandRegionsInter(methodNode, classNode);
      }
    }

    return propagatedRegions;
  }

  private boolean expandRegionsInter(MethodNode methodNode, ClassNode classNode) {
    return this.interExpander.processBlocks(methodNode, classNode);
  }

  private void setStartAndEndBlocks(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToProcess = this.getMethodsToInstrument(classNode);

      if (methodsToProcess.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToProcess) {
        this.startEndRegionBlocksSetter.processBlocks(methodNode, classNode);

        if (this.debug()) {
          this.startEndRegionBlocksSetter.debugBlockData(methodNode, classNode);
        }
      }
    }
  }

  private boolean propagateRegionsIntra(Set<ClassNode> classNodes) {
    boolean propagatedRegions = false;

    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToProcess = this.getMethodsToInstrument(classNode);

      if (methodsToProcess.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToProcess) {
        propagatedRegions = propagatedRegions | this.expandRegionsIntra(methodNode, classNode);
      }
    }

    return propagatedRegions;
  }

  private boolean expandRegionsIntra(MethodNode methodNode, ClassNode classNode) {
    boolean propagatedRegions = false;
    boolean updatedBlocks = true;

    while (updatedBlocks) {
      updatedBlocks = this.upIntraExpander.processBlocks(methodNode, classNode);
      updatedBlocks = updatedBlocks | this.downIntraExpander.processBlocks(methodNode, classNode);

      if (updatedBlocks) {
        propagatedRegions = true;
      }
    }

    if (this.debug()) {
      this.upIntraExpander.debugBlockData(methodNode, classNode);
      this.upIntraExpander.validateAllBlocksHaveRegions(methodNode, classNode);
    }

    return propagatedRegions;
  }

  public static class Builder {

    private final String programName;
    private final String mainClass;
    private final String classDir;
    private final Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints;
    private final Set<String> options;
    private final IDTAMethodInstrumenter idtaMethodInstrumenter;

    private boolean debug = false;

    public Builder(
        String programName,
        String mainClass,
        String classDir,
        Set<String> options,
        Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints,
        IDTAMethodInstrumenter idtaMethodInstrumenter) {
      this.programName = programName;
      this.mainClass = mainClass;
      this.classDir = classDir;
      this.regionsToConstraints = regionsToConstraints;
      this.options = options;
      this.idtaMethodInstrumenter = idtaMethodInstrumenter;
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
