package edu.cmu.cs.mvelezce.language;

import java.io.File;
import java.util.Scanner;

/**
 * This is a helper class for the language.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class Helper {

    /**
     * TODO
     * @param name
     * @return
     * @throws Exception
     */
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
