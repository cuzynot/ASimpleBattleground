package data_structures;

public class SimpleQueue<E> {

	private Node<E> head;
	private Node<E> tail;

	public void enqueue(E item) {
		Node<E> n = new Node<E>(item);

		if (head == null) {
			head = n;
			tail = n;
		} else {
			tail.setNext(n);
			tail = n;
		}
	}
	
	public E dequeue() {
		E it = head.getItem();
		head = head.getNext();
		return it;
	}
	
	public boolean isEmpty() {
		if (head == null) {
			return true;
		} else {
			return false;
		}
	}
}
