/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Integer extends Type {

  public boolean isInteger() {
    return true;
  }

  public String returnType() {
    return "Integer";
  }

  public int getMemSpace() {
    return 4;
  }

  public void accept(Visitor visitor, String name) {
    visitor.visit(this, name);
  }

  public String toString() {
    return "Integer";
  }
}
