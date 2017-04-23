package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.InstrumentationArea;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 4/21/17.
 */
public class Main {

    public static final String FILENAME = Main.class.getCanonicalName();

    public static void main(String[] args) throws InterruptedException {
        if(args[0].equals("sleep")) {
            Main.callSleep(Integer.valueOf(args[1]), Main.getOptionsSet(args[2]));
        }
        else {
            // TODO java program
            InstrumentationArea.executeDummyClass(Integer.valueOf(args[0]));
        }
    }

    public static void callSleep(int program, Set<String> configurations) throws InterruptedException {
        switch(program) {
            case 1:
                Sleep sleep = new Sleep();
                sleep.program1(configurations.contains("A"));
                break;
        }
    }

    public static Set<String> getOptionsSet(String string) {
        Set<String> result = new HashSet<>();
        String[] allOptions = string.split(",");

        for(String options : allOptions) {
            result.add(options.trim());
        }

        return result;
    }
}