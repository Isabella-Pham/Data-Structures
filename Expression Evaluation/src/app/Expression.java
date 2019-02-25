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
    	if(!expr.contains("[") && !expr.contains("]")){
			return simplify(expr);
		}else{
			while(expr.contains("[")){	//must account for arrays inside of arrays	
				expr = solveArray(expr, arrays);
			}	
			return simplify(expr);
		}
    } 
    //a BUNCH of helper methods below 
    public static String solveArray(String expr, ArrayList<Array> arrays){ //simplifies expressions within array brackets & insert value for the arrays
    	StringTokenizer st = new StringTokenizer(expr,"*+-/()]0123456789");
    	String token = "";
    	int nameLength = 0;
    	while(st.hasMoreTokens()) {
    		token = st.nextToken();
    		if(token.contains("[")){
    			nameLength = token.length()-1;
    			break;
    		}
    	}
    	int nameIndex = expr.indexOf('[')-nameLength; //the length of the name of the array
    	String name = expr.substring(nameIndex, expr.indexOf('[')); //the name of the array
    	String wholeArr = expr.substring(nameIndex,expr.indexOf(']')+1); //includes name[content]
    	String expression = wholeArr.substring(wholeArr.indexOf('[')+1,wholeArr.indexOf(']'));
    	int index = (int) simplify(expression);
    	String value = arrValue(name, index, arrays);
    	expr = expr.substring(0,nameIndex) + value + expr.substring(nameIndex+wholeArr.length());
    	return expr;
    }
    private static float simplify(String expr){ //takes in expressions with NO ARRAYS, PASSED TEST CASES :))))
    	Stack<Float> nums = new Stack<Float>();
    	Stack<Character> ops = new Stack<Character>(); 
    	StringTokenizer st = new StringTokenizer(expr, "*+-/()"); 
    	String token = "";
    	for(int i = 0; i < expr.length(); i++) {
    		char tmp = expr.charAt(i);
    		if(tmp >= '0' && tmp <= '9') {
    			token=st.nextToken();
    			nums.push(Float.parseFloat(token));
    			i += token.length()-1;
    		}else if(tmp == '('){ 
    			ops.push(tmp);
    		}else if(tmp == ')'){
    			while(ops.peek() != '('){
    				float num1 = nums.pop();
    				float num2 = nums.pop();
    				nums.push(doMath(ops.pop(), num1, num2));
    			}
    			ops.pop();
    		}else if(tmp == '+' || tmp == '-' || tmp == '*' || tmp == '/') {
    			while(!ops.isEmpty() && order(tmp, ops.peek())){
    				float num1 = nums.pop();
    				float num2 = nums.pop();
    				nums.push(doMath(ops.pop(), num1, num2));
    			}
    			ops.push(tmp);
    		}
    	}
    	while(!ops.isEmpty()){
    		nums.push(doMath(ops.pop(), nums.pop(), nums.pop()));
    	}
    	return nums.pop();
    }
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
    private static String arrValue(String name, int index, ArrayList<Array> arrays) {
    	for(int i = 0; i < arrays.size(); i++) {
    		if(arrays.get(i).name.equals(name)) {
    			int[] arr = arrays.get(i).values;
    			return Integer.toString(arr[index]);
    		}
    	}
    	return "0";
    }
    private static String insertValue(String expr, ArrayList<Variable> vars){
    	StringTokenizer st = new StringTokenizer(expr, delims);
    	String name = "";
    	while(st.hasMoreTokens()){
    		int index = 0;
    		name = st.nextToken();
    		index = vars.indexOf(name);
    		for(int i = 0; i < vars.size(); i++) {
    			if(vars.get(i).name.equals(name)){
    				index = i;
    				break;
    			}
    			index = -1;
    		}
    		if(index != -1) {
    			int value = vars.get(index).value;
    			expr = expr.replaceAll(name, Integer.toString(value));
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