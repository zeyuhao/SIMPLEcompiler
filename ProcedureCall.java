/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.ArrayList;
import java.util.HashMap;

public class ProcedureCall extends Instruction {
  private AST instructions;
  private FormalVariable[] formals;
  private String[] formal_names;
  private Expression[] params;
  private Environment env;

  public ProcedureCall(Procedure procedure, ArrayList<Expression> params,
    Token token) throws Exception {
    this.formals = new FormalVariable[procedure.getFormals().size()];
    this.params = new Expression[params.size()];
    this.formal_names = new String[procedure.getFormalNames().size()];
    procedure.getFormals().toArray(this.formals);
    params.toArray(this.params);
    procedure.getFormalNames().toArray(this.formal_names);
    if (this.params.length != this.formals.length) {
      throw new Exception("Mismatched parameters in Procedure " +
        token.toString());
    }
    for (int i = 0; i < formals.length; i++) {
      if (this.params[i].getType() != formals[i].getType()) {
        throw new Exception("Mismatched parameter Types in Procedure " +
          token.returnVal() + token.posString());
      }
    }
    this.instructions = procedure.getInstructions();
    this.env = procedure.getLocalEnv();
  }

  // Update environment with the runtime parameters supplied
  private void updateEnv(Environment env) throws Exception {
    for (int i = 0; i < this.params.length; i++) {
      Box box = null;
      if (params[i].getType().isInteger()) {
        if (params[i].isConstant()) {
          box = new IntegerBox();
          ((IntegerBox)box).setBox(params[i].returnNumber().returnVal());
        } else {
          box = ((IntegerBox)env.getBox(params[i].getToken().returnVal())).deepCopy();
        }
      } else {
        box = env.getBox(params[i].getToken().returnVal());
      }
      this.env.insertBox(this.formal_names[i], box);
    }
  }

  public Environment getEnv() {
    return this.env;
  }

  public void run(Environment env) throws Exception {
    // Create a new environment each time Procedure is run
    this.updateEnv(env);
    this.instructions.interpret(this.env);
  }

  public String formalNameString() {
    String str = "";
    for (String name : this.formal_names) {
      str += name + "\n";
    }
    return str;
  }

  public String paramString() {
    String str = "";
    for (Expression exp : this.params) {
      str += exp.toString() + "\n";
    }
    return str;
  }

  public String formalString() {
    String str = "";
    for (FormalVariable formal : this.formals) {
      str += formal.toString() + "\n";
    }
    return str;
  }

  public String toString() {
    return "Procedure:" +
      "\n===========================================" +
      "\n  Formal names =>\n" + this.formalNameString() +
      "\n  Formals =>\n" + this.formalString() +
      "\n  Params =>\n" + this.paramString() +
      "\n  Instructions =>\n" + this.instructions.toString() +
      "\n  Locals =>\n" + this.env.toString() +
      "\n===========================================";
  }
}
