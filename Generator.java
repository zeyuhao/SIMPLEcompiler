/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.Map;
import java.lang.String;

public class Generator {

  private Scope symbol_table; // The symbol table
  private AST ast;  // The Abstract Symbol Tree
  private int used_mem; // Currently used memory (bytes)
  private String code; // The generated assembly code
  private String code_end;
  private Environment env; // The code environment for getting information
                            // for each node in the Instructions
  private RegisterDescriptor reg;

  public Generator(Scope symbol_table, Environment env, AST ast) {
    this.symbol_table = symbol_table;
    this.env = env;
    this.ast = ast;
    this.used_mem = 0;
    this.code = "";
    this.code_end = "";
    this.reg = new RegisterDescriptor();
  }

  public String generateCode() throws Exception {
    this.initialize();
    this.process();
    this.end();
    return this.code;
  }

  private void setup_int(String name, Variable var) {
    int size = var.returnSize();
    this.used_mem += size;
    this.code += ".balign " + size + "\n";
    this.code += name + ": .word 0\n\n";
    this.code_end += "addr_" + name + " : .word " + name + "\n";
  }

  private void setup_array_rec(String name, Variable var) {
    int size = var.returnSize();
    this.used_mem += size;
    this.code += ".balign 4" + "\n";
    this.code += name + ": .skip " + size + "\n\n";
    this.code_end += "addr_" + name + " : .word " + name + "\n";
  }

  // Generate code to initialize all variables from symbol table
  private void initialize() {
    this.code += ".data\n\n";
    Map<String, Entry> table = this.symbol_table.getTable();
    for (Map.Entry<String, Entry> entry : table.entrySet()) {
      String name = entry.getKey();  // name of the Variable
      Entry var = entry.getValue(); // the actual Variable
      // Only keep track of variables in the Environment
      if (var.isVariable()) {
        Type type = var.getType(); // the Entry Type
        if (type.isInteger()) {
          setup_int(name, (Variable)var);
        } else if (type.isArray() || type.isRecord()) {
          setup_array_rec(name, (Variable)var);
        }
      }
    }
  }

  // Generate code by processing each instruction in the AST
  private void process() throws Exception {
    this.code += ".text\n.global main\n\nmain:\n";
    this.code += "\tpush {lr}\n\n";
    this.code += this.ast.generateCode(this.env, this.reg);
  }

  private void end() {
    this.code += "\tpop {pc}\n\n";
    if (this.code.contains("printf")) {
      this.code += "wformat : .asciz \"%d\\n\"\n";
    }
    if (this.code.contains("scanf")) {
      this.code += "rformat : .asciz \"%d\"\n\n";
    }
    this.code += this.code_end;
  }
}
