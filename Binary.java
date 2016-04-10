/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Binary extends Expression {
  private Token operator;
  private Expression exp_left;
  private Expression exp_right;

  public Binary(Token operator, Expression exp_left, Expression exp_right,
    Type type) throws Exception {
    super();
    this.type = type;
    this.operator = operator;
    this.exp_left = exp_left;
    this.exp_right = exp_right;
    if (!this.exp_left.getType().isInteger() ||
        !this.exp_right.getType().isInteger()) {
      throw new Exception("Operator " + operator.toString() + " is only " +
        "applicable to Expressions of the Integer Type." +
        "\nFound " + exp_left.getToken().toString() + " of Type " +
        exp_left.getType().returnType() +
        "\nFound " + exp_right.getToken().toString() + " of Type " +
        exp_right.getType().returnType());
    }
  }

  public String toString() {
    return this.exp_left.toString() + " " + this.operator.returnVal() + " " +
      this.exp_right.toString();
  }
}
