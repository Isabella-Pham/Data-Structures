package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]"; 
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */

    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays){ //passed test cases
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	expr = expr.replaceAll(" ","");
    	String delim = " \t*+-/()]0123456789"; //removed [ delim, added integers
    	StringTokenizer st = new StringTokenizer(expr, delim);
    	String token = "";
    	while(st.hasMoreTokens()){
    		token = st.nextToken();
    		if(token.indexOf('[') == -1){
    			Variable newVar = new Variable(token);
    			if(notMultipleV(vars, token)){
    				vars.add(newVar);
    			}
    	      }
    		while(token.indexOf('[') != -1){
    			String arrName = token.substring(0,token.indexOf('['));
    			token = token.substring(token.indexOf('[')+1);
    			Array newArr = new Array(arrName);
    			if(notMultipleA(arrays,arrName)) {
    				arrays.add(newArr);
    			}	
    		}
    	}
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	expr = expr.replaceAll(" ", "");
    	expr = insertValue(expr, vars);
    	System.out.println(expr);
    	Stack<Float> nums = new Stack<Float>();
    	Stack<Character> ops = new Stack<Character>();
    	Stack<String> arrNames = new Stack<String>();
     	StringTokenizer st = new StringTokenizer(expr, "*+-/()[]"); 
    	String token = "";
    	for(int i = 0; i < expr.length(); i++) {
    		char tmp = expr.charAt(i);
    		if(tmp >= '0' && tmp <= '9') {
    			token = st.nextToken();
    			nums.push(Float.parseFloat(token));
    			i += token.length()-1;
    		}else if(Character.isLetter(tmp)){
    			token = st.nextToken();
    			arrNames.push(token);
    			i += token.length()-1;
    		}else if(tmp == '(' || tmp == '['){ 
    			ops.push(tmp);
    		}else if(tmp == ']'){
    			while(ops.peek() != '['){		
    				float num1 = nums.pop();
    				float num2 = nums.pop();
    				nums.push(doMath(ops.pop(), num1, num2));
    			}
    			nums.push(arrValue(arrNames.pop(), nums.pop().intValue(), arrays));
    			ops.pop();
    		}else if(tmp == ')'){
    			while(ops.peek() != '('){
    				float num1 = nums.pop();
    				float num2 = nums.pop();
    				nums.push(doMath(ops.pop(), num1, num2));
    			}
    			ops.pop();
    		}else if(tmp == '+' || tmp == '-' || tmp == '*' || tmp == '/') {
    			while(!ops.isEmpty() && order(tmp, ops.peek()) && ops.peek() != '['){
    				float num1 = nums.pop();
    				float num2 = nums.pop();
    				nums.push(doMath(ops.pop(), num1, num2));
    			}
    			ops.push(tmp);
    		}
    		ops.print();
    		nums.print();
    		arrNames.print();
    	}
    	while(!ops.isEmpty()){
    		nums.push(doMath(ops.pop(), nums.pop(), nums.pop()));
    	}
    	return nums.pop();
    } 
    //a BUNCH of helper methods below 
    private static float doMath(char op, float num1, float num2){
    	if (op == '+'){//System.out.println(Float.toString(num1) + "+" + Float.toString(num2)); 
    		return num1+num2;
    	}
        if (op == '-'){//System.out.println(Float.toString(num2) + "-" + Float.toString(num1)); 
        	return num2-num1;
        }
        if (op == '/'){//System.out.println(Float.toString(num2) + "/" + Float.toString(num1)); 
        	return num2/num1;
        }
        if (op == '*'){//System.out.println(Float.toString(num1) + "*" + Float.toString(num2)); 
        	return num1*num2;
        }
        return 0;
    }
    public static boolean order(char op1, char op2){ //returns true if op2 has higher order than op1
    	if(op2 == '(' || op2 == ')') {
    		return false;
    	}
    	return !((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'));
    }
    private static float arrValue(String name, int index, ArrayList<Array> arrays) {
    	for(int i = 0; i < arrays.size(); i++) {
    		if(arrays.get(i).name.equals(name)) {
    			int[] arr = arrays.get(i).values;
    			return (float)arr[index];
    		}
    	}
    	return (float)0.0;
    }
    private static String insertValue(String expr, ArrayList<Variable> vars){
    	StringTokenizer st = new StringTokenizer(expr, delims);
    	String name = "";
    	while(st.hasMoreTokens()){
    		name = st.nextToken();
    		for(int i = 0; i < vars.size(); i++) {
    			if(vars.get(i).name.equals(name)){
    				int value = vars.get(i).value;
    				expr = expr.replaceFirst(name, Integer.toString(value));
    				break;
    			}
    		}
    	}
    	return expr;
    }
    private static boolean notMultipleV(ArrayList<Variable> arrayList, String token) {
    	for(int i = 0; i < arrayList.size(); i++){
    		String name = arrayList.get(i).name;
    		if(name.equals(token)){
    			return false;
    		}
    	}
    	return true;
    }
    private static boolean notMultipleA(ArrayList<Array> arrayList, String token) {
    	for(int i = 0; i < arrayList.size(); i++){
    		String name = arrayList.get(i).name;
    		if(name.equals(token)){
    			return false;
    		}
    	}
    	return true;
    }
}