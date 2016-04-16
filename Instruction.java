/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Instruction extends Node {
  protected Instruction next;

  public void setNext(Instruction next) {
    this.next = next;
  }

  public Instruction getNext() {
    return this.next;
  }

  public boolean isInstruction() {
    return true;
  }

  public boolean isAssign() {
    return false;
  }

  public boolean isIf() {
    return false;
  }

  public boolean isRepeat() {
    return false;
  }

  public boolean isWrite() {
    return false;
  }

  public boolean isRead() {
    return false;
  }

  public void run(Environment env) {
  }

  // Returns the leftmost Location Variable through recursive calls to itself
  public Location getBase(Location loc) {
    if (loc.isIndex()) {
      return this.getBase(((Index)loc).getLoc());
    } else if (loc.isRecordField()) {
      return this.getBase(((RecordField)loc).getLoc());
    } else {
      return loc;
    }
  }

  // Universal function to get the desired box from the Environment
  public Box getBox(Node base, Environment env) {
    Node curr = base;
    Box box = env.getBox(curr.getToken().returnVal());
    while (!curr.getParent().isInstruction()) {
      curr = curr.getParent();
      if (((Location)curr).isIndex()) {
        int index = ((Index)curr).getExp().returnNumber().returnVal();
        box = ((ArrayBox)box).getBox(index);
      } else if (((Location)curr).isRecordField()) {
        String field = ((RecordField)curr).getToken().returnVal();
        box = ((RecordBox)box).getBox(field);
      }
    }
    return box;
  }
}
