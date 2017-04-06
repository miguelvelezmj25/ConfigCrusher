package edu.cmu.cs.mvelezce.analysis.mongodb;

import edu.cmu.cs.mvelezce.mongo.connector.Casbah;

import java.util.List;
import java.util.Map;

/**
 * Created by mvelezce on 4/5/17.
 */
public class MongoTest {

    public static void test() {
        List<Map<String, String>> query = Casbah.connect();

        for(Map<String, String> result : query) {
            for(Map.Entry<String, String> entry : result.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }


    }
}
