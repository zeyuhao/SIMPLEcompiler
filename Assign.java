/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Assign extends Instruction {
  private Location loc;
  private Expression exp;

  public Assign(Location loc, Expression exp) {
    this.loc = loc;
    this.exp = exp;
  }

  public String toString() {
    return "Assign:\n  Location =>\n    " + this.loc.toString() +
                  "\n  Expression =>\n    " + this.exp.toString() + "\n";
  }
}
