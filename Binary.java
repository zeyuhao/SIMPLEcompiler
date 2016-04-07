/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Binary extends Expression {
  private Token operator;
  private Expression exp_left;
  private Expression exp_right;

  public Binary(Token operator, Expression exp_left, Expression exp_right) {
    super();
    this.operator = operator;
    this.exp_left = exp_left;
    this.exp_right = exp_right;
  }

  public String toString() {
    return this.exp_left.toString() + this.operator.returnVal() +
      this.exp_right.toString();
  }
}
