package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Sleep3 {

    public static final String FILENAME = Sleep3.class.getCanonicalName();
    public static final String PACKAGE = Sleep3.class.getPackage().getName();
    public static final String CLASS = Sleep3.class.getSimpleName();
    public static final String MAIN_METHOD = "main";
    public static final String METHOD_1 = "method1";
    public static final String METHOD_2 = "method2";

    public static void main(String[] args) throws InterruptedException, CloneNotSupportedException {
        // Region program start
        System.out.println("main");
        boolean a = Boolean.valueOf(args[0]);
        Thread.sleep(200);
        if(a) {
//            JavaRegion hold = new JavaRegion(Sleep1.class.getPackage().getName(), Sleep1.class.getSimpleName(), "METHOD_NAME", -12, 327);
//            Region region = Regions.getRegion(hold);
//            region.enter();
//             Region A start
            Thread.sleep(300);
            Sleep3.method1(a);
            Thread.sleep(100);
            Sleep3.method2(a);
//            region.exit();
            // Region A end
        }
        Thread.sleep(100);
        // Region program end
    }

    public static void method1(boolean A) throws InterruptedException {
        System.out.println("method1");
        boolean a = A;
        Thread.sleep(200);
        if(a) {
            // Region A start
            Thread.sleep(300);
            // Region A end
        }
        Thread.sleep(100);
    }

    public static void method2(boolean A) throws InterruptedException {
        System.out.println("method2");
        boolean a = A;
        Thread.sleep(300);
        if(a) {
            // Region A start
            Thread.sleep(100);
            // Region A end
        }
        Thread.sleep(200);
    }

}
