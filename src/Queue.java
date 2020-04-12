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
					this.shape_grids[position][row_subblock][column_subblock].clear_block();
				}
				else {	//if i is 2 or greater, then set sub-block with an additional offset to make the line 4 blocks long
					if(this.shape_grids[position][1][1].get_orientation() == 0)	
						this.shape_grids[position][row_subblock + 1][column_subblock].clear_block();
					else if(this.shape_grids[position][1][1].get_orientation() == 1)
						this.shape_grids[position][row_subblock][column_subblock - 1].clear_block();
					else if(this.shape_grids[position][1][1].get_orientation() == 2)
						this.shape_grids[position][row_subblock - 1][column_subblock].clear_block();
					else 
						this.shape_grids[position][row_subblock][column_subblock + 1].clear_block();
				}
				break;
			case(5): //if shape is a T, then all orbiting sub-blocks have orthogonal coordinates 
				row_subblock = this.shape_grids[position][1][1].get_row_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][0];
				column_subblock = this.shape_grids[position][1][1].get_column_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][1];
				//calculate the orbiting sub-blocks with their respective coordinate offsets
				this.shape_grids[position][row_subblock][column_subblock].clear_block();
				//the the i'th sub-block using the coordinates calculated previously
				break;
			default: //in this case, the shape contains at least 1 block that uses diagonal coordinates (L, S, and Z shapes)
				if(i < 2) { //all blocks at i < 2 use orthogonal coordinates
					row_subblock = this.shape_grids[position][1][1].get_row_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][0];
					column_subblock = this.shape_grids[position][1][1].get_column_coords() + this.ORTHOGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][1];
					//calculate the orbiting sub-blocks with their respective coordinate offsets
					this.shape_grids[position][row_subblock][column_subblock].clear_block();
					//the the i'th sub-block using the coordinates calculated previously
				}
				else { //when i = 2, the second sub-block uses diagonal coordinates
					row_subblock = this.shape_grids[position][1][1].get_row_coords() + this.DIAGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][0];
					column_subblock = this.shape_grids[position][1][1].get_column_coords() + this.DIAGONAL_COORDINATES[this.shape_grids[position][1][1].get_index(i)][1];
					//calculate the orbiting sub-blocks with their respective coordinate offsets
					this.shape_grids[position][row_subblock][column_subblock].clear_block();
					//the the i'th sub-block using the coordinates calculated previously
				}	
			}
		}
	}
	
	public void populate() { //populate queue with 5 shapes
		this.tail = new Node(new Block(1, 1)); //initialize tail node with a new block
		this.tail.get_data().set_shape_type(); //set shape type for the first block
		this.tail.get_data().set_block(); //set "set" first block to true
		this.head = this.tail; //since there is one element in queue -> head = tail
		this.size++; //increase size by 1
		while(size < MAX_SIZE) { //while current size is less than max possible size
			Node new_element = new Node(new Block(1, 1)); //create new node and initialize it with a block
			new_element.get_data().set_shape_type(); //set shape type for the current node block
			new_element.get_data().set_block(); //set "set" current block to true
			this.tail.set_next(new_element); //enqueue current node block to queue
			this.tail = new_element; //set tail queue equal to current node
			this.size++; //increase size by 1
		}
		Node curr = this.head;  //create curr node to traverse across the queue
		for(int i = 0; i < this.shape_grids.length; i++) { //for each shape grid that displays queue and hold shapes
			if(i == 0) { //if i is 0, then map grid 0 with no shape
				map_shape(0);
			}
			else { //else map shapes from queue with their respective shape types using curr
				this.shape_grids[i][1][1].set_shape_type(curr.get_data().get_shape_type());
				map_shape(i);
			}
			curr = curr.get_next(); //set curr to the next shape in queue
		}
	}
	
	public Block dequeue_shape() { 
		Block out = this.head.get_data(); //store queue tail in block variable to return it after
		this.head = this.head.get_next(); //set queue head equal to node next to tail
		Node new_element = new Node(new Block(1, 1)); //initialize node with new Block object
		new_element.get_data().set_shape_type(); //set block shape type 
		new_element.get_data().set_block(); //set "set" block variable to true
		this.tail.set_next(new_element); //set tail next equal to new node
		this.tail = new_element; //enqueue new element to the end of queue
		Node curr = this.head; //set curr node equal to head
		for(int i = 1; i < this.shape_grids.length; i++) { //start for loop from 1 so only update queue grids
			unmap_shape(i); //unmap current grid
			this.shape_grids[i][1][1].set_shape_type(curr.get_data().get_shape_type()); //change current grid position central block shape
			map_shape(i); //map current grid with updated shape
			curr = curr.get_next(); //set current node equal to the next element in grid
		}
		return out; //return out block
	}
	
	public void hold_shape(int shape_type) { //hold control block and place it in grid
		unmap_shape(0); //unmap shape from hold grid
		if(this.move_to_hold) { //if move_to_hold flag is true
			this.hold = new Block(1, 1); //initialized this.hold block 
		}
		this.hold.set_block(); //set "set" this.hold variable to true
		this.hold.set_shape_type(shape_type); //set this.hold shape type to true
		this.shape_grids[0][1][1].copy_data(this.hold); //copy data from this.hold block to grid
		this.move_to_hold = false; //set move to hold to false
		map_shape(0);
	}
	
	public Block get_hold() { //get this.hold block
		return this.hold;
	}
	
	public void set_move_to_hold(boolean flag) { //set move_to_hold flag to either true or false
		this.move_to_hold = flag;
	}
		
	public boolean get_move_to_hold() { //get move_to_hold flag status
		return this.move_to_hold;
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
	
	public String toString() { //return queue and hold as a string
		String out = ""; //declare and initialize return string variable
		Node curr = this.head; //set curr node equal to queue head
		while(curr != null) { //while curr is not null
			out += curr.get_data().toString() + "->"; //append curr block shape to string
			curr = curr.get_next(); //set curr to the next block in queue 
		}
		if(this.hold != null) { //if this.hold shape is null, append Hold to string
			out += "HOLD: " + this.hold.toString();
		}
		return out; //return string
	}
}