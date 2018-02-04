package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.grep;

import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.SPLatMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.grep.GrepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;
import org.unix4j.grep.Main;

import java.util.*;

public class SPLatGrepMain extends SPLatMain {

    public SPLatGrepMain(String programName) {
        super(programName);
    }

    @Override
    public Set<Set<String>> getSPLatConfigurations() {
        if(!this.getProgramName().contains("grep")) {
            throw new RuntimeException("Could not find the main class " + this.getProgramName());
        }

        List<String> options = GrepAdapter.getGrepOptions();
        Map<Set<String>, Set<Set<String>>> configsToCovered = new HashMap<>();
        Set<Set<String>> splatConfigurations = new HashSet<>();
        Adapter adapter = new GrepAdapter();
        Stack<String> stack = new Stack<>();

        Set<String> configuration = new HashSet<>();
        splatConfigurations.add(configuration);
        String[] args = adapter.configurationAsMainArguments(configuration);

//        Main.splat(args, stack);
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
//                Main.splat(args, stack);
//
//                optionsNotInStack = this.getOptionsNotInStack(stack, options);
//                coveredConfigs = this.mapConfigs(configuration, optionsNotInStack);
//                configsToCovered.put(configuration, coveredConfigs);
//            }
//        }
//
//        this.setConfigsToCovered(configsToCovered);

        return splatConfigurations;
    }
}
