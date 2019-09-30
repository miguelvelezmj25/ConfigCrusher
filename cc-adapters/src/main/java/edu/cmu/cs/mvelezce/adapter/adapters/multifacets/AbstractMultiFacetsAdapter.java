package edu.cmu.cs.mvelezce.adapter.adapters.multifacets;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractMultiFacetsAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "multifacets";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.MultiFacets";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public AbstractMultiFacetsAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractMultiFacetsAdapter.PROGRAM_NAME,
        AbstractMultiFacetsAdapter.MAIN_CLASS,
        "",
        AbstractMultiFacetsAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractMultiFacetsAdapter.OPTIONS);
  }
}
