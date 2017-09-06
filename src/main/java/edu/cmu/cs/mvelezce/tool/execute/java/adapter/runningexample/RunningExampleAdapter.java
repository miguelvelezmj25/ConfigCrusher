package edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RunningExampleAdapter extends BaseAdapter {

    public RunningExampleAdapter() {
        this(null, null, null);
    }

    public RunningExampleAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, RunningExampleAdapter.getRunningExampleOptions());
    }

    @Override
    public void execute(Set<String> configuration, int iteration) {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(RunningExampleMain.RUNNING_EXAMPLE_MAIN, newArgs);
    }


    public static List<String> getRunningExampleOptions() {
        String[] options = {"A", "B", "C", "D"};

        return Arrays.asList(options);
    }
}
