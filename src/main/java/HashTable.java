// Yonatan Rubin
// M21105076

/**
 * hash table used to store words in the dictionary. 
 * handles collisions with chaining
 * functions by an array of LinkedLists (that i stole from my previous assignment
 */
public class HashTable {

	// instructions from assignment
	/*
	 * Use an array of linked lists for buckets (you may reuse your generic Linked List from the previous project)
	 * Use the word’s hashCode (from Java String) to find a bucket
	 * Handle collisions using chaining
	 * Implement your own resizing method (optional challenge)
	 */
	private static final int NB = 10; // table size for bucket hashing

	private static String[] tableB = new String[NB]; // bucket hashing table

	// Bucket hashing
	public static int hashB(int key) {
		return key % NB;
	}

	// uses chaining so it's much easier
	public static int searchB(int key) {
		int slot = key % NB;
		String bucket = tableB[slot];

		if (bucket.isEmpty()) {
			return -1;
		}

		// Split the comma-separated string into an array of strings
		String[] elements = bucket.split(", ");
		int comparisons = 0;

		for (String element : elements) {
			comparisons++;
			if (element.equals(String.valueOf(key))) {
				return comparisons; // Returns the position in the bucket
			}
		}
		return -1; // Key not found in the bucket
	}

}
