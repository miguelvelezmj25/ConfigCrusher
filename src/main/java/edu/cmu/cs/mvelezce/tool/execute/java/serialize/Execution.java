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
@JsonDeserialize(using = Execution.ExecutionDeserializer.class)
public class Execution {

    private Set<String> configuration = new HashSet<>();
    private List<Region> trace = new ArrayList<>();

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

    public static class ExecutionDeserializer extends JsonDeserializer<Execution> {

        @Override
        public Execution deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);

            ObjectMapper mapper = new ObjectMapper();
            ObjectReader reader = mapper.readerFor(new TypeReference<Set<String>>() {
            });
            Set<String> configuration = reader.readValue(node.get("configuration"));

            List<Region> regions = new ArrayList<>();
            reader = mapper.readerFor(new TypeReference<List<ObjectNode>>() {
            });
            List<ObjectNode> trace = reader.readValue(node.get("trace"));

            for(ObjectNode object : trace) {
                Region region = new Region(object.get("regionID").asText(), object.get("startTime").longValue(),
                        object.get("endTime").longValue(), object.get("overhead").longValue());
                regions.add(region);
            }

            return new Execution(configuration, regions);
        }
    }
}
