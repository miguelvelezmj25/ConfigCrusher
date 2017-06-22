package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by mvelezce on 6/22/17.
 */
public class VarexJProcessorTest {

    @Test
    public void testParse() throws ParserConfigurationException, SAXException, IOException {
        VarexJProcessor.parse();
    }

}