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
        "\nFound " + exp_left.toString() + exp_left.getToken().posString() +
        " of Type " + exp_left.getType().returnType() +
        "\nFound " + exp_right.toString() + exp_right.getToken().posString() +
        " of Type " + exp_right.getType().returnType());
    }
    this.token = exp_left.getToken();
    exp_left.setParent(this);
    exp_right.setParent(this);
  }

  public boolean isBinary() {
    return true;
  }

  public boolean isConstant() {
    return exp_left.isConstant() && exp_right.isConstant();
  }

  public Expression fold() throws Exception {
    // If expressions in Binary aren't Constant, then return null
    if (this.isConstant()) {
      int ans = 0;
      boolean entered = false;
      int left = this.exp_left.returnNumber().returnVal();
      int right = this.exp_right.returnNumber().returnVal();
      if (this.operator.returnVal().equals("+")) {
        entered = true;
        ans = left + right;
      } else if (this.operator.returnVal().equals("-")) {
        entered = true;
        ans = left - right;
      } else if (this.operator.returnVal().equals("*")) {
        entered = true;
        ans = left * right;
      } else if (this.operator.returnVal().equals("DIV")) {
        entered = true;
        // Check for divide by Zero cases
        if (right == 0) {
          throw new Exception("Divide by zero found in Expression: " +
            this.toString()+ this.exp_right.getToken().posString());
        }
        ans = left / right;
      } else if (this.operator.returnVal().equals("MOD")) {
        entered = true;
        // Check for mod by Zero cases
        if (right == 0) {
          throw new Exception("MOD by zero found in Expression: " +
            this.toString()+ this.exp_right.getToken().posString());
        }
        ans = left % right;
      }
      if (entered) {
        return new Expression(
          new Constant(this.type, ans),
          new Token("integer", Integer.toString(ans), 0 , 0));
      }
    }
    return null;
  }

  public Expression returnLeft() {
    return this.exp_left;
  }

  public Expression returnRight() {
    return this.exp_right;
  }
  public Token returnOp() {
    return this.operator;
  }

  public String toString() {
    return this.exp_left.toString() + " " + this.operator.returnVal() + " " +
      this.exp_right.toString();
  }
}
