/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.ArrayList;
import java.util.HashMap;

public class Procedure extends Entry {
  private Scope details;
  private Type type; // the return Type
  private AST instructions;
  private Expression return_exp; // the return Expression

  public Procedure(Scope details, Type type, AST instructions, Expression exp) {
    this.details = details;
    this.instructions = instructions;
    this.type = type;
    this.return_exp = exp;
  }

  public boolean isProcedure() {
    return true;
  }

  public ArrayList<FormalVariable> getFormals() {
    ArrayList<FormalVariable> formals = new ArrayList<FormalVariable>();
    HashMap<String, Entry> vars = this.details.getTable();
    for (HashMap.Entry<String, Entry> var : vars.entrySet()) {
      Entry value = var.getValue();
      if (((Variable)value).isFormal()) {
        formals.add((FormalVariable)value);
      }
    }
    return formals;
  }

  public ArrayList<String> getFormalNames() {
    ArrayList<String> names = new ArrayList<String>();
    HashMap<String, Entry> vars = this.details.getTable();
    for (HashMap.Entry<String, Entry> var : vars.entrySet()) {
      String key = var.getKey();
      Entry value = var.getValue();
      if (((Variable)value).isFormal()) {
        names.add(key);
      }
    }
    return names;
  }



  public Environment getLocalEnv() {
    Environment env = new Environment();
    HashMap<String, Entry> vars = this.details.getTable();
    for (HashMap.Entry<String, Entry> var : vars.entrySet()) {
      String key = var.getKey();  // name of the Entry
      Entry value = var.getValue(); // the actual Entry
      Box box = null;
      // Only keep track of variables in the Environment
      if (!((Variable)value).isFormal()) {
        Type type = value.getType(); // the Entry Type
        if (type.isInteger()) {
          box = new IntegerBox(0);
        } else if (type.isArray()) {
          box = new ArrayBox(type, 0);
        } else if (type.isRecord()) {
          box = new RecordBox(type, 0);
        }
        env.insertBox(key, box);
      }
    }
    return env;
  }

  public AST getInstructions() {
    return this.instructions;
  }

  public boolean hasReturn() {
    return this.return_exp != null;
  }

  public Expression getReturnExp() {
    return this.return_exp;
  }

  public String toString() {
    return
      "Procedure:\n" +
      "Details:\n" + this.details.toString() + "\n" +
      "Return Type: " + this.type.toString() + "\n" +
      "Instructions: " + this.instructions.toString() + "\n" +
      "Return Expression: " + this.return_exp.toString() + "\n";
  }
}
