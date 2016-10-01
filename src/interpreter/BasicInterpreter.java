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
  
 //  TODO: Handle infinite loops
  int registerCapacity = 5;
	ArrayList <Integer> registers = new ArrayList<Integer>(Collections.nCopies(registerCapacity, 0));
	Stack <StackFrame> stack = new Stack <StackFrame>();
	int registerPointer = 0;
	
	public boolean run(String program) {
		Scanner scan = new Scanner(System.in);
	  char[] commands = program.toCharArray();
		
		int index = 0;
		while (index < commands.length) {
		  boolean read = true;
			char currentInstruction = commands[index];
			if (this.stack.size() > 0) {
			  if (index == this.stack.peek().startIndex) {
			    if (this.registers.get(this.registerPointer) == 0) {
			      index = this.stack.pop().endIndex; //jump to the end
			      index++; //proceed
			    } else {
			      index++; //proceed 
			    }
			  } else if (index == this.stack.peek().endIndex) {
			    index = this.stack.peek().startIndex;
			    read = false;
			  }
			}
//			System.out.println(index);
//			System.out.println(commands[index]);
			if (read && index < commands.length) {
			  currentInstruction = commands[index];
  			switch (currentInstruction) {
  			// There are 8 different instructions in BrainFuck
        // + (increment), - (decrement) 
        // > (next register), < (previous register) 
        // [ (loop start), ] (loop end)
        // , (read a token from input), . (output current value of cell as character)
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
  				case ',':
  				  read(scan);
  				  break;
  				case '.':
  				  print();
  				  break;
  				case '[':
  				  if (!loop(index, commands)) {
  				    System.out.println("Program Error");
  				    return false;
  				  };
  				  index--;
  				  break;
  			}
  			index++;	
  		}
		}
		return true;
	}
	
	//We follow convention and wrap the integers around keeping within 0 - 255
	public void increment(int index) {
		int prevVal = registers.get(index);
		if (prevVal == 255) {
			this.registers.set(index, 0);
		} else {
			this.registers.set(index, prevVal + 1);
		}
	}
	
	public void decrement(int index) {
		int prevVal = registers.get(index);	
		if (prevVal == 0) {
			this.registers.set(index, 255);
		} else {
			this.registers.set(index, prevVal - 1);
		}
	}
	
	//We follow convention and make number of registers unbounded, but wrap around when shifting left
	public void shiftLeft() {
//	  System.out.println("Shift Left!");
		int currentPointer = this.registerPointer;
		
		if (currentPointer == 0) 
		  this.registerPointer = this.registerCapacity-1;
		else 
		  this.registerPointer -= 1;
	}
	
	public void shiftRight() {
//	  System.out.println("Shift right!");
		int currentPointer = this.registerPointer;
	  if (currentPointer == this.registerCapacity-1) {
	    ArrayList <Integer> extend = new ArrayList<Integer>(Collections.nCopies(registerCapacity, 0));
	    this.registers.addAll(extend);
	    this.registerCapacity *= 2;
	  } 
	  this.registerPointer += 1;
	}
	
	//Read in one character and place it in the register
	public void read(Scanner scan) {
	  System.out.println("Please input a character: ");
	  int value = scan.nextLine().charAt(0);
	  this.registers.set(this.registerPointer, value);
	}
	
	public void print() {
	  char letter = (char) (this.registers.get(this.registerPointer).intValue());
	  System.out.println(letter);
	}
	
	public void printRegisters() {
	  System.out.println(this.registers.toString());
	}
	
	public boolean loop(int index, char[] commands) {
//	  System.out.println("Loop! at " + index);
//	  if (this.registers.get(this.registerPointer) > 0) {
	    int counter = 1;
	    int start = index;
	    int end = -1;
	    index++;
	    while(index < commands.length) {
//	      System.out.println("index: " + index + ", counter: " + counter);
	      if (commands[index] == '[') {
          counter++;
        } else if (commands[index] == ']') {
          end = index;
          counter--;
        }
	      if (counter == 0) {
//	        System.out.println(start + "," + end);
	        this.stack.push(new StackFrame(start, end));
	        return true;
	      } else if (counter < 0) {
//	        System.out.println(index);
//	        System.out.println(counter);
//	        System.out.println("Oops!");
	        return false;
	      } 
	      index++;
	    }
//	  }
	  return true;
	}
	
	public static void main(String[] args) {
	  Scanner scan = new Scanner (System.in);
	  
		BasicInterpreter test = new BasicInterpreter();
//		test.run(">>>>>>++");
//		test.run(",,.");
//		test.run("++[->+<]");
//		test.run("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.");
		test.printRegisters();
		test.run(scan.nextLine());
		test.printRegisters();
	}
}
