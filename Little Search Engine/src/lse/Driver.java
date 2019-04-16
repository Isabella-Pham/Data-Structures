package lse;
import java.io.*;
import java.util.*;

public class Driver{
	public static void main(String[] args) throws FileNotFoundException{
		LittleSearchEngine engine = new LittleSearchEngine();
		engine.makeIndex("test.txt", "noiseWords.txt");
		System.out.println(engine.top5search("hi", "bye"));
		HashMap<String, ArrayList<Occurrence>> keywordsIndex = engine.keywordsIndex;
		
		for(String name: keywordsIndex.keySet()){
			String key = name.toString();
			ArrayList<Occurrence> value = keywordsIndex.get(name);
			System.out.println(key + " " + value);
		}
		System.out.println(engine.top5search("hi", "hi")); 
	}
}