package edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions14;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Regions14Adapter extends BaseAdapter {

    public Regions14Adapter() {
        this(null, null, null);
    }

    public Regions14Adapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, Regions14Adapter.getRegions14Options());
    }

    public static List<String> getRegions14Options() {
        String[] options = {"A", "B", "C"};

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(Regions14Main.REGIONS_14_MAIN, newArgs);
    }
}
