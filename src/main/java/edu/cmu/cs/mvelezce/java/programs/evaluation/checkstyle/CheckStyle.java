package edu.cmu.cs.mvelezce.java.programs.evaluation.checkstyle;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by miguelvelez on 5/17/17.
 */
public class CheckStyle {

    public static void main(String[] args) throws IOException {
        List<String> filesToAnalyse = new ArrayList<>();
        filesToAnalyse.add("-c");
        filesToAnalyse.add(CheckStyle.getPath("evaluation/checkstyle/miguel_checks.xml"));

        String[] extensions = new String[1];
        extensions[0] = "java";
        File dir = new File("../");
        Collection<File> files = FileUtils.listFiles(dir, extensions, true);


        for(File file : files) {
            String path = file.getPath();
            filesToAnalyse.add(path);
        }

//        Main.main("-c", CheckStyle.getPath("evaluation/checkstyle/google_checks.xml"), filesToAnalyse.toArray());
        final long start = System.nanoTime();
//        Main.main(filesToAnalyse.toArray(new String[filesToAnalyse.size()]));
        final long end = System.nanoTime();

        System.out.println(Region.getSecondsExecutionTime(start, end));
    }

    private static String getPath(String filename) {
        return "src/main/java/edu/cmu/cs/mvelezce/java/programs/" + filename;
    }

}
