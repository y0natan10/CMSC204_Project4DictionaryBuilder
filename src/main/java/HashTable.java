// Yonatan Rubin
// M21105076

// instructions from assignment
/*
 * Use an array of linked lists for buckets (you may reuse your generic Linked
 * List from the previous project) Use the word’s hashCode (from Java String) to
 * find a bucket Handle collisions using chaining Implement your own resizing
 * method (optional challenge)
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
	private int capacity; // This will be the (4k+3) prime number thingy
	private int uniqueWordCount;

	public HashTable(int initialCapacity) {
		this.capacity = initialCapacity;
		this.uniqueWordCount = 0;

		// Create the array of buckets
		this.buckets = new GenericLinkedList[capacity];
		for (int i = 0; i < this.capacity; ++i) {
			this.buckets[i] = new GenericLinkedList<>();
		}
	}

	/**
	 * Map the object's hashCode to a valid bucket index.
	 */
	private int getIndex(T element) {
		// Math.abs ensures we don't get a negative index from hashCode()
		return Math.abs(element.hashCode()) % this.capacity;
	}

	/**
	 * Adds an element to the appropriate bucket. Your GenericLinkedList.add()
	 * handles duplicates and sorting.
	 */
	public void add(T element) {
		int index = this.getIndex(element);
		int oldBucketSize = this.buckets[index].getSize();

		this.buckets[index].add(element);

		// If the bucket size increased, it was a new unique word
		if (this.buckets[index].getSize() > oldBucketSize) {
			++this.uniqueWordCount;
		}
	}

	/**
	 * Search for a word in the table.
	 */
	public boolean contains(T element) {
		int index = this.getIndex(element);
		return this.buckets[index].contains(element);
	}

	/**
	 * Remove a word from the table.
	 */
	public boolean remove(T element) {
		int index = this.getIndex(element);
		if (this.buckets[index].remove(element)) {
			--this.uniqueWordCount;
			return true;
		}
		return false;
	}

	public int getUniqueCount() {
		return this.uniqueWordCount;
	}

	public double getActualLoadFactor() {
		return (double) this.uniqueWordCount / this.capacity;
	}
}