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
    } else if (box.isRecord() && exp_box.isRecord()) {
      // Deepy copy of RecordBox
      ((RecordBox)box).assign((RecordBox)exp_box);
    }
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String code = "";
    // base should always be a Variable
    Location base = this.getBase(this.loc);
    String name = base.toString();
    String address = "";
    // If base Location is an Array or Record, we need to get the address
    if (base.getType().isArray() || base.getType().isRecord()) {
      // Register used to calculate the offset for addresses
      String offset_reg = reg.available();
      reg.setInUse();
      code += this.getAddressCode(base, env, offset_reg, reg);
      address = offset_reg;
    } else {
      address = "#" + this.getEnvBox(base, env).getAddress();
    }
    String loc_reg = reg.available();
    reg.setInUse();
    String val_reg = reg.available();
    reg.setInUse();
    // First load loc_reg with the base addr of the location we are assigning to
    code += "\tldr " + loc_reg + ", addr_" + name + "\n";
    // Preserve r0
    code += reg.push(loc_reg);
    if (this.exp.isConstant()) {
      code += this.moveConstant(this.exp, val_reg);
    } else {
      code += this.getExpCode(this.exp, env, reg, val_reg);
    }
    // Store value inside val_reg into loc_reg at offset addr
    // Pop off all registers except for r0 from stack
    code += reg.popAll(loc_reg);
    // Restore r0 last
    code += reg.pop(loc_reg);
    code += "\tstr " + val_reg + ", [" + loc_reg + ", +" + address + "]\n\n";
    // Reset all registers for use in the next Instruction
    reg.reset();
    return code;
  }

  public String toString() {
    return "Assign:\n  Location =>\n    " + this.loc.toString() +
                  "\n  Expression =>\n    " + this.exp.toString() + "\n";
  }
}
