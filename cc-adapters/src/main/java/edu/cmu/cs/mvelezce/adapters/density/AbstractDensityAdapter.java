package edu.cmu.cs.mvelezce.adapters.density;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractDensityAdapter extends BaseAdapter {

  public AbstractDensityAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
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
}
