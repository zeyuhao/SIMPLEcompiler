/*
Zeyu Hao
zhao7@jhu.edu
*/

public class Instruction extends Node {
  protected Instruction next;

  public void setNext(Instruction next) {
    this.next = next;
  }

  public Instruction getNext() {
    return this.next;
  }

  public void run(Environment env) throws Exception {
  }

  public String generateCode(Environment env, RegisterDescriptor reg)
    throws Exception {
    return "";
  }

  // Returns the leftmost Location Variable through recursive calls to itself
  public Location getBase(Location loc) {
    if (loc.isIndex()) {
      return this.getBase(((Index)loc).getLoc());
    } else if (loc.isRecordField()) {
      return this.getBase(((RecordField)loc).getLoc());
    } else {
      return loc;
    }
  }

  // Universal function to get the desired box from the Environment
  public Box getEnvBox(Node base, Environment env) throws Exception {
    Node curr = base;
    // Fetch the Box from the Environment by name
    Box box = env.getBox(curr.getToken().returnVal());
    while (curr.getParent().isLocation()) {
      Node prev = curr;
      curr = curr.getParent();
      // If we have an Array Type currently
      if (((Location)curr).isIndex()) {
        Index index = ((Index)curr);
        Expression exp = index.getExp();
        int ind = this.getExpBox(exp, env).getVal();
        if (ind >= ((ArrayBox)box).getSize()) {
          throw new Exception ("Index " + ind + exp.getToken().posString() +
            " is out of bounds. Size of Array " + prev.toString() + " is " +
            ((ArrayBox)box).getSize());
        }
        box = ((ArrayBox)box).getBox(ind);
      } else if (((Location)curr).isRecordField()) {
        String field = ((RecordField)curr).getToken().returnVal();
        box = ((RecordBox)box).getBox(field);
      }
    }
    return box;
  }

  // Evaluate and return a Box representing the 'value' from an Expression
  // Values can be Integers, Records, or Arrays
  public Box getExpBox(Expression exp, Environment env) throws Exception {
    Box box = null;
    if (exp.isConstant()) {
      int value = exp.returnNumber().returnVal();
      // Create a temporary IntegerBox to store a constant value
      box = new IntegerBox();
      ((IntegerBox)box).setBox(value);
    } else if (exp.isBinary()) { // Binary was unfoldable
      Expression left = ((Binary)exp).returnLeft();
      Expression right = ((Binary)exp).returnRight();
      int val1 = this.getExpBox(left, env).getVal();
      int val2 = this.getExpBox(right, env).getVal();
      int sum = 0;
      String op = ((Binary)exp).returnOp().returnVal();
      if (op.equals("+")) {
        sum = val1 + val2;
      } else if (op.equals("-")) {
        sum = val1 - val2;
      } else if (op.equals("*")) {
        sum = val1 * val2;
      } else if (op.equals("DIV")) {
        if (val2 == 0) {
          throw new Exception("DIV by zero found in Expression: " +
            ((Binary)exp).toString() + right.getToken().posString());
        }
        sum = val1 / val2;
      } else if (op.equals("MOD")) {
        if (val2 == 0) {
          throw new Exception("MOD by zero found in Expression: " +
            ((Binary)exp).toString() + right.getToken().posString());
        }
        sum = val1 % val2;
      }
      box = new IntegerBox();
      ((IntegerBox)box).setBox(sum);
    } else {
      // Get the base leftmost Location Variable we are operating from
      Location base = this.getBase(exp.returnLoc());
      box = this.getEnvBox(base, env);
      box.setParent(base.toString());
    }
    return box;
  }

  /**
   * Generate code to move a Constant value into a given Register
   * @param exp the Expression that contains the constant
   * @param reg_name the name of the register that will hold the value
   * @return the generated code
   */
  public String moveConstant(Expression exp, String reg_name) {
    int value = exp.returnNumber().returnVal();
    // If the immediate value is greater than 255, default to
    // using ldr in order to load values (8 bit with 4 bit rotate restrictions)
    if (value > 255) {
      return "\tldr " + reg_name + ", =#" + value + "\n";
    } else {
      return "\tmov " + reg_name + ", #" + value + "\n";
    }
  }
  /**
   * Overloaded function.
   * Generate code to move a Constant value into a given Register
   * @param value the value of the Constant
   * @param reg_name the name of the register that will hold the value
   * @return the generated code
   */
  public String moveConstant(int value, String reg_name) {
    // If the immediate value is greater than 255, default to
    // using ldr in order to load values (8 bit with 4 bit rotate restrictions)
    if (value > 255) {
      return "\tldr " + reg_name + ", =#" + value + "\n";
    } else {
      return "\tmov " + reg_name + ", #" + value + "\n";
    }
  }

  /**
   * Generate code to get the address of a base Location
   * and append it to the given code string
   * @param code the pointer to the code from an Instruction
   */
  public String getAddressCode(Node base, Environment env,
    String offset_reg, RegisterDescriptor reg) throws Exception {
    String code = "";
    Node curr = base;
    /* This 'box' below is used as a model for obtaining base addresses
     * for records. This "model box" will always use element 0 when
     * we encounter an Index location. Reasoning is detailed below.
     * If we have:
     *    arr: ARRAY 2 OF RECORD f,g,h: INTEGER; END;
     * with an instruction such as:
     *    READ i;
     *    arr[i].f := 1
     * The problem with this is that we can't directly get the
     * address of the field 'f' since we don't know what 'i' is
     * at compile time. Therefore, we must generate code to
     * calculate the offset address of the record field based off of
     * an ArrayBox's first element. For example, arr[0].f will
     * have the address 0, arr[0].g will have the address 4, and so on.
     * Therefore, using these base addresses, we can add them
     * to the value stored in a register that contains the address
     * of the Index the record is derived from. For example if 'i'
     * was inputted by a user as 1, its address calculated by generated
     * code below would be 12 (4 for each field * 3 fields * 1 for index).
     * From there, if 'f' was the field selected, the base address from
     * the "model box" would be 0. Therefore, arr[i].f would yield an
     * offset address of 12 + 0 = 12.
     */
    Box box = env.getBox(curr.getToken().returnVal());
    boolean first = true;
    while (curr.getParent().isLocation()) {
      String select_reg = reg.available();
      reg.setInUse();
      Node prev = curr;
      curr = curr.getParent();
      // If we have an Array Type currently
      if (((Location)curr).isIndex()) {
        // model box grabbing its first element
        box = ((ArrayBox)box).getBox(0);
        Index index = ((Index)curr);
        Expression exp = index.getExp();
        // If Index selector is a Constant e.g array[1]
        if (exp.isConstant()) {
          // Get the entire size of the current Array
          int total_size = index.getSize();
          // Get the number of elements of the Array
          int num_elem = index.length();
          // element size = total size / number of elements
          int elem_size = total_size / num_elem;
          // Get the selector index from the Expression
          int select = this.getExpBox(exp, env).getVal();
          // address is selector index * size of each element
          int address = elem_size * select;
          code += this.moveConstant(address, select_reg);
        }
        // if Index selector is a Variable e.g array[i]
        else if (exp.isExpLocation()) {
          // Get the entire size of the current Array
          int total_size = index.getSize();
          // Get the number of elements of the Array
          int num_elem = index.length();
          // element size = total size / number of elements
          int elem_size = total_size / num_elem;

          Location loc = exp.returnLoc();
          // Get the Variable name
          String name = loc.toString();

          String address_reg = reg.available();
          reg.setInUse();
          code += this.moveConstant(elem_size, address_reg);
          code += "\tldr " + select_reg + ", addr_" + name + "\n";
          code += "\tldr " + select_reg + ", [" + select_reg + "]\n";

          // generate code to check array out of bounds
          String size_reg = reg.available();
          reg.setInUse();
          code += this.moveConstant(num_elem, size_reg);
          // if the selector index >= size of array, go to print error
          code += "\tcmp " + select_reg + ", " + size_reg + "\n";
          // skip to continue if not out of bounds
          code += "\tblt continue" + reg.getErrorIndex() + "\n";
          String err_msg = "Index " + exp.getToken().toString() +
            " is out of bounds. Size of Array " + prev.toString() + " is " +
            num_elem;
          String format_name = "eformat" + reg.getErrorIndex();
          String err_format = format_name +
            ": .asciz \"" + err_msg + "\\n\"\n";
          // Save the error format code to environment, added at the end to code
          env.addHelpers(err_format);
          code += "\tldr r0, =" + format_name + "\n";
          code += "\tbl printf\n";
          // Must restore all registers before exiting
          code += reg.popAll();
          // After printing the error, go to end
          code += "\tb end\n";
          reg.reset(size_reg);

          // Skip to this part if index selector is not out of bounds
          code += "continue" + reg.getErrorIndex() + ":\n";
          code += "\tmul " + select_reg + ", " + select_reg + ", " +
            address_reg + "\n";
          reg.reset(address_reg);
          reg.incErrorIndex();
        }
      } else if (((Location)curr).isRecordField()) {
        String field = ((RecordField)curr).getToken().returnVal();
        // fetch the box corresponding to the field
        box = ((RecordBox)box).getBox(field);
        // now get its base address from the "model box"
        int address = box.getAddress();
        code += this.moveConstant(address, select_reg);
      }
      // if first time getting address, store into offset_reg
      if (first) {
        code += "\tmov " + offset_reg + ", " + select_reg + "\n";
        first = false;
      }
      // else add the previous address to the current address
      else {
        code += "\tadd " + offset_reg + ", " + offset_reg + ", " +
          select_reg + "\n";
      }
      // Reset all registers used in this function
      // except for the register holding the address offset
      reg.reset(select_reg);
    }
    return code;
  }

  public String getExpCode(Expression exp, Environment env,
    RegisterDescriptor reg, String reg_name) throws Exception {
    String code = "";
    // Generate Code for Expressions
    if (exp.isBinary()) { // Binary was unfoldable
      Expression left = ((Binary)exp).returnLeft();
      Expression right = ((Binary)exp).returnRight();
      if (!left.isConstant()) {
        code += this.getExpCode(left, env, reg, reg_name);
      }
      String next_reg = reg.available();
      if (!right.isConstant()) {
        reg.setInUse();
        code += this.getExpCode(right, env, reg, next_reg);
      }
      String op = ((Binary)exp).returnOp().returnVal();
      if (op.equals("+")) {
        if (left.isConstant()) {
          if (reg.isPushed(next_reg)) {
            code += reg.pop(next_reg);
          }
          int value = left.returnNumber().returnVal();
          code += "\tadd " + reg_name + ", " + next_reg + ", #" +
            value + "\n";
        } else if (right.isConstant()) {
          if (reg.isPushed(reg_name)) {
            code += reg.pop(reg_name);
          }
          int value = right.returnNumber().returnVal();
          code += "\tadd " + reg_name + ", " + reg_name + ", #" +
            value + "\n";
        } else {
          if (reg.isPushed(next_reg)) {
            code += reg.pop(next_reg);
          }
          if (reg.isPushed(reg_name)) {
            code += reg.pop(reg_name);
          }
          code += "\tadd " + reg_name + ", " + reg_name + ", " +
            next_reg + "\n";
        }
        code += reg.push(reg_name);
      } else if (op.equals("-")) {
        if (left.isConstant()) {
          code += this.moveConstant(left, reg_name);
          if (reg.isPushed(next_reg)) {
            code += reg.pop(next_reg);
          }
          code += "\tsub " + reg_name + ", " + reg_name + ", " +
            next_reg + "\n";
        } else if (right.isConstant()) {
          if (reg.isPushed(reg_name)) {
            code += reg.pop(reg_name);
          }
          int value = right.returnNumber().returnVal();
          code += "\tsub " + reg_name + ", " + reg_name + ", #" +
            value + "\n";
        } else {
          if (reg.isPushed(next_reg)) {
            code += reg.pop(next_reg);
          }
          if (reg.isPushed(reg_name)) {
            code += reg.pop(reg_name);
          }
          code += "\tsub " + reg_name + ", " + reg_name + ", " +
            next_reg + "\n";
        }
        code += reg.push(reg_name);
      } else if (op.equals("*")) {
        if (left.isConstant()) {
          code += this.moveConstant(left, reg_name);
        } else if (right.isConstant()) {
          code += this.moveConstant(right, next_reg);
        }
        if (reg.isPushed(next_reg)) {
          code += reg.pop(next_reg);
        }
        if (reg.isPushed(reg_name)) {
          code += reg.pop(reg_name);
        }
        code += "\tmul " + reg_name + ", " + reg_name + ", " + next_reg + "\n";
        code += reg.push(reg_name);
      } else if (op.equals("DIV")) {
        if (left.isConstant()) {
          code += this.moveConstant(left, reg_name);
        } else if (right.isConstant()) {
          code += this.moveConstant(right, next_reg);
        }
        code += "\tmov r0, " + reg_name + "\n";
        code += "\tmov r1, " + next_reg + "\n";

        // Code to check for div by 0 for values stored in variables
        code += "\tcmp r1, #0\n";
        code += "\tbne continue" + reg.getErrorIndex() + "\n";
        String err_msg = "DIV by zero found in Expression: " +
          ((Binary)exp).toString() + right.getToken().posString();
        String format_name = "divbyzeroformat" + reg.getErrorIndex();
        String err_format = format_name +
          ": .asciz \"" + err_msg + "\\n\"\n";
        // Save the error format code to environment, added at the end to code
        env.addHelpers(err_format);
        code += "\tldr r0, =" + format_name + "\n";
        code += "\tbl printf\n";
        // Must restore all registers before exiting
        code += reg.popAll();
        // After printing the error, go to end
        code += "\tb end\n";

        // Skip to this part if index selector is not out of bounds
        code += "continue" + reg.getErrorIndex() + ":\n";
        code += "\tbl __aeabi_idivmod\n";
        code += "\tmov " + reg_name + ", r0\n";
        code += reg.push(reg_name);
        reg.incErrorIndex();
      } else if (op.equals("MOD")) {
        if (left.isConstant()) {
          code += this.moveConstant(left, reg_name);
        } else if (right.isConstant()) {
          code += this.moveConstant(right, next_reg);
        }
        code += "\tmov r0, " + reg_name + "\n";
        code += "\tmov r1, " + next_reg + "\n";

        // Code to check for div by 0 for values stored in variables
        code += "\tcmp r1, #0\n";
        code += "\tbne continue" + reg.getErrorIndex() + "\n";
        String err_msg = "DIV by zero found in Expression: " +
          ((Binary)exp).toString() + right.getToken().posString();
        String format_name = "modbyzeroformat" + reg.getErrorIndex();
        String err_format = format_name +
          ": .asciz \"" + err_msg + "\\n\"\n";
        // Save the error format code to environment, added at the end to code
        env.addHelpers(err_format);
        code += "\tldr r0, =" + format_name + "\n";
        code += "\tbl printf\n";
        // Must restore all registers before exiting
        code += reg.popAll();
        // After printing the error, go to end
        code += "\tb end\n";

        // Skip to this part if index selector is not out of bounds
        code += "continue" + reg.getErrorIndex() + ":\n";
        code += "\tbl __aeabi_idivmod\n";
        code += "\tmov " + reg_name + ", r1\n";
        code += reg.push(reg_name);
        reg.incErrorIndex();
      }
      reg.preserve();
    } else {
      // Get the base leftmost Location Variable we are operating from
      Location base = this.getBase(exp.returnLoc());
      String name = base.toString();
      String address = "";
      // If base Location is an Array or Record, we need to get the address
      if (base.getType().isArray() || base.getType().isRecord()) {
        // Offset address will be stored in a register
        // Register used to calculate the offset for addresses
        String offset_reg = reg.available();
        reg.setInUse();
        code += this.getAddressCode(base, env, offset_reg, reg);
        address = offset_reg;
      } else {
        // Offset address will be a constant number
        address = "#" + this.getEnvBox(base, env).getAddress();
      }
      String next_reg = reg.available();
      reg.setInUse();
      code += "\tldr " + next_reg + ", addr_" + name + "\n";
      code += "\tldr " + reg_name + ", [" + next_reg + ", +" + address + "]\n";
      // Don't need next_reg anymore after its value is transferred
      reg.reset(next_reg);
    }
    return code;
  }

  public String removeTrailingNewLine(String str) {
    int index = str.lastIndexOf("\n");
    if (index != -1) {
      str = str.substring(0, index);
    }
    return str;
  }

  public String accept(Visitor visitor) {
    return "";
  }
}
