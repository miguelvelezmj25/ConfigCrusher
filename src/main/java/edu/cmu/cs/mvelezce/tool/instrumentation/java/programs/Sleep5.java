package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep5 {

    public static final String FILENAME = Sleep5.class.getCanonicalName();
    public static final String PACKAGE = Sleep5.class.getPackage().getName();
    public static final String CLASS = Sleep5.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";
    public static final String METHOD_2 = "method2";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        boolean b = Boolean.valueOf(args[1]);
        Thread.sleep(200);
        if(a) {
            // Region A start 31
            Thread.sleep(600);
            Sleep5.method1(a);
            Thread.sleep(600);
            Sleep5.method2(b);
            // Region A end 44
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

    public static void method2(boolean B) throws InterruptedException {
        System.out.println("method2");
        boolean b = B;
        Thread.sleep(300);
        if(b) {
            // Region B start 19
            Thread.sleep(600);
            // Region B end 20
        }
        Thread.sleep(200);
    }

}
