/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Type extends Entry {
  protected String node_type;

  public boolean isType() {
    return true;
  }

  public String returnType() {
    return this.node_type;
  }
}
