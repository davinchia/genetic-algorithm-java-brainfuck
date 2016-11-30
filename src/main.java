import java.util.*;
import java.util.concurrent.*;
import generator.RandomGenerator;
import needle.Needle;
import results.FinalOut;
import results.Run;
import runGA.RunGA;
import chromosome.Chromosome;

public class main {
  public static final int NUMRUNS = 10;
  public static final int GENSIZE = 20;
  public static final double TIMELIMIT = 20.0;
  public static final String GOAL = "h";

  public static final double STARTMUTATION = 0.75;
  public static final double ENDMUTATION = 0.95;
  public static final double STARTCROSSOVER = 0.95;
  public static final double ENDCROSSOVER = 0.95;
  public static final double INCRATE = 0.1;

  public static final int STARTLENGTH = 60;
  public static final int ENDLENGTH = 100;
  public static final int INCLENGTH = 10;

  public static void main(String[] args) {
    System.out.println("Starting: " );
    System.out.println("Start Mutate Rate: " + STARTMUTATION + " End Mutate Rate: " + ENDMUTATION + 
                       " Start Cross Rate: " + STARTCROSSOVER + " End Cross Rate: " + ENDCROSSOVER);
    ArrayList<Run> results = new ArrayList<Run>();
    for(double m = STARTMUTATION; m <= ENDMUTATION; m += INCRATE) {
      for (double c = STARTCROSSOVER; c <= ENDCROSSOVER; c+= INCRATE) {
        System.out.println("Running - Mutate Rate: " + m + " Cross Rate: " + c);
        Run r = new Run(GENSIZE, c, m, TIMELIMIT, STARTLENGTH, GOAL);
        for (int n = 0; n < NUMRUNS; n ++) {
          System.out.println("Start run: " + (n+1));
          RunGA g = new RunGA (GENSIZE, c, m, TIMELIMIT, STARTLENGTH, GOAL);
          r.addRun(g.run());
          System.out.println("End run: " + (n+1));
        }
        results.add(r);
      }
    }
    
    int counter = 1;
    for(Run r : results) {
      System.out.println(counter++);
      for (FinalOut f : r.getRuns()) {
        System.out.println(f);
      }
    }
    
  }
}

