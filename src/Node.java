public class Node { //singly link list of Nodes containing Block objects
	private Block data; 
	private Node next;
	
	public Node(Block data_) { //constructor method Node class
		this.data = data_; //set data from parameter value
		this.next = null; //set next Node to null
	}
	
	public void set_data(Block data_) { //set data in Node
		this.data = data_;
	}
	
	public Block get_data() { //get data stored in Node
		return this.data;
	}
	
	public void set_next(Node next_) { //set next Node reference
		this.next = next_;
	}
	
	public Node get_next() { //get reference to next Node
		return this.next;
	}
}
