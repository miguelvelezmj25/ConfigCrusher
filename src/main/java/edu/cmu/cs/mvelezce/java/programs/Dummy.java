package edu.cmu.cs.mvelezce.java.programs;

/**
 * Created by mvelezce on 3/31/17.
 */
public class Dummy {
    public static void main(String[] args) {

        switch(Integer.valueOf(args[0])) {
            case 1: Dummy.inc1(1);
                    break;
            case 2: Dummy.inc2(2);
                    break;
        }
    }

    public static void inc1(int a) {
        int result = a + 1;
        System.out.println(result);
    }

    public static void inc2(int a) {
        int result = a + 1;
        System.out.println(result);
    }

}
