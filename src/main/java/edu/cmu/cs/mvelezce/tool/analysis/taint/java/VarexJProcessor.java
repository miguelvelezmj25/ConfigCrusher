package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by mvelezce on 6/22/17.
 */
public class VarexJProcessor {

    public static void parse() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setValidating(true);
//        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/gpl/coverage.xml");

        if(!file.exists()) {
            throw new RuntimeException("No file");
        }

        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    }


}
