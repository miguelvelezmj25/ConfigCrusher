package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.util.Arrays;
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
        super(programName, mainClass, directory, getSleepOptions());
    }

    @Override
    public void execute(Set<String> configuration, int iteration) {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(SleepMain.SLEEP_MAIN, newArgs);
    }

    @Override
    public void execute(Set<String> configuration) {
        this.execute(configuration, 0);
    }

    private static List<String> getSleepOptions() {
        String[] options = {"A", "B", "C", "D", "IA", "DA"};

        return Arrays.asList(options);
    }

}
