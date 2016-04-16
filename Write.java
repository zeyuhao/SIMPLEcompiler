/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Write extends Instruction {
  private Expression exp;

  public Write(Expression exp) {
    this.exp = exp;
    exp.setParent(this);
  }

  public String toString() {
    return "Write:\n  expression =>\n  " + this.exp.toString() + "\n";
  }
}
