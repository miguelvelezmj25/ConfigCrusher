package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep10 {

    public static final String FILENAME = Sleep10.class.getCanonicalName();
    public static final String PACKAGE = Sleep10.class.getPackage().getName();
    public static final String CLASS = Sleep10.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";
    public static final String METHOD_2 = "method2";

    public static void main(String[] args) throws InterruptedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        boolean b = Boolean.valueOf(args[1]);
        Thread.sleep(200);
        if(a) { // 28
            // Region A start
            Thread.sleep(600);
            Sleep10.method1(a);
            // Region A end
        }
        else if(b){ // 42
            // Region B start
            Thread.sleep(700);
            Sleep10.method2(b);
            // Region B end
        }
        Thread.sleep(100);
        // Region program end
    }

    public static void method1(boolean A) throws InterruptedException {
        System.out.println("method1");
        boolean a = A;
        Thread.sleep(200);

        // Region A start
        if(a) { // 16
            Thread.sleep(600);
        }
        // Region A end 20

        Thread.sleep(100);
    }

    public static void method2(boolean B) throws InterruptedException {
        System.out.println("method2");
        boolean b = B;
        Thread.sleep(300);

        // Region B start
        if(b) { // 16
            Thread.sleep(600);
        }
        // Region B end

        Thread.sleep(200);
    }

}
