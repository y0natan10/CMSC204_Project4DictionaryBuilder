//Yonatan Rubin
//M21105076

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * class for managing the hash table
 */
public class DictionaryBuilder {

	HashTable dictTable = null;

	/**
	 * Constructor that creates a hash table appropriate for the estimated entries.
	 * Must consider load factor and 4k+3 prime table size.
	 * 
	 * @param estimatedEntries
	 */
	DictionaryBuilder(int estimatedEntries) {

		// TODO: idk what goes here yet, just making a skeleton
		// call the constructor for the hash table
		// with a modified version of estimatedEntires
		return;
	}

	/**
	 * Constructor that reads a file and adds all the words to the DictionaryBuilder
	 * 
	 * @param filename
	 */
	DictionaryBuilder(String filename) throws FileNotFoundException {
		// TODO: idk what goes here yet, just making a skeleton
		// read the entire file into a string
		// make a new array for all the words
		// call addWord on each word in the new array
		// profit
		return;
	}

	/**
	 * add a word to the dictionary
	 * 
	 * @param word
	 */
	public boolean addWord(String word) {
		// TODO: idk what goes here yet, just making a skeleton
		/*
		 * each word should be all lowercase. there should be no punctuation (only
		 * letters a-z)
		 */

		return false;
	}

	/**
	 * counts and returns the number of times a certain word appears
	 * 
	 * @param word
	 */
	public int getFrequency(String word) {
		// TODO: idk what goes here yet, just making a skeleton
		return 0;
	}

	/**
	 * removes a word from the dictionary
	 * 
	 * @param word
	 */
	public boolean removeWord(String word) throws DictionaryEntryNotFoundException {
		// TODO: idk what goes here yet, just making a skeleton
		return false;
	}

	/**
	 * takes all words in the dictionary, and arrange them in an ArrayList in sorted
	 * order
	 * 
	 * @return sorted ArrayList of all the words in the dictionary
	 */
	public ArrayList<String> getAllWords() {
		// TODO: idk what goes here yet, just making a skeleton
		// insert each word from the hash table into a new ArrayList
		//
		ArrayList<String> res = new ArrayList();

		Collections.sort(res);
		return res;
	}
}