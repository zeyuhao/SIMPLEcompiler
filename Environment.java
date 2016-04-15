/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.*;

public class Environment {
  private HashMap<String, Box> environment;

  public Environment() {
    environment = new HashMap<String, Box>();
  }

  public void insertBox(String name, Box box) {
    this.environment.put(name, box);
  }

  public Box getBox(String name) {
    if (this.local(name)) {
      return this.environment.get(name);
    }
    return null;
  }

  private boolean local(String name) {
    return this.environment.containsKey(name);
  }

  public String toString() {
    String str = "";
    for (HashMap.Entry<String, Box> entry : this.environment.entrySet()) {
      String key = entry.getKey();
      Box value = entry.getValue();
      str += key + " - " + value.toString() + "\n";
    }
    return str;
  }
}
