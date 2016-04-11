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
    this.type = ((Array) loc.getType()).getType();
    // Makes sure that the Index is referenced by an Integer
    if (!this.exp.getType().isInteger()) {
      throw new Exception("Expression in Index must be an Integer, found " +
        this.exp.toString() + " of Type " + this.exp.getType().returnType());
    }
  }

  public boolean isIndex() {
    return true;
  }

  public String toString() {
    return this.loc.toString() + "[" + exp.toString() + "]";
  }
}
