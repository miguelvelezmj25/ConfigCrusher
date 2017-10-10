package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.regions12;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12.Regions12Adapter;

import java.io.IOException;
import java.util.Set;

public class BFRegions12Adapter extends Regions12Adapter {

    public BFRegions12Adapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFRegions12Main.BF_REGIONS_12_MAIN, newArgs);
    }

}
