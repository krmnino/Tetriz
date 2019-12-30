import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {
		Grid g = new Grid();
		g.set_control_block();
		System.out.println(g.toString());
		Scanner enter = new Scanner(System.in);
		
		while(true) {
			try {
				char input = (char) System.in.read();
				switch (input) {
				case('w'):
				case('W'):
					g.rotate_shape(); 
					break;
				case('a'):
				case('A'):
					g.move_shape_left();
					break;
				case('d'):
				case('D'):
					g.move_shape_right();
					break;
				case('s'):
				case('S'):
					if(!g.move_shape_down())
						g.set_control_block();
					break;
				case(' '):
					g.send_shape_down();
					g.set_control_block();
					break;
				default:
					break;
				}
				System.out.println(g.toString());
			} catch (IOException e) {}
			
		}
	}
}
