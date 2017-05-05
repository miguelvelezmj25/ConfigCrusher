package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep4 {

    public static final String FILENAME = Sleep4.class.getCanonicalName();
    public static final String PACKAGE = Sleep4.class.getPackage().getName();
    public static final String CLASS = Sleep4.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        if(a) { // 20
            // Region A start
            Thread.sleep(600);
            // Region A end
        }
        else {
            // Region !A start
            Thread.sleep(700);
            // Region !A end
        }
        // Region program end
    }

}
