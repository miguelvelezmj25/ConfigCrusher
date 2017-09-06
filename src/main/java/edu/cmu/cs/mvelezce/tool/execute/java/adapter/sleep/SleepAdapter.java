package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepAdapter extends BaseAdapter {

    public SleepAdapter() {
        this(null, null, null);
    }

    public SleepAdapter(String programName, String mainClass, String directory) {
        super(programName, mainClass, directory, readOptions());
    }

    @Override
    public void execute(Set<String> configuration) {
        String[] args = this.configurationAsMainArguments(configuration);

        BaseAdapter.executeJavaProgram(this.getProgramName(), SleepMain.SLEEP_MAIN, this.getMainClass(),
                this.getDirectory(), args);
    }

    public static List<String> readOptions() {
        String[] options = {"A", "B", "C", "D", "IA", "DA"};

        return Arrays.asList(options);
    }

}
