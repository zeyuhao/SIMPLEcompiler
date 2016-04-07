/*
Zeyu Hao
zhao7@jhu.edu
*/

// Field is the equivalent of a Variable Entry for Record Types
public class Field extends Entry {
  public Field(Type type) {
    this.type = type;
  }

  public boolean isVariable() {
    return true;
  }

  public String toString() {
    return "Field of Type: " + this.type.toString();
  }
}
