package edu.cmu.cs.mvelezce.java.execute.instrumentation.producerconsumer.consumer;

import edu.cmu.cs.mvelezce.consumer.IConsumer;
import edu.cmu.cs.mvelezce.setup.IProducerConsumer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class RuntimeConsumer implements IConsumer<String> {

  private static byte[] NEW_LINE_BYTES = "\n".getBytes();

  private final BlockingQueue<String> queue;
  private final FileOutputStream fos;

  public RuntimeConsumer(BlockingQueue<String> queue) {
    this.queue = queue;

    try {
      File outputFile = new File("data.ser");
      this.fos = new FileOutputStream(outputFile);
    } catch (FileNotFoundException fnfe) {
      throw new RuntimeException("Could not initialize the file output stream", fnfe);
    }
  }

  @Override
  public void consume(String data) {
    try {
      this.fos.write(data.getBytes());
      this.fos.write(NEW_LINE_BYTES);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
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

    try {
      this.fos.flush();
      this.fos.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
