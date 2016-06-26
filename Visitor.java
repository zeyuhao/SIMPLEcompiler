/*
Zeyu Hao
zhao7@jhu.edu
*/

public interface Visitor {
  /***************Symbol Table*********************/
  public void visit(Scope scope);
  public void visit(Constant constant, String name);
  public void visit(Variable var, String name);
  public void visit(Field field, String name);
  public void visit(Integer integer, String name);
  public void visit(Array array, String name);
  public void visit(Record record, String name);
  /********************AST*************************/
  public String visit(AST ast);
  public String visit(Assign assign, String name);
  public String visit(Write write, String name);
  public String visit(Read read, String name);
  public String visit(If _if, String name);
  public String visit(Repeat Repeat, String name);
  public void start(Boolean is_ast);
  public void end(Boolean is_ast);
  public int getCount();
  public String getTop();
  public String toString();
}
