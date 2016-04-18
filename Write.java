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

  public String toString() {
    return "Write:\n  expression =>\n  " + this.exp.toString() + "\n";
  }
}
