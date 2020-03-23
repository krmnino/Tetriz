public class Queue {
	private Node head;
	private Node tail;
	private Block hold;
	private boolean move_to_hold;
	private int size;
	private Block[][][] shape_grids;
	/* Hold shape: 0
	 * Queue 1: 1 (queue.head)
	 * Queue 2: 2 (queue.head.next)
	 * Queue 3: 3 (queue.head.next.next)
	 */
	private final int[][] ORTHOGONAL_COORDINATES = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};	
	//orthogonal coordinates for blocks orbiting the control block in an orthogonal position
	private final int[][] DIAGONAL_COORDINATES = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
	//diagonal coordinates for blocks orbiting the  control block in a diagonal position
	private final int DISPLAY_ROWS = 4;
	private final int DISPLAY_COLUMNS = 4;
	private final int MAX_SIZE = 5;
	
	public Queue() {
		this.head = null;
		this.tail = null;
		this.hold = null;
		this.move_to_hold = true;
		this.size = 0;
		this.shape_grids = new Block[4][4][4];
		for(int i = 0; i < 4; i++) { //iterate through every shape container
			for(int j = 0; j < 4; j++) { //iterate through every row in shape container
				for(int k = 0; k < 4; k++) { //iterate through every column in row
					this.shape_grids[i][j][k] = new Block(j, k); //initialize all 4 shape containers
					if(i != 0 && j == 1 && k == 1) { //if current row and column position is 1, set this block
						this.shape_grids[i][j][k].set_block();
					}
				}
			}
		}
	}
	
	private void map_shape(int position) {
		for(int i = 0; i < 3; i++) {	//for each orbiting block in control
			int row_subblock;		//declare variables for row and column sub-block coordinates 
			int column_subblock;
			switch(this.shape_grids[position][1][1].get_shape_type()) {	//switch case based on control's shape type
			case(0):
				return;
			case(1): //if shape is a line, then all orbiting blocks are orthogonal to the control block
				row_subblock = this.shape_grids[position][1][1].get_row_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][0];
				column_subblock = this.shape_grids[position][1][1].get_column_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][1];
				//calculate the orbiting sub-blocks with their respective coordinate offsets
				if(i < 2) { //if current block is less than 2, then set sub-block at the calculated row and column coordinates
					this.shape_grids[position][row_subblock][column_subblock].set_subblock(this.shape_grids[position][1][1]);
				}
				else {	//if i is 2 or greater, then set sub-block with an additional offset to make the line 4 blocks long
					if(this.shape_grids[position][1][1].get_orientation() == 0)	
						this.shape_grids[position][row_subblock + 1][column_subblock].set_subblock(this.shape_grids[position][1][1]);
					else if(this.shape_grids[position][1][1].get_orientation() == 1)
						this.shape_grids[position][row_subblock][column_subblock - 1].set_subblock(this.shape_grids[position][1][1]);
					else if(this.shape_grids[position][1][1].get_orientation() == 2)
						this.shape_grids[position][row_subblock - 1][column_subblock].set_subblock(this.shape_grids[position][1][1]);
					else 
						this.shape_grids[position][row_subblock][column_subblock + 1].set_subblock(this.shape_grids[position][1][1]);
				}
				break;
			case(5): //if shape is a T, then all orbiting sub-blocks have orthogonal coordinates 
				row_subblock = this.shape_grids[position][1][1].get_row_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][0];
				column_subblock = this.shape_grids[position][1][1].get_column_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][1];
				//calculate the orbiting sub-blocks with their respective coordinate offsets
				this.shape_grids[position][row_subblock][column_subblock].set_subblock(this.shape_grids[position][1][1]);
				//the the i'th sub-block using the coordinates calculated previously
				break;
			default: //in this case, the shape contains at least 1 block that uses diagonal coordinates (L, S, and Z shapes)
				if(i < 2) { //all blocks at i < 2 use orthogonal coordinates
					row_subblock = this.shape_grids[position][1][1].get_row_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][0];
					column_subblock = this.shape_grids[position][1][1].get_column_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][1];
					//calculate the orbiting sub-blocks with their respective coordinate offsets
					this.shape_grids[position][row_subblock][column_subblock].set_subblock(this.shape_grids[position][1][1]);
					//the the i'th sub-block using the coordinates calculated previously
				}
				else { //when i = 2, the second sub-block uses diagonal coordinates
					row_subblock = this.shape_grids[position][1][1].get_row_coords() + this.DIAGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][0];
					column_subblock = this.shape_grids[position][1][1].get_column_coords() + this.DIAGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][1];
					//calculate the orbiting sub-blocks with their respective coordinate offsets
					this.shape_grids[position][row_subblock][column_subblock].set_subblock(this.shape_grids[position][1][1]);
					//the the i'th sub-block using the coordinates calculated previously
				}	
			}
		}
	}
	
	private void unmap_shape(int position) {
		
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
		Node curr = this.head;
		for(int i = 0; i < this.shape_grids.length; i++) {
			switch(i) {
			case 0:
				//this.shape_grids[i][1][1].set_shape_type(curr.get_data().get_shape_type());
				map_shape(0);
				break;
			case 1:
				this.shape_grids[i][1][1].set_shape_type(curr.get_data().get_shape_type());
				map_shape(i);
				break;
			case 2:
				this.shape_grids[i][1][1].set_shape_type(curr.get_data().get_shape_type());
				map_shape(i);
				break;
			case 3:
				this.shape_grids[i][1][1].set_shape_type(curr.get_data().get_shape_type());
				map_shape(i);
				break;
			default:
				break;
			}
			curr = curr.get_next();
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
		for(int i = 0; i < DISPLAY_ROWS; i++) { //iterate through each row
			for(int j = 0; j < DISPLAY_COLUMNS; j++) { //iterate through each block in row
				if(j == DISPLAY_COLUMNS - 1) { //if j is at the last column of the grid
					out += this.shape_grids[position][i][j].display(); //append block.display() value to out string
					if(i != DISPLAY_ROWS - 1) { //if i is not at the last row in grid 
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