import java.util.Arrays;

public class Grid {
	private final int ROWS = 22;	//constant number of rows in grid
	private final int COLUMNS = 11;	//constant number of columns in grid
	private final int CONTROL_ROW_COORDS = 1;	//row position where control block spawns
	private final int CONTROL_COLUMN_COORDS = 5;	//column position where control block spawns
	private final int[][] ORTHOGONAL_COORDINATES = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};	
	//orthogonal coordinates for blocks orbiting the control block in an orthogonal position
	private final int[][] DIAGONAL_COORDINATES = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
	//diagonal coordinates for blocks orbiting the  control block in a diagonal position
	private Block[][] grid;	//2D array of Block objects, blocks are not set by default
	private Block control;	//block instance that serves as a reference to the control block in the grid
	
	public Grid() {	//constructor method of Grid, takes no arguments
		this.grid = new Block[ROWS][COLUMNS];	//set size of 2D array to ROWS number of rows and COLUMNS number of columns
		this.control = null;	//set reference to control block to null
		initialize_grid();	//call initialize_grid fn() that initializes each block object in the grid
	}
	
	public Block get_control() {	//function that returns reference to control block
		return this.control;
	}
	
	public void initialize_grid() {	//initializes all block elements in the grid
		for(int i = 0; i < ROWS; i++) 	
			for(int j = 0; j < COLUMNS; j++)
				this.grid[i][j] = new Block(i, j);	//initialize block at i row and j column to i and j coordinates
	}
		
	private boolean check_control_spawn_collisions() {
		int[][] coords = new int[4][2]; //declare array that contains row and column coordinates of all 4 blocks in a shape
		if(this.control.get_shape_type() == 1) { //if control block shape is line
			coords[0][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(0)][0];
			//store control sub-block 0 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[0][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(0)][1];
			//store control sub-block 0 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[1][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(1)][0];
			//store control sub-block 1 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[1][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(1)][1];
			//store control sub-block 1 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + 1;
			//store control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes + 1
			coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1];
			//store control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[3][0] = this.control.get_row_coords();
			//third block is just the control block, store row coordinates
			coords[3][1] = this.control.get_column_coords();
			//third block is just the control block, store column coordinates
		}
		else if(this.control.get_shape_type() == 5) { //if control block is T shape
			for(int i = 0; i < 3; i++) { //iterate through all sub-blocks adding the offsets using orthogonal coordinates
				coords[i][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
				coords[i][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1];
			}
			coords[3][0] = this.control.get_row_coords();
			coords[3][1] = this.control.get_column_coords();
			//add offset to control block
		}
		else { //if control block is any other shape
			for(int i = 0; i < 3; i++) { //iterate through all sub-blocks 
				if(i < 2) { //if i < 2, then add the offsets using orthogonal coordinates
					coords[i][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
					coords[i][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1];
				}
				else { //if i is 2, then add the offsets using diagonal coordinates
					coords[i][0] = this.control.get_row_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][0];
					coords[i][1] = this.control.get_column_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][1];
				}
			}
			coords[3][0] = this.control.get_row_coords();
			coords[3][1] = this.control.get_column_coords();
			//add offset to control block
		}
		for(int i = 0; i < 4; i++) { //iterate through all coordinates in array
			if(coords[i][0] < 0 || coords[i][0] >= this.ROWS) 
				//if current row coordinates are less than 0 or greater than number of rows in grid
				return true; //there is a collision and return true
			if(coords[i][1] < 0 || coords[i][1] >= this.COLUMNS)
				//if current column coordinates are less than 0 or greater than number of columns in grid
				return true; //there is a collision and return true
			if(this.grid[coords[i][0]][coords[i][1]].get_isSet())
				//if block at the given coordinates is set and that block is not child of control block
				return true; //there is a collision and return true
		}
		return false; //else, there is no collisions and return false
	}
	
	public boolean set_control_block() {//set control block at the top of the grid
		this.control = this.grid[CONTROL_ROW_COORDS][CONTROL_COLUMN_COORDS]; //this.control to be block in grid at CRC and CCC
		this.control.set_shape_type(); //set shape type for control shape
		this.control.set_childOf(this.control); //make control block be child of itself
		if(!check_control_spawn_collisions()) { //if there is no collisions when spawning a shape
			this.control.set_block(); //set control block in grid
			map_shape(); //map shape in grid
			return true; //return true
		}
		return false; //if check_control_spawn_collisions() returns true, then return false (could not spawn block)
	}
	
	public boolean set_control_block(int shape_type_) { //set control block at the top of the grid taking integer as shape parameter
			this.control = this.grid[CONTROL_ROW_COORDS][CONTROL_COLUMN_COORDS]; //this.control to be block in grid at CRC and CCC
			this.control.set_shape_type(shape_type_); //set shape type for control shape with fn parameter
			this.control.set_childOf(this.control); //make control block be child of itself
			if(!check_control_spawn_collisions()) { //if there is no collisions when spawning a shape
				this.control.set_block(); //set control block in grid
				map_shape(); //map shape in grid
				return true; //return true
			}
			return false; //if check_control_spawn_collisions() returns true, then return false (could not spawn block)
	}
	
	protected void map_shape() {	//maps the current control block and orbiting sub-blocks in the grid
		for(int i = 0; i < 3; i++) {	//for each orbiting block in control
			int row_subblock;		//declare variables for row and column sub-block coordinates 
			int column_subblock;
			switch(this.control.get_shape_type()) {	//switch case based on control's shape type
			case(1): //if shape is a line, then all orbiting blocks are orthogonal to the control block
				row_subblock = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
				column_subblock = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1];
				//calculate the orbiting sub-blocks with their respective coordinate offsets
				if(i < 2) { //if current block is less than 2, then set sub-block at the calculated row and column coordinates
					this.grid[row_subblock][column_subblock].set_subblock(this.control);
				}
				else {	//if i is 2 or greater, then set sub-block with an additional offset to make the line 4 blocks long
					if(this.control.get_orientation() == 0)	
						this.grid[row_subblock + 1][column_subblock].set_subblock(this.control);
					else if(this.control.get_orientation() == 1)
						this.grid[row_subblock][column_subblock - 1].set_subblock(this.control);
					else if(this.control.get_orientation() == 2)
						this.grid[row_subblock - 1][column_subblock].set_subblock(this.control);
					else 
						this.grid[row_subblock][column_subblock + 1].set_subblock(this.control);
				}
				break;
			case(5): //if shape is a T, then all orbiting sub-blocks have orthogonal coordinates 
				row_subblock = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
				column_subblock = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1];
				//calculate the orbiting sub-blocks with their respective coordinate offsets
				this.grid[row_subblock][column_subblock].set_subblock(this.control);
				//the the i'th sub-block using the coordinates calculated previously
				break;
			default: //in this case, the shape contains at least 1 block that uses diagonal coordinates (L, S, and Z shapes)
				if(i < 2) { //all blocks at i < 2 use orthogonal coordinates
					row_subblock = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
					column_subblock = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1];
					//calculate the orbiting sub-blocks with their respective coordinate offsets
					this.grid[row_subblock][column_subblock].set_subblock(this.control);
					//the the i'th sub-block using the coordinates calculated previously
				}
				else { //when i = 2, the second sub-block uses diagonal coordinates
					row_subblock = this.control.get_row_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][0];
					column_subblock = this.control.get_column_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][1];
					//calculate the orbiting sub-blocks with their respective coordinate offsets
					this.grid[row_subblock][column_subblock].set_subblock(this.control);
					//the the i'th sub-block using the coordinates calculated previously
				}	
			}
		}
	}
	
	protected void unmap_control() {
		this.control.clear_block();
	}
	
	protected void unmap_shape() {	//unmaps the current control block and orbiting sub-blocks in the grid
		for(int i = 0; i < 3; i++) {	//for each orbiting block in control
			int row_subblock;	//declare variables for row and column sub-block coordinates 
			int column_subblock;	
			switch(this.control.get_shape_type()) {	//switch case based on control's shape type
			case(1):	//if shape is a line, then all orbiting blocks are orthogonal to the control block
				row_subblock = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
				column_subblock = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1];
				//calculate the orbiting sub-blocks with their respective coordinate offsets
				if(i < 2) {	//if current block is less than 2, then clear sub-block at the calculated row and column coordinates
					this.grid[row_subblock][column_subblock].clear_block();
				}
				else {	//if i is 2 or greater, then clear sub-block with an additional offset to make the line 4 blocks long
					if(this.control.get_orientation() == 0) 
						this.grid[row_subblock + 1][column_subblock].clear_block();
					else if(this.control.get_orientation() == 1)
						this.grid[row_subblock][column_subblock - 1].clear_block();
					else if(this.control.get_orientation() == 2)
						this.grid[row_subblock - 1][column_subblock].clear_block();
					else 
						this.grid[row_subblock][column_subblock + 1].clear_block();
				}
				break;
			case(5):	//if shape is a T, then all orbiting sub-blocks have orthogonal coordinates 
				row_subblock = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
				column_subblock = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1];
				//calculate the orbiting sub-blocks with their respective coordinate offsets
				this.grid[row_subblock][column_subblock].clear_block();
				//the the i'th sub-block using the coordinates calculated previously
				break;
			default:	//in this case, the shape contains at least 1 block that uses diagonal coordinates (L, S, and Z shapes)
				if(i < 2) {	//all blocks at i < 2 use orthogonal coordinates
					row_subblock = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
					column_subblock = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1];
					//calculate the orbiting sub-blocks with their respective coordinate offsets
					this.grid[row_subblock][column_subblock].clear_block();
					//the the i'th sub-block using the coordinates calculated previously
				}
				else { //all blocks at i > 2 use diagonal coordinates
					row_subblock = this.control.get_row_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][0];
					column_subblock = this.control.get_column_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][1];
					//calculate the orbiting sub-blocks with their respective coordinate offsets
					this.grid[row_subblock][column_subblock].clear_block();
					//the the i'th sub-block using the coordinates calculated previously
				}
			}
		}
	}

	private void shift_control_block(int row_offset, int column_offset) {	//shift control block by a given row/column offset
		Block new_control = this.grid[this.control.get_row_coords() + row_offset][this.control.get_column_coords() + column_offset];
		//create instance of block object with the same coordinates plus the desired offset
		new_control.copy_data(this.control);	//copy data from control block to new control
		this.control.clear_block();	//clear current control block from grid
		this.control = new_control;	//set new control to be the new control block in grid
		this.control.set_childOf(this.control);	//set new control childOf to be its own child
	}
	
	private boolean rotate_check_collisions() { //check for collisions before rotating and returns a boolean
		Block copy_control = new Block(this.control); //make a copy of control block
		int[][] coords = new int[4][2]; //create 2D array that contains new coordinates simulating rotation 
		if(copy_control.get_shape_type() == 1) { //if control block shape is line
			boolean limiting_border = false; //variable that determines if shape is limiting border in grid
			if(copy_control.get_column_coords() == 0 && copy_control.get_orientation() == 0) {
				//if line shape is in left-most column and orientation is 0
				copy_control.set_column_coords(copy_control.get_column_coords() + 2);
				limiting_border = true;
				//displace copy control block 2 columns to the right and set limiting_border to true
			}
			else if(copy_control.get_column_coords() == 0 && copy_control.get_orientation() == 2) {
				//if line shape is in left-most column and orientation is 2
				copy_control.set_column_coords(copy_control.get_column_coords() + 1);
				limiting_border = true;
				//displace copy control block 1 columns to the right and set limiting_border to true
			}
			else if(copy_control.get_column_coords() == this.COLUMNS - 1 && copy_control.get_orientation() == 0) {
				//if line shape is in right-most column and orientation is 0
				copy_control.set_column_coords(copy_control.get_column_coords() - 1);
				limiting_border = true;
				//displace copy control block 1 columns to the left and set limiting_border to true
			}
			else if(copy_control.get_column_coords() == this.COLUMNS - 1 && copy_control.get_orientation() == 2) {
				//if line shape is in right-most column and orientation is 2
				copy_control.set_column_coords(copy_control.get_column_coords() - 2);
				limiting_border = true;
				//displace copy control block 2 columns to the left and set limiting_border to true
			}
			else if(copy_control.get_row_coords() == this.ROWS - 1 && copy_control.get_orientation() == 3) {
				//if line shape is in bottom row and orientation is 3
				copy_control.set_row_coords(copy_control.get_row_coords() - 2);
				limiting_border = true;
				//displace copy control block 2 rows upwards and set limiting_border to true
			}
			else if(copy_control.get_row_coords() == this.ROWS - 1 && copy_control.get_orientation() == 1) {
				//if line shape is in bottom row and orientation is 1
				copy_control.set_row_coords(copy_control.get_row_coords() - 1);
				limiting_border = true;
				//displace copy control block 1 row upwards and set limiting_border to true
			}
			else if(this.grid[copy_control.get_row_coords()][copy_control.get_column_coords() - 1].get_isSet() 
					&& copy_control.get_orientation() == 0) { 
				//if block to the left of control block is set and orientation is 0
				copy_control.set_column_coords(copy_control.get_column_coords() + 2);
				limiting_border = true;
				//displace copy control block 2 columns to the right and set limiting_border to true
			}
			else if(this.grid[copy_control.get_row_coords()][copy_control.get_column_coords() - 1].get_isSet() 
					&& copy_control.get_orientation() == 2) {
				//if block to the left of control block is set and orientation is 2
				copy_control.set_column_coords(copy_control.get_column_coords() + 1);
				limiting_border = true;
				//displace copy control block 1 column to the right and set limiting_border to true
			}
			else if(this.grid[copy_control.get_row_coords()][copy_control.get_column_coords() + 1].get_isSet() 
					&& copy_control.get_orientation() == 0) {
				//if block to the right of control block is set and orientation is 0
				copy_control.set_column_coords(copy_control.get_column_coords() - 1);
				limiting_border = true;
				//displace copy control block 1 column to the left and set limiting_border to true
			}
			else if(this.grid[copy_control.get_row_coords()][copy_control.get_column_coords() + 1].get_isSet() 
					&& copy_control.get_orientation() == 2) {
				//if block to the right of control block is set and orientation is 2
				copy_control.set_column_coords(copy_control.get_column_coords() - 2);
				limiting_border = true;
				//displace copy control block 2 columns to the left and set limiting_border to true
			}
			else if(this.grid[copy_control.get_row_coords() + 1][copy_control.get_column_coords()].get_isSet() 
					&& copy_control.get_orientation() == 3) {
				//if block below of control block is set and orientation is 3
				copy_control.set_row_coords(copy_control.get_row_coords() - 2);
				limiting_border = true;
				//displace copy control block 2 rows upwards and set limiting_border to true
			}
			else if(this.grid[copy_control.get_row_coords() + 1][copy_control.get_column_coords()].get_isSet() 
					&& copy_control.get_orientation() == 1) {
				//if block below of control block is set and orientation is 1
				copy_control.set_row_coords(copy_control.get_row_coords() - 1);
				limiting_border = true;
				//displace copy control block 1 row upwards and set limiting_border to true
			}
			if(!limiting_border) { //if limiting border remains false
				switch(control.get_orientation()) { //switch through contol's orientation
				case(0): //if orientation is 0 
					copy_control.set_column_coords(copy_control.get_column_coords() + 1); //move copy control 1 column to the right
					break;
				case(1): //if orientation is 1
					copy_control.set_row_coords(copy_control.get_row_coords() + 1); //move copy control 1 row downwards
					break;
				case(2): //if orientation is 2
					copy_control.set_column_coords(copy_control.get_column_coords() - 1); //move copy control 1 column to the left
					break;
				case(3): //if orientation is 3
					copy_control.set_row_coords(copy_control.get_row_coords() - 1); //move copy control 1 row to the upwards
					break;
				default:
					break;
				}
			}
			copy_control.rotate_block(); //rotate copy control block (change in coordinates)
			coords[0][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(0)][0];
			//store copy control sub-block 0 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[0][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(0)][1];
			//store copy control sub-block 0 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[1][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(1)][0];
			//store copy control sub-block 1 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[1][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(1)][1];
			//store copy control sub-block 1 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			if(copy_control.get_orientation() == 0) { //if line orientation is 0
				coords[2][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][0] + 1;
				//store copy control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes + 1
				coords[2][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][1];
				//store copy control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			}
			else if(copy_control.get_orientation() == 1) { //if line orientation is 1
				coords[2][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][0];
				//store copy control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
				coords[2][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][1] - 1;
				//store copy control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes - 1
			}
			else if(copy_control.get_orientation() == 2) { //if line orientation is 2
				coords[2][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][0] - 1;
				//store copy control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes - 1
				coords[2][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][1];
				//store copy control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			}
			else { //if line orientation is 3
				coords[2][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][0];
				//store copy control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
				coords[2][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][1] + 1;
				//store copy control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes + 1
			}
			//third block is just the control block, store row and column coordinates
			coords[3][0] = copy_control.get_row_coords();
			coords[3][1] = copy_control.get_column_coords();
			
		}
		else if(copy_control.get_shape_type() != 2) { //if shape is any other shape (not line or square shape)
			if(copy_control.get_column_coords() == 0) //if copy control is in left most column
				copy_control.set_column_coords(copy_control.get_column_coords() + 1); //displace copy control 1 column to the right
			else if(copy_control.get_column_coords() == this.COLUMNS - 1) //if copy control is to the right most column
				copy_control.set_column_coords(copy_control.get_column_coords() - 1); //displace copy control 1 column to the left
			else if(copy_control.get_row_coords() == this.ROWS - 1) //if copy control is at the bottom of the grid
				copy_control.set_row_coords(copy_control.get_row_coords() - 1); //displace copy control 1 row upwards
			copy_control.rotate_block(); //rotate copy control block (change in coordinates)
			if(copy_control.get_shape_type() == 5) { //if shape is T-shape
				for(int i = 0; i < 3; i++) { //iterate through all blocks (except copy control block)
					coords[i][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(i)][0];
					//store copy control sub-block i row coordinates + orthogonal offset at the specified index of coordinate array of indexes
					coords[i][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(i)][1];
					//store copy control sub-block i column coordinates + orthogonal offset at the specified index of coordinate array of indexes
				}
				coords[3][0] = copy_control.get_row_coords();
				coords[3][1] = copy_control.get_column_coords();
			}
			else { //if shape is any other shape (not line, square, or T-shape)
				for(int i = 0; i < 3; i++) { //iterate through all blocks (except copy control block)
					if(i < 2) { //if i is less than 2
						coords[i][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(i)][0];
						//store copy control sub-block i row coordinates + orthogonal offset at the specified index of coordinate array of indexes
						coords[i][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(i)][1];
						//store copy control sub-block i column coordinates + orthogonal offset at the specified index of coordinate array of indexes
					}
					else { //is i is 2
						coords[i][0] = copy_control.get_row_coords() + this.DIAGONAL_COORDINATES[copy_control.get_index(i)][0];
						//store copy control sub-block 2 row coordinates + diagonal offset at the specified index of coordinate array of indexes
						coords[i][1] = copy_control.get_column_coords() + this.DIAGONAL_COORDINATES[copy_control.get_index(i)][1];
						//store copy control sub-block 2 column coordinates + diagonal offset at the specified index of coordinate array of indexes
					}
				}
				//third block is just the control block, store row and column coordinates
				coords[3][0] = copy_control.get_row_coords();
				coords[3][1] = copy_control.get_column_coords();
			}
		}
		for(int i = 0; i < 4; i++) { //iterate through all coordinates in the array
			if(coords[i][0] < 0 || coords[i][0] >= this.ROWS) //if i-th block row coordinates are less than 0 or greater than number of rows...
				return true; //there is a collision
			if(coords[i][1] < 0 || coords[i][1] >= this.COLUMNS) //if i-th block column coordinates are less than 0 or greater than number of columns...
				return true; //there is a collision
			if(this.grid[coords[i][0]][coords[i][1]].get_isSet() && this.grid[coords[i][0]][coords[i][1]].get_childOf() != this.control)
				return true; //there is a collision
		}
		return false; //else, there is no collision and return false
	}
	
	public void rotate_shape() { //function that checks for collision. If none, then perform rotation in control block
		if(!rotate_check_collisions()) { //if no collisions, then check for shape of control block 
			if(this.control.get_shape_type() == 1) { //if control block shape is a line
				unmap_shape(); //clear control block and its sub-blocks from grid
				boolean limiting_border = false; //variable that determines if shape is limiting border in grid
				if(this.control.get_column_coords() == 0 && this.control.get_orientation() == 0) {
					//if line shape is in left-most column and orientation is 0
					shift_control_block(0, 2);
					limiting_border = true;
					//displace control block 2 columns to the right and set limiting_border to true
				}
				else if(this.control.get_column_coords() == 0 && this.control.get_orientation() == 2) {
					//if line shape is in left-most column and orientation is 2
					shift_control_block(0, 1);
					limiting_border = true;
					//displace control block 1 columns to the right and set limiting_border to true
				}
				
				else if(this.control.get_column_coords() == this.COLUMNS - 1 && this.control.get_orientation() == 0) {
					//if line shape is in right-most column and orientation is 0
					shift_control_block(0, -1);
					limiting_border = true;
					//displace control block 1 columns to the left and set limiting_border to true
				}
				else if(this.control.get_column_coords() == this.COLUMNS - 1 && this.control.get_orientation() == 2) {
					//if line shape is in right-most column and orientation is 2
					shift_control_block(0, -2);
					limiting_border = true;
					//displace control block 2 columns to the left and set limiting_border to true
				}
				else if(this.control.get_row_coords() == this.ROWS - 1 && this.control.get_orientation() == 3) {
					//if line shape is in bottom row and orientation is 3
					shift_control_block(-2, 0);
					limiting_border = true;
					//displace control block 2 rows upwards and set limiting_border to true
				}
				else if(this.control.get_row_coords() == this.ROWS - 1 && this.control.get_orientation() == 1) {
					//if line shape is in bottom row and orientation is 1
					shift_control_block(-1, 0);
					limiting_border = true;
					//displace control block 1 row upwards and set limiting_border to true
				}
				else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() - 1].get_isSet() 
						&& this.control.get_orientation() == 0) { 
					//if block to the left of control block is set and orientation is 0
					shift_control_block(0, 2);
					limiting_border = true;
					//displace control block 2 columns to the right and set limiting_border to true
				}
				else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() - 1].get_isSet() 
						&& this.control.get_orientation() == 2) {
					//if block to the left of control block is set and orientation is 2
					shift_control_block(0, 1);
					limiting_border = true;
					//displace control block 1 column to the right and set limiting_border to true
				}
				else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() + 1].get_isSet() 
						&& this.control.get_orientation() == 0) {
					//if block to the right of control block is set and orientation is 0
					shift_control_block(0, -1);
					limiting_border = true;
					//displace control block 1 column to the left and set limiting_border to true
				}
				else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() + 1].get_isSet()
						&& this.control.get_orientation() == 2) {
					//if block to the right of control block is set and orientation is 2
					shift_control_block(0, -2);
					limiting_border = true;
					//displace control block 2 columns to the left and set limiting_border to true
				}
				else if(this.grid[this.control.get_row_coords() + 1][this.control.get_column_coords()].get_isSet() 
						&& this.control.get_orientation() == 3) {
					//if block below of control block is set and orientation is 3
					shift_control_block(-2, 0);
					limiting_border = true;
					//displace copy control block 2 rows upwards and set limiting_border to true
				}
				else if(this.grid[this.control.get_row_coords() + 1][this.control.get_column_coords()].get_isSet() 
						&& this.control.get_orientation() == 1) {
					//if block below of control block is set and orientation is 1
					shift_control_block(-1, 0);
					limiting_border = true;
					//displace copy control block 1 row upwards and set limiting_border to true
				}
				if(!limiting_border) { //if limiting border remains false
					switch(control.get_orientation()) {
					case(0): //if orientation is 0 
						shift_control_block(0, 1); //move copy control 1 column to the right
						break;
					case(1): //if orientation is 1 
						shift_control_block(1, 0); //move copy control 1 row downwards
						break;
					case(2): //if orientation is 2 
						shift_control_block(0, -1); //move copy control 1 column to the left
						break;
					case(3): //if orientation is 3 
						shift_control_block(-1, 0); //move copy control 1 row to the upwards
						break;
					default:
						break;
					}
				}	
				this.control.rotate_block(); //rotate control block by increasing its orientation value by 1
				map_shape(); //map line shape after rotation operation was performed
			}
			else if(this.control.get_shape_type() != 2) { //if shape is any other figure other than square shape
				unmap_shape(); //clear control block and its sub-blocks from grid
				if(this.control.get_column_coords() == 0) //if control block is at the left-most column
					shift_control_block(0, 1); //shift control block 1 column to the right
				else if(this.control.get_column_coords() == this.COLUMNS - 1) //if control block is at the right-most column
					shift_control_block(0, -1); //shift control block 1 column to the left
				else if(this.control.get_row_coords() == this.ROWS - 1) //if control block is at the lowest row in the grid 
					shift_control_block(-1, 0); //shift control block 1 row upwards
				this.control.rotate_block(); //rotate control block by increasing its orientation value by 1
				map_shape(); //map line shape after rotation operation was performed
			}
		}
	}
	
	private boolean move_check_collisions(int row_offset, int column_offset) { //function that checks if there is a collision when shape moves
		int[][] coords = new int[4][2]; //declare array that contains row and column coordinates of all 4 blocks in a shape
		if(this.control.get_shape_type() == 1) { //if control block shape is line
			coords[0][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(0)][0] + row_offset;
			//store control sub-block 0 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[0][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(0)][1] + column_offset;
			//store control sub-block 0 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[1][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(1)][0] + row_offset;
			//store control sub-block 1 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
			coords[1][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(1)][1] + column_offset;
			//store control sub-block 1 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			if(this.control.get_orientation() == 0) { //if line orientation is 0
				coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + row_offset + 1;
				//store control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes + 1
				coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1] + column_offset;
				//store control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			}
			else if(this.control.get_orientation() == 1) { //if line orientation is 1
				coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + row_offset;
				//store control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
				coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1] + column_offset - 1;
				//store control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes - 1
			}
			else if(this.control.get_orientation() == 2) { //if line orientation is 2
				coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + row_offset - 1;
				//store control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes - 1
				coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1] + column_offset;
				//store control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes
			}
			else { //if line orientation is 3
				coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + row_offset;
				//store control sub-block 2 row coordinates + orthogonal offset at the specified index of coordinate array of indexes
				coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1] + column_offset + 1;
				//store control sub-block 2 column coordinates + orthogonal offset at the specified index of coordinate array of indexes + 1
			}
			//third block is just the control block, store row and column coordinates
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + column_offset;
		}
		else if(this.control.get_shape_type() == 5) { //if control block is T shape
			for(int i = 0; i < 3; i++) { //iterate through all sub-blocks adding the offsets using orthogonal coordinates
				coords[i][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0] + row_offset;
				coords[i][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1] + column_offset;
			}
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + column_offset;
			//add offset to control block
		}
		else { //if control block is any other shape
			for(int i = 0; i < 3; i++) { //iterate through all sub-blocks 
				if(i < 2) { //if i < 2, then add the offsets using orthogonal coordinates
					coords[i][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0] + row_offset;
					coords[i][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1] + column_offset;
				}
				else { //if i is 2, then add the offsets using diagonal coordinates
					coords[i][0] = this.control.get_row_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][0] + row_offset;
					coords[i][1] = this.control.get_column_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][1] + column_offset;
				}
			}
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + column_offset;
			//add offset to control block
		}
		for(int i = 0; i < 4; i++) { //iterate through all coordinates in array
			if(coords[i][0] < 0 || coords[i][0] >= this.ROWS) 
				//if current row coordinates are less than 0 or greater than number of rows in grid
				return true; //there is a collision and return true
			if(coords[i][1] < 0 || coords[i][1] >= this.COLUMNS)
				//if current column coordinates are less than 0 or greater than number of columns in grid
				return true; //there is a collision and return true
			if(this.grid[coords[i][0]][coords[i][1]].get_isSet() && this.grid[coords[i][0]][coords[i][1]].get_childOf() != this.control)
				//if block at the given coordinates is set and that block is not child of control block
				return true; //there is a collision and return true
		}
		return false; //else, there is no collisions and return false
	}
	
	public boolean move_shape_down() { //function that moves shape down by one block
		if(!move_check_collisions(1, 0)) { //if there is no collisions moving the block 1 block down
			unmap_shape(); //unmap shape from grid
			shift_control_block(1, 0); //actually move the shape 1 block down
			map_shape(); //remap the shape in grid
			return true; //return true telling the caller that the shape successfully moved 1 block down
		}
		else
			return false; //if move_check_collisions() returns false, then return false to the caller 
	}

	public void move_shape_left() { //function that moves shape left by one block
		if(!move_check_collisions(0, -1)) { //if there is no collisions moving the block 1 block to the left
			unmap_shape(); //unmap shape from grid
			shift_control_block(0, -1); //actually move the shape 1 block to the left
			map_shape(); //remap the shape in grid
		}	
	}
	
	public void move_shape_right() { //function that moves shape right by one block
		if(!move_check_collisions(0, 1)) { //if there is no collisions moving the block 1 block to the right
			unmap_shape(); //unmap shape from grid
			shift_control_block(0, 1); //actually move the shape 1 block to the right
			map_shape(); //remap the shape in grid
		}
	}
	
	public void send_shape_down() {
		int row_offset = 0; //accumulator that counts how many row can the shape move downwards
		while(true) { //while there is no collisions...
			if(!move_check_collisions(row_offset, 0)) //check collisions at row_offset number of rows
				row_offset++; //increase accumulator by 1
			else 
				break; //if there is a collision, break the while loop
		}
		unmap_shape(); //unmap shape from grid
		shift_control_block(row_offset - 1, 0); //shift control block by the row offset value minus 1
		map_shape(); //remap the shape in grid
	}
	
	private void shift_rows_down(int row) { //shift all rows above a specific row down
		for(int i = row; i > 0; i--) { //from row to the top of the grid...
			for(int j = 0; j < this.COLUMNS; j++) { //iterate through each block in a row
				this.grid[i][j].clear_block(); //clear each block in row
				this.grid[i][j].copy_data(this.grid[i - 1][j]); //copy the blocks from the row above
			}
		}
	}
	
	private boolean is_line_complete(int row) { //function that checks if a given row is complete
		for(int i = 0; i < this.COLUMNS; i++) { //iterate through all blocks in a specified row
			if(!this.grid[row][i].get_isSet()) //if block in row is not set
				return false; //then line is not complete and return false
		}
		return true; //if for loop cycles through the entire row, then the row is complete
	}
	
	public int check_completed_lines() { //check completed rows in grid
		int lines_completed = 0; //initialize variable that counts number of lines complete
		int[] row_coords = new int[4]; //declare array to contain row coordinates of all 4 blocks in a shape
		for(int i = 0; i < row_coords.length; i++) //initialize all coordinate values in array to -1
			row_coords[i] = -1;
		if(this.control.get_shape_type() == 1) { //if control block shape is a line
			row_coords[0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(0)][0];
			row_coords[1] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(1)][0];
			//obtain and store in array row coordinates of 2 adjacent sub-blocks to the control block 
			if(this.control.get_orientation() == 0) //if control block orientation is 0
				row_coords[2] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + 1;
				//row coordinates for the 3rd sub-block must have and offset of + 1
			else if(this.control.get_orientation() == 1) //if control block orientation is 1 
				row_coords[2] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0];
				//row coordinates for the 3rd sub-block should not have an offset
			else if(this.control.get_orientation() == 2) //if control block orientation is 2
				row_coords[2] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] - 1;
				//row coordinates for the 3rd sub-block must have and offset of - 1
			else //if control block orientation is 3
				row_coords[2] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0];
				//row coordinates for the 3rd sub-block should not have an offset
			row_coords[3] = this.control.get_row_coords();
			//add row coordinates of control block to the array
		}
		else if(this.control.get_shape_type() == 5) { //if control block shape is a T shape
			for(int i = 0; i < 3; i++) //iterate through all sub-blocks orbiting control block
				row_coords[i] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
				//add row coordinates to array
			row_coords[3] = this.control.get_row_coords();
			//add row coordinates of control block to the array
		}
		else { //if control block is any other shape
			for(int i = 0; i < 3; i++) { //iterate through all sub-blocks orbiting control block
				if(i < 2) //if i < 2, add row coordinates to array using orthogonal coordinates
					row_coords[i] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
				else //if i = 2, add row coordinates to array using diagonal coordinates
					row_coords[i] = this.control.get_row_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][0];
			}
			row_coords[3] = this.control.get_row_coords();
			//add row coordinates of control block to the array
		}
		Arrays.sort(row_coords); //sort array of coordinates
		//this nested for-loop set repeated coordinate values to -1
		for(int i = 0; i < row_coords.length; i++) { //iterate through row coordinate array
			for(int j = 0; j < row_coords.length; j++) { //second index to iterate through row coordinate array and compare later
				if(j == i || row_coords[j] == -1) //if both indexes are the same or current value at index j is -1
					continue; //jump to the next value by increasing index j
				else if(row_coords[i] == row_coords[j]) //if element at index i equals element at index j
					row_coords[j] = -1; //set value at index j to -1
			}
		}
		for(int i = 0; i < row_coords.length; i++) { //iterate through array of row coordinates
			if(row_coords[i] != -1) { //if current value is not -1
				if(is_line_complete(row_coords[i])) { //check if row at row coordinate is complete
					lines_completed++; //increase counter by 1
					shift_rows_down(row_coords[i]); //if return true, then shift rows down
				}
			}
		}
		return lines_completed;
	}
	
	public String toString() { //displays grid using ASCII characters
		String out = ""; //"._._._._._._._._._._._.\n"; //initialize out string with the header of grid
		for(int i = 0; i < this.ROWS; i++) { //iterate through each row
			for(int j = 0; j < this.COLUMNS; j++) { //iterate through each block in row
				if(j == this.COLUMNS - 1) { //if j is at the last column of the grid
					out += this.grid[i][j].display(); //append block.display() value to out string
					if(i != this.ROWS - 1) //if i is not at the last row in grid 
						out += "\n";
						//out += "|\n|"; //append |, add new line, and add another |
					//else //if i is at the last row in grid
						//out += "|"; //just append |
				}
				else //if j is not at the last column in row
					out += this.grid[i][j].display(); //+ "|"; //append block.display() value and |
			}
		}
		return out; //return out string to the caller
	}
}
