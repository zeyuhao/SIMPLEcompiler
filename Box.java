/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Box {
  protected int address; // offset from base address of outermost parent Box
  protected String parent; // name of outermost parent

  public int getVal() {
    return 0;
  }

  public boolean isInteger() {
    return false;
  }

  public boolean isArray() {
    return false;
  }

  public boolean isRecord() {
    return false;
  }

  public int getAddress() {
    return this.address;
  }

  public String getParent() {
    return this.parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String toString() {
    return "box";
  }
}
