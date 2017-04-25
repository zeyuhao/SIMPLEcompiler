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

  public String accept(Visitor visitor) {
    return visitor.visit(this, "Write");
  }

  public Expression getExp() {
    return this.exp;
  }

  public void run(Environment env) throws Exception {
    Box exp_box = this.getExpBox(this.exp, env);
    int value = exp_box.getVal();
    System.out.println(value);
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String code = "";
    String reg_0 = reg.available();
    reg.setInUse();
    code += "\tldr " + reg_0 + ", =wformat\n";
    String reg_1 = reg.available();
    reg.setInUse();
    if (this.exp.isConstant()) {
      code += this.moveConstant(this.exp, reg_1);
    } else {
      code += this.getExpCode(this.exp, env, reg, reg_1);
    }
    // Restore all registers from stack
    code += reg.popAll();
    code += "\tbl printf\n\n";
    reg.reset();
    return code;
  }

  public String toString() {
    return "Write:\n  expression =>\n  " + this.exp.toString() + "\n";
  }
}
