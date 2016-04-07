/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.Stack;

public class GraphicalObserver extends Observer {

  private Stack<String> parents;
  private String output;
  private int count; // used to create unique node names

  public GraphicalObserver() {
    this.parents = new Stack<String>();
    this.output = "";
    this.count = 0;
  }

  private String unique_name() {
    // concatenate the a possibly non-unique token name with a unique int
    // "count" to create a unique node name
    return "node" + this.count;
  }

  public void start() {
    this.output += "digraph X {\n";
  }

  public void end() {
    this.output += "}";
  }

  public void update(String token) {
    String name = unique_name();
    this.output += name + " [label=\"" + token + "\",shape=box]\n";
    if (!this.parents.empty()) {
      this.output += this.parents.peek() + " -> " + name + "\n";
    }
    this.count++;
    this.parents.push(name);
  }

  public void update(Token token) {
    String name = unique_name();
    this.output += name + " [label=\"" + token.returnVal() +
      "\",shape=diamond]\n";
    if (!this.parents.empty()) {
      this.output += this.parents.peek() + " -> " + name + "\n";
    }
    this.count++;
  }

  public void update_end() {
    this.parents.pop();
  }

  public String toString(){
    return this.output;
  }
}
