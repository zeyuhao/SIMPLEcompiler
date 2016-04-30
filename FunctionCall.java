/*
Zeyu Hao
zhao7@jhu.edu
*/

public class FunctionCall extends Instruction {

  private Box exp_box;

  public FunctionCall(ProcedureCall proc_call, Procedure proc, Environment env)
    throws Exception {
    proc_call.run(env);
    this.exp_box = this.getExpBox(proc.getReturnExp(), proc_call.getEnv());
  }

  public Box returnExpBox() {
    return this.exp_box;
  }

  public String toString() {
    return "";
  }
}
