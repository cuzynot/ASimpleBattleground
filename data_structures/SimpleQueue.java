/**
 * [SimpleQueue].java
 * SimpleQueue is a simple queue
 * data structure, stores Nodes in a one
 * dimensional fashion
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package data_structures;

public class SimpleQueue<E> {

	// init vars
	private Node<E> head;
	private Node<E> tail;

	/**
	 * enqueue
	 * adds a new item to the back of queue
	 * @param item, value of new item
	 */
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
	
	/**
	 * dequeue
	 * removes item at front of queue
	 * @return E, value of item removed
	 */
	public E dequeue() {
		E it = head.getItem();
		head = head.getNext();
		return it;
	}
	
	/**
	 * isEmpty
	 * returns if queue is empty
	 * @return b, if queue is empty
	 */
	public boolean isEmpty() {
		if (head == null) {
			return true;
		} else {
			return false;
		}
	}
}