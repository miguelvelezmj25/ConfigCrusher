package edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Regions12Adapter extends BaseAdapter {

    public Regions12Adapter() {
        this(null, null, null);
    }

    public Regions12Adapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, Regions12Adapter.getRegions12Options());
    }

    public static List<String> getRegions12Options() {
        String[] options = {"A", "B", "C"};

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(Regions12Main.REGIONS_12_MAIN, newArgs);
    }
}
