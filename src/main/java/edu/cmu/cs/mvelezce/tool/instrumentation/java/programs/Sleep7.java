package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep7 {

    public static final String FILENAME = Sleep7.class.getCanonicalName();
    public static final String PACKAGE = Sleep7.class.getPackage().getName();
    public static final String CLASS = Sleep7.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("mains");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        if(a) {
//            Region region = Regions.getRegion("ID");
//            r.enter();
            // Region A start 23
//            Sleep2 s = new Sleep2();
            Thread.sleep(600);
//            r.exit();
            // TODO Region A end 24 but there is a jump statement at 25
        }
        else {
            // Region !A start 29
            Thread.sleep(700);
            // Region !A end 30
        }
        Thread.sleep(100);
        // Region program end
    }

}
