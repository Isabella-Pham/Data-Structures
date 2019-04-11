package lse;
import java.io.*;
import java.util.*;

public class Driver{
	public static void main(String[] args) throws FileNotFoundException{
		LittleSearchEngine engine = new LittleSearchEngine();
		/*Scanner sc = new Scanner(System.in);
		System.out.println("Enter first letter of method: (g)etKeyword, (l)oadKeywordsFromDocument, (i)nsertLastOccurrence, (m)ergeKeywords, (t)op5search =>");
		char c = sc.nextLine().charAt(0);
		engine.makeIndex("noisewords.txt", "noisewords.txt");
		System.out.println(engine.keywordsIndex);
		
		if(c == 'g') {
			System.out.println("Enter word =>");
			String word = sc.nextLine();
			System.out.println("Keyword: " + engine.getKeyword(word));
		}else if(c == 'l'){
			System.out.println("Enter document name =>");
			String docFile = sc.nextLine();
			HashMap<String,Occurrence> keywords = engine.loadKeywordsFromDocument(docFile);
			System.out.println("Hashmap of keywords is below");
			for(String name: keywords.keySet()){
				String key = name.toString();
				String value = keywords.get(name).toString();
				System.out.println(key + " " + value);
			}
		}else if(c == 'i'){
			ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
			System.out.println("Enter numbers you would like to insert one by one, pressing enter between each number. The last number you enter will be the one that is inserted. Enter the word 'quit' when done");
			String docFile = null;
			while(true){
				docFile = sc.nextLine();
				if(docFile.equals("quit")){
					break;
				}
				int num = Integer.parseInt(docFile);
				Occurrence oc = new Occurrence("testing", num);
				occs.add(oc);
			}
			engine.insertLastOccurrence(occs);
			System.out.println("Outcome of numbers below, if the numbers are not in DECREASING order then your code is incorrect");
			HashMap<String,ArrayList<Occurrence>> keywordsIndex = engine.keywordsIndex;
			for(String name: keywordsIndex.keySet()){
				ArrayList<Occurrence> value = keywordsIndex.get(name);
				int num = value.get(0).frequency;
				System.out.println(num + " ");
			}
		}else if(c == 'm'){
			System.out.println("Enter document names one by one, pressing enter between each file. Enter the word 'quit' when done");
			String docFile = null;
			while(true){
				docFile = sc.nextLine();
				if(docFile.equals("quit")){
					break;
				}
				HashMap<String,Occurrence> keywords = engine.loadKeywordsFromDocument(docFile);
				engine.mergeKeywords(keywords);
			}
			HashMap<String,ArrayList<Occurrence>> keywordsIndex = engine.keywordsIndex;
			for(String name: keywordsIndex.keySet()){
				String key = name.toString();
				ArrayList<Occurrence> value = keywordsIndex.get(name);
				System.out.println(key + " " + value);
			}
		}else if(c == 't'){
			System.out.println("Enter document names one by one, pressing enter between each file. Enter the word 'quit' when done");
			String docFile = null;
			while(true){
				docFile = sc.nextLine();
				if(docFile.equals("quit")){
					break;
				}
				HashMap<String,Occurrence> keywords = engine.loadKeywordsFromDocument(docFile);
				engine.mergeKeywords(keywords);
			}
			System.out.println("Enter the first keyword =>");
			String kw1 = sc.nextLine();
			System.out.println("Enter the second keyword =>");
			String kw2 = sc.nextLine();
			System.out.println(engine.top5search(kw1,kw2));
		}else{
			System.out.println("Invalid input, terminating program");
			return;
		}*/
		engine.makeIndex("test.txt", "noiseWords.txt");
		//System.out.println(engine.top5search("hi", "bye"));
		HashMap<String, ArrayList<Occurrence>> keywordsIndex = engine.keywordsIndex;
		for(String name: keywordsIndex.keySet()){
			String key = name.toString();
			ArrayList<Occurrence> value = keywordsIndex.get(name);
			System.out.println(key + " " + value);
		}
	}
}