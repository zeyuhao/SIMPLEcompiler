/*
Zeyu Hao
zhao7@jhu.edu
*/

public class RegisterDescriptor {
  private boolean[] registers;
  private int current; // index of most recent available register
  private int branch_index; // index to create unique branch labels
                          // for If, Repeat, While instructions

  public RegisterDescriptor() {
    // r0 - r12 can be used freely
    this.registers = new boolean[13];
    this.current = 0;
    this.branch_index = 0;
    this.reset();
  }

  public void reset() {
    for (int i = 0; i < this.registers.length; i++) {
      this.registers[i] = false; // reset all to false for 'unused'
    }
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

  private void setCurrent(int index) {
    this.current = index;
  }

  public String available() {
    for (int i = 0; i < this.registers.length; i++) {
      if (!this.registers[i]) {
        this.setCurrent(i);
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
}
