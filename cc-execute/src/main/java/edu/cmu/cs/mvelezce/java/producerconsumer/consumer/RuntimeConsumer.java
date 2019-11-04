package edu.cmu.cs.mvelezce.java.producerconsumer.consumer;

import edu.cmu.cs.mvelezce.consumer.IConsumer;
import edu.cmu.cs.mvelezce.setup.IProducerConsumer;

import java.util.concurrent.BlockingQueue;

public class RuntimeConsumer implements IConsumer<String> {

  private final BlockingQueue<String> queue;

  public RuntimeConsumer(BlockingQueue<String> queue) {
    this.queue = queue;
  }

  @Override
  public void consume(String data) {
    System.out.println(data);
  }

  @Override
  public boolean shouldTerminate(String data) {
    return IProducerConsumer.EOF_PRODUCER.equals(data);
  }

  @Override
  public void run() {
    try {
      while (true) {
        String data = this.queue.take();

        if (this.shouldTerminate(data)) {
          break;
        }

        this.consume(data);
      }
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
  }
}
