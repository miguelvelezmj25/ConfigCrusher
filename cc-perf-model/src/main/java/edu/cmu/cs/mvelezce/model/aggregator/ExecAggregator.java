package edu.cmu.cs.mvelezce.model.aggregator;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExecAggregator<T> {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0000000");

  public Set<LocalPerformanceModel<T>> process(
      Set<MultiEntryLocalPerformanceModel<T>> multiEntryLocalPerformanceModels) {
    Set<LocalPerformanceModel<T>> localPerformanceModels = new HashSet<>();

    for (MultiEntryLocalPerformanceModel<T> multiEntryLocalPerformanceModel :
        multiEntryLocalPerformanceModels) {
      LocalPerformanceModel<T> localModel = this.averageTimes(multiEntryLocalPerformanceModel);
      localPerformanceModels.add(localModel);
    }

    return localPerformanceModels;
  }

  private LocalPerformanceModel<T> averageTimes(
      MultiEntryLocalPerformanceModel<T> multiEntryLocalPerformanceModels) {
    System.out.println("Region " + multiEntryLocalPerformanceModels.getRegion());
    Map<T, Long> model = new HashMap<>();

    for (Map.Entry<T, Set<Long>> multiEntryModel :
        multiEntryLocalPerformanceModels.getModel().entrySet()) {
      Set<Long> times = multiEntryModel.getValue();
      long currentTime = 0;

      for (Long time : times) {
        currentTime += time;
      }

      model.put(multiEntryModel.getKey(), currentTime / times.size());
    }

    Map<T, Long> modelToMin = this.getModelToMin(multiEntryLocalPerformanceModels);
    Map<T, Long> modelToMax = this.getModelToDiff(multiEntryLocalPerformanceModels);
    Map<T, Long> modelToDiff = this.getModelToDiff(modelToMin, modelToMax);

    return new LocalPerformanceModel<T>(
        multiEntryLocalPerformanceModels.getRegion(),
        model,
        modelToMin,
        modelToMax,
        modelToDiff,
        this.toHumandReadable(model),
        this.toHumandReadable(modelToMin),
        this.toHumandReadable(modelToMax),
        this.toHumandReadable(modelToDiff));
  }

  private Map<T, String> toHumandReadable(Map<T, Long> model) {
    Map<T, String> modelToHumanReadable = new HashMap<>();

    for (Map.Entry<T, Long> entry : model.entrySet()) {
      modelToHumanReadable.put(entry.getKey(), entry.getValue().toString());
    }

    for (Map.Entry<T, String> entry : modelToHumanReadable.entrySet()) {
      double data = Double.parseDouble(entry.getValue());
      data = data / 1E9;
      modelToHumanReadable.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return modelToHumanReadable;
  }

  private Map<T, Long> getModelToDiff(Map<T, Long> modelToMin, Map<T, Long> modelToMax) {
    Map<T, Long> modelToDiff = new HashMap<>();

    for (T header : modelToMin.keySet()) {
      modelToDiff.put(header, 0L);
    }

    for (T header : modelToDiff.keySet()) {
      long max = modelToMax.get(header);
      long min = modelToMin.get(header);
      long diff = max - min;

      if (diff >= 1E9) {
        System.err.println(
            "The difference between the min and max executions of header "
                + header
                + " is greater than 1 sec. It is "
                + (diff / 1E9));
      }

      modelToDiff.put(header, diff);
    }

    return modelToDiff;
  }

  private Map<T, Long> getModelToDiff(
      MultiEntryLocalPerformanceModel<T> multiEntryLocalPerformanceModels) {
    Map<T, Long> modelsToMaxs =
        this.addHeaders(multiEntryLocalPerformanceModels.getModel().keySet(), Long.MIN_VALUE);

    for (Map.Entry<T, Set<Long>> multiEntryModel :
        multiEntryLocalPerformanceModels.getModel().entrySet()) {
      T header = multiEntryModel.getKey();
      Set<Long> times = multiEntryModel.getValue();

      for (Long time : times) {
        long max = modelsToMaxs.get(header);
        max = Math.max(max, time);
        modelsToMaxs.put(header, max);
      }
    }

    return modelsToMaxs;
  }

  private Map<T, Long> getModelToMin(
      MultiEntryLocalPerformanceModel<T> multiEntryLocalPerformanceModels) {
    Map<T, Long> modelsToMins =
        this.addHeaders(multiEntryLocalPerformanceModels.getModel().keySet(), Long.MAX_VALUE);

    for (Map.Entry<T, Set<Long>> multiEntryModel :
        multiEntryLocalPerformanceModels.getModel().entrySet()) {
      T header = multiEntryModel.getKey();
      Set<Long> times = multiEntryModel.getValue();

      for (Long time : times) {
        long min = modelsToMins.get(header);
        min = Math.min(min, time);
        modelsToMins.put(header, min);
      }
    }

    return modelsToMins;
  }

  private Map<T, Long> addHeaders(Set<T> headers, long defaultValue) {
    Map<T, Long> model = new HashMap<>();

    for (T header : headers) {
      model.put(header, defaultValue);
    }

    return model;
  }
}
