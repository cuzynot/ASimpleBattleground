/**
 * [Node].java
 * Node is the base unit / object to the other
 * data structures, used to store a generic value, T
 * @author      Yili Liu
 * @since       Dec.18.2018
 */
package data_structures;

public class Node<T> {
	
	// init vars
	private T item;
	private Node<T> next;

	/**
	 * Node 
	 * constructor
	 * @param item, the value stored
	 */
	Node(T item) {
		this.item = item;
	}

	/**
	 * getNext
	 * this method returns the next node connected to the current one
	 * @return next, the next node
	 */
	public Node<T> getNext(){
		return this.next;
	}

	/**
	 * setNext
	 * this method sets the next node of the current one
	 * @param next, the next node
	 */
	public void setNext(Node<T> next){
		this.next = next;
	}

	/**
	 * getItem
	 * this method return the value stored in the current node
	 * @return item, value of current node
	 */
	public T getItem(){
		return this.item;
	}
}
