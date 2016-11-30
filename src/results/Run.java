package results;
import java.util.*;

public class Run {
  int genSize;
  double crossoverRate;
  double mutationRate;
  double timeLimit;
  int codeLength;
  String goal;
  ArrayList<FinalOut> runs = new ArrayList<FinalOut>();
  
  public Run(int genSize, double crossoverRate, double mutationRate, double timeLimit, int codeLen, String goal) {
    this.genSize = genSize;
    this.crossoverRate = crossoverRate;
    this.mutationRate = mutationRate;
    this.timeLimit = timeLimit;
    this.codeLength = codeLen;
    this.goal = goal;
  }
  
  public void addRun(FinalOut f) {
    this.runs.add(f);
  }
  
  public ArrayList<FinalOut> getRuns() {
    return this.runs;
  }
}
