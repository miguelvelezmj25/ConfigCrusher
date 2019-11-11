package edu.cmu.cs.mvelezce.java.execute.region;

import edu.cmu.cs.mvelezce.analysis.region.Region;
import edu.cmu.cs.mvelezce.setup.IProducerConsumer;
import jdk.internal.org.objectweb.asm.Type;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RegionsManager implements IProducerConsumer {

  public static final String INTERNAL_NAME = Type.getInternalName(RegionsManager.class);
  public static final String ENTER_REGION = "enter";
  public static final String EXIT_REGION = "exit";
  public static final String REGION_DESCRIPTOR = "(Ljava/lang/String;)V";
  public static final UUID PROGRAM_REGION_ID =
      UUID.fromString("00000000-0000-0000-0000-000000000000");
  public static final String START = "s";
  public static final String END = "e";
  public static final String COMMA = ",";
  private static final Region PROGRAM_REGION = new Region.Builder(PROGRAM_REGION_ID).build();
  private static final BlockingQueue<String> INPUT_QUEUE = new ArrayBlockingQueue<>(1_000_000);
  //  private static final ProducerConsumerSetup<String> PRODUCER_CONSUMER_SETUP;
  //  private static final Runnable PRODUCER;
  //  private static final Runnable CONSUMER;

  static {
    //    BlockingQueue<String> queue = new ArrayBlockingQueue<>(1_000_000);
    //    ExecutorService executorService = Executors.newFixedThreadPool(2);
    //    PRODUCER_CONSUMER_SETUP = new ProducerConsumerSetup<>(queue, executorService);
    //
    //    PRODUCER = new RuntimeProducer(queue, INPUT_QUEUE);
    //    CONSUMER = new RuntimeConsumer(queue);
  }

  public static void enter(String id) {
//    long time = System.nanoTime();
//    try {
//      INPUT_QUEUE.put(START + COMMA + id + COMMA + time);
//    } catch (InterruptedException ie) {
//      throw new RuntimeException(ie);
//    }
  }

  public static void exit(String id) {
//    long time = System.nanoTime();
//    try {
//      INPUT_QUEUE.put(END + COMMA + id + COMMA + time);
//    } catch (InterruptedException ie) {
//      throw new RuntimeException(ie);
//    }
  }

  public static void exitProgram() {
//    try {
//      while (!INPUT_QUEUE.isEmpty()) {
//        Thread.sleep(2000);
//      }
//
//      Thread.sleep(2000);
//    } catch (InterruptedException ie) {
//      throw new RuntimeException(ie);
//    }
//
//    ((RuntimeProducer) PRODUCER).terminate();
  }

  @Override
  public void execute() {
//    PRODUCER_CONSUMER_SETUP.getExecutorService().execute(PRODUCER);
//    PRODUCER_CONSUMER_SETUP.getExecutorService().execute(CONSUMER);
//
//    PRODUCER_CONSUMER_SETUP.getExecutorService().shutdown();
  }
}
