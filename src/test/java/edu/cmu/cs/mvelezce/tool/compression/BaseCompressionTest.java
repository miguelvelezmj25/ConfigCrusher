package edu.cmu.cs.mvelezce.tool.compression;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class BaseCompressionTest {

    public static Set<Set<String>> getOptionsSet(String string) {
        Set<Set<String>> result = new HashSet<>();
        String[] allOptions = string.split(",");

        for(String options : allOptions) {
            Set<String> newOption = new HashSet<>();
            options = options.trim();

            for(int i = 0; i < options.length(); i++) {
                newOption.add(options.charAt(i) + "");
            }

            result.add(newOption);
        }

        return result;
    }

    @Test
    public void filterOptions1() {
        Set<Set<String>> set = BaseCompressionTest.getOptionsSet("AB, AC");
        Assert.assertEquals(set, BaseCompression.filterOptions(set));
    }

    @Test
    public void filterOptions2() {
        Set<Set<String>> set = BaseCompressionTest.getOptionsSet("ABC, ACD");
        Assert.assertEquals(set, BaseCompression.filterOptions(set));
    }

    @Test
    public void filterOptions3() {
        Set<Set<String>> set = BaseCompressionTest.getOptionsSet("AB, ABC");
        Set<Set<String>> result = BaseCompressionTest.getOptionsSet("ABC");
        Assert.assertEquals(result, BaseCompression.filterOptions(set));
    }

    @Test
    public void filterOptions4() {
        Set<Set<String>> set = BaseCompressionTest.getOptionsSet("AB, ABC, BCD, BC, DEF");
        Set<Set<String>> result = BaseCompressionTest.getOptionsSet("ABC, BCD, DEF");
        Assert.assertEquals(result, BaseCompression.filterOptions(set));
    }

}