public class Queue {
	private Node head;
	private Node tail;
	private Block hold;
	private boolean move_to_hold;
	private int size;
	private final int MAX_SIZE = 5;
	
	public Queue() {
		this.head = null;
		this.tail = null;
		this.hold = null;
		this.move_to_hold = true;
		this.size = 0;
	}
	
	public void populate() {
		this.tail = new Node(new Block(1, 1));
		this.tail.get_data().set_shape_type();
		this.tail.get_data().set_block();
		this.head = this.tail;
		this.size++;
		while(size < MAX_SIZE) {
			Node new_element = new Node(new Block(1, 1));
			new_element.get_data().set_shape_type();
			new_element.get_data().set_block();
			this.tail.set_next(new_element);
			this.tail = new_element;
			this.size++;
		}
		
	}
	
	public Block dequeue_shape() {
		Block out = this.head.get_data();
		this.head = this.head.get_next();
		Node new_element = new Node(new Block(1, 1));
		new_element.get_data().set_shape_type();
		new_element.get_data().set_block();
		this.tail.set_next(new_element);
		this.tail = new_element;
		this.size++;
		return out;
	}
	
	public void set_hold(int shape_type_) {
		this.hold = new Block(1, 1);
		this.hold.set_shape_type(shape_type_);
		this.hold.set_block();
	}
	
	public void hold_shape() {
		if(this.move_to_hold) {
			this.hold = this.dequeue_shape();
			this.move_to_hold = false;
		}
	}
	
	public Block get_hold() {
		return this.hold;
	}
	
	public void set_move_to_hold(boolean flag) {
		this.move_to_hold = flag;
	}
		
	public boolean get_move_to_hold() {
		if(this.move_to_hold) {
			return true;
		}
		return false;
	}
	
	public void print_shape(int position) {
	}
	
	public String toString() {
		String out = "";
		Node curr = this.head;
		while(curr != null) {
			out += curr.get_data().toString() + "->";
			curr = curr.get_next();
		}
		if(this.hold != null) {
			out += "HOLD: " + this.hold.toString();
		}
		return out;
	}
}
