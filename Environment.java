/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.*;

public class Environment {
  private Map<String, Box> environment;
  
  public Environment() {
    environment = new HashMap<String, Box>();
  }

  public void addBox(Box box) {
    this.environment.put(box.name(), box);
  }

  public Box find(String name) {
    if (this.local(name)) {
      return this.environment.get(name);
    }
    return null;
  }

  public boolean local(String name) {
    return this.environment.containsKey(name);
  }
}

