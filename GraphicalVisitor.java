/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.Map;
import java.util.TreeMap;

public class GraphicalVisitor implements Visitor {

  private String output;
  private String top_level; // The top level cluster's blank element
  private int count;
  private int depth;
  private TreeMap<String, String> dot_table;
  private GraphicalVisitor parent;

  public GraphicalVisitor() {
    this.output = "";
    this.count = 0;
    this.depth = 0;
    this.dot_table = new TreeMap<String, String>();
    this.parent = null;
    this.top_level = "";
  }

  /**
   * Constructor used when a Record type is encountered.
   */
  public GraphicalVisitor(int count, int depth, GraphicalVisitor parent) {
    this.output = "";
    this.count = count;
    this.depth = depth;
    this.dot_table = new TreeMap<String, String>();
    this.parent = parent;
    this.top_level = "";
  }

  private void table_insert(String key, String name) {
    this.dot_table.put(key, name);
  }

  // search dot_table for the node name associated with an Entry
  private String find(String key) {
    if (this.local(key)) {
      return this.dot_table.get(key);
    } else if (this.parent != null) { // are we in universe Scope or Record?
      return this.parent.find(key);
    }
    return null;
  }

  private boolean local(String name) {
    return this.dot_table.containsKey(name);
  }

  /**
   * Concatenate the possibly non-unique token name with a unique integer.
   * The value of the unique integer is stored in the variable "count" to
   * create a unique name.
   * @param prefix
   * @return String the newly-formed unique node name
   */
  private String unique_name(String prefix) {
    String name = prefix + this.count;
    this.count++;
    return name;
  }

  /**
   * Notify Visitor that parsing has begun.
   * @param is_ast Boolean determining whether or not we are Visiting an
   * AST or Symbol Table. Different Visitor output is generated for each type.
   */
  public void start(Boolean is_ast) {
    this.formatString("digraph X {");
    this.depth++;
    this.formatString("node [shape=rectangle]");
    if (!is_ast) {
      String integer_node = this.unique_name("node");
      this.table_insert("INTEGER", integer_node); // insert INTEGER into table
      this.formatString(integer_node + " [label=\"INTEGER\", style=rounded]");
    }
  }

  /**
   * Notify Visitor that end of parsing has been reached.
   * @param is_ast Boolean determining whether or not we are Visiting an
   * AST or Symbol Table. Different Visitor output is generated for each type.
   */
  public void end(Boolean is_AST) {
    this.output += "}";
  }

  public String getTop() {
    return this.top_level;
  }

  public int getCount() {
    return this.count;
  }

  /**
   * Append the specified String value to the output and increment
   * the tab depth by the specified int value.
   * @param string the specified String value to append
   * @param inc_depth the int value to increment current tab depth by
   */
  private void formatString(String string) {
    String str = "";
    for (int i = 0; i < this.depth; i++) {
      str += "  "; // increment by two spaces for each depth
    }
    this.output += str + string + "\n";
  }

  private void formatType(Type type, String parent) {
    if (type.isInteger()) {
      this.formatString(parent + " -> " + this.find("INTEGER"));
    } else if (type.isArray()) {
      String array_name = this.unique_name("node");
      String array_label = "Array\\nlength: " + ((Array)type).length();
      this.formatString(array_name +
        " [label=\"" + array_label + "\", style=rounded]");
      this.formatString(parent + " -> " + array_name);
      this.formatType(((Array)type).elemType(), array_name);
    } else if (type.isRecord()) {
      String rec_name = this.unique_name("node");
      this.formatString(rec_name + " [label=\"Record\", style=rounded]");
      this.formatString(parent + " -> " + rec_name);
      GraphicalVisitor visitor =
        new GraphicalVisitor(this.count, this.depth, this);
      visitor.visit(((Record)type).getFields());
      this.output += visitor.toString();
      this.count += visitor.getCount();
      this.formatString(rec_name + " -> " + visitor.getTop());
    }
  }

  private void formatExpression(Expression exp, String parent, String label) {
    String node_label = label;
    if (label.equals("")) {
      node_label = "expression";
    }
    String exp_node = this.unique_name("expression");
    if (exp.isConstant()) {
      this.formatString(exp_node + " [label=\"Number\"]");
      this.formatString(parent + " -> " + exp_node +
        " [label=\"" + node_label + "\"]");
      String num_node = this.unique_name("number");
      int num_value = exp.returnNumber().returnVal();
      this.formatString(num_node + " [label=\""
        + num_value + "\", shape=diamond]");
      this.formatString(exp_node + " -> " + num_node + " [label=\"ST\"]");
    } else if (exp.isBinary()) {
      String operator = ((Binary)exp).returnOp().returnVal();
      this.formatString(exp_node + " [label=\"" + operator + "\"]");
      this.formatString(parent + " -> " + exp_node +
      " [label=\"" + node_label + "\"]");
      this.formatExpression(((Binary)exp).returnLeft(), exp_node, "left");
      this.formatExpression(((Binary)exp).returnRight(), exp_node, "right");
    } else {
      if (node_label.equals("expression")) {
        this.formatLocation(exp.returnLoc(), parent, node_label);
      } else {
        this.formatLocation(exp.returnLoc(), parent, label);
      }
    }
  }

  private void formatCondition(Condition cond, String parent, String label) {
    String node_label = label;
    if (label.equals("")) {
      node_label = "condition";
    }
    String cond_node = this.unique_name("expression");
    String condition = (cond.getRelation().returnVal());
    this.formatString(cond_node + " [label=\"" + condition + "\"]");
    this.formatString(parent + " -> " + cond_node +
    " [label=\"" + node_label + "\"]");
    this.formatExpression(cond.getLeft(), cond_node, "left");
    this.formatExpression(cond.getRight(), cond_node, "right");
  }

  private void formatLocation(Location loc, String parent, String label) {
    String node_label = label;
    if (label.equals("")) {
      node_label = "location";
    }
    String loc_node = this.unique_name("location");
    if (loc.isIndex()) {
      this.formatString(loc_node + " [label=\"Index\"]");
      this.formatString(parent + " -> " + loc_node +
      " [label=\"" + node_label + "\"]");
      this.formatLocation(((Index)loc).getLoc(), loc_node, "");
      this.formatExpression(((Index)loc).getExp(), loc_node, "");
    } else if (loc.isRecordField()) {
      this.formatString(loc_node + " [label=\"Field\"]");
      this.formatString(parent + " -> " + loc_node +
      " [label=\"" + node_label + "\"]");
      this.formatLocation(((RecordField)loc).getLoc(), loc_node, "");
      this.formatVariable(loc, loc_node, "");
    } else {
      if (node_label.equals("location")) {
        this.formatVariable(loc, parent, node_label);
      } else {
        this.formatVariable(loc, parent, label);
      }
    }
  }

  private void formatVariable(Location loc, String parent, String label) {
    String node_label = label;
    if (label.equals("")) {
      node_label = "variable";
    }
    String var_node = this.unique_name("variable");
    this.formatString(var_node + " [label=\"Variable\"]");
    this.formatString(parent + " -> " + var_node +
    " [label=\"" + node_label + "\"]");
    String var_name = loc.getToken().returnVal();
    String child_var_node = this.unique_name("variable");
    this.formatString(child_var_node + " [label=\""
      + var_name + "\", shape=circle]");
    this.formatString(var_node + " -> " + child_var_node + " [label=\"ST\"]");
  }

  /***************Symbol Table*********************/

  /**
   * Traverse the Symbol Table once first to add the node names to
   * the top level subgraph cluster. Traverse a second time to
   * actually process the Entries and generate the appropriate DOT code.
   */
  public void visit(Scope scope) {
    // First time through
    this.formatString("subgraph " + this.unique_name("cluster") + " {");
    this.depth++;
    this.top_level = this.unique_name("node");
    this.formatString(this.top_level + " [label=\"\", shape=none]");
    Map<String, Entry> st = scope.getTable();
    for (Map.Entry<String, Entry> entry : st.entrySet()) {
      String name = entry.getKey();
      String node_name = this.unique_name("node");
      this.table_insert(name, node_name);
      this.formatString(node_name + " [label=\"" + name + "\", shape=none]");
    }
    this.depth--;
    this.formatString("}");
    // Second time through
    for (Map.Entry<String, Entry> entry : st.entrySet()) {
      String name = entry.getKey();
      Entry value = entry.getValue();
      value.accept(this, name);
    }
  }

  public void visit(Constant constant, String name) {
    String node_name = this.unique_name("node");
    String parent = this.find(name);
    String value = constant.returnVal() + "";
    this.formatString(node_name + " [label=\"" + value + "\", shape=diamond]");
    this.formatString(parent + " -> " + node_name);
    this.formatType(constant.getType(), node_name);
  }

  public void visit(Variable var, String name) {
    String node_name = this.unique_name("node");
    String parent = this.find(name);
    this.formatString(node_name + " [label=\"\", shape=circle]");
    this.formatString(parent + " -> " + node_name);
    Type type = var.getType();
    this.formatType(var.getType(), node_name);
  }

  public void visit(Field field, String name) {
    String node_name = this.unique_name("node");
    String parent = this.find(name);
    this.formatString(node_name + " [label=\"\", shape=circle]");
    this.formatString(parent + " -> " + node_name);
    Type type = field.getType();
    this.formatType(field.getType(), node_name);
  }

  public void visit(Integer integer, String name) {
    String parent = this.find(name);
    this.formatType(integer, parent);
  }

  public void visit(Array array, String name) {
    String parent = this.find(name);
    this.formatType(array, parent);
  }

  public void visit(Record record, String name) {
    String parent = this.find(name);
    this.formatType(record, parent);
  }

  /********************AST*************************/

  public String visit(AST ast) {
    Instruction curr = ast.getHead();
    String curr_name = "";
    String prev_name = "";
    String first_instr = "";
    boolean first = true;
    while(curr != null) {
      curr_name = curr.accept(this);
      if (first) {
        first_instr = curr_name;
        first = false;
      }
      if (!prev_name.equals("")) {
        this.formatString(prev_name + " -> " + curr_name + " [label=\"next\"]");
      }
      prev_name = curr_name;
      curr = curr.getNext();
    }
    return first_instr;
  }

  public String visit(Assign assign, String name) {
    String parent = this.unique_name(name);
    this.formatString(parent + " [label=\":=\"]");
    this.formatLocation(assign.getLoc(), parent, "");
    this.formatExpression(assign.getExp(), parent, "");
    this.formatString("");
    return parent;
  }

  public String visit(Write write, String name) {
    String parent = this.unique_name(name);
    this.formatString(parent + " [label=\"Write\"]");
    this.formatExpression(write.getExp(), parent, "");
    this.formatString("");
    return parent;
  }

  public String visit(Read read, String name) {
    String parent = this.unique_name(name);
    this.formatString(parent + " [label=\"Read\"]");
    this.formatLocation(read.getLoc(), parent, "");
    this.formatString("");
    return parent;
  }

  public String visit(If _if, String name) {
    String parent = this.unique_name(name);
    this.formatString(parent + " [label=\"If\"]");
    this.formatCondition(_if.getCond(), parent, "");
    String instr_node = this.unique_name("instr");
    String true_name = this.visit(_if.getTrue());
    this.formatString(parent + " -> " + true_name + " [label=\"true\"]");
    if (_if.hasFalse()) {
      String false_name = this.visit(_if.getFalse());
      this.formatString(parent + " -> " + false_name + " [label=\"false\"]");
    }
    this.formatString("");
    return parent;
  }

  public String visit(Repeat repeat, String name) {
    String parent = this.unique_name(name);
    this.formatString(parent + " [label=\"Repeat\"]");
    this.formatCondition(repeat.getCond(), parent, "");
    String instr_node = this.unique_name("instr");
    String instr_name = this.visit(repeat.getInstr());
    this.formatString(parent + " -> " + instr_name + " [label=\"instructions\"]");
    this.formatString("");
    return parent;
  }

  public String toString() {
    return this.output;
  }
}
