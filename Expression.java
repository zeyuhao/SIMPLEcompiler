/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Expression extends Node {
  private Constant number;
  private Location loc;
  private Type type;

  // Default constructor for Binary
  public Expression() {
    this.node_type = "expression";
  }

  public Expression(Constant number, Token token) {
    this.number = number;
    this.type = number.getType();
    this.token = token;
    this.node_type = "expression";
  }

  public Expression(Location loc) {
    this.loc = loc;
    this.type = loc.getType();
    this.token = loc.getToken();
    this.node_type = "expression";
  }

  public String toString() {
    String str = "";
    if (this.number != null || this.loc != null) {
      str = this.token.returnVal();
    }
    return str;
  }
}
