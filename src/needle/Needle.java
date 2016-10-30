package needle;

import interpreter.BasicInterpreter;
import java.util.concurrent.Callable;
import chromosome.Chromosome;

public class Needle implements Runnable, Callable<Void> {
  BasicInterpreter basic = new BasicInterpreter();
  Chromosome x;
  int counter;
  
  public Needle(Chromosome x, int counter) {
    this.x = x;
    this.counter = counter;
  }
  
  public void run() {
    basic.setTimeLimit(1);
    basic.run(this.x.code);
    String out = basic.getOutput();
    int fitnessValue = fitness(out, "hi");
    x.fitness = fitnessValue;
    x.out = out;
//    System.out.println("Num: " + (++counter) + ", fitness: " + x.fitness + ", out: " + x.out + ", code: " + x.code);
    System.out.println("Num: " + counter + ", fitness: " + x.fitness + ", code: " + x.code);
  }
  
  public Void call() {
    this.run();
    return null;  
  }
  
  public static int fitness(String source, String goal) {
    int sIdx = 0, gIdx = 0;
    
    int count = 0;
    char[] sChars = source.toCharArray();
    char[] gChars = goal.toCharArray();
    while(sIdx < sChars.length && gIdx < gChars.length) count += 256 - Math.abs(sChars[sIdx++] - gChars[gIdx++]);
     
   return count;
  }

}
