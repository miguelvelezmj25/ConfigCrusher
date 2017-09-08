package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceEntry2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class Evaluation {

    public static final String DIRECTORY = Options.DIRECTORY + "/evaluation/programs/java";
    public static final String COMPARISON_DIR = "/comparison";
    public static final String FULL_DIR = "/full";
    public static final String DOT_CSV = ".csv";

    // TODO use a class or enum>
    public static final String APPROACH = "approach";
    public static final String BRUTE_FORCE = "brute_force";

    private String programName;

    public Evaluation(String programName) {
        this.programName = programName;
    }

    public void writeConfigurationToPerformance(String approach, Set<PerformanceEntry2> performanceEntries) throws IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + "/" + Evaluation.FULL_DIR + "/"
                + approach + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performance");//;,std");
        result.append("\n");

        for(PerformanceEntry2 performanceEntry : performanceEntries) {
            if(performanceEntry.getRegionsToProcessedPerformanceHumanReadable().size() != 1) {
                throw new RuntimeException("This method can only handle approaches that measure 1 region" +
                        " (e.g. Brute force)");
            }
//            if(perfStat.getRegionsToMean().size() != 1) {
//                throw new RuntimeException("The performancemodel entry should only have measured the entire program " + perfStat.getRegionsToMean().keySet());
//            }
//
////            perfStat.setMeasured("true");
            result.append("true");
            result.append(",");
            result.append('"');
            result.append(performanceEntry.getConfiguration());
            result.append('"');
            result.append(",");
            result.append(performanceEntry.getRegionsToProcessedPerformanceHumanReadable().values().iterator().next());
//            result.append(",");
//            result.append(perfStat.getRegionsToStd().values().iterator().next() / 1000000000.0);
            result.append("\n");
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile, true);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

//    public void writeConfigurationToPerformance(PerformanceModel performanceModel) {
//        File file = new File(BruteForce.BF_RES_DIR + "/" + programName + Options.DOT_CSV);
//
//        if(file.exists()) {
//            if(!file.delete()) {
//                throw new RuntimeException("Could not delete " + file);
//            }
//        }
//
//        StringBuilder result = new StringBuilder();
//        result.append("measured,configuration,performancemodel,std");
//        result.append("\n");
//
//        for(PerformanceStatistic perfStat : perfStats) {
//            if(perfStat.getRegionsToMean().size() != 1) {
//                throw new RuntimeException("The performancemodel entry should only have measured the entire program " + perfStat.getRegionsToMean().keySet());
//            }
//
////            perfStat.setMeasured("true");
//            result.append("true");
//            result.append(",");
//            result.append('"');
//            result.append(perfStat.getConfiguration());
//            result.append('"');
//            result.append(",");
//            result.append(perfStat.getRegionsToMean().values().iterator().next() / 1000000000.0);
//            result.append(",");
//            result.append(perfStat.getRegionsToStd().values().iterator().next() / 1000000000.0);
//            result.append("\n");
//        }
//
//        File directory = new File(BruteForce.BF_RES_DIR);
//
//        if(!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String outputFile = directory + "/" + programName + Options.DOT_CSV;
//        file = new File(outputFile);
//        FileWriter writer = new FileWriter(file, true);
//        writer.write(result.toString());
//        writer.flush();
//        writer.close();
//    }

}
