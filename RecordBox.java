/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.HashMap;
import java.util.ArrayList;

public class RecordBox extends Box {
  private HashMap<String, Box> record;

  // Create a RecordBox based directly off of the S.T Record Entry
  public RecordBox(Type record) {
    this.record = new HashMap<String, Box>();
    this.initialize(record);
  }

  private void initialize(Type record) {
    HashMap<String, Entry> rec = ((Record)record).getFields().getTable();
    for (HashMap.Entry<String, Entry> entry : rec.entrySet()) {
      String name = entry.getKey();
      Entry field = entry.getValue();
      Type type = field.getType();
      if (type.isInteger()) {
        this.insertBox(name, new IntegerBox());
      } else if (type.isArray()) {
        int size = ((Array)type).length();
        Type elem_type = type.getType();
        this.insertBox(name, new ArrayBox(type));
      } else if (type.isRecord()) {
        this.insertBox(name, new RecordBox(type));
      }
    }
  }

  public void insertBox(String name, Box box) {
    this.record.put(name, box);
  }

  public Box getBox(String name) {
    if (this.local(name)) {
      return this.record.get(name);
    }
    return null;
  }

  public HashMap<String, Box> getRecord() {
    return this.record;
  }

  private boolean local(String name) {
    return this.record.containsKey(name);
  }

  public boolean isRecord() {
    return true;
  }

  // Deep copy of other ArrayBox for assigning RecordBox to RecordBox
  public void assign(RecordBox other) {
    this.record = new HashMap<String, Box>();
    for (HashMap.Entry<String, Box> box : other.getRecord().entrySet()) {
      String key = box.getKey();
      Box value = box.getValue();
      this.insertBox(key, value);
    }
  }

  public String toString() {
    String str = "Record:\n";
    for (HashMap.Entry<String, Box> box : this.getRecord().entrySet()) {
      String key = box.getKey();
      Box value = box.getValue();
      str += "  " + key + " - " + value.toString() + "\n";
    }
    int index = str.lastIndexOf("\n");
    str = str.substring(0, index);
    return str;
  }
}
