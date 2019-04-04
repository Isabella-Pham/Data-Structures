package lse;
import java.io.*;
import java.util.*;

public class Driver{
	public static void main(String[] args) throws FileNotFoundException{
		LittleSearchEngine engine = new LittleSearchEngine();
		System.out.println(engine.loadKeywordsFromDocument("AliceCh1.txt"));
	}
}