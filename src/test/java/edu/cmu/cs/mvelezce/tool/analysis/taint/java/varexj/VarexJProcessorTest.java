package edu.cmu.cs.mvelezce.tool.analysis.taint.java.varexj;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.varexj.VarexJProcessor;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by mvelezce on 6/22/17.
 */
public class VarexJProcessorTest {

    @Test
    public void testParse() throws ParserConfigurationException, SAXException, IOException {
        VarexJProcessor.parse();
    }

}