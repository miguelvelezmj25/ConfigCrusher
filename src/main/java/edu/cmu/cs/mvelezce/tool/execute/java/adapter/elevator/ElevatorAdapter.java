package edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class ElevatorAdapter extends BaseAdapter {

    public ElevatorAdapter() {
        this(null, null, null);
    }

    public ElevatorAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, ElevatorAdapter.getElevatorOptions());
    }

    public static List<String> getElevatorOptions() {
        String[] options = {
                "BASE",
                "OVERLOADED",
                "WEIGHT",
                "EMPTY",
                "TWOTHIRDSFULL",
                "EXECUTIVEFLOOR"
        };

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(ElevatorMain.ELEVATOR_MAIN, newArgs);
    }

}
