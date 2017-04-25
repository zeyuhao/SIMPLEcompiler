/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Read extends Instruction {
  private Location loc;

  public Read(Location loc) throws Exception {
    this.loc = loc;
    loc.setParent(this);
  }

  public Location getLoc() {
    return this.loc;
  }

  public String accept(Visitor visitor) {
    return visitor.visit(this, "Read");
  }

  public void run(Environment env) throws Exception {
    // Get the base leftmost Location Variable we are operating from
    Location base = this.getBase(this.loc);
    Box box = this.getEnvBox(base, env);
    int value = env.read_int();
    ((IntegerBox)box).setBox(value);
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String code = "";
    String reg_0 = reg.available();
    reg.setInUse();
    String reg_1 = reg.available();
    reg.setInUse();
    String addr_reg = reg.available();
    reg.setInUse();
    Location base = this.getBase(this.loc);
    String name = base.toString();
    code += "\tldr " + reg_0 + ", =rformat\n";
    code += "\tldr " + addr_reg + ", addr_" + name + "\n";

    // If base Location is an Array or Record, we need to get the address
    if (base.getType().isArray() || base.getType().isRecord()) {
      // Offset address will be stored in a register
      // Register used to calculate the offset for addresses
      String offset_reg = reg.available();
      reg.setInUse();
      code += this.getAddressCode(base, env, offset_reg, reg);
      code += "\tadd " + addr_reg + ", " + addr_reg + ", " + offset_reg + "\n";
    } else {
      // Offset address will be a constant number
      String address = "#" + this.getEnvBox(base, env).getAddress();
      code += "\tadd " + addr_reg + ", " + addr_reg + ", " + address + "\n";
    }
    code += "\tmov " + reg_1 + ", " + addr_reg + "\n";
    // Restore all registers from stack
    code += reg.popAll();
    code += "\tbl scanf\n\n";
    reg.reset();
    return code;
  }

  public String toString() {
    return "Read:\n  Location =>\n  " + this.loc.toString() + "\n";
  }
}
