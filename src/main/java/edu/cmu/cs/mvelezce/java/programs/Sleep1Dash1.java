package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep1Dash1 {

    public static final String FILENAME = Sleep1Dash1.class.getCanonicalName();
    public static final String PACKAGE = Sleep1Dash1.class.getPackage().getName();
    public static final String CLASS = Sleep1Dash1.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        // Region A start
        // 20
        if(a) { Thread.sleep(600); }
        // Region A end

        Thread.sleep(100);
        // Region program end
    }

}
