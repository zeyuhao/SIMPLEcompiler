/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Entry {
  protected Type type;

  public boolean isType() {
    return false;
  }

  public boolean isVariable() {
    return false;
  }

  public boolean isConstant() {
    return false;
  }

  public boolean isProcedure() {
    return false;
  }

  public Type getType() {
    return this.type;
  }

  public void accept(Visitor visitor, String name) {
  }
}
