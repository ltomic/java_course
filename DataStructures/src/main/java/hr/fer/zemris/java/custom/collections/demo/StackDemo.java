package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.collections.EmptyStackException;

/**
 * Program receives an postfix expression consisting of integers and operators
 * +, -, *, /, % enclosed in qoutation marks through command-line and evaluates it.
 * Decimal numbers are not supported(example 3 / 2 = 1).
 * @author ltomic
 * @versio 1.0
 */

public class StackDemo {
	
	/**
	 * Method called at the beginning of the program.
	 * Expression to evaluate is given as first argument.
	 * @param args expression to evaluate
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Wrong number of arguments. Try again.");
		}
		int value;
		try {
			value = evaluateExpression(args[0]);
		} catch (IllegalArgumentException|EmptyStackException ex) {
			System.out.println("Invalid expression given");
			return;
		}
		System.out.println("Expression evaluates to " + value);
	}
	
	/**
	 * Return true if given string represents one of the following
	 * operations('+', '-', '*', '/', '%')
	 * @param s expression to be check
	 * @return true if string is an operand
	 */
	private static boolean isOperation(String s) {
		if (s.length() > 1) return false;
		char c = s.charAt(0);
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
	}
	
	/**
	 * Returns a value evaluated as <code>a op b</code>
	 * @param a first operand
	 * @param b second operand
	 * @param op binary operation to perform on a and b
	 * @return value of <code>a op b</code>
	 * @throws IllegalArgumentException if expression is invalid or division by zero
	 * 									occurs
	 */
	private static int applyOperation(int a, int b, String op) {
		if (op.equals("+")) return a + b;
		if (op.equals("-")) return a - b;
		if (op.equals("*")) return a * b;
		if (op.equals("/")) {
			if (b == 0) {
				throw new IllegalArgumentException("Error! Division by zero!");
			}
			return a/b;
		}
		if (op.equals("%")) {
			if (b == 0) {
				throw new IllegalArgumentException("Error! Division by zero in modular operation!");
			}
			return a % b;
		}
		throw new IllegalArgumentException("Error! Invalid expression!");
	}
	
	/**
	 * Method evaluates a postfix expression (as described in doc of the class)
	 * @param expression postfix to evaluate
	 * @return value of the evaluated expression
	 */
	public static int evaluateExpression(String expression) {
		if (expression.isEmpty()) throw new IllegalArgumentException("Empty string given.");
		ObjectStack stack = new ObjectStack();
		
		String[] splitted = expression.split("\\s+");
		for (String i: splitted) {
			int value;
			if (isOperation(i) == false) {
				try {
					value = Integer.parseInt(i);
				} catch (NumberFormatException ex) {
					throw new IllegalArgumentException("Invalid expression given.");
				}
			} else {	
				int b = (int)stack.pop();
				int a = (int)stack.pop();
				
				value = applyOperation(a, b, i);
			}
			stack.push(value);
		}
		
		if (stack.size() != 1) throw new IllegalArgumentException("Error! Invalid expression!");
		return (int)stack.pop();
	}
}
