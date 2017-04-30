package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Executer {

    public static final String DIRECTORY = Options.DIRECTORY + "/executer/java/programs";

    // JSON strings
    public static final String MEASURED_PERFORMANCE = "measuredPerformance";
    public static final String CONFIGURATION = "configuration";
    public static final String PROGRAM = "program";
    public static final String ID = "ID";
    public static final String REGIONS = "regions";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";

    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String[] args, String mainClass, String directory, Set<Set<String>> configurationsToExecute) throws IOException {
        Options.getCommandLine(args);

        String outputFile = Executer.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

//        if(file.exists()) {
//            try {
//                return Executer.readFromFile(file);
//            }
//            catch (ParseException pe) {
//                throw new RuntimeException("Could not parse the cached results");
//            }
//        }

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(mainClass, directory, configurationsToExecute);

        if(Options.checkIfSave()) {
            Executer.writeToFile(programName, measuredPerformance);
        }

        return measuredPerformance;
    }



    public static Set<PerformanceEntry> measureConfigurationPerformance(String mainClass, String directory, Set<Set<String>> configurationsToExecute) {
        JavaRegion program = new JavaRegion(mainClass, mainClass);
        Regions.addProgram(program);

        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for(Set<String> configuration : configurationsToExecute) {
            Regions.resetRegions();

            // TODO factory pattern or switch statement to create the right adapter
            Adapter adapter = new SleepAdapter(mainClass, directory);
            adapter.execute(configuration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }

            // TODO parse output from file
            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram()));
        }

        return configurationsToPerformance;
    }

    private static void writeToFile(String programName, Set<PerformanceEntry> measuredPerformance) throws IOException {
        JSONArray measuredPerformances = new JSONArray();

        for(PerformanceEntry measuredEntry : measuredPerformance) {
            JSONObject performanceEntry = new JSONObject();
            JSONArray values = new JSONArray();

            for(String value : measuredEntry.getConfiguration()) {
                values.add(value);
            }

            performanceEntry.put(Executer.CONFIGURATION, values);

            JSONObject program = new JSONObject();
            Region measuredProgram = measuredEntry.getProgram();
            program.put(Executer.ID, measuredProgram.getRegionID());
            program.put(Executer.START_TIME, measuredProgram.getStartTime());
            program.put(Executer.END_TIME, measuredProgram.getEndTime());

            performanceEntry.put(Executer.PROGRAM, program);


            JSONArray regions = new JSONArray();

            for(Region measuredRegion : measuredEntry.getRegions()) {
                JSONObject region = new JSONObject();
                region.put(Executer.ID, measuredRegion.getRegionID());
                region.put(Executer.START_TIME, measuredRegion.getStartTime());
                region.put(Executer.END_TIME, measuredRegion.getEndTime());

                regions.add(region);
            }

            performanceEntry.put(Executer.REGIONS, regions);


            measuredPerformances.add(performanceEntry);
        }

        JSONObject result = new JSONObject();
        result.put(Executer.MEASURED_PERFORMANCE, measuredPerformances);

        File directory = new File(Executer.DIRECTORY);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = Executer.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);
        FileWriter writer = new FileWriter(file);
        writer.write(result.toJSONString());
        writer.flush();
        writer.close();
    }

}
