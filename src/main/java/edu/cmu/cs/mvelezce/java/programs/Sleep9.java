package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep9 {

    public static final String FILENAME = Sleep9.class.getCanonicalName();
    public static final String PACKAGE = Sleep9.class.getPackage().getName();
    public static final String CLASS = Sleep9.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        boolean b = Boolean.valueOf(args[1]);
        Thread.sleep(200);
        if(a) {
            // Region A start 31
            Thread.sleep(600);
            Sleep9.method1(a);
            // TODO Region A end 36 but there is a jump statement at 37
        }
        else {
            // Region !A start 41
            Thread.sleep(700);
            Sleep9.method1(b);
            // Region !A end 46
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
        else {
            // Region !A start 25
            Thread.sleep(800);
            // Region !A end 26
        }
        Thread.sleep(100);
    }
}
