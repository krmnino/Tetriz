import java.util.Random;

public class Block {
	private int row_coordinates;
	private int column_coordinates;
	private boolean isSet;
	private int shape_type; 
	private int orientation; //0 = north; 1 = east; 2 = south; 3 = west; 
	private int[] indexes = new int[3];
	private Block childOf;
	
	
	public Block(int row_coordinates_, int column_coordinates_){
		this.row_coordinates = row_coordinates_;
		this.column_coordinates = column_coordinates_;
		this.isSet = false;
		this.orientation = 0;
		this.shape_type = 0;
		this.indexes = new int[3];
		this.childOf = null;
	}
	
	public Block(Block b) {
		this.row_coordinates = b.row_coordinates;
		this.column_coordinates = b.column_coordinates;
		this.isSet = b.isSet;
		this.shape_type = b.shape_type;
		this.orientation = b.orientation;
		for(int i = 0; i < 3; i++)
			this.indexes[i] = b.indexes[i];
		this.childOf = b.childOf;
	}
	
	public void copy_data(Block b) {
		this.isSet = b.isSet;
		this.shape_type = b.shape_type;
		this.orientation = b.orientation;
		for(int i = 0; i < 3; i++)
			this.indexes[i] = b.indexes[i];
		this.childOf = b.childOf;
	}
	
	public int get_row_coords() {
		return this.row_coordinates;
	}
	
	public int get_column_coords() {
		return this.column_coordinates;
	} 
	
	public boolean get_isSet() {
		return this.isSet;
	}
	
	public int get_shape_type() {
		return this.shape_type;
	}
	
	public int get_orientation() {
		return this.orientation;
	}
	
	public int[] get_indexes() {
		return this.indexes;
	}
	
	public int get_index(int i) {
		return this.indexes[i];
	}
	
	public Block get_childOf() {
		return this.childOf;
	}
	
	public void set_row_coords(int row_coordinates_) {
		this.row_coordinates = row_coordinates_;
	}
	
	public void set_column_coords(int column_coordinates_) {
		this.column_coordinates = column_coordinates_;
	}
	
	public void set_coords(int row_coordinates_, int column_coordinates_) {
		this.row_coordinates = row_coordinates_;
		this.column_coordinates = column_coordinates_;
	}
	
	public void set_block() {
		this.isSet = true;
	}
	
	public void set_subblock(Block parent) {
		this.childOf = parent;
		this.isSet = true;
	}
	
	public void set_shape_type() {
		/*	0. None
		 * 	1. Line
		 * 	2. Square
		 * 	3. L-shape
		 * 	4. J-shape
		 * 	5. T-shape
		 * 	6. Z-shape
		 * 	7. S-shape */
		Random rand = new Random();
		//this.shape_type = 1 + rand.nextInt(7);
		this.shape_type = 1;
		switch(this.shape_type) {
		case(1):
			this.indexes[0] = 0;
			this.indexes[1] = 2;
			this.indexes[2] = 2;
			break;
		case(2):
			this.indexes[0] = 1;
			this.indexes[1] = 2;
			this.indexes[2] = 1;
			break;
		case(3):
			this.indexes[0] = 0;
			this.indexes[1] = 2;
			this.indexes[2] = 3;
			break;
		case(4):
			this.indexes[0] = 0;
			this.indexes[1] = 2;
			this.indexes[2] = 0;
			break;
		case(5):
			this.indexes[0] = 3;
			this.indexes[1] = 0;
			this.indexes[2] = 1;
			break;
		case(6):
			this.indexes[0] = 0;
			this.indexes[1] = 3;
			this.indexes[2] = 2;
			break;
		case(7):
			this.indexes[0] = 0;
			this.indexes[1] = 1;
			this.indexes[2] = 1;
			break;
		default:
			this.indexes[0] = 0;
			this.indexes[1] = 0;
			this.indexes[2] = 0;
		}
	}
	
	public void set_indexes_array(int[] new_indexes_) {
		for(int i = 0; i < 3; i++)
			this.indexes[i] = new_indexes_[i];
	}
	
	public void rotate_block() {
		if(this.orientation <  3)
			this.orientation++;
		else
			this.orientation = 0;
		for(int i = 0; i < 3; i++) {
			if(this.indexes[i] < 3) 
				this.indexes[i]++;
			else
				this.indexes[i] = 0;
		}
	}
	
	public void clear_block() {
		this.isSet = false;
		this.shape_type = 0;
		this.orientation = 0;
		for(int i = 0; i < 3; i++)
			this.indexes[i] = 0;
	}
	
	public void clear_subblock() {
		this.childOf = null;
		this.isSet = false;
	}
	
	public boolean compare_reference(Block b) {
		if(this == b) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String display() {
		if(this.isSet) 
			return "0";
		else
			return "_";
	}
}

