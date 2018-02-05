package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.BlackBoxExecutor;
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

public class FeaturewiseExecutor extends BlackBoxExecutor {

    public FeaturewiseExecutor(String programName) {
        this(programName, null, null, null);
    }

    public FeaturewiseExecutor(String programName, String entryPoint, String classDir, Set<Set<String>> configurations) {
        super(programName, entryPoint, classDir, configurations);
    }

}
