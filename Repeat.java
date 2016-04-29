/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Repeat extends Instruction {
  private Condition cond;
  private Condition conv_cond;
  private AST instr;

  public Repeat(Condition cond, AST instr) {
    this.cond = cond;
    this.instr = instr;
    cond.setParent(this);
    instr.setParent(this);
  }

  public void setConvCond(Condition cond) {
    this.conv_cond = cond;
  }

  public void run(Environment env) throws Exception {
    while (this.conv_cond.isTrue(env)) {
      this.instr.interpret(env);
    }
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String str = "";
    return str;
  }

  public String toString() {
    return "Repeat:\nCondition =>\n  " + this.cond.toString() +
      "Instructions =>\n  " + this.instr.toString() + "\n";
  }
}
