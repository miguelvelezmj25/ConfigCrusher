package edu.cmu.cs.mvelezce.evaluation.approaches.family;

import edu.cmu.cs.mvelezce.tool.execute.java.BaseExecutor;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class Family {

    public static final String DIR = BaseExecutor.DIRECTORY + "/family/programs";

    private String programName;

    public Family(String programName) {
        this.programName = programName;
    }

//    public void averageExecutions() throws IOException {
//        List<Map<Set<String>, Double>> models = new ArrayList<>();
//        File dir = new File(Family.DIR + "/" + this.programName + "/");
//        Collection<File> files = FileUtils.listFiles(dir, new String[]{"txt"}, true);
//
//        for(File file : files) {
//            Map<Set<String>, Double> model = this.parseMeasurement(file);
//            models.add(model);
//        }
//
//        Map<Set<String>, Double> averagedModel = this.averageModels(models);
//    }
//
//    private Map<Set<String>,Double> averageModels(List<Map<Set<String>, Double>> models) {
//        Map<Set<String>, Double> model = new HashMap<>();
//
//        for(Set<String> options : models.get(0).keySet()) {
//            model.put(options, 0.0);
//        }
//
//        for(Map<Set<String>, Double> aModel : models) {
//            for(Map.Entry<Set<String>, Double> entry : aModel.entrySet()) {
//                double cur = model.get(entry.getKey());
//                cur += entry.getValue();
//                model.put(entry.getKey(), cur);
//            }
//        }
//
//        for(Map.Entry<Set<String>, Double> entry : model.entrySet()) {
//            model.put(entry.getKey(), entry.getValue() / models.size());
//        }
//
//        return model;
//    }
//
//    private Map<Set<String>, Double> parseMeasurement(File file) throws IOException {
//        Map<Set<String>, Double> model = new HashMap<>();
//
//        FileInputStream fstream = new FileInputStream(file);
//        DataInputStream in = new DataInputStream(fstream);
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//
//        String strLine = br.readLine();
//        br.close();
//
//        String[] terms = strLine.split(";");
//
//        for(String term : terms) {
//            term = term.trim();
//
//            if(term.isEmpty()) {
//                continue;
//            }
//
//            Set<String> options = this.getOptions(term);
//            double time = this.getTime(term);
//
//            model.put(options, time);
//        }
//
//        return model;
//    }
//
//    private double getTime(String term) {
//        int start = term.indexOf("<");
//        int end = term.indexOf(">");
//        String time = term.substring(start + 1, end);
//
//        return Long.valueOf(time) / 1000.0;
//    }
//
//    private Set<String> getOptions(String term) {
//        Set<String> options = new HashSet<>();
//        int start = term.indexOf("<");
//        term = term.substring(0, start);
//        String[] features = term.split("#");
//
//        for(String feature : features) {
//            options.add(feature.trim().toUpperCase());
//        }
//
//        return options;
//    }


}
