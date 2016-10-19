package generator;
import java.util.*;

public class RandomGenerator {
  int baseLength;
  int numInEachGen;
  private final char[] OPS = {'+', '-', '>', '<', ',', '.', '[', ']'};
  
  public RandomGenerator(int base, int num) {
    this.baseLength = base;
    this.numInEachGen = num;
  }
  
  public ArrayList<String> generate() {
    ArrayList<String> initialList = new ArrayList<String>();
    Random rand = new Random();
    for (int i = 0; i < this.numInEachGen; i++) {
      StringBuilder curr = new StringBuilder();
      for (int j = 0; j < this.baseLength; j++) {
        char c = this.OPS[rand.nextInt(this.OPS.length)];
        curr.append(c);
      }
      initialList.add(curr.toString());
    }
    return initialList;
  }
}
