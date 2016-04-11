/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Condition extends Node {
  private Expression left;
  private Expression right;
  private Token relation;

  public Condition(Token relation, Expression left, Expression right)
    throws Exception {
    this.left = left;
    this.right = right;
    this.relation = relation;
    if (!left.getType().isInteger() || !right.getType().isInteger()) {
      throw new Exception("Conditions only apply to Expressions of the " +
      "Integer Type." +
      "\nFound " + left.toString() + left.getToken().posString() +
      " of Type " + left.getType().returnType() +
      "\nFound " + right.toString() + right.getToken().posString() +
      " of Type " + right.getType().returnType());
    }
  }

  public Expression getLeft() {
    return this.left;
  }

  public Expression getRight() {
    return this.right;
  }

  public String toString() {
    return "Condition (" + this.relation.returnVal() + ")" +
      "\nLeft =>\n  " + this.left.toString() +
      "\nRight =>\n  " + this.right.toString() + "\n";
  }
}
