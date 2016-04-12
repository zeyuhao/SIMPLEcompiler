/*
Zeyu Hao
zhao7@jhu.edu
*/

public class ArrayBox extends Box {
  private Box[] array;

  public ArrayBox(String name, int size) {
    this.name = name;
    this.array = new Box[size];
  }

  public void setVal(int index, Box box) {
    this.array[index] = box;
  }


  public int getVal(int index) {
    return this.array[index].getVal();
  }

  public Box getBox(int index) {
    return this.array[index];
  }
}
