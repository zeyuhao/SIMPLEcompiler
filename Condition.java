/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Condition extends Instruction {
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
    left.setParent(this);
    left.setParent(this);
  }

  public Token getRelation() {
    return this.relation;
  }

  public Expression getLeft() {
    return this.left;
  }

  public Expression getRight() {
    return this.right;
  }

  public Condition copyCondition(Token relation) throws Exception {
    return new Condition(relation, this.left, this.right);
  }

  // =, #, <, >, <=, or >=
  public boolean isTrue(Environment env) throws Exception {
    Box exp_left = this.getExpBox(this.left, env);
    Box exp_right = this.getExpBox(this.right, env);
    int val_left = exp_left.getVal();
    int val_right = exp_right.getVal();
    String op = this.relation.returnVal();
    if (op.equals("=")) {
      return val_left == val_right;
    } else if (op.equals("#")) {
      return val_left != val_right;
    } else if (op.equals("<")) {
      return val_left < val_right;
    } else if (op.equals(">")) {
      return val_left > val_right;
    } else if (op.equals("<=")) {
      return val_left <= val_right;
    } else {
      return val_left >= val_right;
    }
  }

  public String getFlag() {
    String op = this.relation.returnVal();
    switch(op) {
      case "=":
        return "bne";
      case "#":
        return "beq";
      case "<":
        return "bge";
      case ">":
        return "ble";
      case "<=":
        return "bgt";
      default:
        return "blt";
    }
  }

  public String toString() {
    return "Condition (" + this.relation.returnVal() + ")" +
      "\nLeft =>\n  " + this.left.toString() +
      "\nRight =>\n  " + this.right.toString() + "\n";
  }
}
