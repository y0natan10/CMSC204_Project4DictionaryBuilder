// Yonatan Rubin
// M21105076

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Manages the dictionary operations and file parsing.
 */
public class DictionaryBuilder {

	private HashTable<String> dictTable;
	private int totalWordsInserted = 0;

	/**
	 * Initializes the dictionary with a table sized for an estimated count.
	 * 
	 * @param estimatedEntries The expected number of unique words.
	 */
	public DictionaryBuilder(int estimatedEntries) {
		// call the constructor for the hash table
		// hash table will handle figuring out the correct size of its own array
		this.dictTable = new HashTable<>(estimatedEntries);
	}

	/**
	 * Reads a file and populates the dictionary with its contents.
	 * 
	 * @param filename The path to the text file
	 * @throws FileNotFoundException If the file cannot be found.
	 */
	public DictionaryBuilder(String filename) throws FileNotFoundException {
		ArrayList<String> words = new ArrayList<>();
		File file = new File(filename);
		Scanner fileScanner = new Scanner(file);

		// Read words into a temporary list to count them for sizing
		while (fileScanner.hasNext()) {
			words.add(fileScanner.next());
		}
		fileScanner.close();

		// assignment says to estimate 1 unique word per 100 words in file
		int estimate = Math.max(10, words.size() / 100);
		this.dictTable = new HashTable<>(estimate);

		for (String w : words) {
			this.addWord(w);
		}
	}

	/**
	 * Cleans the input word and adds it to the hash table.
	 * 
	 * @param word The raw string to be added.
	 * @return True if a valid word was added after cleaning.
	 */
	public boolean addWord(String word) {
		if (word == null || word.isEmpty())
			return false;

		// Strip punctuation and convert to lowercase
		String cleaned = word.toLowerCase().replaceAll("[^a-z]", "");
		// toLowerCase makes everything lower case (obviously)
		// the replaceAll has 2 parameters, first is what we are searching for,
		// 2nd is what we are replacing it with
		// the first parameter is ^ (not) a-z
		// so letters that are not a-z
		// then replace with empty string

		// if, after cleaning, the word is empty, then we didn't/don't add the word
		if (cleaned.isEmpty())
			return false;

		this.dictTable.add(cleaned);
		++this.totalWordsInserted;
		return true;
	}

	/**
	 * Retrieves the frequency of a word in the dictionary.
	 * 
	 * @param word The word to search for.
	 * @return The number of occurrences found.
	 */
	public int getFrequency(String word) {
		// same cleaning logic as before
		return this.dictTable.getFrequency(word.toLowerCase().replaceAll("[^a-z]", ""));
	}

	/**
	 * Removes a word from the dictionary.
	 * 
	 * @param word The word to delete.
	 * @return True if removal was successful.
	 * @throws DictionaryEntryNotFoundException if the word isn't there.
	 */
	public boolean removeWord(String word) throws DictionaryEntryNotFoundException {
		// same cleaning logic as before
		String cleaned = word.toLowerCase().replaceAll("[^a-z]", "");

		// get frequency before deleting so we can update totalWordsInserted
		int freq = this.getFrequency(cleaned);

		// if the word we are trying to remove doesn't exist in the table
		// throw the exception
		if (!this.dictTable.remove(cleaned)) {
			throw new DictionaryEntryNotFoundException("Word not found: " + word);
		}

		// update the total insertions count (subtracting all occurrences of this word)
		this.totalWordsInserted -= freq;
		return true;
	}

	/**
	 * Returns an alphabetical list of all unique words
	 * 
	 * @return A sorted ArrayList of dictionary entries.
	 */
	public ArrayList<String> getAllWords() {
		ArrayList<String> allWords = new ArrayList<>();
		GenericLinkedList<String>[] buckets = this.dictTable.getBuckets();

		// Iterate through every bucket in the hash table
		for (GenericLinkedList<String> bucket : buckets) {
			if (bucket != null) {
				// Use the fillList method to dump the nodes into our ArrayList
				bucket.fillList(allWords);
			}
		}

		// Sort the final collection alphabetically as required by the 'list' command
		Collections.sort(allWords);
		return allWords;
	}

	/**
	 * Returns the total number of words inserted, including duplicates.
	 * 
	 * @return total insertion count.
	 */
	public int getTotalWords() {
		return this.totalWordsInserted;
	}

	/**
	 * Provides access to the underlying HashTable.
	 * 
	 * @return the dictionary hash table.
	 */
	public HashTable<String> getTable() {
		return this.dictTable;
	}
}