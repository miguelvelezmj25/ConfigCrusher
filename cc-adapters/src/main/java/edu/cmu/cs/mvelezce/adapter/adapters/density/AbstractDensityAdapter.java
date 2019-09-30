package edu.cmu.cs.mvelezce.adapter.adapters.density;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractDensityAdapter extends BaseAdapter {

  public AbstractDensityAdapter() {
    this(null, null, null);
  }

  public AbstractDensityAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractDensityAdapter.getDensityOptions());
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
