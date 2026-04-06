// Yonatan Rubin
// M21105076

import java.util.ArrayList;

/**
 * A doubly linked list implementation specialized for hash table separate
 * chaining. This list maintains elements in ascending order and tracks
 * occurrences of duplicate entries.
 *
 * @param <T> The type of elements held in this list, which must be Comparable.
 */
public class GenericLinkedList<T extends Comparable<T>> {

	/**
	 * private inner class representing a single node in the doubly linked list.
	 */
	private class Node {

		private T data;
		Node previous;
		Node next;
		private int occurences = 0;

		/**
		 * Initializes a new node with the specified data and an initial occurrence of
		 * one.
		 *
		 * @param _data The data to be stored in the node.
		 */
		public Node(T _data) {
			this.data = _data;
			this.previous = null;
			this.next = null;
			this.occurences = 1;
		}
	}

	private Node head;
	private Node tail;
	private int size = 0;

	/**
	 * Adds an element in ascending order or increments the count if it already
	 * exists.
	 *
	 * @param element The data element to be inserted or updated.
	 */
	public void add(T element) {
		// an empty list gets things shoved at the head
		// if the head is bigger than whatever we're inserting,
		// put the new thing before the head
		if (this.isEmpty() || this.head.data.compareTo(element) > 0) {
			this.addFirst(element);
			return;
		}

		Node current = this.head;

		while (current != null) {
			// we found where it's supposed to go,
			// put it there and increase occurences
			// skedadle
			if (current.data.equals(element)) {
				++current.occurences;
				return;
			}

			// if the current element is less than the one we want to insert
			// we know that we are either at the right spot to insert
			// or we need to keep going
			// but if the next element (after current) is null,
			// then we are at the end of the list
			// or if the next element is greater than the one we are inserting
			// we are at the right spot and need to insert
			if (current.data.compareTo(element) < 0
					&& (current.next == null || current.next.data.compareTo(element) > 0)) {
				// make a new node based on the thing we are inserting
				Node newNode = new Node(element);
				newNode.next = current.next;
				newNode.previous = current;

				// don't try to assign pointers of a null node
				// because that would be bad
				if (current.next != null) {
					current.next.previous = newNode;
				} else {
					this.addLast(element);
					return;
				}

				// finish linking the new node into the list
				current.next = newNode;
				++this.size;
				return;
			}
			// we didn't find where to put it, go next
			current = current.next;
		}
	}

	/**
	 * Inserts a new node containing the provided data at the beginning of the list.
	 *
	 * @param data The data to be added to the front of the list.
	 */
	public void addFirst(T data) {
		Node newNode = new Node(data);

		// an empty list into a list with one node
		// makes the head and tail the same
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
	 * Inserts a new node containing the provided data at the end of the list.
	 *
	 * @param data The data to be added to the end of the list.
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
	 * Removes the first node that matches the specified element from the list.
	 *
	 * @param element The data element to search for and remove.
	 * @return True if the element was successfully removed, false otherwise.
	 */
	public boolean remove(T element) {
		Node target = searchNode(element);

		if (target == null)
			return false;

		if (target == this.head)
			return this.removeFirst() != null;
		if (target == this.tail)
			return this.removeLast() != null;

		target.previous.next = target.next;
		target.next.previous = target.previous;
		--this.size;
		return true;
	}

	/**
	 * Removes and returns the data from the first node in the list.
	 *
	 * @return The data from the removed node, or null if the list is empty.
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
	 * Retrieves the number of times a specific element has been added to the list.
	 *
	 * @param data The element to look up.
	 * @return The occurrence count of the element, or 0 if not found.
	 */
	public int getFrequency(T data) {
		Node found = searchNode(data);
		return (found != null) ? found.occurences : 0;
	}

	/**
	 * Removes and returns the data from the last node in the list.
	 *
	 * @return The data from the removed node, or null if the list is empty.
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
	 * Internal helper to locate the node containing the specified data.
	 *
	 * @param data The data to search for.
	 * @return The Node object containing the data, or null if not found.
	 */
	private Node searchNode(T data) {
		Node current = this.head;
		while (current != null) {
			if (current.data.equals(data)) {
				return current;
			}
			current = current.next;
		}
		return null;
	}

	/**
	 * Adds all data elements in this list to the provided ArrayList.
	 * 
	 * @param list The target list to populate.
	 */
	public void fillList(ArrayList<T> list) {
		Node current = this.head;
		while (current != null) {
			list.add(current.data);
			current = current.next;
		}
	}

	/**
	 * Checks if the list contains a node with the specified data.
	 *
	 * @param data The data to search for.
	 * @return True if the data exists in the list, false otherwise.
	 */
	public boolean contains(T data) {
		return searchNode(data) != null;
	}

	/**
	 * Public search method that returns the node associated with the provided data.
	 *
	 * @param data The data to search for.
	 * @return The Node containing the data, or null if no match is found.
	 */
	public Node search(T data) {
		return searchNode(data);
	}

	/**
	 * Returns the total number of unique nodes currently in the list.
	 *
	 * @return The size of the list.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Determines whether the list is currently empty.
	 *
	 * @return True if the head is null, false otherwise.
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Clears the list by nullifying the head and tail and resetting the size to
	 * zero.
	 */
	public void clear() {
		this.head = this.tail = null;
		this.size = 0;
	}
}