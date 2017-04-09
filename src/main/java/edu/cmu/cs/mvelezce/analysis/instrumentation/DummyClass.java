package edu.cmu.cs.mvelezce.analysis.instrumentation;

/**
 * Created by mvelezce on 3/31/17.
 */
public class DummyClass {

    private int methodNumber;

    public DummyClass(int methodNumber) {
        this.methodNumber = methodNumber;
    }

    public void execute() {
        if(this.methodNumber == 1) {
            this.inc1(1);
        }
        else if(this.methodNumber == 2) {
            this.inc2(2);
        }
        else if(this.methodNumber == 3) {
            this.inc3(3);
        }
    }

    public void inc1(int a) {
//        System.out.println("Mom");
        int result = a + 1;
        System.out.println(result);
    }

    public void inc2(int a) {
//        Timer.startTimer("MiguelId");
        int result = a + 1;
        System.out.println(result);
//        Timer.stopTimer("MiguelId");
    }

    public void inc3(int a) {
//        System.nanoTime();
        int result = a + 1;
        System.out.println(result);
//        System.nanoTime();
//        long finalTime = end - start;
    }

}
