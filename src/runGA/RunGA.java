package runGA;
import java.util.*;
import java.util.concurrent.*;
import generator.RandomGenerator;
import needle.Needle;
import results.FinalOut;
import chromosome.Chromosome;

public class RunGA {
  int genSize;
  double crossoverRate;
  double mutationRate;
  double timeLimit;
  int codeLength;
  String goal;
   
  public RunGA (int numInd, double crossover, double mutation, double time, int length, String s) {
    this.genSize = numInd;
    this.crossoverRate = crossover;
    this.mutationRate = mutation;
    this.timeLimit = time;
    this.codeLength = length;
    this.goal = s;
  }
   
  public FinalOut run() {
    RandomGenerator rand = new RandomGenerator(codeLength, genSize);
 
    ArrayList<String> init = rand.generate();
    ArrayList<Chromosome> pop = new ArrayList<Chromosome>();

    long begin = System.currentTimeMillis();
      
    for (String x : init) {
      pop.add(new Chromosome(x));
    }

    double bestFitness = 0;
    String bestOutput = "";
    String bestCode = "";

    int counter = 0;
    int numGen = 1;
    ExecutorService pool = Executors.newCachedThreadPool();
    
    while (bestOutput == null || bestOutput.length() < goal.length() || !bestOutput.substring(0, goal.length()).equals(goal)) {
//      long one = System.currentTimeMillis();
      counter = 0;
//      System.out.println("Gen: " + numGen++);
      ListIterator<Chromosome> iter = pop.listIterator();
      List <Callable<Void>> toThread = new ArrayList<>();
      while (iter.hasNext()) {
        // pool.submit(new Needle(iter.next(), ++counter));
        toThread.add(new Needle(iter.next(), ++counter, timeLimit, goal));
      }

      try {
        pool.invokeAll(toThread);
      } catch (InterruptedException e) {
        System.out.println(e);
      }
      
//      System.out.println("One took: " + (System.currentTimeMillis() - one));
      
//      long two = System.currentTimeMillis();
      Collections.sort(pop);
//      System.out.println("Two took: " + (System.currentTimeMillis() - two));
      bestFitness = pop.get(pop.size() - 1).fitness;
      bestCode = pop.get(pop.size() - 1).code;
      bestOutput = pop.get(pop.size() - 1).out;
      int numSame = 0;
      
      for (Chromosome x: pop) if (x.fitness == bestFitness) numSame++;
      
//      System.out.println("Overall: bestFitness: " + bestFitness + ", bestCode: " + bestCode + ", numSame: " + numSame);
//      System.out.println("BestOut: " + bestOutput);
      
//      long three = System.currentTimeMillis();
      pop = genNextPop(pop);
//      System.out.println("Three took: " + (System.currentTimeMillis() - three));
      
    }

    long timeTaken = (System.currentTimeMillis() - begin)/1000;
    System.out.println("Total Time Taken: " + (timeTaken) + "seconds");
    System.out.println(bestOutput);

    return (new FinalOut (numGen, timeTaken, bestCode, bestOutput));
  }

  public ArrayList<Chromosome> genNextPop(ArrayList<Chromosome> pop) {
    ArrayList<Chromosome> nextP = new ArrayList<Chromosome>();
    Random rand = new Random();
    char[] map = { '-', '+', '>', '<', ',', '.', '[', ']' };

    int totalF = (pop.size() * (pop.size() + 1)) / 2;
    int chromLen = pop.get(0).code.length();
    int popSize = pop.size();

    // Conduct some elitism; bring over the fittest individual
    nextP.add(pop.get(pop.size() - 1));
//    System.out.println("Initial: " + nextP.get(nextP.size() - 1).fitness + " Code: " + nextP.get(nextP.size() - 1).code);

    while (nextP.size() <= genSize) {
      String p1 = pop.get(pickOne(popSize, totalF)).code;
      String p2 = pop.get(pickOne(popSize, totalF)).code;

      while (p1.equals(p2))
        p2 = pop.get(pickOne(popSize, totalF)).code;

      if (rand.nextDouble() <= crossoverRate) {
        int crossPoint = rand.nextInt(chromLen);
        String temp = p2.substring(crossPoint);
        p2 = p2.substring(0, crossPoint) + p1.substring(crossPoint);
        p1 = p1.substring(0, crossPoint) + temp;
      }

      if (rand.nextDouble() <= mutationRate) {
        int rIdx = rand.nextInt(p1.length());
        p1 = p1.substring(0, rIdx) + map[rand.nextInt(map.length)] + p1.substring(rIdx + 1);
        if (rand.nextDouble() <= mutationRate) {
          rIdx = rand.nextInt(p2.length());
          p2 = p2.substring(0, rIdx) + map[rand.nextInt(map.length)] + p2.substring(rIdx + 1);
        }
      }

      nextP.add(new Chromosome(p1));
      nextP.add(new Chromosome(p2));
    }
//    Collections.sort(nextP);
//    System.out.println("After:   " + nextP.get(nextP.size() - 1).fitness + " Code: " + nextP.get(nextP.size() - 1).code);
    return nextP;
  }

  // Implements rank selection algorithm
  // public static int pickOne(int len, int totalF) {
  // double randNum = Math.random() * totalF;
  //
  // for(int i = 1; i <= len; i++) {
  // randNum -= i;
  // if (randNum <= 0)
  // return (len - i); //Because we are minimising, we invert the assignments so
  // 20 -> index 0, 19 -> index 1 etc.
  // }
  //
  // return 0;
  // }

  public static int pickOne(int len, int totalF) {
    double randNum = Math.random() * totalF;

    for (int i = 1; i <= len; i++) {
      randNum -= i;
      if (randNum <= 0)
        return i - 1;
    }

    return len - 1;
  }

}
// public static ArrayList<String> selectElite(HashMap<String, Double>
// codeToValue) {
// double total = 0;
// ArrayList<String> seed = new ArrayList<String>();
//
// for (String x: codeToValue.keySet()) {
// total += codeToValue.get(x);
// }
//
// while(seed.size() <= 5) {
// for (String code: codeToValue.keySet()) {
// if (!seed.contains(code)) {
// double rand = Math.random();
// if (rand <= codeToValue.get(code)/total)
// seed.add(code);
// }
// }
// }
// return seed;
// }

// public static ArrayList<String> generateNextPop(ArrayList<String> seed) {
// ArrayList<String> nextPop = new ArrayList<String>();
// Random rand = new Random();
// while(nextPop.size() <= 20) {
// int one = rand.nextInt(seed.size());
// int two = rand.nextInt(seed.size());
//
// String p1 = seed.get(one);
// String p2 = seed.get(two);
//
// if (rand.nextDouble() <= 0.70) {
// int crossPoint = rand.nextInt(seed.get(0).length());
// String temp = p2.substring(crossPoint);
// p2 = p2.substring(0, crossPoint) + p1.substring(crossPoint);
// p1 = p1.substring(0, crossPoint) + temp;
// }
//
// if (rand.nextDouble() <= 0.02) {
// int rIdx = rand.nextInt(p1.length());
// p1 = p1.substring(0, rIdx) + map[rand.nextInt(map.length)] +
// p1.substring(rIdx+1);
// if (rand.nextDouble() <= 0.02) {
// rIdx = rand.nextInt(p2.length());
// p2 = p2.substring(0, rIdx) + map[rand.nextInt(map.length)] +
// p2.substring(rIdx+1);
// }
// }
//
// nextPop.add(p1);
// nextPop.add(p2);
// }
// return nextPop;
// }
