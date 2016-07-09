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

  public Condition getCond() {
    return this.cond;
  }

  public AST getInstr() {
    return this.instr;
  }

  public String accept(Visitor visitor) {
    return visitor.visit(this, "Repeat");
  }

  // Repeat runs at least once
  public void run(Environment env) throws Exception {
    this.instr.interpret(env);
    while (this.conv_cond.isTrue(env)) {
      this.instr.interpret(env);
    }
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String code = "";
    String end_label = "end_rep" + reg.getBranchIndex();
    String rep_label = "rep" + reg.getBranchIndex();
    reg.incBranchIndex();

    code += rep_label + ":\n";

    // First run the instructions
    code += this.instr.generateCode(env, reg);

    // Next, generate code to test if repeat condition is true or false
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
    // Store Constant value of right Expression directly into right_exp
    if (right.isConstant()) {
      code += this.moveConstant(right, right_reg);
    } else {
      code += this.getExpCode(right, env, reg, right_reg);
    }
    code += "\tcmp " + left_reg + ", " + right_reg + "\n";
    reg.reset(); // reset the registers to process instructions

    // conditional flag
    String flag = this.cond.getFlag();
    code += "\t" + flag + " " + rep_label + "\n";

    return code;
  }

  public String toString() {
    return "Repeat:\nCondition =>\n  " + this.cond.toString() +
      "Instructions =>\n  " + this.instr.toString() + "\n";
  }
}
