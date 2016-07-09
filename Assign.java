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

  public String accept(Visitor visitor) {
    return visitor.visit(this, "Assign");
  }

  public Location getLoc() {
    return this.loc;
  }

  public Expression getExp() {
    return this.exp;
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
      // Deep copy of ArrayBox
      ((ArrayBox)box).assign((ArrayBox)exp_box);
      System.out.println(box.toString());
      System.out.println(exp_box.toString());
    } else if (box.isRecord() && exp_box.isRecord()) {
      // Deepy copy of RecordBox
      ((RecordBox)box).assign((RecordBox)exp_box);
    }
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String str = "";
    Location base = this.getBase(this.loc);
    String name = base.toString();
    int addr = this.getEnvBox(base, env).getAddress();
    String loc_reg = reg.available();
    reg.setInUse();
    String val_reg = reg.available();
    reg.setInUse();
    // First load loc_reg with the addr of the location we are assigning to
    str += "\tldr " + loc_reg + ", addr_" + name + "\n";
    if (this.exp.isConstant()) {
      str += this.moveConstant(this.exp, val_reg);
    } else {
      str += this.getExpCode(this.exp, env, reg, val_reg);
    }
    // Store value inside val_reg into loc_reg at offset addr
    str += "\tstr " + val_reg + ", [" + loc_reg + ", +#" + addr + "]\n\n";
    // Reset all registers for use in the next Instruction
    reg.reset();
    return str;
  }

  public String toString() {
    return "Assign:\n  Location =>\n    " + this.loc.toString() +
                  "\n  Expression =>\n    " + this.exp.toString() + "\n";
  }
}
