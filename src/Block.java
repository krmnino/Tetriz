import java.util.Random;

public class Block {
	private int row_coordinates; //row coordinates of block in grid
	private int column_coordinates; //column coordinates of block in 
	private boolean isSet; //boolean flag that determines if block is set (occupied)
	private int shape_type; //for control block: determines the shape
	private int orientation; //0 = north; 1 = east; 2 = south; 3 = west; 
	private int[] indexes = new int[3]; //indexes used to gather the current coordinates of orbiting blocks
	private Block childOf; //if block is child of a control block or if control block, then child of itself
	
	public Block(){ //constructor method, takes coordinates as parameters
		this.row_coordinates = 1;
		this.column_coordinates = 1;
		this.isSet = false; //block is not set by default
		this.orientation = 0; //block facing north by default
		this.shape_type = 0; //shape 0 = no shape at all
		this.indexes = new int[3]; //allocates space for array of indexes
		this.childOf = null; //new block is child of no block by default
	}
	
	public Block(int row_coordinates_, int column_coordinates_){ //constructor method, takes coordinates as parameters
		this.row_coordinates = row_coordinates_;
		this.column_coordinates = column_coordinates_;
		this.isSet = false; //block is not set by default
		this.orientation = 0; //block facing north by default
		this.shape_type = 0; //shape 0 = no shape at all
		this.indexes = new int[3]; //allocates space for array of indexes
		this.childOf = null; //new block is child of no block by default
	}
	
	public Block(Block b) { //copy constructor method, takes another block as parameter
		this.row_coordinates = b.row_coordinates; 
		this.column_coordinates = b.column_coordinates;
		this.isSet = b.isSet;
		this.shape_type = b.shape_type;
		this.orientation = b.orientation;
		for(int i = 0; i < 3; i++)
			this.indexes[i] = b.indexes[i];
		this.childOf = b.childOf;
	}
	
	public void copy_data(Block b) { //copy data method, differs from copy constructor by not copying block coordinates
		this.isSet = b.isSet;
		this.shape_type = b.shape_type;
		this.orientation = b.orientation;
		for(int i = 0; i < 3; i++)
			this.indexes[i] = b.indexes[i];
		this.childOf = b.childOf;
	}
	
	public int get_row_coords() { //return block's row coordinates
		return this.row_coordinates;
	}
	
	public int get_column_coords() { //return block's column coordinates
		return this.column_coordinates;
	} 
	
	public boolean get_isSet() { //return state of isSet boolean flag
		return this.isSet;
	}
	
	public int get_shape_type() { //return block's shape (used only for control block)
		return this.shape_type;
	}
	
	public int get_orientation() { //return orientation of current block
		return this.orientation;
	}
	
	public int[] get_indexes() { //return array of indexes of current block
		return this.indexes;
	}
	
	public int get_index(int i) { //return single index value specified by the parameter i
		return this.indexes[i];
	}
	
	public Block get_childOf() { //return childOf variable from current block
		return this.childOf;
	}
	
	public void set_row_coords(int row_coordinates_) { //set row coordinates of this block (only used when initializing grid)
		this.row_coordinates = row_coordinates_;
	}
	
	public void set_column_coords(int column_coordinates_) { //set column coordinates of this block (only used when initializing grid)
		this.column_coordinates = column_coordinates_;
	}
	
	public void set_coords(int row_coordinates_, int column_coordinates_) { 
		//set row & column coordinates of this block (only used when initializing grid)
		this.row_coordinates = row_coordinates_;
		this.column_coordinates = column_coordinates_;
	}
	
	public void set_block() { //set current block isSet boolean flag to true
		this.isSet = true;
	}
	
	public void set_subblock(Block parent) { //used to initialize orbiting blocks around control block
		this.childOf = parent; //set child block's parent 
		this.isSet = true; //set isSet boolean flag to true
	}
	
	public void set_shape_type() {
		/*	0. None
		 * 	1. Line
		 * 	2. Square
		 * 	3. L-shape
		 * 	4. J-shape
		 * 	5. T-shape
		 * 	6. Z-shape
		 * 	7. S-shape 
		 * 	When a control block is summoned, a shape type is randomly selected. Then,
		 * 	depending on the shape type, a set of indexes is assigned to the control block.
		 * 	These set of indexes will determine the coordinates (specified in Grid.java)
		 * 	depending the current orientation of the block (North orientation by default)
		 * */
		Random rand = new Random();
		this.shape_type = 1 + rand.nextInt(7);
		switch(this.shape_type) {
		case(1): //line
			this.indexes[0] = 0;
			this.indexes[1] = 2;
			this.indexes[2] = 2;
			break;
		case(2): //square
			this.indexes[0] = 1;
			this.indexes[1] = 2;
			this.indexes[2] = 1;
			break;
		case(3): //L-shape
			this.indexes[0] = 0;
			this.indexes[1] = 2;
			this.indexes[2] = 3;
			break;
		case(4): //J-shape
			this.indexes[0] = 0;
			this.indexes[1] = 2;
			this.indexes[2] = 0;
			break;
		case(5): //T-shape
			this.indexes[0] = 3;
			this.indexes[1] = 0;
			this.indexes[2] = 1;
			break;
		case(6): //Z-shape
			this.indexes[0] = 0;
			this.indexes[1] = 3;
			this.indexes[2] = 2;
			break;
		case(7): //S-shape
			this.indexes[0] = 0;
			this.indexes[1] = 1;
			this.indexes[2] = 1;
			break;
		default: //default case, all indexes are 0 (no shape)
			this.indexes[0] = 0;
			this.indexes[1] = 0;
			this.indexes[2] = 0;
		}
	}
	
	public void set_shape_type(int shape_type_) {
		if(0 <= shape_type && shape_type <= 7) {
			this.shape_type = shape_type_;
			switch(this.shape_type) {
			case(1): //line
				this.indexes[0] = 0;
				this.indexes[1] = 2;
				this.indexes[2] = 2;
				break;
			case(2): //square
				this.indexes[0] = 1;
				this.indexes[1] = 2;
				this.indexes[2] = 1;
				break;
			case(3): //L-shape
				this.indexes[0] = 0;
				this.indexes[1] = 2;
				this.indexes[2] = 3;
				break;
			case(4): //J-shape
				this.indexes[0] = 0;
				this.indexes[1] = 2;
				this.indexes[2] = 0;
				break;
			case(5): //T-shape
				this.indexes[0] = 3;
				this.indexes[1] = 0;
				this.indexes[2] = 1;
				break;
			case(6): //Z-shape
				this.indexes[0] = 0;
				this.indexes[1] = 3;
				this.indexes[2] = 2;
				break;
			case(7): //S-shape
				this.indexes[0] = 0;
				this.indexes[1] = 1;
				this.indexes[2] = 1;
				break;
			default: //default case, all indexes are 0 (no shape)
				this.indexes[0] = 0;
				this.indexes[1] = 0;
				this.indexes[2] = 0;
			}
		}
	}
	
	public void set_indexes_array(int[] new_indexes_) { //set block's array of indexes. Be cautious when using this...
		for(int i = 0; i < 3; i++)
			this.indexes[i] = new_indexes_[i];
	}
	
	public void set_childOf(Block parent) { //set block's childOf variable to parent block
		this.childOf = parent;
	}
	
	public void rotate_block() { 	//rotate control block
		if(this.orientation < 3) 	//if control orientation value is less than 3...
			this.orientation++;		//increase orientation by 1 unit
		else
			this.orientation = 0;	//else, set orientation back to 0 (control block has completed a full rotation)
		for(int i = 0; i < 3; i++) {	//loop through the array of indexes
			if(this.indexes[i] < 3) 	//if this index is less than 3...
				this.indexes[i]++;		//increase this index by 1 unit
			else
				this.indexes[i] = 0;	//else, set this index back to 0
		}
	}
	
	public void clear_block() {	//clear a block when is no longer occupied
		this.isSet = false;		//set isSet boolean flag to false
		this.shape_type = 0;	//set shape type to none (0)
		this.orientation = 0;	//set orientation back to 0
		for(int i = 0; i < 3; i++)	//loop through array of indexes
			this.indexes[i] = 0;	//set this index to 0
		this.childOf = null;	//set childOf to null (child of nobody)
	}
	
	public boolean compare_reference(Block b) { //compare hash value between blocks (used for checking collisions)
		if(this == b) 	//if this block hash value equals b's hash value...
			return true;	//return true since they are equal
		else 
			return false;	//else, return false if they are not the same
	}
	
	public String display() { //return string value depending its isSet boolean flag
		if(this.isSet) 	//if isSet is true...
			return "\u2b1b";	//return string "0" meaning that this block is occupied
		else
			return "\u2b1c"; //else, return "_" meaning that this block is free
	}
	
	public String toString() {
		return "[" + this.shape_type + "]";
	}
}