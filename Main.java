import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Grid g = new Grid();
		g.set_control_block();
		System.out.println(g.toString());
		Scanner enter = new Scanner(System.in);
		
		while(true) {
			enter.nextLine();
			g.move_shape_down();
			g.move_shape_left();
			System.out.println(g.toString());
		}
		
		
		/*
		System.out.println(g.toString());
		System.out.println("Row: " + g.get_control().get_row_coords() + " Column: " + g.get_control().get_column_coords());
		System.out.println();
		g.rotate_shape();
		System.out.println(g.toString());
		System.out.println("Row: " + g.get_control().get_row_coords() + " Column: " + g.get_control().get_column_coords());
		Block b2 = g.get_control();
		System.out.println();
		g.rotate_shape();
		System.out.println(g.toString());
		System.out.println("Row: " + g.get_control().get_row_coords() + " Column: " + g.get_control().get_column_coords());
		System.out.println();
		g.rotate_shape();
		System.out.println(g.toString());
		System.out.println("Row: " + g.get_control().get_row_coords() + " Column: " + g.get_control().get_column_coords());
		System.out.println();
		g.rotate_shape();
		System.out.println(g.toString());
		System.out.println("Row: " + g.get_control().get_row_coords() + " Column: " + g.get_control().get_column_coords());
		
		
		g.move_control_down();
		System.out.println("\n" + g.toString());
		g.move_control_down();
		System.out.println("\n" + g.toString());
		g.move_control_right();
		System.out.println("\n" + g.toString());
		g.move_control_right();
		System.out.println("\n" + g.toString());
		g.move_control_left();
		System.out.println("\n" + g.toString());
		*/
	}
}
