package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep6 {

    public static final String FILENAME = Sleep6.class.getCanonicalName();
    public static final String PACKAGE = Sleep6.class.getPackage().getName();
    public static final String CLASS = Sleep6.class.getSimpleName();
    public static final String MAIN_METHOD = "main";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        if(a) {
            Thread.sleep(600);
        }
        else {
            Thread.sleep(700);
            return;
        }

        Thread.sleep(100);
        // Region program end
    }

}
