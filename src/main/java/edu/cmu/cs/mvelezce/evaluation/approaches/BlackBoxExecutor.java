package edu.cmu.cs.mvelezce.evaluation.approaches;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.execute.java.BaseExecutor;
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

public class BlackBoxExecutor extends BaseExecutor {

    public BlackBoxExecutor(String programName) {
        this(programName, null, null, null);
    }

    public BlackBoxExecutor(String programName, String entryPoint, String classDir, Set<Set<String>> configurations) {
        super(programName, entryPoint, classDir, configurations);
    }

    @Override
    public Set<DefaultPerformanceEntry> execute(int iteration) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public Set<PerformanceEntryStatistic> execute(String[] args) throws IOException, InterruptedException {
        String outputDir = this.getOutputDir() + "/" + this.getProgramName();
        File root = new File(outputDir);

        if(!root.exists()) {
            throw new RuntimeException("Could not find the root dir");
        }

        Collection<File> files = FileUtils.listFilesAndDirs(root, new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);
        List<File> filesList = new ArrayList<>(files);
        Collections.sort(filesList);

        File lastFile = filesList.get(filesList.size() - 1);
        String lastFilePath = lastFile.getPath();
        int lastSlashIndex = lastFilePath.lastIndexOf("/");
        String number = lastFilePath.substring(lastSlashIndex + 1).trim();

        if(!StringUtils.isNumeric(number)) {
            throw new RuntimeException("The last directory is not a sample");
        }

        Set<DefaultPerformanceEntry> results = this.aggregateExecutions(lastFile);

        List<Set<DefaultPerformanceEntry>> performanceEntriesList = new ArrayList<>();
        performanceEntriesList.add(results);

        Set<PerformanceEntryStatistic> averagedPerformanceEntries = this.averageExecutions(performanceEntriesList);
        return averagedPerformanceEntries;
    }

    @Override
    public String getOutputDir() {
        return BaseExecutor.DIRECTORY + "/bruteforce/programs";
    }
}
