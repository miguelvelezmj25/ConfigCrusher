package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.runningexample;

import edu.cmu.cs.mvelezce.Example;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.SPLatMain;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;

import java.util.*;

public class SPLatRunningExampleMain extends SPLatMain {

    public SPLatRunningExampleMain(String programName) {
        super(programName);
    }

    @Override
    public Set<Set<String>> getSPLatConfigurations() throws InterruptedException {
        if(!this.getProgramName().contains("example")) {
            throw new RuntimeException("Could not find the main class " + this.getProgramName());
        }

        List<String> options = RunningExampleAdapter.getRunningExampleOptions();
        Map<Set<String>, Set<Set<String>>> configsToCovered = new HashMap<>();
        Set<Set<String>> splatConfigurations = new HashSet<>();
        Adapter adapter = new RunningExampleAdapter();
        Stack<String> stack = new Stack<>();

        Set<String> configuration = new HashSet<>();
        splatConfigurations.add(configuration);
        String[] args = adapter.configurationAsMainArguments(configuration);

//        Example.splat(args, stack);
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
//            }
//            else {
//                configuration = new HashSet<>(configuration);
//                configuration.add(option);
//                splatConfigurations.add(configuration);
//                args = adapter.configurationAsMainArguments(configuration);
//                Example.splat(args, stack);
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
