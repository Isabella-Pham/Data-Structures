package lse;
import java.io.*;
import java.util.*;

public class Driver{
	public static void main(String[] args) throws FileNotFoundException{
		LittleSearchEngine engine = new LittleSearchEngine();
		engine.makeIndex("doc1.txt", "noiseWords.txt");
		engine.makeIndex("doc2.txt", "noiseWords.txt");
		engine.makeIndex("doc3.txt", "noiseWords.txt");
		engine.makeIndex("doc4.txt", "noiseWords.txt");
		engine.makeIndex("doc5.txt", "noiseWords.txt");
		engine.makeIndex("doc6.txt", "noiseWords.txt");
		engine.makeIndex("doc7.txt", "noiseWords.txt");
		engine.makeIndex("doc8.txt", "noiseWords.txt");
		System.out.println(engine.top5search("strange", "case"));
		System.out.println(engine.top5search("color", "strange"));
		System.out.println(engine.top5search("orange", "weird"));
		System.out.println(engine.top5search("red", "orange"));
		System.out.println(engine.top5search("red", "car"));
		
		
		
		/*HashMap<String, ArrayList<Occurrence>> keywordsIndex = engine.keywordsIndex;
		
		for(String name: keywordsIndex.keySet()){
			String key = name.toString();
			ArrayList<Occurrence> value = keywordsIndex.get(name);
			System.out.println(key + " " + value);
		}*/
	}
}