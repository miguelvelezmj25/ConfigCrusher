package edu.cmu.cs.mvelezce.analysis.pipeline;

import edu.cmu.cs.mvelezce.analysis.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.analysis.taint.Processor;
import edu.cmu.cs.mvelezce.analysis.taint.Region;

import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipeline {

    public static final String LOTRACK_DATABASE = "lotrack";
    public static final String PLAYYPUS_PROGRAM = "platypus";
    public static final String LANGUAGETOOL_PROGRAM = "Languagetool";

    public static PerformanceModel getPerformanceModel() throws NoSuchFieldException {
        try {
            Map<Region, Set<String>> regionsToOptions = Processor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, JavaPipeline.PLAYYPUS_PROGRAM);
        } catch (NoSuchFieldException nsfe) {
            throw nsfe;
//            throw new NoSuchFieldException("s", nsfe);
        }


        return null;
    }
}
