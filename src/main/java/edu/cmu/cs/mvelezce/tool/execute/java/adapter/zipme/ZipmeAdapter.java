package edu.cmu.cs.mvelezce.tool.execute.java.adapter.zipme;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class ZipmeAdapter extends Adapter {

    private static final String[] CONFIGURATIONS = {
            "FEATURECRC",
            "FEATUREArchiveCheck",
            "FEATUREGZIP",
            "FEATUREAdler32Checksum",
            "FEATUREDerivativeGZIPCRC",
            "FEATUREDerivativeCompressCRC",
            "FEATUREDerivativeExtractCRC",
            "FEATUREDerivativeCompressGZIP",
            "FEATUREDerivativeCompressAdler32Checksum",
            "FEATUREDerivativeCompressGZIPCRC"
    };

    public ZipmeAdapter(String programName, String mainClass, String directory) {
        super(programName, mainClass, directory);
    }

    @Override
    public void execute(Set<String> configuration) {
        String[] argsArray = ZipmeAdapter.adaptConfigurationToProgram(configuration);
        StringBuilder args = new StringBuilder();

        for(String arg : argsArray) {
            args.append(arg);
            args.append(" ");
        }

        Adapter.executeJavaProgram(programName, ZipmeMain.ZIPME_MAIN, this.mainClass, this.directory, args.toString().trim());
    }

    public static String[] adaptConfigurationToProgram(Set<String> configuration) {
        String[] sleepConfiguration = new String[ZipmeAdapter.CONFIGURATIONS.length];

        for(int i = 0; i < sleepConfiguration.length; i++) {
            if(configuration.contains(ZipmeAdapter.CONFIGURATIONS[i])) {
                sleepConfiguration[i] = "true";
            }
            else {
                sleepConfiguration[i] = "false";
            }
        }

        return sleepConfiguration;
    }

    public static Set<String> adaptConfigurationToPerformanceMeasurement(String[] configuration) {
        Set<String> performanceConfiguration = new HashSet<>();

        for(int i = 0; i < configuration.length; i++) {
            if(configuration[i].equals("true")) {
                performanceConfiguration.add(ZipmeAdapter.CONFIGURATIONS[i]);
            }
        }

        return performanceConfiguration;
    }

}
