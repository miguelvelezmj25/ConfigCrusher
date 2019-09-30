package edu.cmu.cs.mvelezce.tool.execute.java.adapter.kanzi;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class KanziAdapter extends BaseAdapter {

    public KanziAdapter() {
        this(null, null, null);
    }

    public KanziAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, KanziAdapter.getKanziOptions());
    }

    public static List<String> getKanziOptions() {
        String[] options = {"VERBOSE", "FORCE", "BLOCKSIZE", "LEVEL", "ENTROPY", "TRANSFORM", "CHECKSUM"};

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(KanziMain.KANZI_MAIN, newArgs);
    }
}
