/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.*;

public class Scope {

  private Map<String, Entry> symbol_table;
  private Scope parent;

  // default constructor
  public Scope(Scope parent) {
    this.symbol_table = new HashMap<String, Entry>();
    this.parent = parent;
  }

  // constructor for the universe scope
  public Scope() {
    this.symbol_table = new HashMap<String, Entry>();
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

  public String toString() {
    String str = "";
    for (Map.Entry<String, Entry> entry : this.symbol_table.entrySet()) {
      String key = entry.getKey();
      Entry value = entry.getValue();
      str += key + " - " + value.toString() + "\n";
    }
    return str;
  }
}
