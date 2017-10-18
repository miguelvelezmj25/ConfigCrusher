package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.regions16;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions16.Regions16Adapter;

import java.io.IOException;
import java.util.Set;

public class BFRegions16Adapter extends Regions16Adapter {

    public BFRegions16Adapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFRegions16Main.BF_REGIONS_16_MAIN, newArgs);
    }

}
