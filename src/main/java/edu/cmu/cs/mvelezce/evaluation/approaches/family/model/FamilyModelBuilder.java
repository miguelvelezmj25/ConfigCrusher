package edu.cmu.cs.mvelezce.evaluation.approaches.family.model;

import edu.cmu.cs.mvelezce.evaluation.approaches.family.Family;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.BasePerformanceModelBuilder;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class FamilyModelBuilder extends BasePerformanceModelBuilder {

    public static final Set<String> BASE = new HashSet<>(Collections.singletonList("BASE"));

    public static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs/family";

    public FamilyModelBuilder(String programName) {
        super(programName);
    }

    public PerformanceModel createModel() {
        Map<Set<String>, Double> model;

        try {
            model = this.getModel();
        } catch(IOException e) {
            throw new RuntimeException("Coule not get the family model");
        }

        PerformanceModel pm = new FamilyModel();

        if(!model.containsKey(BASE)) {
            throw new RuntimeException("The model does not have a base feature");
        }

        pm.setBaseTimeHumanReadable(model.get(BASE));
        model.remove(BASE);

        Region progRegion= new Region("program");
        Map<Region, Map<Set<String>, Double>> regionMap = new HashMap<>();
        regionMap.put(progRegion, model);
        pm.setRegionsToPerformanceTablesHumanReadable(regionMap);

        return pm;
    }


    private Map<Set<String>, Double> getModel() throws IOException {
        List<Map<Set<String>, Double>> models = new ArrayList<>();
        File dir = new File(Family.DIR + "/" + this.getProgramName() + "/");
        Collection<File> files = FileUtils.listFiles(dir, new String[]{"txt"}, true);

        for(File file : files) {
            Map<Set<String>, Double> model = this.parseModel(file);
            models.add(model);
        }

        return this.averageModels(models);
    }

    private Map<Set<String>, Double> averageModels(List<Map<Set<String>, Double>> models) {
        Map<Set<String>, Double> model = new HashMap<>();

        for(Set<String> options : models.get(0).keySet()) {
            model.put(options, 0.0);
        }

        for(Map<Set<String>, Double> aModel : models) {
            for(Map.Entry<Set<String>, Double> entry : aModel.entrySet()) {
                double cur = model.get(entry.getKey());
                cur += entry.getValue();
                model.put(entry.getKey(), cur);
            }
        }

        for(Map.Entry<Set<String>, Double> entry : model.entrySet()) {
            model.put(entry.getKey(), entry.getValue() / models.size());
        }

        return model;
    }

    private Map<Set<String>, Double> parseModel(File file) throws IOException {
        Map<Set<String>, Double> model = new HashMap<>();

        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine = br.readLine();
        br.close();

        String[] terms = strLine.split(";");

        for(String term : terms) {
            term = term.trim();

            if(term.isEmpty()) {
                continue;
            }

            Set<String> options = this.getOptions(term);
            double time = this.getTime(term);

            model.put(options, time);
        }

        return model;
    }

    private double getTime(String term) {
        int start = term.indexOf("<");
        int end = term.indexOf(">");
        String time = term.substring(start + 1, end);

        return Long.valueOf(time) / 1000.0;
    }

    private Set<String> getOptions(String term) {
        Set<String> options = new HashSet<>();
        int start = term.indexOf("<");
        term = term.substring(0, start);
        String[] features = term.split("#");

        for(String feature : features) {
            options.add(feature.trim().toUpperCase());
        }

        return options;
    }

    @Override
    public String getOutputDir() {
        return FamilyModelBuilder.DIRECTORY;
    }

//    @Override
//    public PerformanceModel createModel(String[] args) throws IOException {
//        throw new UnsupportedOperationException();
//    }
//    @Override
//    public void writeToFile(PerformanceModel performanceModel) throws IOException {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public PerformanceModel readFromFile(File file) throws IOException {
//        throw new UnsupportedOperationException();
//    }
}
