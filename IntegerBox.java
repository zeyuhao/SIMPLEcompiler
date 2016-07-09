/*
Zeyu Hao
zhao7@jhu.edu
*/

public class IntegerBox extends Box {
  private int value;

  public IntegerBox(int address) {
    this.value = 0;
    this.address = address;
  }

  // For Constant literals
  public IntegerBox() {
    this.value = 0;
  }

  public void setBox(int value) {
    this.value = value;
  }

  public int getVal() {
    return this.value;
  }

  public boolean isInteger() {
    return true;
  }

  public IntegerBox deepCopy() {
    IntegerBox box = new IntegerBox(this.address);
    box.setBox(this.value);
    return box;
  }

  public String toString() {
    return "Integer: " + this.value + " of address: " + this.address;
  }
}
