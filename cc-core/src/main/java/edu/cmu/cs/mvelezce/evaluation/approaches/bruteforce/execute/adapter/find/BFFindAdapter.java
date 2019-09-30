package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.find;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.find.FindAdapter;

import java.io.IOException;
import java.util.Set;

public class BFFindAdapter extends FindAdapter {

    public BFFindAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFFindMain.BF_FIND_MAIN, newArgs);
    }

}
