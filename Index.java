/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Index extends Location {
  private Location loc;
  private Expression exp;

  public Index(Location loc, Expression exp) throws Exception {
    super();
    this.loc = loc;
    this.exp = exp;
    this.token = exp.getToken();
    this.type = ((Array)loc.getType()).getType();

    // Makes sure that the Index is referenced by an Integer
    if (!this.exp.getType().isInteger()) {
      throw new Exception("Expression in Index must be an Integer, found " +
        this.exp.toString() + " of Type " + this.exp.getType().returnType());
    }
    loc.setParent(this);
    exp.setParent(this);
  }

  public boolean isIndex() {
    return true;
  }

  public Location getLoc() {
    return this.loc;
  }

  public Expression getExp() {
    return this.exp;
  }

  // Return the total size of the Array
  public int getSize() {
    return ((Array)this.loc.getType()).getMemSpace();
  }

  // Return the total number of elements of the Array
  public int length() {
    return ((Array)this.loc.getType()).length();
  }

  public String toString() {
    return this.loc.toString() + "[" + exp.toString() + "]";
  }
}
