/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Instruction extends Node {
  protected Instruction next;

  public void setNext(Instruction next) {
    this.next = next;
  }

  public Instruction getNext() {
    return this.next;
  }

  public void run(Environment env) throws Exception {
  }

  // Returns the leftmost Location Variable through recursive calls to itself
  public Location getBase(Location loc) {
    if (loc.isIndex()) {
      return this.getBase(((Index)loc).getLoc());
    } else if (loc.isRecordField()) {
      return this.getBase(((RecordField)loc).getLoc());
    } else {
      return loc;
    }
  }

  // Universal function to get the desired box from the Environment
  public Box getEnvBox(Node base, Environment env) throws Exception {
    Node curr = base;
    // Fetch the Box from the Environment by name
    Box box = env.getBox(curr.getToken().returnVal());
    while (curr.getParent().isLocation()) {
      Node prev = curr;
      curr = curr.getParent();
      // If we have an Array Type currently
      if (((Location)curr).isIndex()) {
        Expression exp = ((Index)curr).getExp();
        int index = this.getExpBox(exp, env).getVal();
        if (index >= ((ArrayBox)box).getSize()) {
          throw new Exception ("Index " + exp.getToken().toString() +
            " is out of bounds. Size of Array " + prev.toString() + " is " +
            ((ArrayBox)box).getSize());
        }
        box = ((ArrayBox)box).getBox(index);
      } else if (((Location)curr).isRecordField()) {
        String field = ((RecordField)curr).getToken().returnVal();
        box = ((RecordBox)box).getBox(field);
      }
    }
    return box;
  }

  // Evaluate and return a Box representing the 'value' from an Expression
  // Values can be Integers, Records, or Arrays
  public Box getExpBox(Expression exp, Environment env) throws Exception {
    Box box = null;
    if (exp.isConstant()) {
      int value = exp.returnNumber().returnVal();
      box = new IntegerBox(value);
    } else if (exp.isBinary()) { // Binary was unfoldable
      Expression left = ((Binary)exp).returnLeft();
      Expression right = ((Binary)exp).returnRight();
      int val1 = this.getExpBox(left, env).getVal();
      int val2 = this.getExpBox(right, env).getVal();
      int sum = 0;
      String op = ((Binary)exp).returnOp().returnVal();
      if (op.equals("+")) {
        sum = val1 + val2;
      } else if (op.equals("-")) {
        sum = val1 - val2;
      } else if (op.equals("*")) {
        sum = val1 * val2;
      } else if (op.equals("DIV")) {
        if (val2 == 0) {
          throw new Exception("MOD by zero found in Expression: " +
            ((Binary)exp).toString() + right.getToken().posString());
        }
        sum = val1 / val2;
      } else if (op.equals("MOD")) {
        if (val2 == 0) {
          throw new Exception("MOD by zero found in Expression: " +
            ((Binary)exp).toString() + right.getToken().posString());
        }
        sum = val1 % val2;
      }
      box = new IntegerBox(sum);
    } else {
      // Get the base leftmost Location Variable we are operating from
      Location base = this.getBase(exp.returnLoc());
      box = this.getEnvBox(base, env);
    }
    return box;
  }
}
