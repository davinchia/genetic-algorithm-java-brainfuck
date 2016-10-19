import generator.RandomGenerator;
import interpreter.BasicInterpreter;
import java.util.*;

public class main {
  public static void main(String[] args) {
    RandomGenerator rand = new RandomGenerator(150, 25);
    BasicInterpreter basic = new BasicInterpreter();
    
    basic.setTimeLimit(1);
    ArrayList<String> pop = rand.generate();
    double maxFitness = Double.MIN_VALUE;
    String maxOutput = ""; String maxCode = "";
    HashMap <String, Double> codeToValue = new HashMap<String, Double>();
    
//    int counter = 0;
    int numGen = 1;
    while (!maxOutput.equals("hi")) {
//      counter = 0;
      System.out.print("Gen: " + numGen++ + ", ");        
      for(String code: pop) {
        basic.run(code);
        String out = basic.getOutput();
        double fitnessValue = fitness(out, "hi");
        codeToValue.put(code, fitnessValue);
        
        if (fitnessValue > maxFitness) {
          maxFitness = fitnessValue;
          maxOutput = out;
          maxCode = code;
        }
//        counter++;
        basic.reset();
      }
      
      System.out.print("maxFitness: " + maxFitness + ", maxOut: " + maxOutput + ", maxCode: " + maxCode);
      System.out.println();
      
      ArrayList<String> elite = selectElite(codeToValue);
      pop = generateNextPop(elite);
    }
    System.out.println(maxOutput);
  }
  
  public static double fitness(String source, String goal) {
    int sIdx = 0, gIdx = 0;
    
    int count = 0;
    char[] sChars = source.toCharArray();
    char[] gChars = goal.toCharArray();
    while(sIdx < sChars.length && gIdx < gChars.length) count += Math.abs(sChars[sIdx++] - gChars[gIdx++]);
    while(sIdx < sChars.length) count += sChars[sIdx++];
    while(gIdx < gChars.length) count += gChars[gIdx++];
     
   return 1.0/(count + 0.01);
  }
  
  public static ArrayList<String> selectElite(HashMap<String, Double> codeToValue) {
    double total = 0;
    ArrayList<String> seed = new ArrayList<String>();
    
    for (String x: codeToValue.keySet()) {
      total += codeToValue.get(x);
    }
    
    while(seed.size() <= 5) {
      for (String code: codeToValue.keySet()) {
        if (!seed.contains(code)) {
          double rand = Math.random();
          if (rand <= codeToValue.get(code)/total) 
            seed.add(code);
        }
      }
    }
    return seed;
  }
  
  public static ArrayList<String> generateNextPop(ArrayList<String> seed) {
    char[] map = {'-', '+', '>', '<', ',', '.', '[', ']'};
    ArrayList<String> nextPop = new ArrayList<String>();
    Random rand = new Random();
    while(nextPop.size() <= 20) {
      int one = rand.nextInt(seed.size());
      int two = rand.nextInt(seed.size());
      
      String p1 = seed.get(one);
      String p2 = seed.get(two);
      
      if (rand.nextDouble() <= 0.70) {
        int crossPoint = rand.nextInt(seed.get(0).length());
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
      
      nextPop.add(p1);
      nextPop.add(p2);
    }
    return nextPop;
  }
}
