/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.HashMap;
import java.util.ArrayList;

public class RecordBox extends Box {
  private HashMap<String, Box> record;
  private Type type;

  // Create a RecordBox based directly off of the S.T Record Entry
  public RecordBox(Type record) {
    this.type = record;
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

  private void insertBox(String name, Box box) {
    this.record.put(name, box);
  }

  public Box getBox(String name) {
    if (this.local(name)) {
      return this.record.get(name);
    }
    return null;
  }

  public Type getType() {
    return this.type;
  }

  public void setBox(String name, Box other) throws Exception {
    Box box = this.getBox(name);
    box = other;
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

  public void setRecord(HashMap<String, Box> record) {
    this.record = record;
  }

  public HashMap<String, Box> deepCopy() {
    HashMap<String, Box> copy = new HashMap<String, Box>();
    for (HashMap.Entry<String, Box> entry : this.record.entrySet()) {
      String key = entry.getKey();
      Box box = entry.getValue();
      if (box.isInteger()) {
        copy.put(key, new IntegerBox(((IntegerBox)box).getVal()));
      } else if (box.isArray()) {
        copy.put(key, new ArrayBox(((ArrayBox)box).getType()));
        ((ArrayBox)copy.get(key)).setArray(((ArrayBox)box).deepCopy());
      } else if (box.isRecord()) {
        copy.put(key, new RecordBox(((ArrayBox)box).getType()));
        ((RecordBox)copy.get(key)).setRecord(((RecordBox)box).deepCopy());
      }
    }
    return copy;
  }

  // Deep copy of other ArrayBox for assigning RecordBox to RecordBox
  public void assign(RecordBox other) {
    this.record = other.deepCopy();
    /*this.record = new HashMap<String, Box>();
    for (HashMap.Entry<String, Box> entry : other.getRecord().entrySet()) {
      String key = entry.getKey();
      Box box = entry.getValue();
      this.insertBox(key, box);
    }*/
  }

  public String toString() {
    String str = "Record:\n";
    for (HashMap.Entry<String, Box> entry : this.getRecord().entrySet()) {
      String key = entry.getKey();
      Box box = entry.getValue();
      str += "  " + key + " - " + box.toString() + "\n";
    }
    int index = str.lastIndexOf("\n");
    str = str.substring(0, index);
    return str;
  }
}
