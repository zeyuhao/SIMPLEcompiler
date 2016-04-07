/*
Zeyu Hao
zhao7@jhu.edu
*/

public abstract class Observer {
  public abstract void start();
  public abstract void end();
  public abstract void update(String token);
  public abstract void update(Token token);
  public abstract void update_end();
  public abstract String toString();
}
