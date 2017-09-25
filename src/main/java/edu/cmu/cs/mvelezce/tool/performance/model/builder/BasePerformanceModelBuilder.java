package edu.cmu.cs.mvelezce.tool.performance.model.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/28/17.
 */
public abstract class BasePerformanceModelBuilder implements PerformanceModelBuilder {

    private static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs";

    private String programName;
    private Set<PerformanceEntryStatistic> measuredPerformance;
    private Map<Region, Set<Set<String>>> regionsToOptionSet;
    ;

    public BasePerformanceModelBuilder(String programName, Set<PerformanceEntryStatistic> measuredPerformance, Map<Region, Set<Set<String>>> regionsToOptionSet) {
        this.programName = programName;
        this.measuredPerformance = measuredPerformance;
        this.regionsToOptionSet = regionsToOptionSet;
    }

    public String getProgramName() {
        return programName;
    }

    public Set<PerformanceEntryStatistic> getMeasuredPerformance() {
        return measuredPerformance;
    }

    public Map<Region, Set<Set<String>>> getRegionsToOptionSet() {
        return regionsToOptionSet;
    }

    @Override
    public PerformanceModel createModel(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputDir = BasePerformanceModelBuilder.DIRECTORY + "/" + this.programName;
        File outputFile = new File(outputDir);

        Options.checkIfDeleteResult(outputFile);

        if(outputFile.exists()) {
            Collection<File> files = FileUtils.listFiles(outputFile, null, true);

            if(files.size() != 1) {
                throw new RuntimeException("We expected to find 1 file in the directory, but that is not the case "
                        + outputFile);
            }

            return this.readFromFile(files.iterator().next());
        }

        PerformanceModel performanceModel = this.createModel();

        if(Options.checkIfSave()) {
            this.writeToFile(performanceModel);
        }

        return performanceModel;
    }

    @Override
    public void writeToFile(PerformanceModel performanceModel) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = BasePerformanceModelBuilder.DIRECTORY + "/" + this.programName + "/" + this.programName
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        mapper.writeValue(file, performanceModel);
    }


    @Override
    public PerformanceModel readFromFile(File file) throws IOException {
        throw new UnsupportedOperationException("Have not figured out how to deserialize a performance model");
//        ObjectMapper mapper = new ObjectMapper();
//        PerformanceModel performanceModel = mapper.readValue(file, new TypeReference<PerformanceModel>() {
//        });
//
//        return performanceModel;
    }
}
