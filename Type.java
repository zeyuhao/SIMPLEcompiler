/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Type extends Entry {

  public boolean isType() {
    return true;
  }

  public boolean isInteger() {
    return false;
  }

  public boolean isArray() {
    return false;
  }

  public boolean isRecord() {
    return false;
  }

  public String returnType() {
    return "type";
  }

  // Check if this type matches
  // that of another Node's
  public boolean matchType(Type type) {
    if (this.isInteger()) {
      return type.isInteger();
    } else if (this.isArray()) {
      if (type.isArray()) {
        Type this_elem_type = this.getType();
        Type other_elem_type = type.getType();
        // recursively check that types match
        return this_elem_type.matchType(other_elem_type);
      }
    } else if (this.isRecord()) {
      if (type.isRecord()) {
        Type this_rec_type = this.getType();
        Type other_rec_type = type.getType();
        // recursively check that types match
        return this_rec_type.matchType(other_rec_type);
      }
    }
    return false;
  }
}
