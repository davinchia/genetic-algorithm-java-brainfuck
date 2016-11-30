package needle;

import interpreter.BasicInterpreter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import chromosome.Chromosome;

public class Needle implements Runnable, Callable<Void> {
  BasicInterpreter basic = new BasicInterpreter();
  Chromosome x;
  int counter;
  double timeLimit;
  String goal;
  
  public Needle(Chromosome x, int counter, double timeLimit, String goal) {
    this.x = x;
    this.counter = counter;
    this.timeLimit = timeLimit;
    this.goal = goal;
  }
  
  public void run() {
//    basic.setTimeLimit(this.timeLimit);
//    long r1 = System.currentTimeMillis();
//    basic.run(this.x.code);
//    System.out.println("r1 took: " + (System.currentTimeMillis() - r1));
//    String out = basic.getOutput();
//    long r2 = System.currentTimeMillis();
//    int fitnessValue = fitness(out, this.goal);
//    System.out.println("r2 took: " + (System.currentTimeMillis() - r2));
    if (x.fitness == -1) {
      basic.setTimeLimit(this.timeLimit);
      basic.run(this.x.code);
      String out = basic.getOutput();
      x.fitness = fitness(out, this.goal);
      x.out = out;
    }
//    x.fitness = fitnessValue;
//    x.out = out;
//    System.out.println("Num: " + (++counter) + ", fitness: " + x.fitness + ", out: " + x.out + ", code: " + x.code);
//    System.out.println("Num: " + counter + ", fitness: " + x.fitness + ", code: " + x.code);
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

  public static void main(String[] args) {
    ExecutorService pool = Executors.newCachedThreadPool();
//    pool.execute(new Needle(new Chromosome(",>><<[>,.,.+<]>-[>-<++<>++>++++<+]+>+[.+.[]<++-.+-],<[[.]<>."), 0, 1.0));
  }
}
