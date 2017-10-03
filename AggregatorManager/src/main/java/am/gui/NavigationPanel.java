package am.gui;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.glassfish.grizzly.http.server.HttpServer;

import am.resources.DbConnection;
import am.services.agent.ServerStarting;


/**	<code>NavigationPanel</code> is the main window of the program.
 * 	Contains all the actions that the user handling
 * 	AggregatorManager can perform.
 * 	
 * 	@author		C. Patsouras I. Venieris
 * 	@version	1
 *
 */
public class NavigationPanel extends JFrame {

	/**
	 * identifier of this frame.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * title of this frame.
	 */
	private JLabel title; 
	
	/**
	 * connection label.
	 */
	private JLabel connectionLabel;
	
	/**
	 * connection button (accept/deny Agents).
	 */
	private JButton connectionButton;
	
	/**
	 * status label.
	 */
	private JLabel statusLabel;
	
	/**
	 * status button.
	 * shows online/offline agents and offering the user
	 * the option to stop them.
	 */
	private JButton statusButton;
	
	/**
	 * create a new nmap job label.
	 */
	private JLabel jobLabel;
	
	/**
	 * button for inserting new nmap job.
	 */
	private JButton jobButton;
	
	/**
	 * delete a periodic job label.
	 */
	private JLabel deleteLevel;
	
	/**
	 * button for deleting a periodic job.
	 */
	private JButton deleteButton;
	
	/**
	 * label for showing results.
	 */
	private JLabel outputLabel;
	
	/**
	 * button for showing accepted results.
	 */
	private JButton outputButton;
	
	/**
	 * the server used by <code>AggregatorManager</code>.
	 */
	private HttpServer server;
	/**
	 * label for Android Clients
	 */
	private JLabel androidLabel;

	private JButton androidButton;


	/**
	 * Constructs a <code>NavigationPanel</code>.
	 */
	public NavigationPanel() {
		super("Navigation panel");	// title of navigation panel
		this.setSize(700,400);		// size and location
		this.setLayout(null);		// no default layout used.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// in case of window closing 
		this.addJLabels();										// program will exit
		this.addJButtons();
		this.setResizable(false);
		this.server = ServerStarting.getServer();	// the server used by AM
		this.addWindowListener(new WindowAdapter() {
			// if window is closed, StatusThread will be interrupted
			// and server will be stopped
			public void windowClosing (WindowEvent windowEvent){
				Thread t = ServerStarting.getStatusThread();
				t.interrupt();
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				server.stop();
				// connecting to database
				// to update status of the user 
				// that was handling AggregatorManager
				Connection conn = null;
				Statement stmt = null;
				DbConnection dbc = DbConnection.getInstance();
				try {
					Class.forName(dbc.getDriver());
				} catch (ClassNotFoundException ec) {
					ec.printStackTrace();
				}
				try {
					conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
					stmt = conn.createStatement();
					String q = "update Users set Active=false where Active=true";
					stmt.executeUpdate(q);		
					q = "update Agents set Online=false where Online=true";
					stmt.executeUpdate(q);		
				} catch (SQLException es) {
					es.printStackTrace();
				}
				finally{
					// closing statement and connection
					try {
						stmt.close();
						conn.close();
					} catch (SQLException esq) {
						esq.printStackTrace();
					}
				}
				// program exiting
				System.exit(0);
			}        
		}); 
		this.setVisible(true);
	}

	/**
	 * Adds labels to <code>NavigationPanel</code>.
	 */
	private void addJLabels(){
		this.title = new JLabel("Select one of the following services");
		this.title.setSize(400, 50);
		this.title.setLocation(200, 10);
		this.title.setFont(new Font("Serif", Font.BOLD, 15));
		this.getContentPane().add(title);
		this.connectionLabel = new JLabel("Accept/Deny connection requests");
		this.connectionLabel.setSize(400, 50);
		this.connectionLabel.setLocation(20, 50);
		this.getContentPane().add(this.connectionLabel);
		this.statusLabel = new JLabel("Check Agents' status and stop them");
		this.statusLabel.setSize(400, 50);
		this.statusLabel.setLocation(20, 100);
		this.getContentPane().add(this.statusLabel);
		this.jobLabel = new JLabel("Create a new nmap job for a Software Agent");
		this.jobLabel.setSize(400, 50);
		this.jobLabel.setLocation(20, 150);
		this.getContentPane().add(this.jobLabel);
		this.deleteLevel = new JLabel("Delete periodic nmap job");
		this.deleteLevel.setSize(400,50);
		this.deleteLevel.setLocation(20, 200);
		this.getContentPane().add(this.deleteLevel);
		this.outputLabel = new JLabel("See output of nmap jobs for a specific period");
		this.outputLabel.setSize(400, 50);
		this.outputLabel.setLocation(20, 250);
		this.getContentPane().add(this.outputLabel);
		this.androidLabel = new JLabel("Check for new Android clients");
		this.androidLabel.setSize(400, 50);
		this.androidLabel.setLocation(20, 300);
		this.getContentPane().add(this.androidLabel);
	}

	/**
	 * Adds buttons to <code>NavigationPanel</code> and a listener for them.
	 */
	private void addJButtons(){
		this.connectionButton= new JButton();
		this.connectionButton.setText("Accept/Deny");
		this.connectionButton.setSize(150, 25);
		this.connectionButton.setLocation(500, 65);
		this.connectionButton.setActionCommand("Accept/Deny");
		this.connectionButton.addActionListener(new NavigationListener());
		this.getContentPane().add(this.connectionButton);
		this.statusButton= new JButton();
		this.statusButton.setText("Get status");
		this.statusButton.setSize(150, 25);
		this.statusButton.setLocation(500, 115);
		this.statusButton.setActionCommand("Get status");
		this.statusButton.addActionListener(new NavigationListener());
		this.getContentPane().add(this.statusButton);
		this.jobButton= new JButton();
		this.jobButton.setText("Create job");
		this.jobButton.setSize(150, 25);
		this.jobButton.setLocation(500, 165);
		this.jobButton.setActionCommand("Create job");
		this.jobButton.addActionListener(new NavigationListener());
		this.getContentPane().add(this.jobButton);
		this.deleteButton = new JButton();
		this.deleteButton.setText("Delete periodic");
		this.deleteButton.setSize(150, 25);
		this.deleteButton.setLocation(500, 215);
		this.deleteButton.setActionCommand("Delete periodic");
		this.deleteButton.addActionListener(new NavigationListener());
		this.getContentPane().add(this.deleteButton);
		this.outputButton = new JButton();
		this.outputButton.setText("See output");
		this.outputButton.setSize(150, 25);
		this.outputButton.setLocation(500, 265);
		this.outputButton.setActionCommand("See output");
		this.outputButton.addActionListener(new NavigationListener());
		this.getContentPane().add(this.outputButton);
		this.androidButton= new JButton();
		this.androidButton.setText("Android Clients");
		this.androidButton.setSize(150, 25);
		this.androidButton.setLocation(500, 315);
		this.androidButton.setActionCommand("Android Client");
		this.androidButton.addActionListener(new NavigationListener());
		this.getContentPane().add(this.androidButton);

	}

	/** 
	 * 	Listener for buttons of <code>NavigationPanel</code>.
	 * 	@author		Omada13
	 *	@version 	1
	 */
	public class NavigationListener implements ActionListener {
		public void actionPerformed(ActionEvent ae){
			String command = ae.getActionCommand();
			if (command.equals("Accept/Deny")){
				// creates the AcceptDeny frame
				new AcceptDenyMenu();
			}
			else if (command.equals("Get status")){
				// creates the Status frame
				new Status();
			}
			else if (command.equals("Create job")){
				// creates the job frame
				new PeriodicMenu();
			}
			else if (command.equals("Delete periodic")){
				// creates the window used for deleting a
				// periodic nmap job.
				new DeletePeriodicJobGraph();
			}
			else if (command.equals("See output")){
				// creates the frame used for
				// viewing received results
				new OutputGraph();
			}
			else if (command.equals("Android Client")) {
				new AndroidMenu();
			}
		}
	}
}