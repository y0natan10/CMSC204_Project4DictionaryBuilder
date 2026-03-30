//Yonatan Rubin
//M21105076

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * custom implementation of a doubly linked list for forward and backwards
 * iteration
 * 
 * @param <T> the type of elements in the list
 */
public class GenericLinkedList<T> implements Iterable<T> {
	/**
	 * custom inner class that represents a link in the linked list
	 */
	private class Node {
		private T data;
		Node previous;
		Node next;

		/**
		 * default constructor for the node class
		 */
		public Node() {
			this.data = null;
			this.previous = null;
			this.next = null;
		}

		/**
		 * parameterized constructor for the node class
		 *
		 * @param _data
		 */
		public Node(T _data) {
			data = _data;
			previous = null;
			next = null;
		}

	}

	private Node head;
	private Node tail;
	private int size = 0;

	private class GenericIterator implements ListIterator<T> {

		// position of the iterator
		private int index;
		// the node returned by calling next()
		private Node current;
		// node that was just returned by called next()
		private Node lastReturned;

		public GenericIterator() {
			this.index = 0;
			this.current = head; // can't use 'this.' because GenericIterator doesn't have a head
			this.lastReturned = null;
		}

		@Override
		public boolean hasNext() {
			// current is only null at the tail, when there is no next
			return (current != null);
			// could i also do
			// return this.index < size;
		}

		@Override
		public T next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			++this.index;
			// change lastReturned to be the currently next node
			this.lastReturned = this.current;
			// move the pointer that is supposed to point to the next node forward by 1
			this.current = this.current.next;
			return this.lastReturned.data;
		}

		@Override
		public boolean hasPrevious() {
			// the only time we cannot move backward in a list,
			// is when we are the head
			if (this.current != head) {
				return true;
			}
			return false;
			// can i do this?
			// return this.index > 0;
		}

		@Override
		public T previous() {
			if (!this.hasPrevious()) {
				// there is no previous to navigate too
				throw new NoSuchElementException();
			}
			// when current is null, we've past the tail
			// so going backwards should bring us to the tail
			if (this.current == null) {
				this.current = tail;
			}
			// else (not past the tail) we're safe to move backwards,
			// since we already checked if previous was null at the start
			else {
				this.current = this.current.previous;
			}
			--this.index;
			this.lastReturned = this.current;
			return this.lastReturned.data;
		}

		@Override
		public void remove() {
			if (this.lastReturned == null) {
				// when lastReturned is null, that means next() or previous() hasn't been called
				throw new IllegalStateException("next() or previous() not yet called");
			}

			// for some reason i can't call
			// remove(nodeToRemove.data);
			// idk why, would make this much much easierzzz
			Node nodeToRemove = lastReturned;
			// node before the node we are removing
			Node before = nodeToRemove.previous;
			// node after the node we are removing
			Node after = nodeToRemove.next;

			//

			if (nodeToRemove == head) {
				removeFirst(); // size decremented here
				// can't return yet because we need to check the nonsense
				// caused by calling next()
			} else if (nodeToRemove == tail) {
				removeLast(); // size decremented here
				// same return issue here
			} else {
				// we're somewhere in the middle so we can blindly assign stuff
				// without additional checks
				before.next = after;
				after.previous = before;
				--size;
			}

			// If we removed after calling next(), index must shift back
			if (this.lastReturned != this.current) {
				--this.index;
			} else {
				// if we removed after calling previous(), current moves to the next valid node
				this.current = after;
			}

			this.lastReturned = null;
		}

		@Override
		public void set(Object e) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public int nextIndex() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public int previousIndex() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(Object e) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

	}

	// these 2 methods are problematic
	/**
	 * TODO: comments
	 */
	@Override
	public ListIterator<T> iterator() {
		return new GenericIterator();
	}

	/**
	 * TODO: comments
	 * 
	 * @return
	 */
	public ListIterator<T> listIterator() {
		return new GenericIterator();
	}

	/**
	 * returns the element at the specified position in this list
	 *
	 * @param index
	 * @return the node that the index is referring too
	 */
	T get(int index) {
		if (index < 0 || index >= this.size()) {
			// index out of bounds, this works for if the list is empty too
			// since the size will be 0
			throw new IndexOutOfBoundsException();
		}
		Node res = this.head;
		for (int i = 0; i < index; ++i) {
			res = res.next;
		}
		return res.data;
	}

	/**
	 * get the first element of the list without removing it
	 *
	 * @return first element of the list
	 */
	T getFirst() {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		return this.head.data;
	}

	/**
	 * get the last element in the list without removing it
	 *
	 * @return the last element in the list
	 */
	T getLast() {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		return this.tail.data;
	}

	/**
	 * tells us if the list has any data in it
	 *
	 * @return true if the list is empty
	 */
	Boolean isEmpty() {
		return (this.head == null);
	}

	/**
	 * removes and returns the element at the given index
	 *
	 * @param index, index of the item to remove
	 * @return the removed item
	 * @throws IndexOutOfBoundsException
	 */
	T remove(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= this.size()) {
			throw new IndexOutOfBoundsException();
		} else if (index == 0) {
			return this.removeFirst();
		} else if (index == this.size() - 1) {
			return this.removeLast();
		}

		// we made it down here so
		// the index is somewhere that is not at the start or end
		Node currentNode = this.head;
		for (int i = 0; i < index; ++i) {
			currentNode = currentNode.next;
		}

		// save the data before it is thanos snapped
		T data = currentNode.data;

		// delinkify the node
		currentNode.previous.next = currentNode.next;
		currentNode.next.previous = currentNode.previous;

		// decrement size
		--this.size;
		return data;
	}

	/**
	 * remove the first occurence of the specified element from the list
	 *
	 * @param element, object we are searching for to remove
	 * @return true if the element was found and removed
	 */
	boolean remove(T element) {
		if (this.isEmpty()) {
			return false;
		}

		Node current = this.head;

		// search for the first matching element
		while (current != null && !current.data.equals(element)) {
			current = current.next;
		}

		// element not found
		if (current == null) {
			return false;
		}

		// case 1: removing the head
		if (current == this.head) {
			// reuse the old code woooooooooo
			this.removeFirst();
			return true;
		}

		// case 2: removing the tail
		if (current == this.tail) {

			this.removeLast();
			return true;
		}

		// case 3: removing from the middle
		current.previous.next = current.next;
		current.next.previous = current.previous;

		--this.size;
		return true;
	}

	/**
	 * remove and return the first element in the list assuming the list is not
	 * empty
	 *
	 * @return the first element in the list
	 * @throws NoSuchElementException
	 */
	T removeFirst() throws NoSuchElementException {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		// save the stuff we need to return because it's about to vanish
		T res = this.head.data;

		// we are removing the only element in the list
		if (this.size() == 1) {
			this.head = this.tail = null;
		} else {
			// overwrite the first element with the 2nd
			this.head = this.head.next;

			// change the new head's.previous to be null
			this.head.previous = null;
		}
		--this.size;
		return res;
	}

	/**
	 * remove and return the last element in the list assuming the list is not empty
	 *
	 * @return the last element in the list
	 * @throws NoSuchElementException
	 */
	T removeLast() throws NoSuchElementException {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		// save the stuff we need to return because it's about to vanish
		T res = this.tail.data;

		// we are removing the only element in the list
		if (this.size() == 1) {
			this.head = this.tail = null;
		} else {
			// overwrite the last element with the 2nd to last element
			this.tail = this.tail.previous;

			// change the new tail's.next to be null
			this.tail.next = null;
		}
		--this.size;
		return res;
	}

	/**
	 * getter for the length of the list
	 *
	 * @return length of the list
	 */
	int size() {
		return this.size;
	}

	/**
	 * create an array the size of the list and copy the element over
	 *
	 * @return the linked list as an array
	 */
	Object[] toArray() {
		// i would end up calling the method this.Size() for
		// however long the list was + 1 more
		// easy temp variable to avoid endless calling
		int tempSize = this.size();
		Object[] res = new Object[tempSize];
		Node currentNode = this.head;
		for (int i = 0; i < tempSize; ++i) {
			res[i] = currentNode.data;
			currentNode = currentNode.next;
		}
		return res;
	}

	/**
	 * add some data to the front of the list
	 *
	 * @param _data, the data to add to the list
	 */
	public void addFirst(T data) {
		// create a new node with the parameterized constructor
		Node newNode = new Node(data);

		// if the list is currently empty,
		// then the element we are adding will be the first and last in the list
		if (this.isEmpty()) {
			this.head = this.tail = newNode;
		} else {
			// assign the newNode's next to be the (soon to be replaced) head
			newNode.next = this.head;
			// make the (soon to be) 2nd element point
			// back to the first (soon to be new head)
			this.head.previous = newNode;
			// reassign the head to be newNode
			this.head = newNode;
		}

		// always need to increment the size
		++this.size;
	}

	/**
	 * add some data to the end of the list
	 *
	 * @param _data, the data to add to the list
	 */
	public void addLast(T data) {
		// create a new node with the parameterized constructor
		Node newNode = new Node(data);

		// if the list is currently empty,
		// then the element we are adding will be the first and last in the list
		if (this.isEmpty()) {
			this.head = this.tail = newNode;
		} else {
			// assign the newNode's previous to be the (old) tail
			newNode.previous = this.tail;
			// make the now 2nd to last element (old tail) point to the last element (new
			// tail)
			this.tail.next = newNode;
			// reassign the tail to be the new node
			this.tail = newNode;
		}

		// always need to increment the size
		++this.size;
	}

	public boolean contains(T lookForData) {
		Node currentNode = this.head;
		while (currentNode != null) {
			// if the current element is the one we are searching for
			if (currentNode.data.equals(lookForData)) {
				return true;
			} else {
				// this is not the node is not the one we are searching for
				// move to the next node
				currentNode = currentNode.next;
			}
		}
		// if we managed to finish the whole list without finding a match,
		// then it is not in the list
		return false;
	}

	public void clear() {
		this.head = this.tail = null;
		this.size = 0;
	}

}
