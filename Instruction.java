/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Instruction extends Node {
  protected Instruction next;

  public void setNext(Instruction next) {
    this.next = next;
  }

  public Instruction next() {
    return this.next;
  }
}
