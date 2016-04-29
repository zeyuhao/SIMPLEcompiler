/*
Zeyu Hao
zhao7@jhu.edu
*/

// Field is the equivalent of a Variable Entry for Record Types
public class Field extends Variable {
  public Field(Type type) {
    super(type);
  }

  public String toString() {
    return "Field with size " + this.data_size + " of Type: " +
      this.type.toString();
  }
}
