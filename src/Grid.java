public class Grid {
	private final int[][] orthogonal_coordinates = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	private final int[][] diagonal_coordinates = {{-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
	private Block[][] grid;
	private Block control;
	
	public Grid() {
		this.grid = new Block[22][11];
		this.control = null;
		initialize_grid();
	}
	
	public Block get_control() {
		return this.control;
	}
	
	public void initialize_grid() {
		for(int i = 0; i < this.grid.length; i++) 
			for(int j = 0; j < this.grid[0].length; j++)
				this.grid[i][j] = new Block(i, j);
	}
	
	public void set_control_block() {
		this.control = this.grid[1][5];
		this.control.set_block();
		this.control.set_shape_type();
		this.control.set_childOf(this.control);
		map_shape();
	}
	
	public void map_shape() {
		for(int i = 0; i < 3; i++) {
			int row_subblock;
			int column_subblock;
			switch(this.control.get_shape_type()) {
			case(1):
				row_subblock = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0];
				column_subblock = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(i)][1];
				if(i < 2) {
					this.grid[row_subblock][column_subblock].set_subblock(this.control);
				}
				else {
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
			case(5):
				row_subblock = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0];
				column_subblock = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(i)][1];
				this.grid[row_subblock][column_subblock].set_subblock(this.control);
				break;
			default:
				if(i < 2) {
					row_subblock = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0];
					column_subblock = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(i)][1];
					this.grid[row_subblock][column_subblock].set_subblock(this.control);
				}
				else {
					row_subblock = this.control.get_row_coords() + this.diagonal_coordinates[this.control.get_index(i)][0];
					column_subblock = this.control.get_column_coords() + this.diagonal_coordinates[this.control.get_index(i)][1];
					this.grid[row_subblock][column_subblock].set_subblock(this.control);
				}	
			}
		}
	}
	
	public void unmap_shape() {
		for(int i = 0; i < 3; i++) {
			int row_subblock;
			int column_subblock;
			switch(this.control.get_shape_type()) {
			case(1):
				row_subblock = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0];
				column_subblock = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(i)][1];
				if(i < 2) {
					this.grid[row_subblock][column_subblock].clear_subblock();
				}
				else {
					if(this.control.get_orientation() == 0) 
						this.grid[row_subblock + 1][column_subblock].clear_subblock();
					else if(this.control.get_orientation() == 1)
						this.grid[row_subblock][column_subblock - 1].clear_subblock();
					else if(this.control.get_orientation() == 2)
						this.grid[row_subblock - 1][column_subblock].clear_subblock();
					else 
						this.grid[row_subblock][column_subblock + 1].clear_subblock();
				}
				break;
			case(5):
				row_subblock = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0];
				column_subblock = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(i)][1];
				this.grid[row_subblock][column_subblock].clear_subblock();
				break;
			default:
				if(i < 2) {
					row_subblock = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0];
					column_subblock = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(i)][1];
					this.grid[row_subblock][column_subblock].clear_subblock();
				}
				else {
					row_subblock = this.control.get_row_coords() + this.diagonal_coordinates[this.control.get_index(i)][0];
					column_subblock = this.control.get_column_coords() + this.diagonal_coordinates[this.control.get_index(i)][1];
					this.grid[row_subblock][column_subblock].clear_subblock();
				}
			}
		}
	}

	public void shift_control_block(int row_offset, int column_offset) {
		Block new_control = this.grid[this.control.get_row_coords() + row_offset][this.control.get_column_coords() + column_offset];
		new_control.copy_data(this.control);
		this.control.clear_block();
		this.control = new_control;
		this.control.set_childOf(this.control);
	}
	
	public boolean rotate_check_collisions() {
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
			
			else if(copy_control.get_column_coords() == this.grid[0].length - 1 && copy_control.get_orientation() == 0) {
				copy_control.set_column_coords(copy_control.get_column_coords() - 1);
				limiting_border = true;
			}
			else if(copy_control.get_column_coords() == this.grid[0].length - 1 && copy_control.get_orientation() == 2) {
				copy_control.set_column_coords(copy_control.get_column_coords() - 2);
				limiting_border = true;
			}
			else if(copy_control.get_row_coords() == this.grid.length - 1 && copy_control.get_orientation() == 3) {
				copy_control.set_row_coords(copy_control.get_row_coords() - 2);
				limiting_border = true;
			}
			else if(copy_control.get_row_coords() == this.grid.length - 1 && copy_control.get_orientation() == 1) {
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
			coords[0][0] = copy_control.get_row_coords() + this.orthogonal_coordinates[copy_control.get_index(0)][0];
			coords[0][1] = copy_control.get_column_coords() + this.orthogonal_coordinates[copy_control.get_index(0)][1];
			coords[1][0] = copy_control.get_row_coords() + this.orthogonal_coordinates[copy_control.get_index(1)][0];
			coords[1][1] = copy_control.get_column_coords() + this.orthogonal_coordinates[copy_control.get_index(1)][1];
			if(copy_control.get_orientation() == 0) {
				coords[2][0] = copy_control.get_row_coords() + this.orthogonal_coordinates[copy_control.get_index(2)][0] + 1;
				coords[2][1] = copy_control.get_column_coords() + this.orthogonal_coordinates[copy_control.get_index(2)][1];
			}
			else if(copy_control.get_orientation() == 1) {
				coords[2][0] = copy_control.get_row_coords() + this.orthogonal_coordinates[copy_control.get_index(2)][0];
				coords[2][1] = copy_control.get_column_coords() + this.orthogonal_coordinates[copy_control.get_index(2)][1] - 1;
			}
			else if(copy_control.get_orientation() == 2) {
				coords[2][0] = copy_control.get_row_coords() + this.orthogonal_coordinates[copy_control.get_index(2)][0] - 1;
				coords[2][1] = copy_control.get_column_coords() + this.orthogonal_coordinates[copy_control.get_index(2)][1];
			}
			else {
				coords[2][0] = copy_control.get_row_coords() + this.orthogonal_coordinates[copy_control.get_index(2)][0];
				coords[2][1] = copy_control.get_column_coords() + this.orthogonal_coordinates[copy_control.get_index(2)][1] + 1;
			}
			coords[3][0] = copy_control.get_row_coords();
			coords[3][1] = copy_control.get_column_coords();
			
		}
		else if(copy_control.get_shape_type() != 2) {
			if(copy_control.get_column_coords() == 0) 
				copy_control.set_column_coords(copy_control.get_column_coords() + 1);
			else if(copy_control.get_column_coords() == this.grid[0].length - 1) 
				copy_control.set_column_coords(copy_control.get_column_coords() - 1);
			else if(copy_control.get_row_coords() == this.grid.length - 1) 
				copy_control.set_row_coords(copy_control.get_row_coords() - 1);
			copy_control.rotate_block();
			if(copy_control.get_shape_type() == 5) {
				for(int i = 0; i < 3; i++) {
					coords[i][0] = copy_control.get_row_coords() + this.orthogonal_coordinates[copy_control.get_index(i)][0];
					coords[i][1] = copy_control.get_column_coords() + this.orthogonal_coordinates[copy_control.get_index(i)][1];
				}
				coords[3][0] = copy_control.get_row_coords();
				coords[3][1] = copy_control.get_column_coords();
			}
			else {
				for(int i = 0; i < 3; i++) {
					if(i < 2) {
						coords[i][0] = copy_control.get_row_coords() + this.orthogonal_coordinates[copy_control.get_index(i)][0];
						coords[i][1] = copy_control.get_column_coords() + this.orthogonal_coordinates[copy_control.get_index(i)][1];
					}
					else {
						coords[i][0] = copy_control.get_row_coords() + this.diagonal_coordinates[copy_control.get_index(i)][0];
						coords[i][1] = copy_control.get_column_coords() + this.diagonal_coordinates[copy_control.get_index(i)][1];
					}
				}
				coords[3][0] = copy_control.get_row_coords();
				coords[3][1] = copy_control.get_column_coords();
			}
		}
		for(int i = 0; i < 4; i++) {
			if(coords[i][0] < 0 || coords[i][0] >= this.grid.length)
				return true;
			if(coords[i][1] < 0 || coords[i][1] >= this.grid[0].length)
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
				
				else if(this.control.get_column_coords() == this.grid[0].length - 1 && this.control.get_orientation() == 0) {
					shift_control_block(0, -1);
					limiting_border = true;
				}
				else if(this.control.get_column_coords() == this.grid[0].length - 1 && this.control.get_orientation() == 2) {
					shift_control_block(0, -2);
					limiting_border = true;
				}
				
				else if(this.control.get_row_coords() == this.grid.length - 1 && this.control.get_orientation() == 3) {
					shift_control_block(-2, 0);
					limiting_border = true;
				}
				else if(this.control.get_row_coords() == this.grid.length - 1 && this.control.get_orientation() == 1) {
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
				else if(this.control.get_column_coords() == this.grid[0].length - 1) 
					shift_control_block(0, -1);
				else if(this.control.get_row_coords() == this.grid.length - 1) 
					shift_control_block(-1, 0);
				this.control.rotate_block();
				map_shape();
			}
		}
	}
	
	public boolean move_check_collisions(int row_offset, int column_offset) {
		int[][] coords = new int[4][2];
		if(this.control.get_shape_type() == 1) {
			coords[0][0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(0)][0] + row_offset;
			coords[0][1] = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(0)][1] + column_offset;
			coords[1][0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(1)][0] + row_offset;
			coords[1][1] = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(1)][1] + column_offset;
			if(this.control.get_orientation() == 0) {
				coords[2][0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(2)][0] + row_offset + 1;
				coords[2][1] = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(2)][1] + column_offset;
			}
			else if(this.control.get_orientation() == 1) {
				coords[2][0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(2)][0] + row_offset;
				coords[2][1] = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(2)][1] + column_offset - 1;
			}
			else if(this.control.get_orientation() == 2) {
				coords[2][0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(2)][0] + row_offset - 1;
				coords[2][1] = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(2)][1] + column_offset;
			}
			else {
				coords[2][0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(2)][0] + row_offset;
				coords[2][1] = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(2)][1] + column_offset + 1;
			}
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + column_offset;
		}
		else if(this.control.get_shape_type() == 5) {
			for(int i = 0; i < 3; i++) {
				coords[i][0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0] + row_offset;
				coords[i][1] = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(i)][1] + column_offset;
			}
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + row_offset;
		}
		else {
			for(int i = 0; i < 3; i++) {
				if(i < 2) {
					coords[i][0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0] + row_offset;
					coords[i][1] = this.control.get_column_coords() + this.orthogonal_coordinates[this.control.get_index(i)][1] + column_offset;
				}
				else {
					coords[i][0] = this.control.get_row_coords() + this.diagonal_coordinates[this.control.get_index(i)][0] + row_offset;
					coords[i][1] = this.control.get_column_coords() + this.diagonal_coordinates[this.control.get_index(i)][1] + column_offset;
				}
			}
			coords[3][0] = this.control.get_row_coords() + row_offset;
			coords[3][1] = this.control.get_column_coords() + column_offset;
		}
		for(int i = 0; i < 4; i++) {
			if(coords[i][0] < 0 || coords[i][0] >= this.grid.length)
				return true;
			if(coords[i][1] < 0 || coords[i][1] >= this.grid[0].length)
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
	
	public boolean is_line_complete() {
		int[] row_coords = new int[4];
		if(this.control.get_shape_type() == 1) {
			row_coords[0] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(0)][0];
			row_coords[1] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(1)][0];
			if(this.control.get_orientation() == 0) 
				row_coords[2] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(2)][0] + 1;
			else if(this.control.get_orientation() == 1) 
				row_coords[2] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(2)][0];
			else if(this.control.get_orientation() == 2) 
				row_coords[2] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(2)][0] - 1;
			else 
				row_coords[2] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(2)][0];
			row_coords[3] = this.control.get_row_coords();
		}
		else if(this.control.get_shape_type() == 5) {
			for(int i = 0; i < 3; i++) 
				row_coords[i] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0];
			row_coords[3] = this.control.get_row_coords();
		}
		else {
			for(int i = 0; i < 3; i++) {
				if(i < 2) {
					row_coords[i] = this.control.get_row_coords() + this.orthogonal_coordinates[this.control.get_index(i)][0];
				}
				else {
					row_coords[i] = this.control.get_row_coords() + this.diagonal_coordinates[this.control.get_index(i)][0];
				}
			}
			row_coords[3] = this.control.get_row_coords();
		}
		return true;
	}
	
	public String toString() {
		String out = "._._._._._._._._._._._.\n|";
		for(int i = 0; i < this.grid.length; i++) {
			for(int j = 0; j < this.grid[0].length; j++) {
				if(j == this.grid[0].length-1) {
					out += this.grid[i][j].display();
					if(i != this.grid.length-1)
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
