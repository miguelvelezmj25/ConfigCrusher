package edu.cmu.cs.mvelezce.tool.execute.java.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mvelezce on 7/12/17.
 */
public class Execution {

    private Set<String> configuration = new HashSet<>();
    private List<Region> trace = new ArrayList<>();

    private Execution() { ; }

    public Execution(Set<String> configuration, List<Region> trace) {
        this.configuration = configuration;
        this.trace = trace;
    }

    public Set<String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Set<String> configuration) {
        this.configuration = configuration;
    }

    public List<Region> getTrace() {
        return trace;
    }

    public void setTrace(List<Region> trace) {
        this.trace = trace;
    }
}
