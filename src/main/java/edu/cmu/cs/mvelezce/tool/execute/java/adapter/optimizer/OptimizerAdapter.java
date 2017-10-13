package edu.cmu.cs.mvelezce.tool.execute.java.adapter.optimizer;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OptimizerAdapter extends BaseAdapter {

    public OptimizerAdapter() {
        this(null, null, null);
    }

    public OptimizerAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, OptimizerAdapter.getOptimizerOptions());
    }

    public static List<String> getOptimizerOptions() {
        String[] options = {"REMOVEGAMMA", "COMPRESSIONLEVEL", "COMPRESSOR", "ITERATIONS", "LOGLEVEL"};

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(OptimizerMain.OPTIMIZER_MAIN, newArgs);
    }
}
