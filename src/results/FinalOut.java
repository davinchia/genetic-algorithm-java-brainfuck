package results;

public class FinalOut {
  int numGen;
  long time;
  String code;
  String result;

  public FinalOut (int num, long t, String c, String out) {
    this.numGen = num;
    this.time = t;
    this.code = c;
    this.result = out;
  }
  
  public String toString() {
    return "Num: " + this.numGen + ", Time: " + this.time + ", Code: " + this.code + ", Result: " + this.result;
  }
}