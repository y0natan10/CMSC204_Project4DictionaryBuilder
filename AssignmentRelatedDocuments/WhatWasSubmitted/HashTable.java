// Yonatan Rubin
// M21105076

// instructions from assignment
/*
 * Use an array of linked lists for buckets (you may reuse your generic Linked
 * List from the previous project) 
 * 
 * Use the word’s hashCode (from Java String) to find a bucket 
 * 
 * Handle collisions using chaining 
 * 
 * Implement your own resizing method (optional challenge)
 */

/**
 * A hash table implementation using chaining with GenericLinkedLists. This
 * class is designed to store dictionary words and their number of occurrences,
 * the assignment wanted a load factor of 0.6 and an array with a prime size in
 * the form of 4k+3.
 * 
 * @param <T>
 */

public class HashTable<T extends Comparable<T>> {

	private GenericLinkedList<T>[] buckets;
	private int capacity;
	private int uniqueWordCount = 0;

	/**
	 * Constructs a hash table with a capacity based on a 0.6 load factor and 4k+3
	 * prime.
	 * 
	 * @param estimatedCount The total number of words expected from the file.
	 */
	@SuppressWarnings("unchecked")
	// added this because i don't like seeing errors/warnings
	// when EVERYTHING IS FINE
	public HashTable(int estimatedCount) {
		int minCapacity = (int) Math.ceil(estimatedCount / 0.6);
		this.capacity = findNext4k3Prime(minCapacity);

		this.buckets = new GenericLinkedList[this.capacity];
		for (int i = 0; i < this.capacity; i++) {
			buckets[i] = new GenericLinkedList<>();
		}
	}

	/**
	 * Maps an element's hash code to a valid array index within the bucket range.
	 * 
	 * @param element The data element to be indexed.
	 * @return A non-negative integer representing the bucket index.
	 */
	private int getIndex(T element) {
		return Math.abs(element.hashCode()) % this.capacity;
	}

	/**
	 * Inserts an element into the table using the bucket's sorted add method.
	 * 
	 * @param element The data to be added to the hash table.
	 */
	public void add(T element) {
		int index = this.getIndex(element);
		int oldBucketSize = buckets[index].getSize();

		buckets[index].add(element);

		if (buckets[index].getSize() > oldBucketSize) {
			this.uniqueWordCount++;
		}
	}

	/**
	 * Checks if the specified element exists within its corresponding hash bucket.
	 * 
	 * @param element The data element to search for.
	 * @return True if the element is found, false otherwise.
	 */
	public boolean contains(T element) {
		int index = this.getIndex(element);
		return buckets[index].contains(element);
	}

	/**
	 * Removes an element from the table and updates the unique word count if
	 * successful.
	 * 
	 * @param element The data element to be removed.
	 * @return True if the element was removed, false if it was not found.
	 */
	public boolean remove(T element) {
		int index = this.getIndex(element);
		if (buckets[index].remove(element)) {
			--this.uniqueWordCount;
			return true;
		}
		return false;
	}

	/**
	 * Searches for the next prime number that satisfies the 4k + 3 requirement.
	 * 
	 * @param min The starting value for the prime search.
	 * @return The first prime number greater than or equal to min that equals 4k +
	 *         3.
	 */
	private int findNext4k3Prime(int min) {
		int n = (min < 3) ? 3 : min;

		while (true) {
			if (n % 4 == 3 && this.isPrime(n)) {
				return n;
			}
			++n;
		}
	}

	/**
	 * Determines if a number is prime using the optimized 6k +/- 1 method.
	 * 
	 * @param n The integer to check for primality.
	 * @return True if the number is prime, false otherwise.
	 */
	private boolean isPrime(int n) {
		if (n <= 1)
			return false;
		if (n <= 3)
			return true;
		if (n % 2 == 0 || n % 3 == 0)
			return false;

		for (int i = 5; i * i <= n; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0)
				return false;
		}
		return true;
	}

	/**
	 * Returns the number of times a specific word has been inserted into the table.
	 * 
	 * @param element The word to look up.
	 * @return The frequency count of the word.
	 */
	public int getFrequency(T element) {
		int index = getIndex(element);
		return buckets[index].getFrequency(element);
	}

	/**
	 * Returns the current physical capacity of the hash table's array.
	 * 
	 * @return The total number of buckets available.
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * Returns the total number of unique elements currently stored in the table.
	 * 
	 * @return The count of unique words.
	 */
	public int getUniqueCount() {
		return this.uniqueWordCount;
	}

	/**
	 * Calculates the current load factor based on unique entries and table
	 * capacity.
	 * 
	 * @return The ratio of unique entries to the number of buckets.
	 */
	public double getActualLoadFactor() {
		return (double) this.uniqueWordCount / this.capacity;
	}

	/**
	 * Provides access to the underlying bucket array for iteration purposes.
	 * 
	 * @return The array of GenericLinkedList buckets.
	 */
	public GenericLinkedList<T>[] getBuckets() {
		return this.buckets;
	}
}