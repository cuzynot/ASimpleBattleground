/**
 * [SimpleLinkedList].java
 * SimpleLinkedList is a simple linked list
 * data structure, stores Nodes in a one
 * dimensional fashion
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package data_structures;

public class SimpleLinkedList<E> {
	// init vars
	private Node<E> head;

	/**
	 * contains
	 * this method returns whether this linkedlist contains an item
	 * @param item, the item user checks
	 * @return b, if this data structure contains a certain item
	 */
	public boolean contains(E item) {
		Node<E> cur = head;
		if (cur == null) {
			return false;
		}

		while (cur.getNext() != null) {
			if (cur.getItem().equals(item)) {
				return true;
			}
			cur = cur.getNext();
		}
		return false;
	}

	/**
	 * add
	 * this method adds a new item to the data structure
	 * @param item, item to be added
	 */
	public void add(E item) {
		if (head == null) {
			head = new Node<E>(item);
		} else {
			Node<E> last = head;

			while (last.getNext() != null) {
				last = last.getNext();
			}
			Node<E> n = new Node<E>(item);
			last.setNext(n);
		}
	}

	/**
	 * add
	 * this method adds a new item to the data structure at a certain index
	 * @param index, the position for the item to be added
	 * @param item, item to be added
	 */
	public void add(int index, E item) {
		if (index == 0) {
			Node<E> n = new Node<E>(item);
			n.setNext(head);
			head = n;
		} else {
			Node<E> last = head;
			for (int i = 0; i < index - 1; i++) {
				last = last.getNext();
			}
			Node<E> n = new Node<E>(item);
			n.setNext(last.getNext());
			last.setNext(n);
		}
	}

	/**
	 * remove
	 * this method removes an item from the data structure
	 * @param item, item to be removed
	 * @return boolean b, if the data structure originally contained the item
	 */
	public boolean remove(E item) {
		if (head == null) {
			return false;
		} else if (head.getItem().equals(item)) {
			head = head.getNext();
			return true;
		}

		Node<E> cur = head;
		while (cur.getNext() != null) {
			if (cur.getNext().getItem().equals(item)) {
				Node<E> n = cur.getNext();
				cur.setNext(n.getNext());

				return true;
			}
			cur = cur.getNext();
		}
		return false;
	}

	/**
	 * remove
	 * this method removes an item at an index
	 * @param index, index of the item to be removed
	 * @return item, item removed
	 */
	public E remove(int index) {
		if (index == 0) {
			E item = head.getItem();
			head = head.getNext();
			return item;
		} else {
			Node<E> last = head;
			for (int i = 0; i < index - 1; i++) {
				last = last.getNext();
			}
			Node<E> n = last.getNext();
			last.setNext(n.getNext());
			return n.getItem();
		}
	}
	
	/**
	 * get
	 * this method returns the item at a given index
	 * @param index, the index of the item
	 * @return item, the item at a certain index
	 */
	public E get(int index) {
		Node<E> last = head;
		for (int i = 0; i < index; i++) {
			last = last.getNext();
		}
		return last.getItem();
	}

	/**
	 * indexOf
	 * this method returns the first index of a given item
	 * @param item, item to be checked
	 * @return index, first index of the given item
	 */
	public int indexOf(E item) {
		int count = 0;
		Node<E> cur = head;

		while (cur != null) {
			if (cur.getItem().equals(item)) {
				return count;
			}
			cur = cur.getNext();
			count++;
		}
		return -1;
	}

	/**
	 * clear
	 * this method clears the linkedlist
	 */
	public void clear() {
		head = null;
	}

	/**
	 * size
	 * this method returns the size of the list
	 * @return size, number of elements in this list
	 */
	public int size() {
		int count = 0;
		Node<E> cur = head;

		while (cur != null) {
			cur = cur.getNext();
			count++;
		}

		return count;
	}

	/**
	 * toString
	 * this method returns all the elements in this list as a string
	 * @return s, representing all items in the list
	 */
	public String toString() {
		String s = "[";
		Node<E> cur = head;

		while (cur != null) {
			s += cur.getItem() + ", ";
			cur = cur.getNext();
		}
		if (s.length() > 2) {
			s = s.substring(0, s.length() - 2);
		}
		s += "]";

		return s;
	}
	
	/**
	 * isEmpty
	 * this method returns whether or not this data structure has no elements
	 * @return b, whether or not this data structure is empty
	 */
	public boolean isEmpty() {
		if (head == null) {
			return true;
		} else {
			return false;
		}
	}
}
