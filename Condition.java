/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Condition extends Node {
  private Expression left;
  private Expression right;
  private Token relation;

  public Condition(Token relation, Expression left, Expression right) {
    this.left = left;
    this.right = right;
    this.relation = relation;
  }

  public String toString() {
    return "Condition (" + this.relation.returnVal() + ")" +
      "\nLeft =>\n  " + this.left.toString() +
      "\nRight =>\n  " + this.right.toString() + "\n";
  }
}
