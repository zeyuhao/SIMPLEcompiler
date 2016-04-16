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
    loc.setParent(this);
    exp.setParent(this);
  }

  public boolean isAssign() {
    return true;
  }

  public void run(Environment env) {
    // Get the base leftmost Location Variable we are operating from
    Location base = this.getBase(this.loc);
    Box box = this.getBox(base, env);
    int value = this.exp.returnNumber().returnVal();
    if (box.isInteger()) {
      ((IntegerBox)box).setBox(value);
    }
    System.out.println(box.toString());
    System.out.println();
  }

  public String toString() {
    return "Assign:\n  Location =>\n    " + this.loc.toString() +
                  "\n  Expression =>\n    " + this.exp.toString() + "\n";
  }
}
