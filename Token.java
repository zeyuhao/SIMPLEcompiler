/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.lang.Integer;

public class Token {

  private String type;
  private String val;
  private int[] position = new int[2];

  public Token(String type, String val, int start, int end) {
    this.type = type;
    this.val = val;
    this.position[0] = start;
    this.position[1] = end;
  }

  public boolean isIdentifier() {
    return this.type.equals("identifier");
  }

  public boolean isInteger() {
    return this.type.equals("integer");
  }

  public boolean isKeyword() {
    return this.type.equals("keyword");
  }

  public String returnType() {
    return this.type;
  }

  public String returnVal() {
    return this.val;
  }

  public int returnIntVal() {
    try {
      Integer.parseInt(this.val);
    } catch (Exception e) {
      System.err.println("Value of arithmetic Expression " +
        this.toString() + " exceeds system restrictions:\n" +
        "[-2,147,483,648, 2,147,483,647] (inclusive)");
      System.exit(0);
    }
    return Integer.parseInt(this.val);
  }

  public int[] returnPos() {
    return this.position;
  }

  public void setPos(int pos1, int pos2) {
    this.position[0] = pos1;
    this.position[1] = pos2;
  }

  public String posString() {
    return "@(" + this.position[0] + ", " + this.position[1] + ")";
  }

  public String toString() {
    String str = "";
    if (this.isKeyword() || this.type.equals("eof")) {
      str = this.val + "@(" + this.position[0] + ", " +
               this.position[1] + ")";
    } else if (this.isIdentifier() || this.isInteger()) {
      str = this.type + "<" + this.val + ">@(" + this.position[0] + ", " +
               this.position[1] + ")";
    }
    return str;
  }
}
