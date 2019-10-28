package edu.cmu.cs.mvelezce.adapter.adapters.canRemoveNestedConstraintsMultipleCallSites;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "canRemoveNestedConstraintsMultipleCallSites";
  public static final String MAIN_CLASS =
      "edu.cmu.cs.mvelezce.analysis.CanRemoveNestedConstraintsMultipleCallSites";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.PROGRAM_NAME,
        BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.MAIN_CLASS,
        "",
        BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.getListOfOptions());
  }

  public BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter(
      String programName, String entryPoint, String classDir) {
    super(
        programName,
        entryPoint,
        classDir,
        BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    throw new UnsupportedOperationException("Implement");
  }
}
