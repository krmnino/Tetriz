public class Queue {
	private Node head;
	private Node tail;
	private int size;
	private final int MAX_SIZE = 5;
	
	public Queue() {
		this.head = null;
		this.tail = null;
		this.size = 0;
	}
	public void populate() {
		
	}
	public Block dequeue_shape() {
		Block out = this.head.get_data();
		this.head = this.head.get_next();
		Block new_tail = new Block();
		new_tail.set_shape_type();
		new_tail.set_block();
		this.tail = new Node(new_tail);
		return out;
	}
	public void swap_first_and_second() {
		
	}
	public void print_shape(int position) {
		
	}
}
