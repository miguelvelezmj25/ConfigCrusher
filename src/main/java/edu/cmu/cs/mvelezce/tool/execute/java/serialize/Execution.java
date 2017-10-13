package edu.cmu.cs.mvelezce.tool.execute.java.serialize;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.RegionID;

import java.util.*;

/**
 * Created by mvelezce on 7/12/17.
 */
public class Execution {

    private Set<String> configuration = new HashSet<>();
    //    private List<Region> trace = new ArrayList<>();
    private Map<String, Long> regionsToProcessedPerformance = new LinkedHashMap<>();

    private Execution() {
        ;
    }

    public Execution(Set<String> configuration, Map<String, Long> regionsToProcessedPerformance) {
        this.configuration = configuration;
        this.regionsToProcessedPerformance = regionsToProcessedPerformance;

//        this.checkTrace();
    }

//    public Execution(Set<String> configuration, List<Region> trace) {
//        this.configuration = configuration;
//        this.trace = trace;
//
////        this.checkTrace();
//    }

//    public void checkTrace() {
//        Stack<Region> executingRegions = new Stack<>();
//
//        for(Region region : this.trace) {
//            if(executingRegions.empty()) {
//                executingRegions.add(region);
//
//                continue;
//            }
//
//            Region top = executingRegions.peek();
//
//            if(top.getRegionID().equals(region.getRegionID())) {
//                if(top.getStartTime() != 0 && region.getEndTime() != 0) {
//                    if(region.getEndTime() < top.getStartTime()) {
//                        throw new RuntimeException("The end time of a future region is less than the start time of previously visited region");
//                    }
//
//                    executingRegions.pop();
//                }
//            }
//            else {
//                executingRegions.add(region);
//            }
//        }
//
//        if(!executingRegions.empty()) {
//            throw new RuntimeException("The execution trace had an executing region that was not ended");
//        }
//
//    }

    public Set<String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Set<String> configuration) {
        this.configuration = configuration;
    }

    public Map<String, Long> getRegionsToProcessedPerformance() {
        return regionsToProcessedPerformance;
    }

    public void setRegionsToProcessedPerformance(Map<String, Long> regionsToProcessedPerformance) {
        this.regionsToProcessedPerformance = regionsToProcessedPerformance;
    }

    //    public List<Region> getTrace() {
//        return trace;
//    }
//
//    public void setTrace(List<Region> trace) {
//        this.trace = trace;
//    }
}
