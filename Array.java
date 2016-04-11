/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Array extends Type {
  private Constant length;
  public Array(Type type, Constant length) {
    this.type = type; // element type
    this.length = length; // length points to a Constant
  }

  public boolean isArray() {
    return true;
  }

  public String returnType() {
    return "Array";
  }

  public Type elemType() {
    return this.type;
  }

  public int length() {
    return this.length.returnVal();
  }

  public String toString() {
    return "Array " + this.length.returnVal() + " of " + this.type.toString();
  }
}
