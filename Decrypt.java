import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

/* Additional classes or files required: 
 * Word.java, WordList.java, words-complete.txt
 * 
 * Decrypt is a program that recursively tries to decrypt a
 * single-substitution cypher string (or strings) from the 
 * user. A single substitution cypher means that every 
 * character in the alphabet is scrambled to only one other 
 * character. "William" -> "Qcttceg," for example.
 * 
 * How it works: 
 * 
 * Forget about letters, we use numbers in their place. Beccause
 * the encrypted string is, well, encrypted, we begin the 
 * decryption process by its letters with numbers. The first letter
 * is assigned 0, followed by increasing numbers if a different 
 * letter occurs throughout the string. If the same letter occurs,
 * then the same number assignment takes its place. We do the same 
 * for our word bank, entitled "words-complete.txt." Now, the 
 * encrypted string has a matching (or multiple matching) number-
 * words. Back to the example, "William" -> "Qcttceg" -> "0122134." 
 * 
 * Then, we begin to try and build an alpabet that is associated with
 * our numbers. Longer strings are the key here, because the longer a 
 * string is, the better chance at finding the letters associated with
 * the numbers. This is done recursivley throughout the strings entered
 * by the user. 
 * 
 * How it doesn't work:
 * 
 * If the user enters a string with a low length, the program might 
 * not decrypt it. For the same reason that longer strings aid our
 * decryption, shorter strings are more difficult to decrypt. 
 * We aren't wizards :(
 * 
 * For the record:
 * This was the only program in our CS327 Data Structures & Algorithms 
 * class that was able to repeatedly and accurately decrypt strings. 
 * Call us Alan Turing from now on!
 * 
 * Created: March 2016
 * Authors: Billy Bird, Garett Tounge
 * Last Modified: March 2017
 */

public class Decrypt {

	public static HashMap<Character,Character> decryption;

	public static HashMap<Character,Character> bestTry;

	public static int bestNum;

	public static ArrayList<ArrayList<Word>> testWordsPossible;

	public static ArrayList<Word> testingStrings;

	public static void main(String[] args){

		bestNum = 0;

		//user prompted for encrypted sentence/String
		System.out.println("Please enter an encrypted sentence:");
		Scanner scan = new Scanner(System.in);
		String encrypted = scan.nextLine();		//user's encryption

		HashSet<String> wordList;

		//ordered will contain, from the largest to the smallest, 
		//words from the users encrypted String
		ArrayList<String> ordered = new ArrayList<String>();

		ArrayList<String> wordListOrdered = new ArrayList<String>();
		WordList list = new WordList();
		ordered = WordList.orderWords(encrypted);
		wordList = list.getList();
		for(String s: wordList){
			wordListOrdered.add(s);
		}

		Collections.sort(wordListOrdered, new StringComparator());
		ArrayList<Word> words = new ArrayList<Word>();
		for (String element : ordered){
			words.add(new Word(element));
		}

		ArrayList<Word> listWords = new ArrayList<Word>();
		for (String element : wordListOrdered){
			listWords.add(new Word(element));
		}	
		testingStrings = new ArrayList<Word>();
		for(int i = 0; i < ordered.size(); i++){
			//for(int i = 0; i < 200; i++){
			try{
				testingStrings.add(new Word(ordered.get(i)));
			}catch(IndexOutOfBoundsException e){
				break;
			}
		}
		decryption = new HashMap<Character,Character>();
		bestTry = new HashMap<Character,Character>();
		Stack stack = new Stack();
		for(Word s: testingStrings){
			stack.push(s);
		}
		testWordsPossible = generateTestWords(listWords,stack);
		for(int i = 0; i < testWordsPossible.size()-1; i++){
			if(testWordsPossible.get(i).isEmpty() || testWordsPossible.get(i).size() > 70){
				testWordsPossible.remove(testWordsPossible.get(i));
				testingStrings.remove(testingStrings.size()-(i+1));
				i--;
			}
		}
		boolean b = recursiveCheck(0);
		System.out.println(b);
		/*	for(Word w: testingStrings){
			System.out.println(w.toString());
		}
		for(ArrayList<Word> w: testWordsPossible){
			System.out.println(w.toString());
		}*/
		if(!b){
			decryption = new HashMap<Character,Character>(bestTry);
		}
		String decrypted =  "";
		for(int i = 0; i < encrypted.length(); i++){
			if(decryption.containsKey(encrypted.charAt(i))){
				decrypted+=decryption.get(encrypted.charAt(i));
			}else{
				decrypted+= encrypted.charAt(i);
			}
		}
		System.out.println(decrypted);
		
		//lettersToNums should contain a String of numbers, 
		//generaed from words that contain two or more of
		//the same letter. HashSet for quick lookup
		HashSet<String> lettersToNums = new HashSet<String>();		


	}
	public static ArrayList<ArrayList<Word>> generateTestWords(ArrayList<Word> wordList, Stack testWords){
		ArrayList<ArrayList<Word>> testWordsPossible = new ArrayList<ArrayList<Word>>();
		while(!testWords.isEmpty()){
			Word w = (Word) testWords.pop();
			ArrayList<Word> temp = new ArrayList<Word>();
			for(int i = 0; i < wordList.size(); i++){
				if(wordList.get(i).getWord().equals("sexuality")){
				}
				if(w.checkNum(wordList.get(i))){

					temp.add(wordList.get(i));
				}
			}
			testWordsPossible.add(temp);
		}
		return testWordsPossible;
	}
	public static void addToMap(Word enc, Word dec){
		for(int i = 0; i < enc.getWord().length(); i++){
			decryption.put(Character.toLowerCase(enc.getWord().charAt(i)), Character.toLowerCase(dec.getWord().charAt(i)));
			decryption.put(Character.toUpperCase(enc.getWord().charAt(i)), Character.toUpperCase(dec.getWord().charAt(i)));
		}
	}
	public static boolean recursiveCheck(int numCheck){
		if(numCheck > bestNum){
			bestNum = numCheck;
			bestTry = new HashMap<Character,Character>(decryption);
		}
		HashMap<Character,Character> temp = new HashMap<Character,Character>(decryption);
		if(numCheck == testWordsPossible.size()){
			return true;
		}
		ArrayList<Word> testList = testWordsPossible.get(numCheck);
		for(int i = 0; i < testList.size(); i++){
			decryption = new HashMap<Character,Character>(temp);
			if(testList.get(i).tryParse(decryption,testingStrings.get(testingStrings.size()-(numCheck+1)).getWord())){
				addToMap(testingStrings.get(testingStrings.size()-(numCheck+1)),testList.get(i));
				boolean decrypted = recursiveCheck(numCheck+1);
				if(decrypted){
					return true;
				}
			}
		}
		return false;
	}
	
	//takes a word and turns it into a String of numbers,
	//starting from 1-n, if a new character in the String 
	//is seen, n = n + 1
	public static String generateLetterstoNums(String word){
		String generated = "";
		HashMap<Character, Integer> dictionary = 
				new HashMap<Character, Integer>();
		int num = 1;

		for (int i = 0; i < word.length(); i++){
			char ch = word.charAt(i);
			ch = Character.toLowerCase(ch);

			//if dictinoary.get(ch) == 0, the letter 
			//isn't in the word already
			if (dictionary.containsKey(ch) && Character.isLetter(ch)){
				generated += dictionary.get(ch);
			}

			else if(!dictionary.containsKey(ch) && Character.isLetter(ch)){
				dictionary.put(ch, num);
				num++;
				generated += dictionary.get(ch);
			}

			else {
				generated += ch;
			}

		}

		return generated;
	}

	//stringsInHash takes an ArrayList containing the 10 largest words from
	//our encrypted input in their number form, and checks if that number
	//is in our HashSet of numbers. If the number is in the HashSet, a second
	//ArrayList of Booleans, with the same indexing, is changed to true for 
	//that given index.
	public static ArrayList<Boolean> stringsInHash(ArrayList<String> nums, 
			HashSet<String> comp){

		ArrayList<Boolean> set = new ArrayList<Boolean>();

		for(int i = 0;i < nums.size();i++){
			boolean isIn = comp.contains(nums.get(i));
			set.add(isIn);
		}

		return set;
	}

}
