package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.density;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.density.DensityAdapter;

import java.io.IOException;
import java.util.Set;

public class BFDensityAdapter extends DensityAdapter {

    public BFDensityAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFDensityMain.BF_DENSITY_MAIN, newArgs);
    }

}
