package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.kanzi;


import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.optimizer.BFKanziMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.kanzi.KanziAdapter;

import java.io.IOException;
import java.util.Set;

public class BFKanziAdapter extends KanziAdapter {

    public BFKanziAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(BFKanziMain.BF_KANZI_MAIN, newArgs);
    }

}
