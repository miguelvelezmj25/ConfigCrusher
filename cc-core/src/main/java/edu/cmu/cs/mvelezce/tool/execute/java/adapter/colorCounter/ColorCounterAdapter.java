package edu.cmu.cs.mvelezce.tool.execute.java.adapter.colorCounter;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ColorCounterAdapter extends BaseAdapter {

    public ColorCounterAdapter() {
        this(null, null, null);
    }

    public ColorCounterAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, ColorCounterAdapter.getColorCounterOptions());
    }

    public static List<String> getColorCounterOptions() {
        String[] options = {"FREQTHRESHOLD", "DISTTHRESHOLD", "MINALPHA", "TIMEOUT", "LOGLEVEL"};

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(ColorCounterMain.COLORCOUNTER_MAIN, newArgs);
    }
}
