package data_structures;

public class Node<T> {
	private T item;
	private Node<T> next;

	Node(T item) {
		this.item = item;
	}

	public Node<T> getNext(){
		return this.next;
	}

	public void setNext(Node<T> next){
		this.next = next;
	}

	public T getItem(){
		return this.item;
	}
}
