package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface Instrumenter {

    public void instrument(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, InterruptedException;

    public void instrument() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException;

    public void compileFromSource() throws IOException, InterruptedException;

}
