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

  // Checking that number != null ensures it's not a Variable
  public boolean isConstant() {
    return (this.number != null && this.type.isInteger());
  }

  // Checking that number != null ensures it's not a Variable
  public boolean isExpLocation() {
    return this.returnLoc() != null;
  }

  public boolean isBinary() {
    return false;
  }

  public Constant returnNumber() {
    return this.number;
  }

  public Location returnLoc() {
    return this.loc;
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
