/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.Map;
import java.util.TreeMap;

public class Scope {

  private Map<String, Entry> symbol_table;
  private Scope parent;

  // default constructor
  public Scope(Scope parent) {
    this.symbol_table = new TreeMap<String, Entry>();
    this.parent = parent;
  }

  // constructor for the universe scope
  public Scope() {
    this.symbol_table = new TreeMap<String, Entry>();
    this.parent = null;
  }

  public void insert(String name, Entry declaration) {
    this.symbol_table.put(name, declaration);
  }

  // recursively search for Entry if not in current scope
  public Entry find(String name) {
    if (this.local(name)) {
      return this.symbol_table.get(name);
    } else if (this.parent != null) { // are we in universe Scope or Record?
      return this.parent.find(name);
    }
    return null;
  }

  // Only search for Entry in current scope (for record searching)
  public Entry local_find(String name) {
    if (this.local(name)) {
      return this.symbol_table.get(name);
    }
    return null;
  }

  public boolean local(String name) {
    return this.symbol_table.containsKey(name);
  }

  public Scope getParent() {
    return this.parent;
  }

  public void setParent(Scope parent) {
    this.parent = parent;
  }

  public boolean isEmpty() {
    return this.symbol_table.size() == 0;
  }

  public Map<String, Entry> getTable() {
    return this.symbol_table;
  }

  public void createEnvironment(Environment env) {
    for (Map.Entry<String, Entry> entry : this.symbol_table.entrySet()) {
      String key = entry.getKey();  // name of the Entry
      Entry value = entry.getValue(); // the actual Entry
      Box box = null;
      // Only keep track of variables in the Environment
      if (value.isVariable()) {
        Type type = value.getType(); // the Entry Type
        if (type.isInteger()) {
          box = new IntegerBox(0);
        } else if (type.isArray()) {
          box = new ArrayBox(type, 0);
        } else if (type.isRecord()) {
          box = new RecordBox(type, 0);
        }
        env.insertBox(key, box);
      }
    }
  }

  public String toString() {
    String str = "";
    for (Map.Entry<String, Entry> entry : this.symbol_table.entrySet()) {
      String key = entry.getKey();
      Entry value = entry.getValue();
      str += key + " - " + value.toString() + "\n";
    }
    int index = str.lastIndexOf("\n");
    if (index != -1) {
      str = str.substring(0, index);
    }
    return str;
  }
}
