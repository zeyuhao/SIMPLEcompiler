/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Type extends Entry {

  public boolean isType() {
    return true;
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

  public String returnType() {
    return "type";
  }

  public int getMemSpace() {
    return 0;
  }
}
