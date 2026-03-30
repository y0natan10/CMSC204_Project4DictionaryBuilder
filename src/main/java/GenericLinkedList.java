// Yonatan Rubin
// M21105076

/**
 * Cleaned implementation of a doubly linked list for a Hash Table bucket. i got
 * tired of scrolling through the iterable interface that has done nothing but
 * get in my way so i removed it and refactored the entire class
 */
public class GenericLinkedList<T extends Comparable<T>> {

	/**
	 * A class used to contain the data we actually want to keep track of
	 */
	private class Node {

		private T data;
		Node previous;
		Node next;
		private int occurences = 0;

		/**
		 * parameterized constructor to make a new node with the desired data
		 *
		 * @param _data
		 */
		public Node(T _data) {
			this.data = _data;
			this.previous = null;
			this.next = null;
			this.setOccurences(1);
		}

		/**
		 * getter method for number of occurrences a piece of data has been inserted
		 *
		 * @return number of times this data has been inserted
		 */
		public int getOccurences() {
			return occurences;
		}

		public void setOccurences(int occurences) {
			this.occurences = occurences;
		}
	}

	private Node head;
	private Node tail;
	private int size = 0;

	/**
	 * Add an element to the list while maintaining ascending order. If the element
	 * already exists, it increments the occurrence count.
	 *
	 * @param element the data to add
	 */
	public void add(T element) {
		// Case 1: List is empty OR element we are inserting belongs at the head
		if (this.isEmpty() || this.head.data.compareTo(element) > 0) {
			this.addFirst(element);
			return;
		}

		Node current = this.head;

		while (current != null) {
			// If the current node is equal to the one we are inserting
			if (current.data.equals(element)) {
				current.setOccurences(current.getOccurences() + 1);
				return;
			}

			// If current data is less than the one we are inserting,
			// and either it's the last node OR the next node is greater than our element
			// (because the next node might be equal to the data we are inserting,
			// in which case we want to update the counter, not insert a new node)
			if (current.data.compareTo(element) < 0
					&& (current.next == null || current.next.data.compareTo(element) > 0)) {
				Node newNode = new Node(element);
				newNode.next = current.next;
				newNode.previous = current;

				if (current.next != null) {
					current.next.previous = newNode;
				} else {
					this.addLast(element);
					return;
				}

				current.next = newNode;
				++this.size;
				return;
			}

			current = current.next;
		}

	}

	/**
	 * add an element to the front of the list
	 *
	 * @param data
	 */
	public void addFirst(T data) {
		Node newNode = new Node(data);
		if (this.isEmpty()) {
			this.head = this.tail = newNode;
		} else {
			newNode.next = this.head;
			this.head.previous = newNode;
			this.head = newNode;
		}
		++this.size;
	}

	/**
	 * add an element to the end of the list
	 *
	 * @param data
	 */
	public void addLast(T data) {
		Node newNode = new Node(data);
		if (this.isEmpty()) {
			this.head = this.tail = newNode;
		} else {
			newNode.previous = this.tail;
			this.tail.next = newNode;
			this.tail = newNode;
		}
		++this.size;
	}

	/**
	 * remove an element from the list
	 *
	 * @param element
	 * @return true if the element was removed
	 */
	public boolean remove(T element) {
		if (this.isEmpty()) {
			return false;
		}

		Node current = head;
		while (current != null && !current.data.equals(element)) {
			current = current.next;
		}

		if (current == null) {
			return false;
		}

		if (current == this.head) {
			return this.removeFirst() != null;
		}
		if (current == this.tail) {
			return this.removeLast() != null;
		}

		current.previous.next = current.next;
		current.next.previous = current.previous;
		--this.size;
		return true;
	}

	/**
	 * remove and return the first element of the list
	 *
	 * @return the first element of the list
	 */
	public T removeFirst() {
		if (this.isEmpty()) {
			return null;
		}
		T res = this.head.data;
		if (this.getSize() == 1) {
			this.head = this.tail = null;
		} else {
			this.head = this.head.next;
			this.head.previous = null;
		}
		--this.size;
		return res;
	}

	/**
	 * remove and return the last element of the list
	 *
	 * @return the last element of the list
	 */
	public T removeLast() {
		if (this.isEmpty()) {
			return null;
		}
		T res = this.tail.data;
		if (this.size == 1) {
			this.head = this.tail = null;
		} else {
			this.tail = this.tail.previous;
			this.tail.next = null;
		}
		--this.size;
		return res;
	}

	/**
	 * checks through the list to see if a piece of data exists
	 *
	 * @param data
	 * @return true if the data is found
	 */
	public boolean contains(T data) {
		Node current = this.head;
		while (current != null) {
			if (current.data.equals(data)) {
				return true;
			}
			current = current.next;
		}
		return false;
	}

	/**
	 * getter method for the size of the list
	 *
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * boolean method to determine if the list is empty
	 *
	 * @return true if the list is empty
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * sets the head and tail to null and the size to 0 to empty the list
	 */
	public void clear() {
		this.head = this.tail = null;
		this.size = 0;
	}
}