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
		Graph graph = new Graph("graph2.txt");
		pst = pst.initialize(graph);
		//printTree(pst);
		//System.out.println(pst.execute(pst));
		pst.remove();
		Vertex v = graph.vertices[2];
		
		/*PartialTree tmp = pst.remove();
		tmp.merge(pst.remove());
		pst.append(tmp);*/
		PartialTree removed = pst.removeTreeContaining(v);
		
		//System.out.println(removed.toString());
		
		Vertex v1 = graph.vertices[1];
		pst.removeTreeContaining(v1);
		
		Vertex v2 = graph.vertices[3];
		removed = pst.removeTreeContaining(v2);

		System.out.println(removed.toString());
		
		printTree(pst);
		
	}
	public static void printTree(PartialTreeList pst) {
		System.out.println();
		Iterator<PartialTree> it = pst.iterator();
		while(it.hasNext()) {
			PartialTree curr = it.next();
			System.out.println(curr.toString());
		}	
	}
	public static void printLL(PartialTreeList.Node n) {
		if(n == null) {
			System.out.println(n);
			return;
		}
		PartialTreeList.Node ptr = n.next;
		do {
			System.out.print(ptr.tree.getRoot() + " ");
			ptr = ptr.next;
		}while(ptr != n.next);
		return;
	}

}
