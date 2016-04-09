/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Node {
  protected Type type;
  protected Token token;
  protected String node_type;

  public Type getType() {
    return this.type;
  }

  public Token getToken() {
    return this.token;
  }

  // Each Node subclass has its own nodeType()
  public String nodeType() {
    return "node";
  }

  public boolean isExpression() {
    return false;
  }

  public boolean isLocation() {
    return false;
  }
}
