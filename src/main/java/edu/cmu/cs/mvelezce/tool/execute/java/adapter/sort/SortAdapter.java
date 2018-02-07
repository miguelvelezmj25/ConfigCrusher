package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sort;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SortAdapter extends BaseAdapter {

    public SortAdapter() {
        this(null, null, null);
    }

    public SortAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, SortAdapter.getSortOptions());
    }

    public static List<String> getSortOptions() {
        String[] options = {"CHECK", "MERGE", "UNIQUE", "IGNORELEADINGBLANKS", "DICTIONARYORDER", "IGNORECASE", "NUMERICSORT", "GENERALNUMERICSORT", "HUMANNUMERICSORT", "MONTHSORT", "VERSIONSORT", "REVERSE"};

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(SortMain.SORT_MAIN, newArgs);
    }
}
