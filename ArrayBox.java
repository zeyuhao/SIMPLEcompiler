/*
Zeyu Hao
zhao7@jhu.edu
*/

import java.util.ArrayList;

public class ArrayBox extends Box {
  private Box[] array;
  private Type type; // The Array itself
  private Type elemType; // element Type of Array

  // Create an ArrayBox based directly off of the S.T Array Entry
  public ArrayBox(Type array, int address) {
    int size = ((Array)array).length();
    this.type = array;
    this.elemType = array.getType(); // store the element type
    this.array = new Box[size];
    this.address = address;
    this.initialize();
  }

  // Initialize each element of the Array
  private void initialize() {
    if (this.elemType.isInteger()) {
      for (int i = 0; i < this.getSize(); i++) {
        int curr_address = i * this.elemType.getMemSpace() + this.address;
        this.setBox(i, new IntegerBox(curr_address));
      }
    } else if (this.elemType.isArray()) {
      for (int i = 0; i < this.getSize(); i++) {
        int curr_address = i * this.elemType.getMemSpace() + this.address;
        this.setBox(i, new ArrayBox(this.elemType, curr_address));
      }
    } else if (this.elemType.isRecord()) {
      for (int i = 0; i < this.getSize(); i++) {
        int curr_address = i * this.elemType.getMemSpace() + this.address;
        this.setBox(i, new RecordBox(this.elemType, curr_address));
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

  public Type getElemType() {
    return this.elemType;
  }

  public boolean isArray() {
    return true;
  }

  public void setArray(Box[] array) {
    this.array = array;
  }

  public Box[] deepCopy() {
    // Make an empty copy with same size
    Box[] copy = new Box[this.getSize()];
    // Iterate through each element of the current ArrayBox
    for (int i = 0; i < this.getSize(); i++) {
      Box box = this.getBox(i);
      // If element is an IntegerBox
      if (box.isInteger()) {
        // Invoke IntegerBox's deepCopy()
        copy[i] = ((IntegerBox)box).deepCopy();
      }
      // If element is another ArrayBox
      else if (box.isArray()) {
        // this.elemType refers to this ArrayBox's element Type
        copy[i] = new ArrayBox(this.elemType, box.getAddress());
        ((ArrayBox)copy[i]).setArray(((ArrayBox)box).deepCopy());
      }
      // If element is a RecordBox
      else if (box.isRecord()) {
        copy[i] = new RecordBox(this.elemType, box.getAddress());
        ((RecordBox)copy[i]).setRecord(((RecordBox)box).deepCopy());
      }
    }
    return copy;
  }

  // Deep copy of other ArrayBox for assigning ArrayBox to ArrayBox
  public void assign(ArrayBox other) {
    this.array = other.deepCopy();
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
