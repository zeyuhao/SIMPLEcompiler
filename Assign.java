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

  public void run(Environment env) throws Exception {
    // Get the base leftmost Location Variable we are operating from
    Location base = this.getBase(this.loc);
    Box box = this.getEnvBox(base, env);
    Box exp_box = this.getExpBox(this.exp, env);
    if (box.isInteger() && exp_box.isInteger()) {
      int value = exp_box.getVal();
      ((IntegerBox)box).setBox(value);
    } else if (box.isArray() && exp_box.isArray()) {
      ((ArrayBox)box).assign((ArrayBox)exp_box);
    } else if (box.isRecord() && exp_box.isRecord()) {
      ((RecordBox)box).assign((RecordBox)exp_box);
    }
  }

  public String toString() {
    return "Assign:\n  Location =>\n    " + this.loc.toString() +
                  "\n  Expression =>\n    " + this.exp.toString() + "\n";
  }
}
