/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Read extends Instruction {
  private Location loc;

  public Read(Location loc) {
    this.loc = loc;
    loc.setParent(this);
  }

  public String toString() {
    return "Read:\n  Location =>\n  " + this.loc.toString() + "\n";
  }
}
