package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.elevator;

import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.elevator.ElevatorFM;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.SPLatMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import family.PL_Interface_impl;

import java.util.*;

public class SPLatElevatorMain extends SPLatMain {

    public SPLatElevatorMain(String programName) {
        super(programName);
    }

    @Override
    public Set<Set<String>> getSPLatConfigurations() {
        if(!this.getProgramName().contains("elevator")) {
            throw new RuntimeException("Could not find the main class " + this.getProgramName());
        }

        List<String> options = new ArrayList<>(ElevatorAdapter.getElevatorOptions());
        options.remove(FeatureModel.BASE);

        Map<Set<String>, Set<Set<String>>> configsToCovered = new HashMap<>();
        Set<Set<String>> splatConfigurations = new HashSet<>();
        Adapter adapter = new ElevatorAdapter();
        Stack<String> stack = new Stack<>();

        FeatureModel fm = new ElevatorFM();
        Set<String> configuration = new HashSet<>();
        configuration.add(FeatureModel.BASE);

        if(!fm.isValidProduct(configuration)) {
            throw new RuntimeException("The feature base is not the only required feature");
        }

        String[] args = adapter.configurationAsMainArguments(configuration);
        splatConfigurations.add(configuration);

//        PL_Interface_impl.splat(args, stack);
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
//
//                boolean valid = fm.isValidProduct(configuration);
//
//                if(!fm.isValidProduct(configuration)) {
////                    continue;
//                    System.out.println();
//                }
//
//                splatConfigurations.add(configuration);
//                args = adapter.configurationAsMainArguments(configuration);
//                PL_Interface_impl.splat(args, stack);
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
