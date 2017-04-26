package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep1 {

    public static final String FILENAME = Sleep1.class.getCanonicalName();
    public static final String PACKAGE = Sleep1.class.getPackage().getName();
    public static final String CLASS = Sleep1.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException, CloneNotSupportedException {
//        JavaRegion region = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 38, 39);
//        Regions.addRegion(region);
        // Region program start
        System.out.println("mains");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        if(a) {
//            r.enter();
            // Region A start
//            Sleep2 s = new Sleep2();
//            s.method1(a);
            Thread.sleep(1000);
//            r.exit();
            // Region A end
        }
        Thread.sleep(100);
        // Region program end
    }

}
