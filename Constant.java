/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Constant extends Entry {
  private int value;
  
  public Constant(Type type, int value) {
    this.type = type;
    this.value = value;
  }

  public boolean isConstant() {
    return true;
  }

  public int returnVal() {
    return this.value;
  }

  public String toString() {
    return "Constant of Type: " + this.type.toString() +
      ", Value: " + this.value;
  }
}
