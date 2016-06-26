/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.Map;

public class TextualVisitor implements Visitor {

  private int depth;
  private String output;

  public TextualVisitor() {
    this.depth = 0;
    this.output = "";
  }

  /**
   * Append the specified String value to the output and increment
   * the tab depth by the specified int value.
   * @param string the specified String value to append
   * @param inc_depth the int value to increment current tab depth by
   */
  private void formatString(String string, int inc_depth) {
    String str = "";
    for (int i = 0; i < this.depth; i++) {
      str += "  "; // increment by two spaces for each depth
    }
    this.output += str + string + "\n";
    this.depth += inc_depth;
  }

  private void formatType(Type type, Boolean is_type) {
    if (!is_type) {
      this.formatString("type:", 1);
    }
    if (type.isInteger()) {
      this.formatString("INTEGER", -1);
    } else if (type.isArray()) {
      this.formatString("ARRAY BEGIN", 1);
      this.formatType(((Array)type).elemType(), false);
      this.formatString("length:", 1);
      this.formatString(((Array)type).length() + "", -2);
      this.formatString("END ARRAY", -1);
    } else if (type.isRecord()) {
      this.formatString("RECORD BEGIN", 1);
      this.visit(((Record)type).getFields());
      this.formatString("END SCOPE", -1);
      this.formatString("END RECORD", -1);
    }
  }

  private void formatLocation(Location loc) {
    if (loc.isIndex()) {
      this.formatString("Index:", 0);
      this.formatString("location =>", 1);
      this.formatLocation(((Index)loc).getLoc());
      this.formatString("expression =>", 1);
      this.formatExpression(((Index)loc).getExp());
    } else if (loc.isRecordField()) {
      this.formatString("Field:", 0);
      this.formatString("location =>", 1);
      this.formatLocation(((RecordField)loc).getLoc());
      this.formatVariable(((RecordField)loc).getField().getType());
    } else {
      this.formatString("Variable:", 0);
      this.formatVariable(loc.getType());
    }
    this.depth--;
  }

  private void formatVariable(Type type) {
    this.formatString("variable =>", 1);
    this.formatString("VAR BEGIN", 1);
    this.formatString("type:", 1);
    this.formatType(type, true);
    this.depth--;
    this.formatString("END VAR", -1);
  }

  private void formatConst(Constant constant) {
    this.formatString("CONST BEGIN", 1);
    this.formatType(constant.getType(), false);
    this.formatString("value:", 1);
    this.formatString(constant.returnVal() + "", -2);
    this.formatString("END CONST", -1);
  }

  private void formatCondition(Condition cond) {
    this.formatString("condition =>", 1);
    this.formatString("Condition (" +
      cond.getRelation().returnVal() + "):", 0);
    this.formatString("left =>", 1);
    this.formatExpression(cond.getLeft());
    this.formatString("right =>", 1);
    this.formatExpression(cond.getRight());
    this.depth--;
  }

  private void formatExpression(Expression exp) {
    if (exp.isConstant()) {
      this.formatString("Number:", 0);
      this.formatString("value =>", 1);
      this.formatConst(exp.returnNumber());
      this.depth--;
    } else if (exp.isBinary()) {
      this.formatString("Binary (" +
        ((Binary)exp).returnOp().returnVal() + "):", 0);
      this.formatString("left =>", 1);
      this.formatExpression(((Binary)exp).returnLeft());
      this.formatString("right =>", 1);
      this.formatExpression(((Binary)exp).returnRight());
      this.depth--;
    } else {
      this.formatLocation(exp.returnLoc());
    }
  }

  /**
   * Empty function. Only used in GraphicalVisitor.
   */
  public int getCount() {
    return 0;
  }

  /**
   * Empty function. Only used in GraphicalVisitor.
   */
  public String getTop() {
    return "";
  }

  /**
   * Notify Visitor that parsing has begun.
   * @param is_ast Boolean determining whether or not we are Visiting an
   * AST or Symbol Table. Different Visitor output is generated for each type.
   */
  public void start(Boolean is_ast) {
    if (is_ast) {
      this.formatString("instructions =>", 1);
    }
  }

  /**
   * Notify Visitor that end of parsing has been reached.
   * @param is_ast Boolean determining whether or not we are Visiting an
   * AST or Symbol Table. Different Visitor output is generated for each type.
   */
  public void end(Boolean is_ast) {
    if (!is_ast) {
      this.output += "END SCOPE";
    } else {
      // Remove the last \n if it's an AST
      int index = this.output.lastIndexOf("\n");
      if (index != -1) {
        this.output = this.output.substring(0, index);
      }
    }
  }

  public void visit(Scope scope) {
    this.formatString("SCOPE BEGIN", 1);
    Map<String, Entry> st = scope.getTable();
    for (Map.Entry<String, Entry> entry : st.entrySet()) {
      String name = entry.getKey();
      Entry value = entry.getValue();
      value.accept(this, name);
    }
    this.depth--;
  }

  public void visit(Constant constant, String name) {
    this.formatString(name + " =>", 1);
    this.formatConst(constant);
  }

  public void visit(Variable var, String name) {
    this.formatString(name + " =>", 1);
    this.formatString("VAR BEGIN", 1);
    this.formatType(var.getType(), false);
    this.depth--;
    this.formatString("END VAR", -1);
  }

  public void visit(Field field, String name) {
    this.formatString(name + " =>", 1);
    this.formatString("VAR BEGIN", 1);
    this.formatType(field.getType(), false);
    this.depth--;
    this.formatString("END VAR", -1);
  }

  public void visit(Integer integer, String name) {
    this.formatString(name + " =>", 1);
    this.formatType(integer, true);
  }

  public void visit(Array array, String name) {
    this.formatString(name + " =>", 1);
    this.formatType(array, true);
  }

  public void visit(Record record, String name) {
    this.formatString(name + " =>", 1);
    this.formatType(record, true);
  }

  public String visit(AST ast) {
    Instruction curr = ast.getHead();
    while(curr != null) {
      curr.accept(this);
      curr = curr.getNext();
    }
    return "";
  }

  public String visit(Assign assign, String name) {
    this.formatString(name + ":", 0);
    this.formatString("location =>", 1);
    this.formatLocation(assign.getLoc());
    this.formatString("expression =>", 1);
    this.formatExpression(assign.getExp());
    return "";
  }

  public String visit(Write write, String name) {
    this.formatString(name + ":", 0);
    this.formatString("expression =>", 1);
    this.formatExpression(write.getExp());
    return "";
  }

  public String visit(Read read, String name) {
    this.formatString(name + ":", 0);
    this.formatString("location =>", 1);
    this.formatLocation(read.getLoc());
    return "";
  }

  public String visit(If _if, String name) {
    this.formatString(name + ":", 0);
    this.formatCondition(_if.getCond());
    this.formatString("true =>", 1);
    // Visit another AST
    this.visit(_if.getTrue());
    this.depth--;
    if (_if.hasFalse()) {
      this.formatString("false =>", 1);
      this.visit(_if.getFalse());
      this.depth--;
    }
    return "";
  }

  public String visit(Repeat repeat, String name) {
    this.formatString(name + ":", 0);
    this.formatCondition(repeat.getCond());
    this.formatString("instructions =>", 1);
    this.visit(repeat.getInstr());
    this.depth--;
    return "";
  }

  public String toString() {
    return this.output;
  }
}
