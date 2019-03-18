package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build(){ //passed test cases
		if(sc == null){
			return;
		}
		root = new TagNode("html", null, null);
		TagNode leaf = root;
		String line = sc.nextLine();
		Stack<TagNode> tags = new Stack<TagNode>();
		while(sc.hasNextLine()){
			line = sc.nextLine();
			StringTokenizer st = new StringTokenizer(line, "</>");
			String token = "";
			for(int i = 0; i < line.length(); i++){		
				char tmp = line.charAt(i);
				token = st.nextToken();
				if(tmp == '<' && line.charAt(i+1) == '/'){
					while(!tags.peek().tag.equals(token)){
						tags.pop();
					}
					leaf = tags.pop();
					i += 2+token.length();
					continue; 
				}else if(tmp == '<'){
					if(leaf.firstChild == null && isTag(leaf.tag)){
						leaf.firstChild = new TagNode(token, null, null);
						tags.push(leaf);
						leaf = leaf.firstChild;
					}else{
						leaf.sibling = new TagNode(token, null, null);
						tags.push(leaf);
						leaf = leaf.sibling;
					}
					i += 1+token.length();
				}else{
					if(leaf.firstChild == null){
						leaf.firstChild = new TagNode(token, null, null);
						tags.push(leaf);
						leaf = leaf.firstChild;
					}else{
						leaf.sibling = new TagNode(token, null, null);
						tags.push(leaf);
						leaf = leaf.sibling;
					}
					i += token.length()-1;
				}
			}
		}
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) { //passed test cases
		/** COMPLETE THIS METHOD **/
		root = replaceTag(root, oldTag, newTag);
		
	}
	//helper method to use recursion w/ below
	private TagNode replaceTag(TagNode curr, String oldTag, String newTag) {
		if(oldTag.equals(curr.tag)){
			curr.tag = newTag;
		}
		if(curr.sibling != null) {
			curr.sibling = replaceTag(curr.sibling, oldTag, newTag);
		}
		if(curr.firstChild != null) {
			curr.firstChild = replaceTag(curr.firstChild, oldTag, newTag);
		}
		return curr;
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) { //passed test cases
		if(row < 1){
			return;
		}
		boldRow(root, root.firstChild, row);
	}
	//helper method to use recursion w/ below
	private void boldRow(TagNode prev, TagNode curr, int row){ 
		if(curr == null){
			return;
		}
		if(row == 0 && curr.firstChild == null){
			prev.firstChild = new TagNode("b",curr,null);
		}else if(curr.tag.equals("tr")){
			row--;
		}
		boldRow(curr, curr.firstChild, row);
		boldRow(curr, curr.sibling, row);
	}
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag){ //passed test cases
		if(root == null){
			return;
		}
		while(hasTag(root, tag)){
			removeTag(root, root.firstChild, tag);
		}
	}
	//helper method to use recursion w/ below
	private void removeTag(TagNode prev, TagNode curr, String tag){
		if(prev == null || curr == null){
			return;
		}
		if(tag.equals(curr.tag)){
			if(tag.equals("ol") || tag.equals("ul")){
				TagNode ptr = curr.firstChild;
				while(ptr != null){
					if(ptr.tag.equals("li")) {
						ptr.tag = "p";
					}
					ptr = ptr.sibling;
				}
			}
			if(prev.firstChild == curr){ //node to be deleted is child of previous
				prev.firstChild = curr.firstChild;
				//need to make last sibling of curr point to sibling of prev
				TagNode ptr = curr.firstChild;
				while(ptr.sibling != null){
					ptr = ptr.sibling;
				}
				ptr.sibling = curr.sibling;	
			}else if(prev.sibling == curr){ //node to be deleted is sibling of previous
				TagNode ptr = curr.firstChild;
				while(ptr.sibling != null){
					ptr = ptr.sibling; 
				}
				//the previous sibling becomes the last sibling of the upgraded node
				ptr.sibling = curr.sibling;
				prev.sibling = curr.firstChild;
			}
			return; 
		}
		prev = curr;
		removeTag(prev, prev.firstChild, tag);
		removeTag(prev, prev.sibling, tag);
	}
	private boolean hasTag(TagNode curr, String tag) {
		if(curr == null){
			return false;
		}
		if(tag.equals(curr.tag)) {
			return true;
		}
		return hasTag(curr.firstChild, tag) || hasTag(curr.sibling, tag);
		
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		
	}
	public void addTag(TagNode prev, TagNode curr, String word, String tag){
		if(prev == null || curr == null){
			return; 
		}
		if(matches(word,curr.tag)){
			if(prev.firstChild == curr) {
				
			}else if(prev.sibling == curr) {
				
			}
		}
		prev = curr;
		addTag(prev, prev.firstChild, word, tag);
		addTag(prev, prev.sibling, word, tag);
	}
	private boolean matches(String word, String nodeTag){ //returns whether or not the tag is the same as the word when not including punctuation and case
		//passed test cases
		nodeTag = nodeTag.toLowerCase();
		word = word.toLowerCase();
		char last = nodeTag.charAt(nodeTag.length()-1);
		if(last == '!' || last == '?' || last == '.' || last == ';' || last == ':') {
			nodeTag = nodeTag.substring(0,nodeTag.length()-1);
		}
		if(nodeTag.equals(word)){
			return true;
		}
		return false;
	}
	private boolean isTag(String tag){
		String[] tags = {"html","body","p","em", "b","table","tr","td","ol","ul","li"};
		for(int i = 0; i < tags.length; i++) {
			if(tag.equals(tags[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
