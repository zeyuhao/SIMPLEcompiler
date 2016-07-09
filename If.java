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

  public Condition getCond() {
    return this.cond;
  }

  public AST getTrue() {
    return this.instr_true;
  }

  public AST getFalse() {
    return this.instr_false;
  }

  public Boolean hasFalse() {
    return this.instr_false != null;
  }

  public String accept(Visitor visitor) {
    return visitor.visit(this, "If");
  }

  public void run(Environment env) throws Exception {
    if (this.cond.isTrue(env)) {
      this.instr_true.interpret(env);
    } else if (this.hasFalse()) {
      this.instr_false.interpret(env);
    }
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String code = "";
    String start_label = "start_if" + reg.getBranchIndex();
    String end_label = "end_if" + reg.getBranchIndex();
    String if_label = "if" + reg.getBranchIndex();
    reg.incBranchIndex();

    code += start_label + ":\n";

    // Next, generate code to test if condition is true or false
    Expression left = this.cond.getLeft();
    Expression right = this.cond.getRight();
    // Assign Register to store Variable value of left Expression
    String left_reg = reg.available();
    reg.setInUse();
    // Assign Register to store value of Right Expression
    String right_reg = reg.available();
    reg.setInUse();
    // Store Constant value of left Expression directly into left_exp
    if (left.isConstant()) {
      code += this.moveConstant(left, left_reg);
    } else {
      code += this.getExpCode(left, env, reg, left_reg);
    }
    // Store value of right Expression into right_reg
    if (right.isConstant()) {
      code += this.moveConstant(right, right_reg);
    } else {
      code += this.getExpCode(right, env, reg, right_reg);
    }
    code += "\tcmp " + left_reg + ", " + right_reg + "\n";
    reg.reset(); // reset the registers to process instructions

    // conditional flag
    String flag = this.cond.getFlag();

    String if_section = if_label + ":\n";
    if_section += this.instr_true.generateCode(env, reg);
    if_section += "\tb " + end_label + "\n";

    // If the Else instruction exists
    if (this.hasFalse()) {
      String else_label = "else" + reg.getBranchIndex();
      code += "\t" + flag + " " + else_label + "\n";
      code += if_section;
      code += else_label + ":\n";
      code += this.instr_false.generateCode(env, reg);
      code = removeTrailingNewLine(code);
    } else {
      code += "\t" + flag + " " + end_label + "\n\n";
      code += if_section;
    }
    // end if
    code += end_label + ":\n\n";
    return code;
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
