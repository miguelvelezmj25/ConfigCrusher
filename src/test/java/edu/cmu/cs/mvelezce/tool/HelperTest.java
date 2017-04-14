package edu.cmu.cs.mvelezce.tool;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class HelperTest {

    @Test
    public void getConfigurations1() throws Exception {
        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");
        parameters.add("C");

        Set<Set<String>> configurations = new HashSet<>();
        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("C");
        configuration.add("B");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configurations.add(configuration);

        Assert.assertEquals(configurations, Helper.getConfigurations(parameters));
    }

}