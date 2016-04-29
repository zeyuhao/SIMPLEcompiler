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

  public String toString() {
    return "Integer";
  }
}
