// Yonatan Rubin
// M21105076

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Student test suite for the DictionaryBuilder project. Verifies cleaning
 * logic, frequency tracking, and edge case handling.
 */
public class DictionaryBuilderStudentTests {

	private DictionaryBuilder db;

	// stolen from the input given to us
	private final String sampleInput = "The quick brown fox jumps over the lazy dog. \n"
			+ "The quick red fox leapt over the sleepy cat.";
	// the 4
	// fox 2
	// cat 1

	@BeforeEach
	public void setUp() {
		// Initialize with a reasonable estimate for the sample input
		db = new DictionaryBuilder(20);
	}

	/**
	 * Verifies that words with different cases and punctuation are cleaned and
	 * treated as the same unique entry.
	 */
	@Test
	public void testAddWordForCaseAndPunctuationCleaning() {

		// add 'apple' 3 times
		db.addWord("Apple");
		db.addWord("apple!");
		db.addWord("APPLE...");

		// there should only be 1 unique word, apple
		assertEquals(1, db.getTable().getUniqueCount(), "Should only have 1 unique word.");
		// but it should have a frequency of 3
		assertEquals(3, db.getFrequency("apple"), "Frequency of 'apple' should be 3.");
	}

	/**
	 * Uses the provided sample input (thank you) to verify that frequency counts
	 * are calculated correctly for repeated words.
	 */
	@Test
	public void testFrequencyCountsForSampleInput() {
		// Split the sample input into individual words
		String[] words = sampleInput.split("\\s+");
		for (String w : words) {
			db.addWord(w);
		}

		// "The" appears 4 times (varied case/punctuation)
		assertEquals(4, db.getFrequency("the"));
		// "fox" appears 2 times
		assertEquals(2, db.getFrequency("fox"));
		// "cat" appears 1 time (cleaning the period)
		assertEquals(1, db.getFrequency("cat"));
	}

	/**
	 * Verifies that removing a word correctly updates the unique count and the
	 * total word count in the dictionary.
	 */
	@Test
	public void testRemoveWordUpdatesDictionary() throws DictionaryEntryNotFoundException {
		db.addWord("fox");
		db.addWord("fox");
		db.addWord("dog");

		int initialUnique = db.getTable().getUniqueCount();
		int initialTotal = db.getTotalWords();

		db.removeWord("fox");

		assertEquals(initialUnique - 1, db.getTable().getUniqueCount(), "Unique count should decrease by 1.");
		assertEquals(initialTotal - 2, db.getTotalWords(), "Total words should decrease by the frequency of 'fox'.");
		assertEquals(0, db.getFrequency("fox"), "Frequency should be 0 after removal.");
	}

	/**
	 * Verifies that adding a duplicate word increments frequency but does not
	 * increase the number of unique entries in the hash table.
	 */
	@Test
	public void testDuplicateWords() {
		db.addWord("jump");
		int uniqueAfterFirst = db.getTable().getUniqueCount();

		db.addWord("jump");
		db.addWord("jump");

		assertEquals(uniqueAfterFirst, db.getTable().getUniqueCount(),
				"Unique count should not change for duplicates.");
		assertEquals(3, db.getFrequency("jump"), "Frequency should increment for duplicates.");
	}

	/**
	 * Tests edge cases including searching/deleting in an empty dictionary and
	 * handling nonexistent words.
	 */
	@Test
	public void testEdgeCasesEmptyAndNonexistent() {
		// Test search on empty
		assertEquals(0, db.getFrequency("anything"), "Frequency in empty dictionary should be 0.");

		// Test delete on nonexistent (should throw exception)
		assertThrows(DictionaryEntryNotFoundException.class, () -> {
			db.removeWord("ghost");
		}, "Removing a nonexistent word must throw DictionaryEntryNotFoundException.");

		// Test cleaning results in empty string
		assertFalse(db.addWord("!!!"), "Should return false if word contains no letters.");
		assertEquals(0, db.getTable().getUniqueCount(), "Dictionary should remain empty after invalid add.");
	}

	/**
	 * Verifies that the list command returns words in alphabetical order.
	 */
	@Test
	public void testGetAllWordsSortedOrder() {
		db.addWord("zebra");
		db.addWord("apple");
		db.addWord("middle");

		ArrayList<String> sortedList = db.getAllWords();

		assertEquals("apple", sortedList.get(0));
		assertEquals("middle", sortedList.get(1));
		assertEquals("zebra", sortedList.get(2));
	}
}