package edu.cmu.cs.mvelezce.evaluation.approaches.groundtruth.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GroundTruthEvaluationExecutor extends BruteForceExecutor {

    public GroundTruthEvaluationExecutor(String programName) {
        super(programName);
    }

    @Override
    public Set<PerformanceEntryStatistic> execute(String[] args) throws IOException, InterruptedException {
        String outputDir = this.getOutputDir() + "/" + this.getProgramName();
        File root = new File(outputDir);

        if(!root.exists()) {
            throw new RuntimeException("Could not find the root dir");
        }

        Collection<File> files = FileUtils.listFilesAndDirs(root, new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);

        List<File> filesList = new ArrayList<>();

        for(File file : files) {
            String name = file.getName();

            if(StringUtils.isNumeric(name)) {
                filesList.add(file);
            }
        }

        Collections.sort(filesList);

        List<Set<DefaultPerformanceEntry>> performanceEntriesList = new ArrayList<>();

        for(int i = 0; i < (filesList.size() - 1); i++) {
            File outputFile = new File(root + "/" + i);

            Set<DefaultPerformanceEntry> results = this.aggregateExecutions(outputFile);
            performanceEntriesList.add(results);
        }

        Set<PerformanceEntryStatistic> averagedPerformanceEntries = this.averageExecutions(performanceEntriesList);
        return averagedPerformanceEntries;
    }
}
