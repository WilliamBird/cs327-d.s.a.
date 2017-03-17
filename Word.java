/* This class is created to store additional, easily 
 * accessed information for a given encrypted or decrypted
 * word. Such information includes the encryted word itself,
 * its associated number generation, as well as methods
 * toString(), getWord(), getInts(), and checkNum().
 */


import java.util.HashMap;

public class Word {
	String word;
	String ints;

	//Constructor for Word
	public Word(String word){
		this.word = word;
		ints = Decrypt.generateLetterstoNums(word);
	}

	//toString() returns a String containing the stored
	//word and the words number generation
	public String toString(){
		return word + " " + ints;
	}

	//getWord() returns the stored word
	public String getWord(){
		return word;
	}

	//getInts() returns the stored word's number
	//genreation
	public String getInts(){
		return ints;
	}

	//checkNum() returns true when the parameter, a 
	//String of numbers in this case, is equal to 
	//the stored word's number generation, and false
	//otherwise
	public boolean checkNum(String i){
		if(i.equals(ints)){
			return true;
		}else{
			return false;
		}
	}

	//checkNum() returns true when the paramter, a
	//String, is equal to the stored word, and false
	//otherwise
	public boolean checkNum(Word w){
		if(w.getInts().equals(ints)){
			return true;
		}else{
			return false;
		}
	}

	//tryParse() returns true when a word can be mapped correctly, 
	//also known as mapped without a collision, and false otherwise.
	public boolean tryParse(HashMap<Character,Character> decryption, String word){
		for(int i = 0; i < word.length(); i++){
			char key = Character.toLowerCase(word.charAt(i));
			char value = Character.toLowerCase(this.word.charAt(i));
			if(decryption.containsKey(key) &&  decryption.get(key) != value){
				return false; 
			}else if(decryption.containsValue(value) && decryption.containsKey(key) && decryption.get(key) != value){
				return false;
			}else if(decryption.containsValue(value) && !decryption.containsKey(key)){
				return false;
			}
		}
		return true;
	}

} //end of class
