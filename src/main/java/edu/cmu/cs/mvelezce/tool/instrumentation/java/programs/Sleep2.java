package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep2 {

    public static final String FILENAME = Sleep2.class.getCanonicalName();
    public static final String PACKAGE = Sleep2.class.getPackage().getName();
    public static final String CLASS = Sleep2.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        if(a) {
            // Region A start 23
            Thread.sleep(600);
            Sleep2.method1(a);
            // Region A end 28
        }
        Thread.sleep(100);
        // Region program end
    }

    public static void method1(boolean A) throws InterruptedException {
        System.out.println("method1");
        boolean a = A;
        Thread.sleep(200);
        if(a) {
            // Region A start 19
            Thread.sleep(600);
            // Region A end 20
        }
        Thread.sleep(100);
    }

}
