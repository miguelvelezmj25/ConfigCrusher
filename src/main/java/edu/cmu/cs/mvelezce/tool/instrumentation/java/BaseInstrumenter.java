package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class BaseInstrumenter implements Instrumenter {
    private String srcDir;
    private String classDir;

    public BaseInstrumenter(String srcDir, String classDir) {
        this.srcDir = srcDir;
        this.classDir = classDir;
    }

    @Override
    public void instrument(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        Options.getCommandLine(args);

        if(Options.checkIfDeleteResult()) {
            this.compileFromSource();
        }

        if(Options.checkIfSave()) {
            this.instrument();
        }
    }

    public String getSrcDir() {
        return srcDir;
    }

    public String getClassDir() {
        return classDir;
    }
}
