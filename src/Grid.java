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
	
	public Block get_control() {	//function that returns reference to control block (used for debugging purposes)
		return this.control;
	}
	
	public void initialize_grid() {	//initializes all block elements in the grid
		for(int i = 0; i < ROWS; i++) 	
			for(int j = 0; j < COLUMNS; j++)
				this.grid[i][j] = new Block(i, j);	//initialize block at i row and j column to i and j coordinates
	}
	
	public void set_control_block() { //set control block at the top of the grid
		this.control = this.grid[CONTROL_ROW_COORDS][CONTROL_COLUMN_COORDS];	//
		this.control.set_block();
		this.control.set_shape_type();
		this.control.set_childOf(this.control);
		map_shape();
	}
	
	private void map_shape() {	//maps the current control block and orbiting sub-blocks in the grid
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
	
	private void unmap_shape() {	//unmaps the current control block and orbiting sub-blocks in the grid
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
				else {
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
	
	private boolean rotate_check_collisions() {
		Block copy_control = new Block(this.control);
		int[][] coords = new int[4][2];
		if(copy_control.get_shape_type() == 1) {
			boolean limiting_border = false;
			if(copy_control.get_column_coords() == 0 && copy_control.get_orientation() == 0) {
				copy_control.set_column_coords(copy_control.get_column_coords() + 2);
				limiting_border = true;
			}
			else if(copy_control.get_column_coords() == 0 && copy_control.get_orientation() == 2) {
				copy_control.set_column_coords(copy_control.get_column_coords() + 1);
				limiting_border = true;
			}
			
			else if(copy_control.get_column_coords() == this.COLUMNS - 1 && copy_control.get_orientation() == 0) {
				copy_control.set_column_coords(copy_control.get_column_coords() - 1);
				limiting_border = true;
			}
			else if(copy_control.get_column_coords() == this.COLUMNS - 1 && copy_control.get_orientation() == 2) {
				copy_control.set_column_coords(copy_control.get_column_coords() - 2);
				limiting_border = true;
			}
			else if(copy_control.get_row_coords() == this.ROWS - 1 && copy_control.get_orientation() == 3) {
				copy_control.set_row_coords(copy_control.get_row_coords() - 2);
				limiting_border = true;
			}
			else if(copy_control.get_row_coords() == this.ROWS - 1 && copy_control.get_orientation() == 1) {
				copy_control.set_row_coords(copy_control.get_row_coords() - 1);
				limiting_border = true;
			}
			else if(this.grid[copy_control.get_row_coords()][copy_control.get_column_coords() - 1].get_isSet() 
					&& copy_control.get_orientation() == 0) { //check if next block is set
				copy_control.set_column_coords(copy_control.get_column_coords() + 2);
				limiting_border = true;
			}
			else if(this.grid[copy_control.get_row_coords()][copy_control.get_column_coords() - 1].get_isSet() 
					&& copy_control.get_orientation() == 2) {
				copy_control.set_column_coords(copy_control.get_column_coords() + 1);
				limiting_border = true;
			}
			else if(this.grid[copy_control.get_row_coords()][copy_control.get_column_coords() + 1].get_isSet() 
					&& copy_control.get_orientation() == 0) {
				copy_control.set_column_coords(copy_control.get_column_coords() - 1);
				limiting_border = true;
			}
			else if(this.grid[copy_control.get_row_coords()][copy_control.get_column_coords() - 1].get_isSet() 
					&& copy_control.get_orientation() == 2) {
				copy_control.set_column_coords(copy_control.get_column_coords() - 2);
				limiting_border = true;
			}
			else if(this.grid[copy_control.get_row_coords() + 1][copy_control.get_column_coords()].get_isSet() 
					&& copy_control.get_orientation() == 3) {
				copy_control.set_row_coords(copy_control.get_row_coords() - 2);
				limiting_border = true;
			}
			else if(this.grid[copy_control.get_row_coords() + 1][copy_control.get_column_coords()].get_isSet() 
					&& copy_control.get_orientation() == 1) {
				copy_control.set_row_coords(copy_control.get_row_coords() - 1);
				limiting_border = true;
			}
			if(!limiting_border) {
				switch(control.get_orientation()) {
				case(0):
					copy_control.set_column_coords(copy_control.get_column_coords() + 1);
					break;
				case(1):
					copy_control.set_row_coords(copy_control.get_row_coords() + 1);
					break;
				case(2):
					copy_control.set_column_coords(copy_control.get_column_coords() - 1);
					break;
				case(3):
					copy_control.set_row_coords(copy_control.get_row_coords() - 1);
					break;
				default:
					break;
				}
			}
			copy_control.rotate_block();
			coords[0][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(0)][0];
			coords[0][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(0)][1];
			coords[1][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(1)][0];
			coords[1][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(1)][1];
			if(copy_control.get_orientation() == 0) {
				coords[2][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][0] + 1;
				coords[2][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][1];
			}
			else if(copy_control.get_orientation() == 1) {
				coords[2][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][0];
				coords[2][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][1] - 1;
			}
			else if(copy_control.get_orientation() == 2) {
				coords[2][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][0] - 1;
				coords[2][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][1];
			}
			else {
				coords[2][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][0];
				coords[2][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(2)][1] + 1;
			}
			coords[3][0] = copy_control.get_row_coords();
			coords[3][1] = copy_control.get_column_coords();
			
		}
		else if(copy_control.get_shape_type() != 2) {
			if(copy_control.get_column_coords() == 0) 
				copy_control.set_column_coords(copy_control.get_column_coords() + 1);
			else if(copy_control.get_column_coords() == this.COLUMNS - 1) 
				copy_control.set_column_coords(copy_control.get_column_coords() - 1);
			else if(copy_control.get_row_coords() == this.ROWS - 1) 
				copy_control.set_row_coords(copy_control.get_row_coords() - 1);
			copy_control.rotate_block();
			if(copy_control.get_shape_type() == 5) {
				for(int i = 0; i < 3; i++) {
					coords[i][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(i)][0];
					coords[i][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(i)][1];
				}
				coords[3][0] = copy_control.get_row_coords();
				coords[3][1] = copy_control.get_column_coords();
			}
			else {
				for(int i = 0; i < 3; i++) {
					if(i < 2) {
						coords[i][0] = copy_control.get_row_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(i)][0];
						coords[i][1] = copy_control.get_column_coords() + this.ORTHOGONAL_COORDINATES[copy_control.get_index(i)][1];
					}
					else {
						coords[i][0] = copy_control.get_row_coords() + this.DIAGONAL_COORDINATES[copy_control.get_index(i)][0];
						coords[i][1] = copy_control.get_column_coords() + this.DIAGONAL_COORDINATES[copy_control.get_index(i)][1];
					}
				}
				coords[3][0] = copy_control.get_row_coords();
				coords[3][1] = copy_control.get_column_coords();
			}
		}
		for(int i = 0; i < 4; i++) {
			if(coords[i][0] < 0 || coords[i][0] >= this.ROWS)
				return true;
			if(coords[i][1] < 0 || coords[i][1] >= this.COLUMNS)
				return true;
			if(this.grid[coords[i][0]][coords[i][1]].get_isSet() && this.grid[coords[i][0]][coords[i][1]].get_childOf() != this.control)
				return true;
		}
		return false;
	}
	
	public void rotate_shape() {
		if(!rotate_check_collisions()) {
			if(this.control.get_shape_type() == 1) {
				unmap_shape();
				boolean limiting_border = false;
				if(this.control.get_column_coords() == 0 && this.control.get_orientation() == 0) {
					shift_control_block(0, 2);
					limiting_border = true;
				}
				else if(this.control.get_column_coords() == 0 && this.control.get_orientation() == 2) {
					shift_control_block(0, 1);
					limiting_border = true;
				}
				
				else if(this.control.get_column_coords() == this.COLUMNS - 1 && this.control.get_orientation() == 0) {
					shift_control_block(0, -1);
					limiting_border = true;
				}
				else if(this.control.get_column_coords() == this.COLUMNS - 1 && this.control.get_orientation() == 2) {
					shift_control_block(0, -2);
					limiting_border = true;
				}
				
				else if(this.control.get_row_coords() == this.ROWS - 1 && this.control.get_orientation() == 3) {
					shift_control_block(-2, 0);
					limiting_border = true;
				}
				else if(this.control.get_row_coords() == this.ROWS - 1 && this.control.get_orientation() == 1) {
					shift_control_block(-1, 0);
					limiting_border = true;
				}
				else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() - 1].get_isSet() 
						&& this.control.get_orientation() == 0) { //check if next block is set\
					shift_control_block(0, 2);
					limiting_border = true;
				}
				else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() - 1].get_isSet() 
						&& this.control.get_orientation() == 2) {
					shift_control_block(0, 1);
					limiting_border = true;
				}
				else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() + 1].get_isSet() 
						&& this.control.get_orientation() == 0) {
					shift_control_block(0, -1);
					limiting_border = true;
				}
				else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() - 1].get_isSet() 
						&& this.control.get_orientation() == 2) {
					shift_control_block(0, -2);
					limiting_border = true;
				}
				else if(this.grid[this.control.get_row_coords() + 1][this.control.get_column_coords()].get_isSet() 
						&& this.control.get_orientation() == 3) {
					shift_control_block(-2, 0);
					limiting_border = true;
				}
				else if(this.grid[this.control.get_row_coords() + 1][this.control.get_column_coords()].get_isSet() 
						&& this.control.get_orientation() == 1) {
					shift_control_block(-1, 0);
					limiting_border = true;
				}
				if(!limiting_border) {
					switch(control.get_orientation()) {
					case(0):
						shift_control_block(0, 1); 
						break;
					case(1):
						shift_control_block(1, 0);
						break;
					case(2):
						shift_control_block(0, -1);
						break;
					case(3):
						shift_control_block(-1, 0);
						break;
					default:
						break;
					}
				}	
				this.control.rotate_block();
				map_shape();
			}
			else if(this.control.get_shape_type() != 2) {
				unmap_shape();
				if(this.control.get_column_coords() == 0) 
					shift_control_block(0, 1);
				else if(this.control.get_column_coords() == this.COLUMNS - 1) 
					shift_control_block(0, -1);
				else if(this.control.get_row_coords() == this.ROWS - 1) 
					shift_control_block(-1, 0);
				this.control.rotate_block();
				map_shape();
			}
		}
	}
	
	private boolean move_check_collisions(int row_offset, int column_offset) {
		int[][] coords = new int[4][2];
		if(this.control.get_shape_type() == 1) {
			coords[0][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(0)][0] + row_offset;
			coords[0][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(0)][1] + column_offset;
			coords[1][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(1)][0] + row_offset;
			coords[1][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(1)][1] + column_offset;
			if(this.control.get_orientation() == 0) {
				coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + row_offset + 1;
				coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1] + column_offset;
			}
			else if(this.control.get_orientation() == 1) {
				coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + row_offset;
				coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1] + column_offset - 1;
			}
			else if(this.control.get_orientation() == 2) {
				coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + row_offset - 1;
				coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1] + column_offset;
			}
			else {
				coords[2][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + row_offset;
				coords[2][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][1] + column_offset + 1;
			}
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + column_offset;
		}
		else if(this.control.get_shape_type() == 5) {
			for(int i = 0; i < 3; i++) {
				coords[i][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0] + row_offset;
				coords[i][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1] + column_offset;
			}
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + column_offset;
		}
		else {
			for(int i = 0; i < 3; i++) {
				if(i < 2) {
					coords[i][0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0] + row_offset;
					coords[i][1] = this.control.get_column_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][1] + column_offset;
				}
				else {
					coords[i][0] = this.control.get_row_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][0] + row_offset;
					coords[i][1] = this.control.get_column_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][1] + column_offset;
				}
			}
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + column_offset;
		}
		for(int i = 0; i < 4; i++) {
			if(coords[i][0] < 0 || coords[i][0] >= this.ROWS)
				return true;
			if(coords[i][1] < 0 || coords[i][1] >= this.COLUMNS)
				return true;
			if(this.grid[coords[i][0]][coords[i][1]].get_isSet() && this.grid[coords[i][0]][coords[i][1]].get_childOf() != this.control)
				return true;
		}
		return false;
	}
	
	public boolean move_shape_down() {
		if(!move_check_collisions(1, 0)) {
			unmap_shape();
			shift_control_block(1, 0);
			map_shape();
			return true;
		}
		else
			return false;
	}

	public void move_shape_left() {
		if(!move_check_collisions(0, -1)) {
			unmap_shape();
			shift_control_block(0, -1);
			map_shape();
		}	
	}
	
	public void move_shape_right() {
		if(!move_check_collisions(0, 1)) {
			unmap_shape();
			shift_control_block(0, 1);
			map_shape();
		}
	}
	
	public void send_shape_down() {
		int row_offset = 0;
		while(true) {
			if(!move_check_collisions(row_offset, 0)) 
				row_offset++;
			else 
				break;
		}
		unmap_shape();
		shift_control_block(row_offset - 1, 0);
		map_shape();
	}
	
	private void shift_rows_down(int row) {
		for(int i = row; i > 0; i--) {
			for(int j = 0; j < 11; j++) {
				this.grid[i][j].clear_block();
				this.grid[i][j].copy_data(this.grid[i - 1][j]);
				this.grid[i - 1][j].clear_block();
			}
		}
	}
	
	private boolean is_line_complete(int row) {
		for(int i = 0; i < this.COLUMNS; i++) {
			if(!this.grid[row][i].get_isSet())
				return false;
		}
		return true;
	}
	
	public void check_completed_lines() {
		int[] row_coords = new int[4];
		for(int i = 0; i < row_coords.length; i++)
			row_coords[i] = -1;
		if(this.control.get_shape_type() == 1) {
			row_coords[0] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(0)][0];
			row_coords[1] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(1)][0];
			if(this.control.get_orientation() == 0) 
				row_coords[2] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] + 1;
			else if(this.control.get_orientation() == 1) 
				row_coords[2] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0];
			else if(this.control.get_orientation() == 2) 
				row_coords[2] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0] - 1;
			else 
				row_coords[2] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(2)][0];
			row_coords[3] = this.control.get_row_coords();
		}
		else if(this.control.get_shape_type() == 5) {
			for(int i = 0; i < 3; i++) 
				row_coords[i] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
			row_coords[3] = this.control.get_row_coords();
		}
		else {
			for(int i = 0; i < 3; i++) {
				if(i < 2) 
					row_coords[i] = this.control.get_row_coords() + this.ORTHOGONAL_COORDINATES[this.control.get_index(i)][0];
				else 
					row_coords[i] = this.control.get_row_coords() + this.DIAGONAL_COORDINATES[this.control.get_index(i)][0];
			}
			row_coords[3] = this.control.get_row_coords();
		}
		Arrays.sort(row_coords);
		for(int i = 0; i < row_coords.length; i++) {
			for(int j = 0; j < row_coords.length; j++) {
				if(j == i || row_coords[j] == -1)
					continue;
				else if(row_coords[i] == row_coords[j])
					row_coords[j] = -1;
			}
		}
		for(int i = 0; i < row_coords.length; i++) {
			if(row_coords[i] != -1) {
				if(is_line_complete(row_coords[i])) {
					shift_rows_down(row_coords[i]);
				}
			}
		}
	}
	
	public String toString() {
		String out = "._._._._._._._._._._._.\n|";
		for(int i = 0; i < this.ROWS; i++) {
			for(int j = 0; j < this.COLUMNS; j++) {
				if(j == this.COLUMNS - 1) {
					out += this.grid[i][j].display();
					if(i != this.ROWS - 1)
						out += "|\n|";
					else
						out += "|";
				}
				else 
					out += this.grid[i][j].display() + "|";
			}
		}
		return out;
	}
}
