/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.ArrayList;

public class ArrayBox extends Box {
  private Box[] array;
  private Type type; // element Type

  // Create an ArrayBox based directly off of the S.T Array Entry
  public ArrayBox(Type array, int address) {
    int size = ((Array)array).length();
    this.type = array.getType(); // store the element type
    this.array = new Box[size];
    this.address = address;
    this.initialize();
  }

  // Initialize each element of the Array
  private void initialize() {
    if (this.type.isInteger()) {
      for (int i = 0; i < this.getSize(); i++) {
        int curr_address = i * this.type.getMemSpace() + this.address;
        this.setBox(i, new IntegerBox(curr_address));
      }
    } else if (this.type.isArray()) {
      for (int i = 0; i < this.getSize(); i++) {
        int curr_address = i * this.type.getMemSpace() + this.address;
        this.setBox(i, new ArrayBox(this.type, curr_address));
      }
    } else if (this.type.isRecord()) {
      for (int i = 0; i < this.getSize(); i++) {
        int curr_address = i * this.type.getMemSpace() + this.address;
        this.setBox(i, new RecordBox(this.type, curr_address));
      }
    }
  }

  // Set an element of the ArrayBox to another Box
  public void setBox(int index, Box other) {
    this.array[index] = other;
  }

  public int getSize() {
    return this.array.length;
  }

  public Box getBox(int index) {
    return this.array[index];
  }

  public Type getType() {
    return this.type;
  }

  public boolean isArray() {
    return true;
  }

  public void setArray(Box[] array) {
    this.array = array;
  }

  public Box[] deepCopy() {
    Box[] copy = new Box[this.getSize()];
    for (int i = 0; i < this.getSize(); i++) {
      Box box = this.getBox(i);
      if (box.isInteger()) {
        int value = ((IntegerBox)box).getVal();
        copy[i] = new IntegerBox(box.getAddress());
        ((IntegerBox)copy[i]).setBox(value);
      } else if (box.isArray()) {
        System.out.println(box.toString());
        copy[i] = new ArrayBox(this.type, box.getAddress());
        ((ArrayBox)copy[i]).setArray(((ArrayBox)box).deepCopy());
      } else if (box.isRecord()) {
        copy[i] = new RecordBox(this.type.getType(), box.getAddress());
        ((RecordBox)copy[i]).setRecord(((RecordBox)box).deepCopy());
      }
    }
    return copy;
  }

  // Deep copy of other ArrayBox for assigning ArrayBox to ArrayBox
  public void assign(ArrayBox other) {
    this.array = other.deepCopy();
    /*this.array = new Box[other.getSize()];
    for (int i = 0; i < other.getSize(); i++) {
      this.setBox(i, other.getBox(i));
    }*/
  }

  public String toString() {
    String str = "Array of " + this.getSize() + ":\n";
    for (int i = 0; i < this.getSize(); i++) {
      str += "  [" + i + "] " + this.getBox(i).toString() + "\n";
    }
    int index = str.lastIndexOf("\n");
    str = str.substring(0, index);
    return str;
  }
}
