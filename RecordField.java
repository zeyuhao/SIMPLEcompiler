/*
Zeyu Hao
zhao7@jhu.edu
*/

public class RecordField extends Location {
  private Location loc;
  private Field field;

  public RecordField(Location loc, Field field, Token token)
    throws Exception {
    super();
    this.loc = loc;
    this.field = field;
    this.token = token;
    this.type = field.getType();
    loc.setParent(this);
  }

  public Location getLoc() {
    return this.loc;
  }

  public Field getField() {
    return this.field;
  }

  public boolean isRecordField() {
    return true;
  }

  public String toString() {
    return this.loc.toString() + "." + token.returnVal();
  }
}
