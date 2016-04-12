/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.*;

public class RecordBox extends Box {
  private Map<String, Box> record;
  
  public RecordBox(String name) {
    this.name = name;
    this.record = new HashMap<String, Box>();
  }

  public void insert(String name, Box box) {
    this.record.put(name, box);
  }

  public Box find(String name) {
    if (this.local(name)) {
      return this.record.get(name);
    }
    return null;
  }

  public boolean local(String name) {
    return this.record.containsKey(name);
  }
}
