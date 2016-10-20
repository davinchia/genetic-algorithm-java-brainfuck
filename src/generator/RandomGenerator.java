package generator;
import java.util.*;

public class RandomGenerator {
  int baseLength;
  int numInEachGen;
  private final char[] OPS = {'+', '-', '>', '<', ',', '.', '[', ']'};
  Random rand = new Random();
  
  public RandomGenerator(int base, int num) {
    this.baseLength = base;
    this.numInEachGen = num;
  }
  
  public ArrayList<String> generate() {
    ArrayList<String> initialList = new ArrayList<String>();
    for (int i = 0; i < this.numInEachGen; i++) {
      initialList.add(this.generateOne());
    }
    return initialList;
  }
  
  public String generateOne() {
    StringBuilder curr = new StringBuilder();
    for (int j = 0; j < this.baseLength; j++) {
      char c = this.OPS[this.rand.nextInt(this.OPS.length)];
      curr.append(c);
    }
    return curr.toString();   
  }
}
