/**
 * This file is part of the Joana IFC project. It is developed at the
 * Programming Paradigms Group of the Karlsruhe Institute of Technology.
 * <p>
 * For further details on licensing please read the information at
 * http://joana.ipd.kit.edu or contact the authors.
 */
package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

/**
 * Utility class to build SDGs for tests.
 *
 * @author Simon Bischof <simon.bischof@kit.edu>
 */
public final class BuildSDG {

//	private static final Stubs STUBS = Stubs.JRE_14;
//
//	public static final SDGConfig top_sequential = new SDGConfig(JoanaPath.JOANA_API_TEST_DATA_CLASSPATH, null, STUBS, ExceptionAnalysis.INTERPROC,
//			FieldPropagation.OBJ_GRAPH, PointsToPrecision.OBJECT_SENSITIVE, false, // no
//																					// access
//																					// paths
//			false, // no interference
//			MHPType.NONE);
//
//	public static final SDGConfig bottom_sequential = new SDGConfig(JoanaPath.JOANA_API_TEST_DATA_CLASSPATH, null, STUBS,
//			ExceptionAnalysis.ALL_NO_ANALYSIS, FieldPropagation.OBJ_GRAPH, PointsToPrecision.TYPE_BASED, false, // no
//																											// access
//																											// paths
//			false, // no interference
//			MHPType.NONE);
//
//	public static final SDGConfig top_concurrent = new SDGConfig(JoanaPath.JOANA_API_TEST_DATA_CLASSPATH, null, STUBS, ExceptionAnalysis.INTERPROC,
//			FieldPropagation.OBJ_GRAPH, PointsToPrecision.OBJECT_SENSITIVE, false, // no
//																					// access
//																					// paths
//			true, // interference
//			MHPType.PRECISE);
//
//	public static final SDGConfig bottom_concurrent = new SDGConfig(JoanaPath.JOANA_API_TEST_DATA_CLASSPATH, null, STUBS,
//			ExceptionAnalysis.ALL_NO_ANALYSIS, FieldPropagation.OBJ_GRAPH, PointsToPrecision.TYPE_BASED, false, // no
//																											// access
//																											// paths
//			true, // interference
//			MHPType.SIMPLE);
//
//	private BuildSDG() {
//
//	}
//
//	public static void saveSDGProgram(SDG sdg, String path) throws FileNotFoundException {
//		SDGSerializer.toPDGFormat(sdg, new BufferedOutputStream(new FileOutputStream(path)));
//	}
//
//	public static void standardConcBuild(String classPath, JavaMethodSignature entryMethod, String saveAs, PointsToPrecision ptsPrec) {
//		SDGConfig cfg = new SDGConfig(classPath, entryMethod.toBCString(), STUBS);
//		cfg.setComputeInterferences(true);
//		cfg.setExceptionAnalysis(ExceptionAnalysis.IGNORE_ALL);
//		cfg.setMhpType(MHPType.NONE);
//		cfg.setFieldPropagation(FieldPropagation.OBJ_GRAPH);
//		cfg.setPointsToPrecision(ptsPrec);
//		SDGProgram p;
//		try {
//			p = SDGProgram.createSDGProgram(cfg, new PrintStream(new ByteArrayOutputStream()),
//					NullProgressMonitor.INSTANCE);
//			SDG sdg = p.getSDG();
//			PruneInterferences.preprocessAndPruneCSDG(sdg, MHPType.PRECISE);
//			saveSDGProgram(sdg, saveAs);
//		} catch (ClassHierarchyException | IOException | UnsoundGraphException
//					| CancelException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	public static void standardConcBuild(String classPath, String mainClass, String saveAs) {
//		standardConcBuild(classPath, JavaMethodSignature.mainMethodOfClass(mainClass), saveAs, PointsToPrecision.INSTANCE_BASED);
//	}
//
//	public static <T> IFCAnalysis build(Class<T> clazz, SDGConfig config, boolean ignore) throws ClassHierarchyException, IOException, UnsoundGraphException, CancelException {
//		final String className = clazz.getCanonicalName();
//		final String classPath;
//		if (ignore) {
//			classPath = JoanaPath.JOANA_API_TEST_DATA_CLASSPATH + File.pathSeparator + JoanaPath.ANNOTATIONS_IGNORE_CLASSPATH;
//		} else {
//			classPath = JoanaPath.JOANA_API_TEST_DATA_CLASSPATH + File.pathSeparator + JoanaPath.ANNOTATIONS_PASSON_CLASSPATH;
//		}
//		config.setClassPath(classPath);
//		JavaMethodSignature mainMethod = JavaMethodSignature.mainMethodOfClass(className);
//		config.setEntryMethod(mainMethod.toBCString());
//
//		SDGProgram prog = SDGProgram.createSDGProgram(config);
//
//		return new IFCAnalysis(prog);
//	}
//
////	public static <T> IFCAnalysis buldAndUseJavaAnnotations(Class<T> clazz, SDGConfig config, boolean ignore)
////				throws ApiTestException, ClassHierarchyException, IOException, UnsoundGraphException, CancelException {
////			IFCAnalysis ana = build(clazz,config,ignore);
////			ana.addAllJavaSourceAnnotations();
////			return ana;
////	}
}
