/*
Zeyu Hao
zhao7@jhu.edu
*/

public class AST extends Node {
  private Instruction head;
  private Instruction current;

  public AST(Instruction head) {
    this.head = head;
    this.current = this.head;
  }

  public void addNode(Instruction next) {
    this.current.setNext(next);
    this.current = next;
  }

  private boolean hasNext() {
    return this.current.getNext() != null;
  }

  public Instruction getHead() {
    return this.head;
  }

  // Run each Instruction in the AST
  public void interpret(Environment env) throws Exception {
    Instruction curr = this.head;
    while(curr != null) {
      curr.run(env);
      curr = curr.getNext();
    }
  }

  // Generate Code for each Instruction in the AST
  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String str = "";
    Instruction curr = this.head;
    while(curr != null) {
      str += curr.generateCode(env, reg);
      curr = curr.getNext();
    }
    return str;
  }

  public String toString() {
    String tree = this.head.toString() + "\n";
    Instruction curr = this.head;
    while(this.hasNext()) {
      curr = curr.getNext();
      tree += curr.toString() + "\n";
    }
    int index = tree.lastIndexOf("\n");
    tree = tree.substring(0, index);
    return tree;
  }
}
