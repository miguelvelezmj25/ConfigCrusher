package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.kanzi;

import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.SPLatMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.kanzi.KanziAdapter;
import kanzi.Run;

import java.io.IOException;
import java.util.*;

public class SPLatKanziMain extends SPLatMain {

    public SPLatKanziMain(String programName) {
        super(programName);
    }

    @Override
    public Set<Set<String>> getSPLatConfigurations() {
        if(!this.getProgramName().contains("kanzi")) {
            throw new RuntimeException("Could not find the main class " + this.getProgramName());
        }

        List<String> options = KanziAdapter.getKanziOptions();
        Map<Set<String>, Set<Set<String>>> configsToCovered = new HashMap<>();
        Set<Set<String>> splatConfigurations = new HashSet<>();
        Adapter adapter = new KanziAdapter();
        Stack<String> stack = new Stack<>();

        Set<String> configuration = new HashSet<>();
        splatConfigurations.add(configuration);
        String[] args = adapter.configurationAsMainArguments(configuration);

//        try {
//            Run.splat(args, stack);
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//
//        Set<String> optionsNotInStack = this.getOptionsNotInStack(stack, options);
//        Set<Set<String>> coveredConfigs = this.mapConfigs(configuration, optionsNotInStack);
//        configsToCovered.put(configuration, coveredConfigs);
//
//        while(!stack.isEmpty()) {
//            String option = stack.peek();
//
//            if(configuration.contains(option)) {
//                configuration = new HashSet<>(configuration);
//                configuration.remove(option);
//                stack.pop();
//            } else {
//                configuration = new HashSet<>(configuration);
//                configuration.add(option);
//                splatConfigurations.add(configuration);
//                args = adapter.configurationAsMainArguments(configuration);
//                try {
//                    Run.splat(args, stack);
//                } catch(IOException e) {
//                    e.printStackTrace();
//                }
//
//                optionsNotInStack = this.getOptionsNotInStack(stack, options);
//                coveredConfigs = this.mapConfigs(configuration, optionsNotInStack);
//                configsToCovered.put(configuration, coveredConfigs);
//            }
//        }

        this.setConfigsToCovered(configsToCovered);

        return splatConfigurations;
    }
}
