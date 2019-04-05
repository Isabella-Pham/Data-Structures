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
	 * @throws IOException 
	 */
	private HashSet<String> nwHashSet() throws IOException {
		noiseWords = new HashSet<String>(143,2.0f);
		FileInputStream f = new FileInputStream("noisewords.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(f));
		String line = br.readLine();
		while(line != null) {
			noiseWords.add(line);
			line = br.readLine();
		}
		f.close();
		return noiseWords;
	}
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		try {
			noiseWords = nwHashSet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, Occurrence> keywords = new HashMap<String, Occurrence>(1000, 2.0f);
		FileInputStream f = new FileInputStream(docFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(f));
		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(line != null) {
			StringTokenizer st = new StringTokenizer(line, " ");
			String token = "";
			while(st.hasMoreTokens()){
				token = st.nextToken();
				String key = getKeyword(token);
				if(keywords.containsKey(key)){
					//increment occurrence
					keywords.get(key).frequency += 1;
				}else {
					//add to hashmap
					Occurrence occ = new Occurrence(docFile, 1);
					keywords.put(key, occ);
				}	
			}
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String name: keywords.keySet()){
			if(name == null) {
				continue;
			}
			String key = name.toString();
			String value = keywords.get(name).toString();
			System.out.println(key + " " + value);
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
		/** COMPLETE THIS METHOD **/
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
		ArrayList<Integer> ret = new ArrayList<Integer>();
		int lo = 0;
		int hi = occs.size()-2;
		Occurrence item = occs.get(occs.size()-1);
		occs.remove(occs.size()-1);
		int mid = -1;
		while(lo < hi){
			mid = (lo+hi)/2;
			int freq = occs.get(mid).frequency;
			if(freq == item.frequency) {
				occs.add(mid,item);
				break;
			}else if(freq < item.frequency) {
				hi = mid-1;
			}else{
				lo = mid+1;
			}
		}
		if(lo > hi){
			occs.add(mid,item);
		}
		return ret;
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
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<Occurrence> top = new ArrayList<Occurrence>();
		ArrayList<Occurrence> oc1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> oc2 = keywordsIndex.get(kw2);
		if(oc1 != null) {
			for(int i = 0; i < oc1.size(); i++){
				Occurrence occ = oc1.get(i);
				if(top.size() == 0){
					top.add(occ);
					continue;
				}
				for(int j = 0; j < top.size(); j++){
					if(occ.frequency > top.get(j).frequency){
							top.add(j,occ);
						if(top.size() > 5) {
							top.remove(top.size()-1);
						}
					}
				}
			}
		}
		if(oc2 != null) {
			for(int i = 0; i < oc2.size(); i++){
				Occurrence occ = oc2.get(i);
				if(top.size() == 0){
					top.add(occ);
					continue;
				}
				for(int j = 0; j < top.size(); j++){
					if(occ.frequency > top.get(j).frequency){
							top.add(j,occ);
						if(top.size() > 5) {
							top.remove(top.size()-1);
						}
					}
				}
			}
		}
		ArrayList<String> ret = new ArrayList<String>();
		for(int i = 0; i < top.size(); i++){
			ret.add(top.get(i).document);
		}
		return ret;
	
	}
}
