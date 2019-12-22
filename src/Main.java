import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {
		Grid g = new Grid();
		g.set_control_block();
		System.out.println(g.toString());
		Scanner enter = new Scanner(System.in);	
		g.rotate_shape();
		g.rotate_shape();
		g.rotate_shape();
		System.out.println(g.toString());
		
		while(true) {
			/*
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
			enter.nextLine();
			//g.move_shape_down();
			g.rotate_shape();
			System.out.println(g.toString());
		}
	}
}
