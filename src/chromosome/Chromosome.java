package chromosome;
import java.util.*;

public class Chromosome implements Comparable<Chromosome> {
  public String code;
  public String out;
  public int    fitness;

  public Chromosome(String x) {
      this.code = x;
      this.fitness = -1;
    }

  public int compareTo(Chromosome x) {
    return this.fitness - x.fitness;
  }
  
  public static void main(String[] args) {
    ArrayList<Chromosome> a = new ArrayList<Chromosome>();
    
    Chromosome x = new Chromosome("");
    x.fitness = 10;
    Chromosome y = new Chromosome("");
    y.fitness = 10;
    Chromosome z = new Chromosome("");
    z.fitness = 1;
    Chromosome l = new Chromosome("");
    l.fitness = -1;
    Chromosome m = new Chromosome("");
    m.fitness = 1000;
    
    a.add(x); a.add(z); a.add(y); a.add(l); a.add(m);
    
    for (Chromosome c: a) {
      System.out.println(c.fitness);
    }
    
    Collections.sort(a);
    System.out.println("Sorting!");
    
    for (Chromosome c: a) {
      System.out.println(c.fitness);
    }
  }
}
