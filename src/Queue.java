public class Queue {
	private Node head;
	private Node tail;
	private Block hold;
	private boolean move_to_hold;
	private int size;
	private Block[][][] shape_grids;
	private final int ROWS = 4;
	private final int COLUMNS = 4;
	private final int MAX_SIZE = 5;
	
	public Queue() {
		this.head = null;
		this.tail = null;
		this.hold = null;
		this.move_to_hold = true;
		this.size = 0;
		this.shape_grids = new Block[4][4][4];
		for(int i = 0; i < 4; i++) {	
			for(int j = 0; j < 4; j++) {
				for(int k = 0; k < 4; k++) {
					this.shape_grids[i][j][k] = new Block(j, k); //initialize all 4 shape containers
				}
			}
		}
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
		for(int i = 0; i < this.shape_grids.length; i++) {
			if()
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
	
	public String print_shape(int position) {
		String out = ""; //initialize out string with the header of grid
		for(int i = 0; i < ROWS; i++) { //iterate through each row
			for(int j = 0; j < COLUMNS; j++) { //iterate through each block in row
				if(j == COLUMNS - 1) { //if j is at the last column of the grid
					out += this.shape_grids[position][i][j].display(); //append block.display() value to out string
					if(i != ROWS - 1) { //if i is not at the last row in grid 
						out += "\n";
					}
				}
				else { //if j is not at the last column in row
					out += this.shape_grids[position][i][j].display(); //append block.display() value
				}
			}
		}
		return out; //return out string to the caller
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