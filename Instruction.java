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

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    return "";
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
      box = new IntegerBox();
      ((IntegerBox)box).setBox(value);
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
      box = new IntegerBox();
      ((IntegerBox)box).setBox(sum);
    } else {
      // Get the base leftmost Location Variable we are operating from
      Location base = this.getBase(exp.returnLoc());
      box = this.getEnvBox(base, env);
      box.setParent(base.toString());
    }
    return box;
  }

  public String moveConstant(Expression exp, String reg_name) {
    int value = exp.returnNumber().returnVal();
    return "\tmov " + reg_name + ", #" + value + "\n";
  }

  public String getExpCode(Expression exp, Environment env,
    RegisterDescriptor reg, String reg_name) throws Exception {
    String str = "";
    // Generate Code for Expressions
    if (exp.isBinary()) { // Binary was unfoldable
      Expression left = ((Binary)exp).returnLeft();
      Expression right = ((Binary)exp).returnRight();
      if (!left.isConstant()) {
        str += this.getExpCode(left, env, reg, reg_name);
      }
      String next_reg = reg.available();
      if (!right.isConstant()) {
        reg.setInUse();
        str += this.getExpCode(right, env, reg, next_reg);
      }
      String op = ((Binary)exp).returnOp().returnVal();
      if (op.equals("+")) {
        if (left.isConstant()) {
          int value = left.returnNumber().returnVal();
          str += "\tadd " + reg_name + ", " + next_reg + ", #" +
            value + "\n";
        } else if (right.isConstant()) {
          int value = right.returnNumber().returnVal();
          str += "\tadd " + reg_name + ", " + reg_name + ", #" +
            value + "\n";
        } else {
          str += "\tadd " + reg_name + ", " + reg_name + ", " +
            next_reg + "\n";
        }
      } else if (op.equals("-")) {
        if (left.isConstant()) {
          str += this.moveConstant(left, reg_name);
          str += "\tsub " + reg_name + ", " + reg_name + ", " +
            next_reg + "\n";
        } else if (right.isConstant()) {
          int value = right.returnNumber().returnVal();
          str += "\tsub " + reg_name + ", " + reg_name + ", #" +
            value + "\n";
        } else {
          str += "\tsub " + reg_name + ", " + reg_name + ", " +
            next_reg + "\n";
        }
      } else if (op.equals("*")) {
        if (left.isConstant()) {
          str += this.moveConstant(left, reg_name);
        } else if (right.isConstant()) {
          str += this.moveConstant(right, next_reg);
        }
        str += "\tmul " + reg_name + ", " + reg_name + ", " + next_reg + "\n";
      } else if (op.equals("DIV")) {
        if (left.isConstant()) {
          str += this.moveConstant(left, reg_name);
        } else if (right.isConstant()) {
          str += this.moveConstant(right, next_reg);
        }
        str += "\tpush {r0}\n";
        str += "\tmov r0, " + reg_name + "\n";
        str += "\tmov r1, " + next_reg + "\n";
        str += "\tbl __aeabi_idivmod\n";
        str += "\tmov " + reg_name + ", r0\n";
        str += "\tpop {r0}\n";
      } else if (op.equals("MOD")) {
        if (left.isConstant()) {
          str += this.moveConstant(left, reg_name);
        } else if (right.isConstant()) {
          str += this.moveConstant(right, next_reg);
        }
        str += "\tpush {r0}\n";
        str += "\tmov r0, " + reg_name + "\n";
        str += "\tmov r1, " + next_reg + "\n";
        str += "\tbl __aeabi_idivmod\n";
        str += "\tmov " + reg_name + ", r1\n";
        str += "\tpop {r0}\n";
      }
      reg.preserve();
    } else {
      // Get the base leftmost Location Variable we are operating from
      Location base = this.getBase(exp.returnLoc());
      String name = base.toString();
      int addr = this.getEnvBox(base, env).getAddress();
      String next_reg = reg.available();
      reg.setInUse();
      str += "\tldr " + next_reg + ", addr_" + name + "\n";
      str += "\tldr " + reg_name + ", [" + next_reg + ", +#" + addr + "]\n";
    }
    return str;
  }

  public String accept(Visitor visitor) {
    return "";
  }
}
