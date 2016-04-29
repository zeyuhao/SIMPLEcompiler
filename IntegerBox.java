/*
Zeyu Hao
zhao7@jhu.edu
*/

public class IntegerBox extends Box {
  private int value;
  private boolean literal; // was this box created from a literal?

  public IntegerBox(int address) {
    this.value = 0;
    this.address = address;
    this.literal = false;
  }

  // For Constant literals
  public IntegerBox() {
    this.value = 0;
    this.literal = true;
  }

  public void setBox(int value) {
    this.value = value;
  }

  public int getVal() {
    return this.value;
  }

  public boolean isLiteral() {
    return this.literal;
  }

  public boolean isInteger() {
    return true;
  }

  public IntegerBox deepCopy() {
    IntegerBox box = new IntegerBox(0);
    box.setBox(this.value);
    return box;
  }

  public String toString() {
    return "Integer: " + this.value + " of address: " + this.address;
  }
}
