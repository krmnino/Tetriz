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
	}
	
	public void rotate_shape() {
		unmap_shape();
		if(this.control.get_column_coords() == 0 && this.control.get_shape_type() != 2) {
			shift_control_block(0, 1);
		}
		else if(this.control.get_column_coords() == this.grid[0].length - 1) {
			shift_control_block(0, -1);
		}
		else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() - 1].get_isSet() &&
				this.grid[this.control.get_row_coords()][this.control.get_column_coords() - 1].get_childOf() != this.control) {
			if(this.control.get_shape_type() == 1) 
				shift_control_block(0, 2);
			else if(this.control.get_shape_type() != 2)
				shift_control_block(0, 1);
		}
		else if(this.grid[this.control.get_row_coords()][this.control.get_column_coords() + 1].get_isSet() &&
				this.grid[this.control.get_row_coords()][this.control.get_column_coords() + 1].get_childOf() != this.control) {
			if(this.control.get_shape_type() == 1) 
				shift_control_block(0, 2);
			else if(this.control.get_shape_type() != 2)
				shift_control_block(0, 1);
		}
		if(this.control.get_shape_type() == 1) {
			switch(this.control.get_orientation()) {
			case(0):
				shift_control_block(0, 1);
				break;
			case(1):
				shift_control_block(1, 0);
				break;
			case(2):
				shift_control_block(-1,0);
				break;
			case(3):
				shift_control_block(0, -1);
				break;
			}
			this.control.rotate_block();
			map_shape();
		}
		else if (this.control.get_shape_type() != 2 && this.control.get_shape_type() != 2) {
			unmap_shape();
			this.control.rotate_block();
			map_shape();
		}
	}
	
	public Block[] all_blocks_shape() {
		Block[] blocks = new Block[4];
		switch(this.control.get_shape_type()) {
		case(1):
			for(int i = 0; i < 3; i++) {
				if(i < 2) 
					blocks[i] = this.grid[this.control.get_row_coords() - this.orthogonal_coordinates[this.control.get_index(i)][0]]
									 	 [this.control.get_column_coords() - this.orthogonal_coordinates[this.control.get_index(i)][1]];
				else {
					if(this.control.get_orientation() == 0) 
						blocks[i] = this.grid[this.control.get_row_coords() - this.orthogonal_coordinates[this.control.get_index(i)][0] + 1]
								 			 [this.control.get_column_coords() - this.orthogonal_coordinates[this.control.get_index(i)][1]];
					else if(this.control.get_orientation() == 1)
						blocks[i] = this.grid[this.control.get_row_coords() - this.orthogonal_coordinates[this.control.get_index(i)][0]]
								 			 [this.control.get_column_coords() - this.orthogonal_coordinates[this.control.get_index(i)][1] - 1];
					else if(this.control.get_orientation() == 2)
						blocks[i] = this.grid[this.control.get_row_coords() - this.orthogonal_coordinates[this.control.get_index(i)][0] - 1]
								 		 	 [this.control.get_column_coords() - this.orthogonal_coordinates[this.control.get_index(i)][1]];
					else 
						blocks[i] = this.grid[this.control.get_row_coords() - this.orthogonal_coordinates[this.control.get_index(i)][0]]
								 			 [this.control.get_column_coords() - this.orthogonal_coordinates[this.control.get_index(i)][1] + 1];
				}
			}
			break;
		case(5):
			for(int i = 0; i < 3; i++)
				blocks[i] = this.grid[this.control.get_row_coords() - this.orthogonal_coordinates[this.control.get_index(i)][0]]
									 [this.control.get_column_coords() - this.orthogonal_coordinates[this.control.get_index(i)][1]];
		default:
			for(int i = 0; i < 3; i++) {
				if(i < 2)
					blocks[i] = this.grid[this.control.get_row_coords() - this.orthogonal_coordinates[this.control.get_index(i)][0]]
									 	 [this.control.get_column_coords() - this.orthogonal_coordinates[this.control.get_index(i)][1]];
				else
					blocks[i] = this.grid[this.control.get_row_coords() - this.diagonal_coordinates[this.control.get_index(i)][0]]
						 	 			 [this.control.get_column_coords() -this.diagonal_coordinates[this.control.get_index(i)][1]];
			}
		}
		blocks[3] = this.control;
		return blocks;
	}
	
	public boolean check_collisions(int row_offset, int column_offset) {
		Block[] blocks = all_blocks_shape();
		int row_coords;
		int column_coords;
		for(int i = 0; i < 4; i++) {
			row_coords = blocks[i].get_row_coords() + row_offset;
			column_coords = blocks[i].get_column_coords() + column_offset;
			if(row_coords >= this.grid.length - 1|| column_coords >= this.grid[0].length || column_coords < 0)
				return true;
			if(this.grid[row_coords][column_coords] == this.control)
				continue;
			else if(this.grid[row_coords][column_coords].get_isSet() && this.grid[row_coords][column_coords].get_childOf() != this.control)
				return true;
		}
		return false;
	}
	
	public void move_shape_down() {
		if(!check_collisions(1, 0)) {
			unmap_shape();
			shift_control_block(1, 0);
			map_shape();
		}
	}

	public void move_shape_left() {
		if(!check_collisions(0, -1)) {
			unmap_shape();
			shift_control_block(0, -1);
			map_shape();
		}	
	}
	
	public void move_shape_right() {
		if(!check_collisions(0, 1)) {
			unmap_shape();
			shift_control_block(0, 1);
			map_shape();
		}
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
