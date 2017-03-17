/* Class WordList() reads a file and adds all the Strings 
 * in that file. WordList() also contains methods such as
 * getList() and orderWords(), as well as a String
 * Comparator.
 */

import java.io.*;
import java.util.*;

public class WordList {
	HashSet<String> wordList = new HashSet<String>();

	public WordList() {
		File f = new File("words-complete.txt");
		try {
			FileReader file = new FileReader(f);
			BufferedReader reader = new BufferedReader(file);
			String next;
			try {
				next = reader.readLine();
				while (next != null) {
					wordList.add(next);
					next = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	//getList() returns the wordList, or the collection of
	//Strings from the file in a HashSet<String>
	public HashSet<String> getList() {
		return wordList;
	}

	//orderWords() returns an ArrayList containing words, in
	//order by their length. Index 0 of this ArrayList
	//should contain the largest words from a given String
	public static ArrayList<String> orderWords(String s) {
		ArrayList<String> ordered = new ArrayList<String>();

		while (s.length() != 0) {
			try {
				ordered.add(s.substring(0, s.indexOf(' ')));
				s = s.substring(s.indexOf(' ') + 1);
			}

			catch (IndexOutOfBoundsException e) {
				ordered.add(s);
				s = "";
			}
		}

		//uses our Comparator for Strings to order the words
		Collections.sort(ordered, new StringComparator());
		return ordered;
	}
}

//a String comparator that compares Strings based on length
class StringComparator implements java.util.Comparator<String> {
	public StringComparator() {
		super();
	}

	public int compare(String s1, String s2) {
		return s1.length() - s2.length();
	}
}
