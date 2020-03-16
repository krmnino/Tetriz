public class Node {
	private Block data;
	private Node next;
	
	public Node(Block data_) {
		this.data = data_;
		this.next = null;
	}
	
	public void set_data(Block data_) {
		this.data = data_;
	}
	
	public Block get_data() {
		return this.data;
	}
	
	public void set_next(Node next_) {
		this.next = next_;
	}
	
	public Node get_next() {
		return this.next;
	}
}
