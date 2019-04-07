package lse;
import java.io.*;
import java.util.*;

public class Driver{
	public static void main(String[] args) throws FileNotFoundException{
		LittleSearchEngine engine = new LittleSearchEngine();
		ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
		/*occs.add(new Occurrence("test.txt", 12));
		occs.add(new Occurrence("test.txt", 8));
		occs.add(new Occurrence("test.txt", 7));
		occs.add(new Occurrence("test.txt", 5));
		occs.add(new Occurrence("test.txt", 3));
		occs.add(new Occurrence("test.txt", 2));
		occs.add(new Occurrence("test.txt", 6));
		ArrayList<Integer> ans = engine.insertLastOccurrence(occs);*/
		HashMap<String, Occurrence> doc1 = engine.loadKeywordsFromDocument("AliceCh1.txt");
		HashMap<String, Occurrence> doc2 = engine.loadKeywordsFromDocument("WowCh1.txt");
		engine.mergeKeywords(doc1);
		engine.mergeKeywords(doc2);
		/*for(String key:engine.keywordsIndex.keySet()) {
			System.out.println(key + " " + engine.keywordsIndex.get(key));
		}*/
		System.out.println(engine.top5search("deep", null));
		
	}
}