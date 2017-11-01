package edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PrevaylerAdapter extends BaseAdapter {

    public PrevaylerAdapter() {
        this(null, null, null);
    }

    public PrevaylerAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, PrevaylerAdapter.getPrevaylerOptions());
    }

    public static List<String> getPrevaylerOptions() {
        String[] options = {"TRANSIENTMODE", "CLOCK", "DEEPCOPY", "DISKSYNC", "FILESIZETHRESHOLD", "FILEAGETHRESHOLD",
                "MONITOR", "JOURNALSERIALIZER", "SNAPSHOTSERIALIZER"};

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(PrevaylerMain.PREVAYLER_MAIN, newArgs);
    }
}
