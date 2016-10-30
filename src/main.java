import generator.RandomGenerator;
import interpreter.BasicInterpreter;
import java.util.*;

public class main {
  public static final int EACHGEN = 20;
  
  public static class chromosome implements Comparable<chromosome>{
    String code;
    String out;
    int fitness;
    
    public chromosome(String x) {
      this.code = x;
    }
    
    public int compareTo(chromosome x) {
      return this.fitness - x.fitness;
    }
  }
  
  public static void main(String[] args) {
    RandomGenerator rand = new RandomGenerator(60, EACHGEN);
    BasicInterpreter basic = new BasicInterpreter();
    
    basic.setTimeLimit(1);
    ArrayList<String> init = rand.generate();
    ArrayList<chromosome> pop = new ArrayList<chromosome>();
    
    for(String x: init) {
      pop.add(new chromosome(x));
    }
    
    double bestFitness = 0;
    String bestOutput = ""; String bestCode = "";
    
    int counter = 0;
    int numGen = 1;
//    boolean first = true;
    while (bestOutput.length() < 2 || !bestOutput.substring(0, 2).equals("hi")) {
      counter = 0;
      System.out.println("Gen: " + numGen++);     
      ListIterator<chromosome> iter = pop.listIterator();
      while(iter.hasNext()) {
        chromosome x = iter.next();
        basic.run(x.code);
//        if (!basic.run(x.code) && first) {
//          iter.set(new chromosome(rand.generateOne()));
//          iter.previous();
//          continue;
//        }
        
        String out = basic.getOutput();
        int fitnessValue = fitness(out, "hi");
        x.fitness = fitnessValue;
        x.out = out;
        basic.reset();
      }
      
//      first = false;
      Collections.sort(pop);
      bestFitness = pop.get(pop.size()-1).fitness;
      bestCode = pop.get(pop.size()-1).code;
      bestOutput = pop.get(pop.size()-1).out;
      System.out.println("Overall: bestFitness: " + bestFitness + ", bestCode: " + bestCode);
      System.out.println( "BestOut: " + bestOutput);
      pop = genNextPop(pop);
    }
    System.out.println(bestOutput);
  }

  public static int fitness(String source, String goal) {
    int sIdx = 0, gIdx = 0;
    
    int count = 0;
    char[] sChars = source.toCharArray();
    char[] gChars = goal.toCharArray();
    while(sIdx < sChars.length && gIdx < gChars.length) count += 256 - Math.abs(sChars[sIdx++] - gChars[gIdx++]);
     
   return count;
  }

  public static ArrayList<chromosome> genNextPop(ArrayList<chromosome> pop) {
    ArrayList<chromosome> nextP = new ArrayList<chromosome>();
    Random rand = new Random();
    char[] map = {'-', '+', '>', '<', ',', '.', '[', ']'};
    
    Collections.sort(pop);
    int totalF = (pop.size() * (pop.size() + 1)) / 2;
    int chromLen = pop.get(0).code.length();
    int popSize = pop.size();
    
    //Conduct some elitism; bring over the fittest individual
    nextP.add(pop.get(pop.size()-1));
    
    while (nextP.size() <= EACHGEN) {
      String p1 = pop.get(pickOne(popSize, totalF)).code;
      String p2 = pop.get(pickOne(popSize, totalF)).code;
      
      while(p1.equals(p2)) p2 = pop.get(pickOne(popSize, totalF)).code;
      
      if (rand.nextDouble() <= 0.65) {
        int crossPoint = rand.nextInt(chromLen);
        String temp = p2.substring(crossPoint);
        p2 = p2.substring(0, crossPoint) + p1.substring(crossPoint);
        p1 = p1.substring(0, crossPoint) + temp;
      }
      
      if (rand.nextDouble() <= 0.02) {
        int rIdx = rand.nextInt(p1.length());
        p1 = p1.substring(0, rIdx) + map[rand.nextInt(map.length)] + p1.substring(rIdx+1);
        if (rand.nextDouble() <= 0.02) {
          rIdx = rand.nextInt(p2.length());
          p2 = p2.substring(0, rIdx) + map[rand.nextInt(map.length)] + p2.substring(rIdx+1);
        }
      }
      
      nextP.add(new chromosome(p1));
      nextP.add(new chromosome(p2));
    }
    return nextP;
  }
  
  //Implements rank selection algorithm   
//  public static int pickOne(int len, int totalF) {
//    double randNum = Math.random() * totalF;
//      
//    for(int i = 1; i <= len; i++) {
//      randNum -= i;
//      if (randNum <= 0)
//        return (len - i); //Because we are minimising, we invert the assignments so 20 -> index 0, 19 -> index 1 etc.
//    }
//    
//    return 0;
//  }
  
  public static int pickOne(int len, int totalF) {
    double randNum = Math.random() * totalF;
      
    for(int i = 1; i <= len; i++) {
      randNum -= i;
      if (randNum <= 0)
        return i-1; 
    }
    
    return len-1;
  }
  
    
}
//  public static ArrayList<String> selectElite(HashMap<String, Double> codeToValue) {
//    double total = 0;
//    ArrayList<String> seed = new ArrayList<String>();
//    
//    for (String x: codeToValue.keySet()) {
//      total += codeToValue.get(x);
//    }
//    
//    while(seed.size() <= 5) {
//      for (String code: codeToValue.keySet()) {
//        if (!seed.contains(code)) {
//          double rand = Math.random();
//          if (rand <= codeToValue.get(code)/total) 
//            seed.add(code);
//        }
//      }
//    }
//    return seed;
//  }
  

  
//  public static ArrayList<String> generateNextPop(ArrayList<String> seed) {
//    ArrayList<String> nextPop = new ArrayList<String>();
//    Random rand = new Random();
//    while(nextPop.size() <= 20) {
//      int one = rand.nextInt(seed.size());
//      int two = rand.nextInt(seed.size());
//      
//      String p1 = seed.get(one);
//      String p2 = seed.get(two);
//      
//      if (rand.nextDouble() <= 0.70) {
//        int crossPoint = rand.nextInt(seed.get(0).length());
//        String temp = p2.substring(crossPoint);
//        p2 = p2.substring(0, crossPoint) + p1.substring(crossPoint);
//        p1 = p1.substring(0, crossPoint) + temp;
//      }
//      
//      if (rand.nextDouble() <= 0.02) {
//        int rIdx = rand.nextInt(p1.length());
//        p1 = p1.substring(0, rIdx) + map[rand.nextInt(map.length)] + p1.substring(rIdx+1);
//        if (rand.nextDouble() <= 0.02) {
//          rIdx = rand.nextInt(p2.length());
//          p2 = p2.substring(0, rIdx) + map[rand.nextInt(map.length)] + p2.substring(rIdx+1);
//        }
//      }
//      
//      nextPop.add(p1);
//      nextPop.add(p2);
//    }
//    return nextPop;
//  }
