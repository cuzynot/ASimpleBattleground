package data_structures;

public class SimpleLinkedList<E> {
	private Node<E> head;

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

	public E get(int index) {
		Node<E> last = head;
		for (int i = 0; i < index; i++) {
			last = last.getNext();
		}
		return last.getItem();
	}

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

	public void clear() {
		head = null;
	}

	public int size() {
		int count = 0;
		Node<E> cur = head;

		while (cur != null) {
			cur = cur.getNext();
			count++;
		}

		return count;
	}

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
	
	public boolean isEmpty() {
		if (head == null) {
			return true;
		} else {
			return false;
		}
	}
}
