package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2){ //passed some test cases, does not work for empty txt document
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		//If either poly1 or poly2 are null
		Node sum = null;
		/*poly1 = zeroPoly(poly1);
		poly2 = zeroPoly(poly2);*/
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		
		while(ptr1 != null && ptr2 != null){
			while(ptr1.term.degree < ptr2.term.degree) {
				sum =  new Node(ptr1.term.coeff, ptr1.term.degree, sum);
				ptr1 = ptr1.next;
				if(ptr1 == null) {
					break;
				}
			}
			if(ptr1 == null) {
				break;
			}
			while(ptr2.term.degree < ptr1.term.degree) {
				sum = new Node(ptr2.term.coeff, ptr2.term.degree, sum);
				ptr2 = ptr2.next;
				if(ptr2 == null){
					break;
				}
			}
			if(ptr2 == null) {
				break;
			}
			while(ptr1.term.degree == ptr2.term.degree) {
				sum = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr2.term.degree, sum);
				ptr1 = ptr1.next;
				ptr2 = ptr2.next; 
				if(ptr1 == null || ptr2 == null) {
					break; 
				}
			}
		}
		if(ptr1 == null){
			while(ptr2 != null) {
				sum = new Node(ptr2.term.coeff, ptr2.term.degree, sum);
				ptr2 = ptr2.next;
			}
		}else if(ptr2 == null){
			while(ptr1 != null){
				sum =  new Node(ptr1.term.coeff, ptr1.term.degree, sum);
				ptr1 = ptr1.next;
			}
		}
		if(isZero(sum)){
			return null; 
		}
		return orderAscending(sum); 
	}
	
	/** 
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node smallProduct = null;
		Node product = null;
		for(Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next) {
			for(Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next) {
				smallProduct = new Node(ptr1.term.coeff*ptr2.term.coeff, ptr1.term.degree + ptr2.term.degree, smallProduct);
			}
			smallProduct = orderAscending(smallProduct);
			product = add(product, smallProduct);
			smallProduct = null;
		}
		return product;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x){ //passed test cases
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node ptr = poly;
		float ans = 0; 
		while(ptr != null) {
			ans += ptr.term.coeff * Math.pow(x, ptr.term.degree);
			ptr = ptr.next;
		}
		return ans;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
	private static Node orderAscending(Node poly){ //if polynomial is descending
		Node ret = null;
		for(Node ptr = poly; ptr != null; ptr = ptr.next) { 
			ret = new Node(ptr.term.coeff, ptr.term.degree, ret);
		}
		return ret;
	}
	private static boolean isZero(Node poly){ 
		for(Node ptr = poly; ptr != null; ptr = ptr.next) {
			if(poly.term.coeff != 0) {
				return false;
			}
		}
		return true;
	}
}
