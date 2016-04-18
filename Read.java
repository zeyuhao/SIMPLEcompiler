/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.io.*;
import java.lang.Integer;

public class Read extends Instruction {
  private Location loc;

  public Read(Location loc) {
    this.loc = loc;
    loc.setParent(this);
  }

  public void run(Environment env) throws Exception {
    Box box = this.getEnvBox(this.loc, env);
    int value = this.read_int();
    ((IntegerBox)box).setBox(value);
  }

  private int read_int() throws Exception {
    String input = "";
    BufferedReader in = null;
    int value = 0;
    char c;
    try {
      in = new BufferedReader(new InputStreamReader(System.in));
      // reads until an EOF
      input = in.readLine();
    } finally {
      // close the InputStream
      if (in != null) {
        in.close();
      }
    }
    return Integer.parseInt(input);
  }

  public String toString() {
    return "Read:\n  Location =>\n  " + this.loc.toString() + "\n";
  }
}
