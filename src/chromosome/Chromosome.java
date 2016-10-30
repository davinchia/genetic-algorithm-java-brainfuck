package chromosome;

public class Chromosome implements Comparable<Chromosome> {
  public String code;
  public String out;
  public int    fitness;

  public Chromosome(String x) {
      this.code = x;
    }

  public int compareTo(Chromosome x) {
    return this.fitness - x.fitness;
  }
}
