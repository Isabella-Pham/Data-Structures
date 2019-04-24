package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.*;


/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		PartialTreeList ret = new PartialTreeList();
		for(int i = 0; i < graph.vertices.length; i++) {
			Vertex vx = graph.vertices[i];
			PartialTree pt = new PartialTree(vx);
			for(Vertex.Neighbor ptr = vx.neighbors; ptr != null; ptr = ptr.next){ //Vertex.Neighbor is a linked list
				Arc arc = new Arc(vx, ptr.vertex, ptr.weight);
				pt.getArcs().insert(arc);
			}
			ret.append(pt);
		}
		return ret;
	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		ArrayList<Arc> ret = new ArrayList<>();
		while(ptlist.size() != 1){
			//3. Remove the first partial tree PTX from L. Let PQX be PTX's priority queue.
			PartialTree ptx = ptlist.remove();
			MinHeap<Arc> pqx = ptx.getArcs(); 
			//4. Remove the highest-priority arc from PQX. Say this arc is a. Let v1 and v2 be the two vertices connected by a, where v1 belongs to PTX
			Arc a = pqx.deleteMin();
			//5. If v2 also belongs to PTX, go back to Step 4 and pick the next highest priority arc, otherwise continue to the next step.
			while(contains(ptx, a.getv2())){
				a = pqx.deleteMin();
			}
			//6. Report a - this is a component of the minimum spanning tree.
			ret.add(a);
			//7. Find the partial tree PTY to which v2 belongs. Remove PTY from the partial tree list L. Let PQY be PTY's priority queue.
			PartialTree pty = ptlist.removeTreeContaining(a.getv2());
			//8. Combine PTX and PTY. This includes merging the priority queues PQX and PQY into a single priority queue. Append the resulting tree to the end of L.
			ptx.getRoot().neighbors.next = pty.getRoot().neighbors;
			/*when doing ptx.toString() it does not list all the vertices
			 * System.out.println(ptx.toString());
			System.out.println(pty.toString());
			System.out.println();*/
			ptx.merge(pty);
			ptlist.append(ptx);
		}
		return ret;
		
	}
	/*private static boolean contains(Vertex root, Vertex v2){
		for(Vertex ptr = root; ptr != null; ptr = ptr.neighbors.next.vertex) {
			if(v2.name.equals(ptr.name)) {
				return true;
			}
		}
		return false;
	}*/
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    		if(rear == null) {
    			throw new NoSuchElementException("Tree List is Empty");
    		}
    		PartialTree ret = null;
    		Node prev = rear;
    		Node ptr = rear.next;
    		do{
    			//System.out.println(ptr.tree.toString());
    			//System.out.println();
    			ret =  ptr.tree;
    			if(contains(ret, vertex)){
    				size--;
    				if(size == 0) {
    					rear = null; //CLL is empty
    				}else{
  						if(ptr == rear) {
  							rear = prev; 
  						}
  						prev.next = ptr.next;    
   					}	
    				break;
    			}
    			prev = ptr;
    			ptr = ptr.next;
    		}while(ptr != rear.next);
    		if(ret == null) {
    			throw new NoSuchElementException("Tree is not in list");
    		}
    		return ret;
     }
    private static boolean contains(PartialTree pt, Vertex vertex) {
    	Vertex v = vertex;
    	while(v.parent != v) {
    		v = v.parent;
    	}
    	return v == pt.getRoot();
    }
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


