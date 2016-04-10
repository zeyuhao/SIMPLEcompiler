/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Location extends Node {
  private Variable var;

  // Default Constructor for Index and Field
  public Location() {
  }

  // Variable
  public Location(Variable var, Token token) {
    this.var = var;
    this.type = this.var.getType();
    this.token = token;
  }

  public String nodeType() {
    return "location";
  }

  public boolean isIndex() {
    return false;
  }

  public boolean isRecordField() {
    return false;
  }

  public boolean isLocation() {
    return true;
  }

  public String toString() {
    return this.token.returnVal();
  }
}
