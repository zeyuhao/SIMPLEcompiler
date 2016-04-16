/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Expression extends Node {
  private Constant number;
  private Location loc;

  // Default constructor for Binary
  public Expression() {
  }

  // Constructor for a Number Expression (Constant)
  public Expression(Constant number, Token token) {
    this.number = number;
    this.type = number.getType();
    this.token = token;
  }

  // Constructor for a Location Expression
  public Expression(Location loc) {
    this.loc = loc;
    this.type = loc.getType();
    this.token = loc.getToken();
    loc.setParent(this);
  }

  public String nodeType() {
    return "expression";
  }

  public boolean isExpression() {
    return true;
  }

  public boolean isConstant() {
    return (this.number != null && this.type.isInteger());
  }

  public Constant returnNumber() {
    return this.number;
  }

  public String toString() {
    String str = "";
    if (this.number != null) {
      str += this.number.returnVal();
    } else if (this.loc != null) {
      str = this.loc.toString();
    }
    return str;
  }
}
