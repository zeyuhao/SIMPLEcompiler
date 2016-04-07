/*
Zeyu Hao
zhao7@jhu.edu
*/

public class TextualObserver extends Observer {

  private int depth;
  private String tree;
  public TextualObserver() {
    this.depth = 0;
    this.tree = "";
  }

  private String token_string(Token token) {
    String str = "";
    for (int i = 0; i < this.depth; i++) {
      str += "  "; // increment by two spaces for each depth
    }
    return str += token.toString() + "\n";
  }

  public void start() {}

  public void end() {
    // Remove the last \n
    this.tree = this.tree.substring(0,this.tree.lastIndexOf('\n'));
  }

  public void update(Token token) {
    this.tree += this.token_string(token);
  }
  public void update(String token) {
    for (int i = 0; i < this.depth; i++) {
      this.tree += "  "; // increment by two spaces for each depth
    }
    this.tree += token + "\n";
    this.depth++;
  }

  public void update_end() {
    this.depth--;
  }

  public String toString() {
    return this.tree;
  }
}
