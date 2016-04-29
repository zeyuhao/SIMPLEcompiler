/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.HashMap;

public class Record extends Type {
  private Scope fields;
  public Record(Scope fields) {
    this.fields = fields; // fields of the record, of type variable
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

  public int getMemSpace() {
    int mem = 0;
    HashMap<String, Entry> rec = this.fields.getTable();
    for (HashMap.Entry<String, Entry> entry : rec.entrySet()) {
      String key = entry.getKey();
      Type type = entry.getValue().getType();
      mem += type.getMemSpace();
    }
    return mem;
  }

  public String toString() {
    return "Record:\n" + this.fields.toString();
  }
}
