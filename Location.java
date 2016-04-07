/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Location extends Node {
  private Variable var;
  private Field field;
  private Index index;

  // Default Constructor for Index
  public Location() {
    this.node_type = "location";
  }

  // Variable
  public Location(Variable var, Token token) {
    this.var = var;
    this.type = this.var.getType();
    this.token = token;
    this.node_type = "location";
  }

  // Field
  public Location(Field field, Token token) {
    this.field = field;
    this.type = this.field.getType();
    this.token = token;
    this.node_type = "location";
  }

  public String toString() {
    return this.token.returnVal();
  }
}
