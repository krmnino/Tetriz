public class Node {
	private Block data;
	private Node previous;
	private Node next;
	
	public Node(Block data_) {
		this.data = data_;
		this.previous = null;
		this.next = null;
	}
}
