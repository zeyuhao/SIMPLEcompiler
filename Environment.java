/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.Map;
import java.util.TreeMap;
import java.io.*;
import java.lang.Integer;

public class Environment {
  private Map<String, Box> environment;
  private BufferedReader in;
  private String helpers; // helper code used for formatting text based off
                          // of context info

  public Environment() {
    this.environment = new TreeMap<String, Box>();
    this.in = new BufferedReader(new InputStreamReader(System.in));
    this.helpers = "";
  }

  public void addHelpers(String code) {
    this.helpers += code;
  }

  public String getHelpers() {
    return this.helpers;
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

  public int read_int() throws Exception {
    String input = "";
    int value = 0;
    char c;
    try {
      // reads until an EOF
      input = this.in.readLine();
    } catch (IOException e) {
      //System.err.println("error: Could not read from stdin");
      e.printStackTrace();
      System.exit(0);
    }
    return Integer.parseInt(input);
  }

  public void close_reader() throws Exception {
    try {
      if (this.in != null) {
        this.in.close();
      }
    } catch (IOException e) {
      System.err.println("error: Could not close BufferedReader");
      System.exit(0);
    }
  }

  public String toString() {
    String str = "";
    for (Map.Entry<String, Box> entry : this.environment.entrySet()) {
      String key = entry.getKey();
      Box value = entry.getValue();
      str += key + " - " + value.toString() + "\n";
    }
    return str;
  }
}
