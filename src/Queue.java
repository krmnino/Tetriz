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
		Node curr;
		this.head = new Node(new Block(1, 1));
		this.head.get_data().set_shape_type();
		this.head.get_data().set_block();
		this.size++;
		curr = this.head.get_next();
		while(size < MAX_SIZE) {
			curr = new Node(new Block(1, 1));
			curr.get_data().set_shape_type();
			curr.get_data().set_block();
			curr = this.head.get_next();
			this.size++;
		}
		
	}
	public Block dequeue_shape() {
		Block out = this.head.get_data();
		this.head = this.head.get_next();
		Block new_tail = new Block(1, 1);
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
