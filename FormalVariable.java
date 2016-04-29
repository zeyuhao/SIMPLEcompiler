/*
Zeyu Hao
zhao7@jhu.edu
*/

public class FormalVariable extends Variable {

  public FormalVariable(Type type) {
    super(type);
  }

  public boolean isFormal() {
    return true;
  }

  public String toString() {
    return "Formal Variable with size " + this.data_size + " of Type: " +
      this.type.toString();
  }
}
