package edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

import java.io.IOException;
import java.util.Set;

//@JsonDeserialize(using = DecisionAndOptions.DecisionAndOptionsDeserializer.class)
public class DecisionAndOptions {

    private JavaRegion region;
    private Set<Set<String>> options;

    public DecisionAndOptions() { ; }

    public DecisionAndOptions(JavaRegion region, Set<Set<String>> options) {
        this.region = region;
        this.options = options;
    }

    public JavaRegion getRegion() {
        return region;
    }

    public void setRegion(JavaRegion region) {
        this.region = region;
    }

    public Set<Set<String>> getOptions() {
        return options;
    }

    public void setOptions(Set<Set<String>> options) {
        this.options = options;
    }

//    public static class DecisionAndOptionsDeserializer extends JsonDeserializer<DecisionAndOptions> {
//
//        @Override
//        public DecisionAndOptions deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
//            ObjectCodec oc = jsonParser.getCodec();
//            JsonNode node = oc.readTree(jsonParser);
//
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectReader reader = mapper.readerFor(new TypeReference<Set<Set<String>>>() {
//            });
//            Set<Set<String>> optionsSet = reader.readValue(node.get("options"));
//
//            JsonNode region = node.get("region");
//
//            JavaRegion javaRegion = new JavaRegion(region.get("regionID").asText(), region.get("regionPackage").asText(),
//                    region.get("regionClass").asText(), region.get("regionMethod").asText(),
//                    region.get("startBytecodeIndex").intValue());
//
//            return new DecisionAndOptions(javaRegion, optionsSet);
//        }
//    }
}
