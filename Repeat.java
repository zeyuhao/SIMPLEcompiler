/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Repeat extends Instruction {
  private Condition cond;
  private Instruction instr;

  public Repeat(Condition cond, Instruction instr) {
    this.cond = cond;
    this.instr = instr;
  }

}
