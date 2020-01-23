import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class GameCore extends JPanel {
	
	private static JTextArea display;	//Creates text area where the grid is displayed
	private static Grid g = new Grid();	//Creates new grid of blocks object
	
	public GameCore() {
		
		KeyboardDectection listener = new KeyboardDectection(); //Initialize keyboard detection object
		addKeyListener(listener);	//Add key listener to Key detection object
		setFocusable(true);	//Sets focusable to true and overrides original default state
	}
	
	public static void set_GUI() {
		JFrame frame = new JFrame();
		frame.setLayout(null);
		GameCore key_detector = new GameCore();
		display = new JTextArea();
		display.setSize(120, 370);
		display.setLocation(50, 20);
		display.add(key_detector);
		display.setVisible(true);
		display.setBackground(SystemColor.info);
		display.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.add(key_detector);
		frame.add(display);
		frame.setTitle("Tetriz");
		frame.setFocusCycleRoot(true);
		frame.setSize(250, 450);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void game_loop() {
		g.set_control_block();
		display.setText(g.toString());
		while(true) {
			try {
				Thread.sleep(1000);
				if(!g.move_shape_down()) {
					g.check_completed_lines();
					g.set_control_block();
				}
				display.setText(g.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class KeyboardDectection implements KeyListener {
		
		public void keyTyped(KeyEvent e) {}
		
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyChar()) {
			case('W'):
			case('w'):
				g.rotate_shape();
				display.setText(g.toString());
				System.out.println("W - Rotate");
				break;
			case('A'):
			case('a'):
				g.move_shape_left();
				display.setText(g.toString());
				System.out.println("A - Left");
				break;
			case('S'):
			case('s'):
				if(!g.move_shape_down()) {
					g.check_completed_lines();
					g.set_control_block();
				}
				display.setText(g.toString());
				System.out.println("D - Right");
				break;
			case('D'):
			case('d'):
				g.move_shape_right();
				display.setText(g.toString());
				System.out.println("S - Down");
				break;
			case(' '):
				g.send_shape_down();
				g.check_completed_lines();
				g.set_control_block();
				display.setText(g.toString());
				//g.set_control_block();
				System.out.println("ESP - Send down");
				break;
			default:
				break;
			}
		}
		
		public void keyReleased(KeyEvent e) {}
	}
	
	public static void main(String[] args) {
		set_GUI();
		game_loop();
		/*/TEST AREA
		g.set_control_block();
		g.grid[21][0].set_block();
		g.grid[21][1].set_block();
		g.grid[21][2].set_block();
		g.grid[21][3].set_block();
		g.grid[21][4].set_block();
		g.grid[21][5].set_block();
		g.grid[21][6].set_block();
		g.grid[21][7].set_block();
		g.grid[21][8].set_block();
		g.grid[21][9].set_block();
		g.grid[20][0].set_block();
		g.grid[20][1].set_block();
		g.grid[20][2].set_block();
		g.grid[20][3].set_block();
		g.grid[20][4].set_block();
		g.grid[20][5].set_block();
		g.grid[20][6].set_block();
		g.grid[20][7].set_block();
		g.grid[20][8].set_block();
		g.grid[20][9].set_block();
		g.grid[19][8].set_block();
		System.out.println(g.toString());
		g.move_shape_down();
		System.out.println(g.toString());
		g.check_completed_lines();
		System.out.println(g.toString());
		*///TEST AREA
	}
	/*
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
	 */
}