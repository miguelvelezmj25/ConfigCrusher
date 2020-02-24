package edu.cmu.cs.mvelezce.learning.builder.generate.data;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.approaches.sampling.fw.FeatureWiseSampling;
import edu.cmu.cs.mvelezce.approaches.sampling.pw.PairWiseSampling;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.fw.FeatureWiseInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.pw.PairWiseInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.fw.FeatureWiseTimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.pw.PairWiseTimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CSVDataGeneratorTest {

  @Test
  public void berkeleyDB_FW_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();

    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new FeatureWiseInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    CSVDataGenerator generator =
        new CSVDataGenerator(programName, options, performanceEntries, samplingApproach);
    generator.generateCSVFile();
  }

  @Test
  public void berkeleyDB_PW_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();

    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new PairWiseInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    CSVDataGenerator generator =
        new CSVDataGenerator(programName, options, performanceEntries, samplingApproach);
    generator.generateCSVFile();
  }

  @Test
  public void lucene_FW_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();

    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new FeatureWiseInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    CSVDataGenerator generator =
        new CSVDataGenerator(programName, options, performanceEntries, samplingApproach);
    generator.generateCSVFile();
  }

  @Test
  public void lucene_PW_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();

    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new PairWiseInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    CSVDataGenerator generator =
        new CSVDataGenerator(programName, options, performanceEntries, samplingApproach);
    generator.generateCSVFile();
  }

  @Test
  public void convert_FW_time() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();

    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new FeatureWiseTimePerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    CSVDataGenerator generator =
        new CSVDataGenerator(programName, options, performanceEntries, samplingApproach);
    generator.generateCSVFile();
  }

  @Test
  public void convert_PW_time() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();

    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new PairWiseTimePerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    CSVDataGenerator generator =
        new CSVDataGenerator(programName, options, performanceEntries, samplingApproach);
    generator.generateCSVFile();
  }
}
