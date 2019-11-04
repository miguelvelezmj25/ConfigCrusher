package edu.cmu.cs.mvelezce.java.producerconsumer.producer;

import edu.cmu.cs.mvelezce.producer.IProducer;
import edu.cmu.cs.mvelezce.setup.IProducerConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class RuntimeProducer implements IProducer<String> {

  private final AtomicBoolean shouldTerminate = new AtomicBoolean(false);
  private final BlockingQueue<String> mainQueue;
  private final BlockingQueue<String> inputQueue;

  public RuntimeProducer(BlockingQueue<String> mainQueue, BlockingQueue<String> inputQueue) {
    this.mainQueue = mainQueue;
    this.inputQueue = inputQueue;
  }

  @Override
  public boolean shouldTerminate() {
    return this.shouldTerminate.get();
  }

  @Override
  public void terminate() {
    try {
      this.inputQueue.put(IProducerConsumer.EOF_PRODUCER);
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }

    this.shouldTerminate.set(true);
  }

  @Override
  public void run() {
    try {
      while (!this.shouldTerminate()) {
        this.mainQueue.put(this.inputQueue.take());
      }

    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
  }
}
