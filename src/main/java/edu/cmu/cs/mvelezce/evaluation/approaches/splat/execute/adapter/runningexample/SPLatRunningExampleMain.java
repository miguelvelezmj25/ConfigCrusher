package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.runningexample;

import edu.cmu.cs.mvelezce.Example;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class SPLatRunningExampleMain {

    public Set<Set<String>> getSPLatConfigurations(String programName) throws InterruptedException {
        if(!programName.contains("example")) {
            throw new RuntimeException("Could not find the main class " + programName);
        }

        Set<Set<String>> splatConfigurations = new HashSet<>();
        Adapter adapter = new RunningExampleAdapter();
        Stack<String> stack = new Stack<>();

        Set<String> configuration = new HashSet<>();
        splatConfigurations.add(configuration);
        String[] args = adapter.configurationAsMainArguments(configuration);

        Example.splat(args, stack);

        while(!stack.isEmpty()) {
            String option = stack.peek();

            if(configuration.contains(option)) {
                configuration = new HashSet<>(configuration);
                configuration.remove(option);
                stack.pop();
            }
            else {
                configuration = new HashSet<>(configuration);
                configuration.add(option);
                splatConfigurations.add(configuration);
                args = adapter.configurationAsMainArguments(configuration);
                Example.splat(args, stack);
            }
        }

        return splatConfigurations;
    }
}
