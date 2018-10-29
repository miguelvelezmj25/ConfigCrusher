package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.varexj;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.varexj.Coverage;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.varexj.Interaction;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.varexj.UnsupportedCoverageException;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.varexj.XMLReader;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;

public class XMLReaderTest {

  @Test
  public void readFromFile()
      throws UnsupportedCoverageException, ParserConfigurationException, IOException, SAXException {
    File file = new File(
        "/Users/mvelezce/Documents/Programming/Java/runtime-EclipseApplication/test/coverage.xml");

    XMLReader xmlReader = new XMLReader();
    Coverage coverage = xmlReader.readFromFile(file);
    Collection<Interaction> interactions = coverage.getCoverage("Example2.java");
    System.out.println(interactions);
  }
}