package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.prevayler;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler.PrevaylerAdapter;

import java.io.IOException;
import java.util.Set;

public class BFPrevaylerAdapter extends PrevaylerAdapter {

    public BFPrevaylerAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFPrevaylerMain.BF_PREVAYLER_MAIN, newArgs);
    }

}
