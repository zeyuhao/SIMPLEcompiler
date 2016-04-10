/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Repeat extends Instruction {
  private Condition cond;
  private AST instr;

  public Repeat(Condition cond, AST instr) {
    this.cond = cond;
    this.instr = instr;
  }

  public String toString() {
    return "Repeat:\nCondition =>\n  " + this.cond.toString() +
      "Instructions =>\n  " + this.instr.toString() + "\n";
  }
}
