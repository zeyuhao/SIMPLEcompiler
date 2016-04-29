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

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    String str = "";
    String reg_0 = reg.available();
    reg.setInUse();
    str += "\tldr " + reg_0 + ", =rformat\n";
    Location base = this.getBase(this.loc);
    String name = base.toString();
    int addr = this.getEnvBox(base, env).getAddress();
    String reg_1 = reg.available();
    reg.setInUse();
    String reg_2 = reg.available();
    reg.setInUse();
    String reg_3 = reg.available();
    reg.setInUse();
    str += "\tldr " + reg_2 + ", addr_" + name + "\n";
    str += "\tmov " + reg_3 + ", #" + addr + "\n";
    str += "\tadd " + reg_2 + ", " + reg_2 + ", " + reg_3 + "\n";
    str += "\tmov " + reg_1 + ", " + reg_2 + "\n";
    str += "\tbl scanf\n\n";
    reg.reset();
    return str;
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
