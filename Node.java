/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Node {
  protected Type type;
  protected Token token;

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

  // Check if this Node's type matches
  // that of another Node's
  public boolean matchType(Node node) {
    if (this.type.isInteger()) {
      return node.getType().isInteger();
    } else if (this.type.isArray()) {
      return node.getType().isArray();
    } else if (this.type.isRecord()) {
      return node.getType().isRecord();
    }
    return false;
  }

  public boolean isExpression() {
    return false;
  }

  public boolean isLocation() {
    return false;
  }
}
