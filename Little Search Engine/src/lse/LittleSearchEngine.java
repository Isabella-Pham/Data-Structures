package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException 
	 */
	private HashSet<String> nwHashSet() throws FileNotFoundException{
		noiseWords = new HashSet<String>(143,2.0f);
		Scanner sc = new Scanner(new File("noisewords.txt"));
		String word = "";
		while(sc.hasNext()){
			word = sc.next();
			noiseWords.add(word);
		}
		return noiseWords;
	}
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		if(docFile == null) {
			throw new FileNotFoundException();
		}
		noiseWords = nwHashSet();
		HashMap<String, Occurrence> keywords = new HashMap<String, Occurrence>(1000, 2.0f);
		Scanner sc = new Scanner(new File(docFile));
		String token = "";
		while(sc.hasNext()) {
			token = sc.next();
			String key = getKeyword(token);
			if(key == null){
				continue;
			}
			if(keywords.containsKey(key)){
				//increment occurrence
				keywords.get(key).frequency += 1;
			}else{
				//add to hashmap
				Occurrence occ = new Occurrence(docFile, 1);
				keywords.put(key, occ);
			}	
		}
		return keywords;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		for(String key:kws.keySet()){
			Occurrence toAdd = kws.get(key);
			if(keywordsIndex.containsKey(key)) {
				keywordsIndex.get(key).add(toAdd);
				insertLastOccurrence(keywordsIndex.get(key));
			}else{
				ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
				occs.add(toAdd);
				keywordsIndex.put(key,occs);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word){
		if(word == null) {
			return null;
		}
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(Character.isLetter(c)){
				str.append(c);
			}else{
				if(!isPunctuation(c)){
					return null;
				}
				if(i < word.length()-1 && Character.isLetter(word.charAt(i+1))) {
					return null;
				}
			}
		}
		String ret = str.toString().toLowerCase();
		if(noiseWords.contains(ret)) {
			return null;
		}
		return ret;
	}
	private boolean isPunctuation(char c){
		if(c == '.' || c == ',' || c == '?' || c == ':' || c == ';' || c == '!'){
			return true;
		}
		return false;
	}
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		if(occs.size() == 1){
			return null;
		}
		ArrayList<Integer> mids = new ArrayList<Integer>();
		int lo = 0;
		Occurrence item = occs.get(occs.size()-1);
		occs.remove(occs.size()-1);
		int hi = occs.size()-1;
		int mid = -1;
		while(lo <= hi){
			mid = (lo+hi)/2;
			mids.add(mid);
			int freq = occs.get(mid).frequency;
			if(freq == item.frequency) {
				break;
			}else if(freq < item.frequency){
				hi = mid-1;
			}else{
				lo = mid+1;
			}
		}
		occs.add(mid,item);
		if(occs.get(mid).frequency < occs.get(mid+1).frequency){
			Occurrence temp = occs.get(mid);
			occs.set(mid, occs.get(mid+1));
			occs.set(mid+1, temp);
		}
		return mids;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) { //NEED TO FIX
		ArrayList<Occurrence> top = new ArrayList<Occurrence>();
		//will kw1 or kw2 have punctuation or uppercase letters?
		ArrayList<Occurrence> oc1 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> oc2 = new ArrayList<Occurrence>();
		if(keywordsIndex.containsKey(kw1)){
			kw1 = getKeyword(kw1);
			oc1 = keywordsIndex.get(kw1);
		}
		if(keywordsIndex.containsKey(kw2)) {
			kw2 = getKeyword(kw2);
			oc2 = keywordsIndex.get(kw2);
		}
		if(!(oc2.isEmpty())) {
			for(int i = 0; i < oc2.size(); i++){
				Occurrence curr = oc2.get(i);
				top = removeDupes(top);
				if(top.size() == 0){
					top.add(curr);
				}else if(top.size() < 5){
					for(int j = 0; j < 5; j++){
						if(j < top.size() && curr.frequency > top.get(j).frequency){
							top.add(j, curr);
							break;
						}else if(j > top.size()-1){
							top.add(curr);
							break;
						}
					}
				}else{
					for(int j = 0; j < 5; j++){
						if(curr.frequency > top.get(j).frequency){
							top.add(j, curr);
							top.remove(4);
							break;
						}
					}
				}
			}
		}
		top = removeDupes(top);
		if(!(oc1.isEmpty())){
			for(int i = 0; i < oc1.size(); i++){
				Occurrence curr = oc1.get(i);
				top = removeDupes(top);
				if(top.size() == 0){
					top.add(curr);
				}else if(top.size() < 5){
					for(int j = 0; j < 5; j++){
						if(j < top.size() && curr.frequency >= top.get(j).frequency){
							top.add(j, curr);
							break;
						}else if(j > top.size()-1){
							top.add(curr);
							break;
						}
					}
				}else{
					for(int j = 0; j < 5; j++){
						if(curr.frequency >= top.get(j).frequency){
							top.add(j, curr);
							top.remove(4);
							break;
						}
					}
				}
			}

		}
		top = removeDupes(top);
		ArrayList<String> ret = new ArrayList<String>();
		for(int i = 0; i < top.size(); i++){
			ret.add(top.get(i).document);
		}
		return ret;
	}
	private ArrayList<Occurrence> removeDupes(ArrayList<Occurrence> occs){
		if(occs == null){
			return null;
		}
		ArrayList<String> docs = new ArrayList<String>();
		for(int i = 0; i < occs.size(); i++) {
			Occurrence oc = occs.get(i);
			if(docs.contains(oc.document)){
				occs.remove(i);
			}else {
				docs.add(oc.document);
			}
		}
		return occs;
	}
}
