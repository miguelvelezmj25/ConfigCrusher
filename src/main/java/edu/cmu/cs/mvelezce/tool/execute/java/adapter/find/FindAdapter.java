package edu.cmu.cs.mvelezce.tool.execute.java.adapter.find;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FindAdapter extends BaseAdapter {

    public FindAdapter() {
        this(null, null, null);
    }

    public FindAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, FindAdapter.getFindOptions());
    }

    public static List<String> getFindOptions() {
        String[] options = {"ISTYPEDIRECTORY",
                "ISTYPEFILE",
                "ISTYPESYMLINK",
                "ISTYPEOTHER",
                "ISREGEX",
                "ISIGNORECASE",
                "ISTIMENEWER",
                "ISTIMEOLDER",
                "ISTIMECREATE",
                "ISTIMEACCESS",
                "ISTIMEMODIFIED",
                "ISPRINT0"};

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(FindMain.FIND_MAIN, newArgs);
    }
}
