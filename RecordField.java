/*
Zeyu Hao
zhao7@jhu.edu
*/

public class RecordField extends Location {
  private Location loc;
  private Variable field;

  public RecordField(Location loc, Variable field, Token token)
    throws Exception {
    super();
    this.loc = loc;
    this.field = field;
    this.token = token;
    this.type = field.getType();
  }

  public boolean isRecordField() {
    return true;
  }

  public String toString() {
    return this.loc.toString() + "." + token.returnVal();
  }
}
