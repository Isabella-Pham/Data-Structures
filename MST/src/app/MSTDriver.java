/**
 * 
 */
package app;
import java.io.IOException;
import java.util.Iterator;

import structures.*;

/**
 * @author Isabella Pham
 *
 */
public class MSTDriver {
	public static void main(String[] args) throws IOException {
		PartialTreeList pst = new PartialTreeList();
		Graph graph = new Graph("graph3.txt");
		pst = pst.initialize(graph);
		printTree(pst);
		System.out.println();
		//printTree(pst);
		System.out.println("Arcs: "+ pst.execute(pst));
	}
	public static void printTree(PartialTreeList pst) {
		Iterator<PartialTree> it = pst.iterator();
		while(it.hasNext()) {
			PartialTree curr = it.next();
			System.out.println(curr.toString());
		}
	}

}
