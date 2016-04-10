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

  public Expression(Constant number, Token token) {
    this.number = number;
    this.type = number.getType();
    this.token = token;
  }

  public Expression(Location loc) {
    this.loc = loc;
    this.type = loc.getType();
    this.token = loc.getToken();
  }

  public String nodeType() {
    return "expression";
  }

  public boolean isExpression() {
    return true;
  }

  public String toString() {
    String str = "";
    if (this.number != null) {
      str += this.token.returnVal();
    } else if (this.loc != null) {
      str = this.loc.toString();
    }
    return str;
  }
}
