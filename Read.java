/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Read extends Instruction {
  private Location loc;

  public Read(Location loc) {
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
    String str = "";
    String reg_0 = reg.available();
    reg.setInUse();
    str += "\tldr " + reg_0 + ", =rformat\n";
    Location base = this.getBase(this.loc);
    String name = base.toString();
    int addr = this.getEnvBox(base, env).getAddress();
    String reg_1 = reg.available();
    reg.setInUse();
    String reg_2 = reg.available();
    reg.setInUse();
    String reg_3 = reg.available();
    reg.setInUse();
    str += "\tldr " + reg_2 + ", addr_" + name + "\n";
    str += "\tmov " + reg_3 + ", #" + addr + "\n";
    str += "\tadd " + reg_2 + ", " + reg_2 + ", " + reg_3 + "\n";
    str += "\tmov " + reg_1 + ", " + reg_2 + "\n";
    str += "\tbl scanf\n\n";
    reg.reset();
    return str;
  }

  public String toString() {
    return "Read:\n  Location =>\n  " + this.loc.toString() + "\n";
  }
}
