import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class GameCore extends JPanel {
	
	private static JTextArea display;	//Creates text area where the grid is displayed
	private static JTextArea hold_display;
	private static JTextArea queue_1;
	private static JTextArea queue_2;
	private static JTextArea queue_3;
	private static JLabel current_score;
	private static Grid g = new Grid();	//Creates new grid of blocks object
	private static Queue q = new Queue();
	private static boolean game_running = true;
	private static int score = 0;
	
	public GameCore() {
		KeyboardDectection listener = new KeyboardDectection(); //Initialize keyboard detection object
		addKeyListener(listener);	//Add key listener to Key detection object
		setFocusable(true);	//Sets focusable to true and overrides original default state
	}
	
	public static void set_GUI() {	//set GUI elements and launch it
		GameCore key_detector = new GameCore();	// initialize new instance of key detector class
		display = new JTextArea();	//initialize new JTextArea to be the screen
		display.setSize(120, 370);	//set size of display in pixels
		display.setLocation(150, 20);	//set location of display in frame
		display.setVisible(true);	//make display visible
		display.setBackground(SystemColor.info);	//set display background color to an arbitrary color that contrasts
		display.setBorder(new LineBorder(new Color(0, 0, 0)));	//let the display have a black border
		current_score = new JLabel();
		current_score.setSize(120, 30);
		current_score.setLocation(150, 400);
		current_score.setVisible(true);
		current_score.setBackground(SystemColor.info);
		current_score.setBorder(new LineBorder(new Color(0, 0, 0)));
		current_score.setText("Score: " + Integer.toString(score));
		JFrame frame = new JFrame();	//set frame object that contains the display and other things
		frame.setLayout(null);	// not set a layout for the frame to allow place objects freely (any coordinates)
		frame.add(key_detector); //add key detector object to frame
		frame.add(display);	//add display to frame
		frame.add(current_score);
		frame.setTitle("Tetriz");	//set frame title as "Tetriz"
		frame.setFocusCycleRoot(true);	//set frame as root of focus traversal cycle
		frame.setSize(440, 480);	//set frame size
		frame.setVisible(true);	//set frame visible (display window)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//set default close operation (press X to close)
	}
	
	public static void game_loop() {
		q.populate();
		int shape_type = q.dequeue_shape().get_shape_type();
		System.out.println(shape_type);
		//g.set_control_block(shape_type);
		g.set_control_block(shape_type);
		display.setText(g.toString());	//display grid in display
		while(game_running) { //actual game loop 
			try {
				Thread.sleep(1000);	//make program sleep for 1 second before sending the next update
				if(!g.move_shape_down()) {	//if shape could not move
					int lines_completed = g.check_completed_lines(); //variable that saves number of lines completed
					score += lines_completed * lines_completed * 10; // check if lines are completed and to score variable
					current_score.setText("Score: " + score);
					shape_type = q.dequeue_shape().get_shape_type();
					if(!g.set_control_block(shape_type)) {	//set new control block at the top
						game_running = false;
						break;
					}
				}
				display.setText(g.toString());	//else, allow the shape to move and update the grid 
			} catch (Exception e) {
				e.printStackTrace();	
			}
		}
	}
	
	class KeyboardDectection implements KeyListener {
		public void keyTyped(KeyEvent e) {}	//mandatory keyReleased fn() due to abstract class
		
		public void keyPressed(KeyEvent e) {
			if(game_running) { //if game is running, check for keyboard input
				switch(e.getKeyChar()) {	//get char from KeyEvent object when a key is pressed
				case('W'):	//W - rotate shape
				case('w'):
					g.rotate_shape();	//rotate control block
					display.setText(g.toString());	//update screen after remapping
					System.out.println("W - Rotate");	//display in console that 'w' was pressed
					break;
				case('A'):	//A - move shape left
				case('a'):
					g.move_shape_left();	//move control block left
					display.setText(g.toString());	//update screen after remapping
					System.out.println("A - Left");	//display in console that 'a' was pressed
					break;
				case('S'): //S - move shape down
				case('s'):
					if(!g.move_shape_down()) {	//if control block could not move down...
						int lines_completed = g.check_completed_lines(); //variable that saves number of lines completed
						score += lines_completed * lines_completed * 10; // check if lines are completed and to score variable
						current_score.setText("Score: " + score);
						int shape_type = q.dequeue_shape().get_shape_type();
						if(!g.set_control_block(shape_type)) { //check if spawn area of control block shape is empty
							game_running = false;
							break;
						}
					}
					display.setText(g.toString());	//update screen after remapping
					System.out.println("D - Right");	//display in console that 'd' was pressed
					break;
				case('D'):	//D - move shape right
				case('d'):
					g.move_shape_right();	//move control block right
					display.setText(g.toString());	//update screen after remapping
					System.out.println("S - Down");	//display in console that 's' was pressed
					break;
				case('C'):
				case('c'):
					if(q.get_hold() == null) {
						g.unmap_shape();
						q.set_hold(g.get_control().get_shape_type());
						q.set_move_to_hold(false);
						g.unmap_control();
						if(!g.set_control_block(q.dequeue_shape().get_shape_type())) {
							game_running = false;
						}
						display.setText(g.toString());
						System.out.println(q.toString());
					}
					else if(q.get_move_to_hold()) {
						g.unmap_shape();
						int shape_type = q.get_hold().get_shape_type();
						System.out.println(shape_type);
						q.set_hold(q.dequeue_shape().get_shape_type());
						q.set_move_to_hold(false);
						g.unmap_control();
						if(!g.set_control_block(shape_type)) {
							game_running = false;
						}
						display.setText(g.toString());
						System.out.println("C - Hold");
						System.out.println(q.toString());

					}
					break;
				case(' '): //ESPACE - send shape down
					g.send_shape_down();	//send control block down
					int lines_completed = g.check_completed_lines(); //variable that saves number of lines completed
					score += lines_completed * lines_completed * 10; // check if lines are completed and add to score variable
					current_score.setText("Score: " + score);
					int shape_type = q.dequeue_shape().get_shape_type();
					if(!g.set_control_block(shape_type)) { //check if spawn area of control block shape is empty
						game_running = false;
						break;
					}
					q.set_move_to_hold(true);
					display.setText(g.toString());	//update screen after remapping
					System.out.println("ESP - Send down");	//display in console that ' ' was pressed
					break;
				default:
					break;
				}
		    }
		}
		
		public void keyReleased(KeyEvent e) {}	//mandatory keyReleased fn() due to abstract class
	}
	
	public static void main(String[] args) {
		q.populate();
		
		
		/*
		set_GUI();	//set up the GUI and its elements
		game_loop();	//start the game loop
		display.setText("\n\n\n\n\n\n\n\n\n     GAME OVER\n    Final Score: " + score); //when game loop ends, print GAME OVER on display
		*/
		
	}
}