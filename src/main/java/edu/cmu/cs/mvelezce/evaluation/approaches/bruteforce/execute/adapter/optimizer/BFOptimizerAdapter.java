package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.optimizer;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.optimizer.OptimizerAdapter;

import java.io.IOException;
import java.util.Set;

public class BFOptimizerAdapter extends OptimizerAdapter {

    public BFOptimizerAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFOptimizerMain.BF_OPTIMIZER_MAIN, newArgs);
    }

}
