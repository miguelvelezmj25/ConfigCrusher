package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.grep;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.grep.GrepAdapter;

import java.io.IOException;
import java.util.Set;

public class BFGrepAdapter extends GrepAdapter {

    public BFGrepAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFGrepMain.BF_GREP_MAIN, newArgs);
    }

}
