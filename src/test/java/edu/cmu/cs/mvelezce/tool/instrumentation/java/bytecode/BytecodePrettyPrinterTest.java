package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import org.junit.Test;

public class BytecodePrettyPrinterTest {

    @Test
    public void readClasses() throws Exception {
        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";
        BytecodePrettyPrinter printer = new BytecodePrettyPrinter(classDir);
        printer.readClasses();
        printer.hashCode();
    }


}