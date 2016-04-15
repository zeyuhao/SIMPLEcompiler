/*
Zeyu Hao
zhao7@jhu.edu
*/

public class IntegerBox extends Box {
  private int value;

  public IntegerBox() {
    this.value = 0;
  }

  public void setVal(int value) {
    this.value = value;
  }

  public int getVal() {
    return this.value;
  }

  public boolean isInteger() {
    return true;
  }

  public String toString() {
    return "Integer: " + this.value;
  }
}
