/*
Zeyu Hao
zhao7@jhu.edu
*/
import java.lang.Integer;

public class RegisterDescriptor {
  // Array to keep track of used register
  private boolean[] registers;
  // Array to keep track of which registers are pushed onto the stack
  private boolean[] stack;
  // index of most recent available register
  private int current;
  // index to create unique branch labels for If, Repeat, While instructions
  private int branch_index;
  // index to create unique format labels for error messages
  private int error_index;
  // boolean to keep track of whether or there are registers pushed onto the
  // stack becaused pushUsed() was called (registers were full at some point)
  private boolean full;
  private RegisterDescriptor parent;

  public RegisterDescriptor() {
    // r0 - r12 can be used freely
    this.registers = new boolean[13];
    this.stack = new boolean[15];
    this.current = 0;
    this.branch_index = 0;
    this.error_index = 0;
    this.full = false;
    this.parent = null;
    this.reset(); //safety measure, not really needed
  }

  public void setParent(RegisterDescriptor parent) {
    this.parent = parent;
  }

  public RegisterDescriptor getParent() {
    return this.parent;
  }

  public int getCurrent() {
    return this.current;
  }

  // Reset all registers
  public void reset() {
    for (int i = 0; i < this.registers.length; i++) {
      this.registers[i] = false; // reset all to false for 'unused'
    }
  }

  /** Overloaded function.
   *  Reset the specified register(s)
   */
  public void reset(String[] regs) {
    for (String reg : regs) {
      int saved = Integer.parseInt(reg.substring(1, reg.length()));
      this.registers[saved] = false;
    }
  }
  /** Overloaded function.
   *  Reset the specified register
   */
  public void reset(String reg) {
    int saved = Integer.parseInt(reg.substring(1, reg.length()));
    this.registers[saved] = false;
  }

  // Reset all registers except r0 and r1
  public void preserve() {
    for (int i = 2; i < this.registers.length; i++) {
      this.registers[i] = false; // reset all to false for 'unused'
    }
  }

  public void setInUse() {
    this.registers[this.current] = true;
  }

  public String available() {
    for (int i = 0; i < this.registers.length; i++) {
      if (!this.registers[i]) {
        this.current = i;
        return "r" + i;
      }
    }
    return "full";
  }

  public int getBranchIndex() {
    return this.branch_index;
  }

  public void incBranchIndex() {
    this.branch_index++;
  }

  public int getErrorIndex() {
    return this.error_index;
  }

  public void incErrorIndex() {
    this.error_index++;
  }

  /* Checks if the specified register is currently pushed onto the stack
   */
  public boolean isPushed(String reg_name) {
    int reg = Integer.parseInt(reg_name.substring(1, reg_name.length()));
    return this.stack[reg];
  }

  /* Do we currently have registers pushed onto the stack because at some
   * point all registers were full?
   */
  public boolean isFull() {
    return this.full;
  }

  public void setFull() {
    this.full = true;
  }

  /* Push the specified register onto the stack
   */
  public String push(String reg_name) {
    int reg = Integer.parseInt(reg_name.substring(1, reg_name.length()));
    this.stack[reg] = true;
    return "\tpush {" + reg_name + "}\n";
  }

  /* Push all used registers onto the stack. This method is used when all
   * registers will be full, and we need to free up space.
   */
  public String pushUsed() {
    for (int reg = 1; reg <= this.current; reg++) {
      this.stack[reg] = true;
    }
    return "\tpush {r1-r" + this.current + "}\n";
  }

  /* Pop the specified register off of the stack
   */
  public String pop(String reg_name) {
    int reg = Integer.parseInt(reg_name.substring(1, reg_name.length()));
    this.stack[reg] = false;
    return "\tpop {" + reg_name + "}\n";
  }

  /* Restore all registers from the stack except for the one specified
   * by 'keep'.
   */
  public String popAll(String keep) {
    String code = "";
    String reg_name = "";
    int reg = Integer.parseInt(keep.substring(1, keep.length()));
    for (int i = 0; i < this.stack.length; i++) {
      if (this.stack[i] && i != reg) {
        this.stack[i] = false;
        reg_name = "r" + i;
        code += "\tpop {" + reg_name + "}\n";
      }
    }
    return code;
  }
  /* Overloaded function.
   * Restore all registers from the stack. This is only used for
   * restoring registers when an error is detected and we need to exit.
   */
  public String popAll() {
    String code = "";
    String reg_name = "";
    for (int i = 0; i < this.stack.length; i++) {
      if (this.stack[i]) {
        reg_name = "r" + i;
        code += "\tpop {" + reg_name + "}\n";
      }
    }
    return code;
  }
}
