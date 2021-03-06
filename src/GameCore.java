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
	private static JTextArea hold_display; //Creates text area for hold shape
	private static JTextArea queue_1; //Creates text area for queued shape in position 1
	private static JTextArea queue_2; //Creates text area for queued shape in position 2
	private static JTextArea queue_3; //Creates text area for queued shape in position 3
	private static JLabel current_score; //Variable that holds current score
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
		display.setSize(115, 355);	//set size of display in pixels
		display.setLocation(80, 20);	//set location of display in frame
		display.setVisible(true);	//make display visible
		display.setBackground(SystemColor.info);	//set display background color to an arbitrary color that contrasts
		display.setBorder(new LineBorder(new Color(0, 0, 0)));	//let the display have a black border

		hold_display = new JTextArea();	//initialize new JTextArea to be the screen
		hold_display.setSize(45, 67);	//set size of display in pixels
		hold_display.setLocation(20, 20);	//set location of display in frame
		hold_display.setVisible(true);	//make display visible
		hold_display.setBackground(SystemColor.info);	//set display background color to an arbitrary color that contrasts
		hold_display.setBorder(new LineBorder(new Color(0, 0, 0)));	//let the display have a black border
		
		queue_1 = new JTextArea();	//initialize new JTextArea to be the screen
		queue_1.setSize(45, 67);	//set size of display in pixels
		queue_1.setLocation(210, 20);	//set location of display in frame
		queue_1.setVisible(true);	//make display visible
		queue_1.setBackground(SystemColor.info);	//set display background color to an arbitrary color that contrasts
		queue_1.setBorder(new LineBorder(new Color(0, 0, 0)));	//let the display have a black border
		
		queue_2 = new JTextArea();	//initialize new JTextArea to be the screen
		queue_2.setSize(45, 67);	//set size of display in pixels
		queue_2.setLocation(210, 95);	//set location of display in frame
		queue_2.setVisible(true);	//make display visible
		queue_2.setBackground(SystemColor.info);	//set display background color to an arbitrary color that contrasts
		queue_2.setBorder(new LineBorder(new Color(0, 0, 0)));	//let the display have a black border
		
		queue_3 = new JTextArea();	//initialize new JTextArea to be the screen
		queue_3.setSize(45, 67);	//set size of display in pixels
		queue_3.setLocation(210, 170);	//set location of display in frame
		queue_3.setVisible(true);	//make display visible
		queue_3.setBackground(SystemColor.info);	//set display background color to an arbitrary color that contrasts
		queue_3.setBorder(new LineBorder(new Color(0, 0, 0)));	//let the display have a black border
		
		current_score = new JLabel(); //delcare and initialize label
		current_score.setSize(115, 30); //set size of label
		current_score.setLocation(80, 380); //set location of label in frame
		current_score.setVisible(true); //set label to be visible
		current_score.setBackground(SystemColor.info); //set label color
		current_score.setBorder(new LineBorder(new Color(0, 0, 0))); //set label border visible
		current_score.setText("Score: " + Integer.toString(score)); //set initial text 
		JFrame frame = new JFrame();	//set frame object that contains the display and other things
		frame.setLayout(null);	// not set a layout for the frame to allow place objects freely (any coordinates)
		frame.add(key_detector); //add key detector object to frame
		frame.add(display);	//add display to frame
		frame.add(hold_display); //add hold display to frame
		frame.add(current_score); //add current score label to frame
		frame.add(queue_1); //add queue 1 display to frame
		frame.add(queue_2); //add queue 2 display to frame
		frame.add(queue_3); //add queue 3 display to frame
		frame.setTitle("Tetriz");	//set frame title as "Tetriz"
		frame.setFocusCycleRoot(true);	//set frame as root of focus traversal cycle
		frame.setSize(440, 480);	//set frame size
		frame.setVisible(true);	//set frame visible (display window)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//set default close operation (press X to close)
	}
	
	public static void game_loop() {
		q.populate();
		int shape_type = q.dequeue_shape().get_shape_type(); //
		g.set_control_block(shape_type); //set control block with shape type as parameter
		display.setText(g.toString());	//display grid in display
		hold_display.setText(q.print_shape(0));
		queue_1.setText(q.print_shape(1)); //display queued shape in position 1
		queue_2.setText(q.print_shape(2)); //display queued shape in position 2
		queue_3.setText(q.print_shape(3)); //display queued shape in position 3
		while(game_running) { //actual game loop 
			try {
				Thread.sleep(1000);	//make program sleep for 1 second before sending the next update
				if(!g.move_shape_down()) {	//if shape could not move down
					int lines_completed = g.check_completed_lines(); //variable that saves number of lines completed
					score += lines_completed * lines_completed * 10; // check if lines are completed and to score variable
					current_score.setText("Score: " + score); //update current score
					shape_type = q.dequeue_shape().get_shape_type(); //dequeue shape and get its shape type
					if(!g.set_control_block(shape_type)) {	//set new control block at the top, if not possible
						game_running = false; //then set to false
					}
				}
				display.setText(g.toString());	//else, allow the shape to move and update the grid 
				hold_display.setText(q.print_shape(0));
				queue_1.setText(q.print_shape(1)); //display queued shape in position 1
				queue_2.setText(q.print_shape(2)); //display queued shape in position 2
				queue_3.setText(q.print_shape(3)); //display queued shape in position 3
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
					hold_display.setText(q.print_shape(0)); //display shape on hold
					queue_1.setText(q.print_shape(1)); //display queued shape in position 1
					queue_2.setText(q.print_shape(2)); //display queued shape in position 2
					queue_3.setText(q.print_shape(3)); //display queued shape in position 3
					System.out.println("D - Right");	//display in console that 'd' was pressed
					break;
				case('D'):	//D - move shape right
				case('d'):
					g.move_shape_right();	//move control block right
					display.setText(g.toString());	//update screen after remapping
					System.out.println("S - Down");	//display in console that 's' was pressed
					break;
				case('C'):
				case('c'): //C - hold control shape
					if(q.get_hold() == null) {
						g.unmap_shape(); //unmap control shape from grid
						q.hold_shape(g.get_control().get_shape_type()); //hold control shape my getting its shape type
						q.set_move_to_hold(false); //set move_to_hold flag to false
						g.unmap_control(); //unmap control block from grid
						if(!g.set_control_block(q.dequeue_shape().get_shape_type())) { //if cannot set control block in grid
							game_running = false; //stop game
						}
					}
					else if(q.get_move_to_hold()) { //if move_to_hold flag is true
						g.unmap_shape(); //unmap shape from grid
						int shape_type = q.get_hold().get_shape_type(); //get shape type from hold block variable
						q.hold_shape(q.dequeue_shape().get_shape_type()); //hold next shape from queue by dequeueing next shape
						q.set_move_to_hold(false); //set move_to_hold variable to false
						g.unmap_control(); //unmap control block from grid
						if(!g.set_control_block(shape_type)) { //if cannot set control block in grid
							game_running = false; //stop game
						}
					}
					display.setText(g.toString()); //update main screen
					hold_display.setText(q.print_shape(0)); //display hold shape
					queue_1.setText(q.print_shape(1)); //display queued shape in position 1
					queue_2.setText(q.print_shape(2)); //display queued shape in position 2
					queue_3.setText(q.print_shape(3)); //display queued shape in position 3
					System.out.println("C - Hold");
					break;
				case(' '): //ESPACE - send shape down
					g.send_shape_down();	//send control block down
					int lines_completed = g.check_completed_lines(); //variable that saves number of lines completed
					score += lines_completed * lines_completed * 10; // check if lines are completed and add to score variable
					current_score.setText("Score: " + score); //update current score
					int shape_type = q.dequeue_shape().get_shape_type();
					if(!g.set_control_block(shape_type)) { //check if spawn area of control block shape is empty
						game_running = false;
						break;
					}
					q.set_move_to_hold(true); //set move_to_hold flag to true
					display.setText(g.toString());	//update screen after remapping
					hold_display.setText(q.print_shape(0)); //display hold shape
					queue_1.setText(q.print_shape(1)); //display queued shape in position 1
					queue_2.setText(q.print_shape(2)); //display queued shape in position 2
					queue_3.setText(q.print_shape(3)); //display queued shape in position 3
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
		set_GUI();	//set up the GUI and its elements
		game_loop();	//start the game loop
		display.setText("\n\n\n\n\n\n\n\n\n     GAME OVER\n    Final Score: " + score); //when game loop ends, print GAME OVER on display		
	}
}