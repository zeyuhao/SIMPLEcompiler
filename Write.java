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

  public void run(Environment env) throws Exception {
    Box exp_box = this.getExpBox(this.exp, env);
    int value = exp_box.getVal();
    System.out.println(value);
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String str = "";
    String reg_0 = reg.available();
    reg.setInUse();
    str += "\tldr " + reg_0 + ", =wformat\n";
    String reg_1 = reg.available();
    reg.setInUse();
    str += this.getExpCode(this.exp, env, reg, reg_1);
    str += "\tbl printf\n\n";
    reg.reset();
    return str;
  }

  public String toString() {
    return "Write:\n  expression =>\n  " + this.exp.toString() + "\n";
  }
}
