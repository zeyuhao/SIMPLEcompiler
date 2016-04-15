/*
Zeyu Hao
zhao7@jhu.edu
*/

public class AST {
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

  public Instruction returnHead() {
    return this.head;
  }

  public Instruction returnLast() {
    return this.current;
  }

  private Instruction nextNode(Instruction node) {
    return node.next();
  }

  private boolean hasNext(Instruction node) {
    return node.next() != null;
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
