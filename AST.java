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

  private Instruction nextNode(Instruction node) {
    return node.getNext();
  }

  private boolean hasNext(Instruction node) {
    return node.getNext() != null;
  }

  // Run each Instruction in the AST
  public void interpret(Environment env) throws Exception {
    Instruction curr = this.head;
    while(curr != null) {
      curr.run(env);
      curr = this.nextNode(curr);
    }
  }

  public String toString() {
    String tree = this.head.toString() + "\n";
    Instruction curr = this.head;
    while(this.hasNext(curr)) {
      curr = this.nextNode(curr);
      tree += curr.toString() + "\n";
    }
    int index = tree.lastIndexOf("\n");
    tree = tree.substring(0, index);
    return tree;
  }
}
