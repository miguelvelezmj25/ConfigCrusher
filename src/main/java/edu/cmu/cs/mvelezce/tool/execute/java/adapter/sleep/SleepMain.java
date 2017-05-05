package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.java.programs.Sleep2;
import edu.cmu.cs.mvelezce.java.programs.Sleep3;
import edu.cmu.cs.mvelezce.java.programs.Sleep4;
import edu.cmu.cs.mvelezce.java.programs.Sleep5;
import edu.cmu.cs.mvelezce.java.programs.Sleep6;
import edu.cmu.cs.mvelezce.java.programs.Sleep7;
import edu.cmu.cs.mvelezce.java.programs.Sleep8;
import edu.cmu.cs.mvelezce.java.programs.Sleep9;
import edu.cmu.cs.mvelezce.java.programs.Sleep10;
import edu.cmu.cs.mvelezce.java.programs.Sleep11;
import edu.cmu.cs.mvelezce.java.programs.Sleep12;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.Executer;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepMain {

    public static final String SLEEP_MAIN = SleepMain.class.getCanonicalName();

    public static void main(String[] args) throws InterruptedException, IOException, ParseException {
        String programName = args[0];
        String mainClass = args[1];
        String[] sleepArgs = Arrays.copyOfRange(args, 2, args.length);

        if(mainClass.toLowerCase().equals(Sleep1.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep1.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep2.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep2.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep3.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep3.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep4.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep4.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep5.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep5.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep6.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep6.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep7.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep7.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep8.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep8.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep9.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep9.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep10.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep10.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep11.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep11.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }
        else if(mainClass.toLowerCase().equals(Sleep12.FILENAME.toLowerCase())) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep12.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);
        }

        Set<String> performanceConfiguration = SleepAdapter.adaptConfigurationToPerformanceMeasurement(sleepArgs);
        Executer.logExecutedRegions(programName, performanceConfiguration, Regions.getExecutedRegionsTrace());
    }
}
