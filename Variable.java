/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Variable extends Entry {
  protected int data_size;

  public Variable(Type type) {
    this.type = type;
    this.data_size = this.type.getMemSpace();
  }

  public boolean isVariable() {
    return true;
  }

  public int returnSize() {
    return this.data_size;
  }

  public void accept(Visitor visitor, String name) {
    visitor.visit(this, name);
  }

  public String toString() {
    return "Variable with size " + this.data_size + " of Type: " +
      this.type.toString();
  }
}
