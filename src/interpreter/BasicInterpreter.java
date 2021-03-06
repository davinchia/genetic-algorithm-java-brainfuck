package interpreter;

import java.util.*;

public class BasicInterpreter {
  private class StackFrame {
    int startIndex;
    int endIndex;

    public StackFrame(int start, int end) {
      this.startIndex = start;
      this.endIndex = end;
    }
  }

  int  registerCapacity = 5;
  int  registerPointer  = 0;
  double timeLimit        = 20 * 1000;
  // ArrayList <Integer> registers = new
  // ArrayList<Integer>(Collections.nCopies(registerCapacity, 0));
  int[]              registers = new int[60000];
  Stack<StackFrame>  stack     = new Stack<StackFrame>();
  ArrayList<String>  output    = new ArrayList<String>();
  ArrayList<Integer> intOutput = new ArrayList<Integer>();

  // GETTERS/SETTERS
  public void setTimeLimit(double limit) {
    this.timeLimit = limit * 1000000;
  }

  public String getOutput() {
    StringBuilder output = new StringBuilder();

    int limit = Math.min(20, this.output.size());
    
    for (int i = 0; i < limit; i++) {
      output.append(this.output.get(i));
    }

    return output.toString();
  }

  public void getIntOutput() {
    for (int x : this.intOutput) {
      System.out.print(x);
      System.out.println(" ");
    }
    System.out.println();
  }

  public void reset() {
    this.output.clear();
    this.registerCapacity = 5;
    this.registerPointer = 0;
    this.registers = new int[60000];
    this.stack.clear();
  }

  // MAIN METHODS
  public boolean run(String program) {
    Scanner scan = new Scanner(System.in);
    char[] commands = program.toCharArray();
    long begin = System.nanoTime();

    int index = 0;
    int counter = 0;

    while (index < commands.length) {
      if (System.nanoTime() - begin <= this.timeLimit) {
        // System.out.println(System.currentTimeMillis() - begin);
        boolean read = true;
        char currentInstruction = commands[index];
        if (this.stack.size() > 0) {
          if (index == this.stack.peek().startIndex) {
            // if (this.registers.get(this.registerPointer) == 0) {
            if (this.registers[this.registerPointer] == 0) {
              index = this.stack.pop().endIndex; // jump to the end
              index++; // proceed
            } else {
              index++; // proceed
            }
          } else if (index == this.stack.peek().endIndex) {
            if (this.registers[this.registerPointer] != 0) {
              index = this.stack.peek().startIndex;
              read = false;
            } else {
              this.stack.pop();
              index++;
            }
          }
        }
        // System.out.println(index);
        // System.out.println(commands[index]);
        if (read && index < commands.length) {
          counter++;
          currentInstruction = commands[index];
          switch (currentInstruction) {
          // There are 8 different instructions in BrainFuck
          // + (increment), - (decrement)
          // > (next register), < (previous register)
          // [ (loop start), ] (loop end)
          // , (read a token from input), . (output current value of cell as
          // character)
          case '+':
            increment(this.registerPointer);
            break;
          case '-':
            decrement(this.registerPointer);
            break;
          case '>':
            shiftRight();
            break;
          case '<':
            shiftLeft();
            break;
          // case ',':
          // read(scan);
          // break;
          case '.':
            print();
            break;
          case '[':
            if (!loop(index, commands)) {
               System.out.println("Error in Program");
              return false; // If not balanced, stop.
            }
            ;
            index--;
            break;
          }
          index++;
        }
      } else {
        return false;
      }
    }
    // System.out.println(counter + " instructions executed.");
    // System.out.println("Ran in " + (System.currentTimeMillis() - begin) + "
    // milliseconds");
    return true;
  }

  // We follow convention and wrap the integers around keeping within 0 - 255
  public void increment(int index) {
    // int prevVal = registers.get(index);
    int prevVal = this.registers[index];
    if (prevVal == 255) {
      // this.registers.set(index, 0);
      this.registers[index] = 0;
    } else {
      // this.registers.set(index, prevVal + 1);
      this.registers[index]++;
    }
  }

  public void decrement(int index) {
    int prevVal = this.registers[index];
    if (prevVal == 0) {
      this.registers[index] = 255;
    } else {
      this.registers[index]--;
    }
  }

  // We follow convention and make number of registers unbounded, but wrap
  // around when shifting left
  public void shiftLeft() {
    int currentPointer = this.registerPointer;

    if (currentPointer == 0)
      this.registerPointer = this.registerCapacity - 1;
    else
      this.registerPointer -= 1;
  }

  public void shiftRight() {
    if (this.registerPointer == 59999) {
      this.registerPointer = 0;
    }
    this.registerPointer += 1;
  }

  // Read in one character and place it in the register
  public void read(Scanner scan) {
    System.out.println("Please input a character: ");
    int value = scan.nextLine().charAt(0);
    // this.registers.set(this.registerPointer, value);
    this.registers[this.registerPointer] = value;
  }

  // This saves the output to the output array
  public void print() {
    // char letter = (char)
    // (this.registers.get(this.registerPointer).intValue());
    char letter = (char) this.registers[this.registerPointer];
    this.intOutput.add(this.registers[this.registerPointer]);
    // System.out.println("print: " + this.registers[this.registerPointer]);
    this.output.add(Character.toString(letter));
  }

  public void printRegisters() {
   for (int x: this.registers) {
     System.out.print(x);
     System.out.print(" ");
   }
   System.out.println();
  }

  public boolean loop(int index, char[] commands) {
    // System.out.println("Loop! at " + index);
    // if (this.registers.get(this.registerPointer) > 0) {
    int counter = 1;
    int start = index;
    int end = -1;
    index++;
    while (index < commands.length) {
      // System.out.println("index: " + index + ", counter: " + counter);
      if (commands[index] == '[') {
        counter++;
      } else if (commands[index] == ']') {
        end = index;
        counter--;
      }
      if (counter == 0) {
        // System.out.println(start + "," + end);
        this.stack.push(new StackFrame(start, end));
        return true;
      } else if (counter < 0) {
        // System.out.println(index);
        // System.out.println(counter);
         System.out.println("Invalid Parenthesis.");
        return false;
      }
      index++;
    }
    // }
    return true;
  }

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);

    BasicInterpreter test = new BasicInterpreter();
    // test.run(">>>>>>++");
    // test.run(",,.");
    // test.run("++[->+<]");
    // test.run("+[[>+]>[+>]+]");
    long begin = System.currentTimeMillis();
    test.setTimeLimit(0.5);
    // test.run("++++++++++[>++++÷+++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.");
    // test.run(">.-[-<><<>+++-><[[+,<.+]><>>,.-<]-+<<<[->>],>][[>[.-<+,,<-.>,.<+[>.[>..+]+-]><-,,.-,.+]>-.>]>]+.]-,<]+-[>.]<>++[..>->..,<++,[<][<.>>[>]>>>].>.,<>>.]..");
//     test.run("+++++++++++..<>.[[+[,+..>-]>+-[],.],.,<<<<][.-,>-++><-],-.-+>-+,+<]],]-[-+.-+<[<..]->-[+]],>[[[-]-<+,--><<-+<].<++-.]]++.]-[]][-<..+.><.>[<,].]+,,->,,");
//     test.run("+++++++>][[<.,[<-<-,.-[-<,[]<+-.>]]>-+>[-<+<+<+,,[-+[[].-.+,,-,>,.,>>[.[,<[<.,.>]-]-<[<]]<+<>].-<[-.>+<.+,->-+<.--<-<<+]-][-]].<>->+]]<++><++++.++.[>]");
//     test.run(")++++++++[->-[->-[->-[-]<]<]<]>++++++++[<++++++++++>-]<[>+>+<<-]>-.>-----.>");
//     test.run("+++><+++,++++>]-[,,]]<<<<<<++++++++++++++++++++++++++[++++.]");
//     test.run("+++++++[++++>+,>+<<+.++->,<++][]]<-]-]<>]->.->>-[][,,->]<-.]");
//     test.run("+[+++++-+>++>++-++++++<<]>++.[+.]");
//    test.run("+-+-+>-<[++++>+++++<+<>++]>[-[---.--[[-.++++[+++..].]]]]");
//    test.run("+++++++++<><<,[+][+>-],<-],]+,[<--]<+++]+.+>-<+.<>[,++.,[>,-");
//    test.run("++++>+<++++>,<+++++++++[[+,++.,,]],.,]<,][,,+>,,+>.[>]]>+<.<");
//    test.run(",>><<[>,.,.+<]>-[>-<++<>++>++++<+]+>+[.+.[]<++-.+-],<[[.]<>.");
    test.run("++++><+++[++.++++++],[>[<>-]+,+<,,-,>],]]>]-],[.,<<].>-]<+-,");
    System.out.println("Output: " + test.getOutput());
//    test.printRegisters();
//    test.getIntOutput();
    System.out.println("Time taken: " + (System.currentTimeMillis() - begin));
    // test.printRegisters();
    // test.run(scan.nextLine());
    // test.printRegisters();
  }
}
