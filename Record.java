/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Record extends Type {
  private Scope fields;
  public Record(Scope fields) {
    this.fields = fields; // fields of the record, of type variable
  }

  public boolean isRecord() {
    return true;
  }

  public Scope getFields() {
    return this.fields;
  }

  public String toString() {
    return "Record:\n" + this.fields.toString();
  }
}
