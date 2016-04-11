/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.lang.Integer;

public class Binary extends Expression {
  private Token operator;
  private Expression exp_left;
  private Expression exp_right;

  // Operator can be +, -, *, DIV, or MOD
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
    this.token = exp_left.getToken();
  }

  public boolean isConstant() {
    return exp_left.isConstant() && exp_right.isConstant();
  }

  public Expression fold() {
    // If expressions in Binary aren't Constant, then return null
    if (this.isConstant()) {
      int left = this.exp_left.returnNumber().returnVal();
      int right = this.exp_right.returnNumber().returnVal();
      if (this.operator.returnVal().equals("+")) {
        int ans = left + right;
        return new Expression(
          new Constant(this.type, ans),
          new Token("integer", Integer.toString(ans), 0 , 0));
      } else if (this.operator.returnVal().equals("-")) {
        int ans = left - right;
        return new Expression(
          new Constant(this.type, ans),
          new Token("integer", Integer.toString(ans), 0 , 0));
      } else if (this.operator.returnVal().equals("*")) {
        int ans = left * right;
        return new Expression(
          new Constant(this.type, ans),
          new Token("integer", Integer.toString(ans), 0 , 0));
      } else if (this.operator.returnVal().equals("DIV")) {
        int ans = left / right;
        return new Expression(
          new Constant(this.type, ans),
          new Token("integer", Integer.toString(ans), 0 , 0));
      } else if (this.operator.returnVal().equals("MOD")) {
        int ans = left % right;
        return new Expression(
          new Constant(this.type, ans),
          new Token("integer", Integer.toString(ans), 0 , 0));
      }
    }
    return null;
  }

  public String toString() {
    return this.exp_left.toString() + " " + this.operator.returnVal() + " " +
      this.exp_right.toString();
  }
}
