/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Node {
  protected Type type;
  protected Token token;
  protected Node parent;

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public Node getParent() {
    return this.parent;
  }

  public boolean hasParent() {
    return this.parent != null;
  }

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

  public boolean isInstruction() {
    return false;
  }

  // For testing
  public String printParent() {
    if (this.parent != null) {
      return "Parent of " + this.toString() + " is " + this.parent.toString();
    } else return this.toString() + "has no parent";
  }
}
