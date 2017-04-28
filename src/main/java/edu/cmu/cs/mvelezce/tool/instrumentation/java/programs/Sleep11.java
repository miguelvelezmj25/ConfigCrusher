package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep11 {

    public static final String FILENAME = Sleep11.class.getCanonicalName();
    public static final String PACKAGE = Sleep11.class.getPackage().getName();
    public static final String CLASS = Sleep11.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);

        int repeat;

        if(a) {
            // Region A start 19
            Thread.sleep(100);
            repeat = 5;
            // Region A end 24
        }
        else {
            // Region !A start 30
            Thread.sleep(200);
            repeat = 10;
            // Region !A end 35
        }

        Region region = Regions.getRegion("ID");
        region.enter();
        for(int i = 0; i < repeat; i++) { // TODO do we make it run all executions?
            // Region A start TODO where to instrument? It does not break before the i = 0 @ 39
            Thread.sleep(200);
            // Region A end TODO where to instrument? 53
        }
        region.exit();
    }

}
