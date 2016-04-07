/*
Zeyu Hao
zhao7@jhu.edu
*/

public class If extends Instruction {
  private Condition cond;
  private Instruction instr_true;
  private Instruction instr_false;

  // If constructor with an ELSE Instruction
  public If(Condition cond, Instruction instr_true, Instruction instr_false) {
    this.cond = cond;
    this.instr_true = instr_true;
    this.instr_false = instr_false;
  }

  // If constructor with no ELSE Instruction
  public If(Condition cond, Instruction instr_true) {
    this.cond = cond;
    this.instr_true = instr_true;
  }

}
