package edu.cmu.cs.mvelezce.tool.execute.java.adapter.density;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DensityAdapter extends BaseAdapter {

    public DensityAdapter() {
        this(null, null, null);
    }

    public DensityAdapter(String programName, String entryPoint, String dir) {
        super(programName, entryPoint, dir, DensityAdapter.getDensityOptions());
    }

    public static List<String> getDensityOptions() {
        String[] options = {
                "SCALE",
                "PLATFORM",
                "OUTPUTCOMPRESSIONGMODE",
                "SCALEMODE",
                "SCALEISHEIGHTDP",
                "DOWNSCALINGALGORITHM",
                "UPSCALINGALGORITHM",
                "COMPRESSQUALITY",
//                "SKIPEXISTINGFILES",
                "SKIPUPSCALING",
                "VERBOSELOG",
                "INCLUDEANDROIDLDPITVDPI",
                "HALTONERROR",
                "CREATEMIPMAPINSTEADOFDRAWABLEDIR",
                "ENABLEPNGCRUSH",
                "ENABLEMOZJPEG",
                "POSTCONVERTWEBP",
                "ENABLEANTIALIASING",
//                "DRYRUN",
                "KEEPUNOPTIMIZEDFILESPOSTPROCESSOR",
                "ROUNDINGHANDLER",
                "IOSCREATEIMAGESETFOLDERS",
                "CLEARDIRBEFORECONVERT",
//                "HELP",
//                "VERSION",
                "GUIADVANCEDOPTIONS"
        };

        return Arrays.asList(options);
    }

    @Override
    public void execute(Set<String> configuration, int iteration) throws IOException, InterruptedException {
        String[] args = this.configurationAsMainArguments(configuration);
        String[] newArgs = new String[args.length + 1];

        newArgs[0] = iteration + "";
        System.arraycopy(args, 0, newArgs, 1, args.length);

        this.execute(DensityMain.DENSITY_MAIN, newArgs);
    }
}
