/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Variable extends Entry {
  public Variable(Type type) {
    this.type = type;
  }

  public boolean isVariable() {
    return true;
  }

  public String toString() {
    return "Variable of Type: " + this.type.toString();
  }
}
