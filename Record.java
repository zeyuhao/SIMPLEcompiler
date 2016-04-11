/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Record extends Type {
  private Scope fields;
  public Record(Scope fields, Type type) {
    this.fields = fields; // fields of the record, of type variable
    this.type = type; // Type of the field
  }

  public boolean isRecord() {
    return true;
  }

  public String returnType() {
    return "Record";
  }

  public Scope getFields() {
    return this.fields;
  }

  public String toString() {
    return "Record:\n" + this.fields.toString();
  }
}
