package edu.cmu.cs.mvelezce.language;

import java.io.File;
import java.util.Scanner;

/**
 * Created by mvelezce on 2/6/17.
 */
public class Helper {

    public static String loadFile(String name) throws Exception {
        Scanner s = new Scanner(new File(name));
        String file = "";
        while (s.hasNext()) {
            file += s.nextLine() + "\n";
        }
        s.close();
        return file;
    }

}
