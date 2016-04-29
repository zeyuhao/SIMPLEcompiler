/*
Zeyu Hao
zhao7@jhu.edu
*/

public class If extends Instruction {
  private Condition cond;
  private AST instr_true;
  private AST instr_false;

  // If constructor with an ELSE Instruction
  public If(Condition cond, AST instr_true, AST instr_false) {
    this.cond = cond;
    this.instr_true = instr_true;
    this.instr_false = instr_false;
    cond.setParent(this);
    instr_true.setParent(this);
    instr_false.setParent(this);
  }

  // If constructor with no ELSE Instruction
  public If(Condition cond, AST instr_true) {
    this.cond = cond;
    this.instr_true = instr_true;
  }

  public void run(Environment env) throws Exception {
    if (this.cond.isTrue(env)) {
      this.instr_true.interpret(env);
    } else if (this.instr_false != null) {
      this.instr_false.interpret(env);
    }
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String str = "";
    return str;
  }

  public String toString() {
    String str = "If:\nCondition =>\n  " + this.cond.toString() +
      "True =>\n  " + this.instr_true.toString();
    if (this.instr_false != null) {
      str += "false =>\n  " + this.instr_false.toString();
    }
    int index = str.lastIndexOf("\n");
    str = str.substring(0, index);
    return str;
  }
}
