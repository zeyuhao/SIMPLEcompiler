/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Array extends Type {
  private Constant length;
  public Array(Type type, Constant length) {
    this.type = type; // element type
    this.length = length; // length points to a Constant
    this.node_type = "array";
  }

  public Type elemType() {
    return this.type;
  }

  public int length() {
    return this.length.returnVal();
  }

  public String toString() {
    return "Array " + this.length.toString() + " of " + this.type.toString();
  }
}
