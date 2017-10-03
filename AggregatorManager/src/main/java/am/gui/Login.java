package am.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.glassfish.grizzly.http.server.HttpServer;

import am.resources.DbConnection;
import am.securelogin.SecureLogin;
import am.services.agent.ServerStarting;

/**	Login window prompts the user to type username and password
 * 	in order to startup the server and and act like
 * 	<code>AggregatorManager</code>.
 * 	
 * 	@author		C. Patsouras I. Venieris
 * 	@version	1
 *
 */
public class Login extends JFrame{

	/**
	 * identifier for this window.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * title of this window.
	 */
	private JLabel title;
	
	/**
	 * username label.
	 */
	private JLabel username;
	
	/**
	 * username field.
	 */
	private JTextField userfield;
	
	/**
	 * password label.
	 */
	private JLabel password;
	
	/**
	 * password field.
	 */
	private JTextField passfield;
	
	/**
	 * Cancel button.
	 */
	private JButton cancelButton;
	
	/**
	 * Submit button.
	 */
	private JButton submitButton;

	/**
	 * 	Constructs a <code>Login</code> window
	 * 	with specific values.
	 */
	public Login(){
		super("Login window");	// calls Frame constructor
		this.setSize(700,300);	// size of login window
		this.setLayout(null);	// no default layout is used
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// close operation
		this.addLabel();	//
		this.addUsername();	//
		this.addPassword();	//
		this.addButtons();	//
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				// program ends if Login window is closed
				System.exit(0);
			}        
		}); 
		this.setResizable(false);
		this.setVisible(true);
	}


	/**
	 * 	Creating a label to inform the user.
	 */
	private void addLabel(){
		this.title=new JLabel("Please login to connect to Aggregator Manager");
		title.setSize(400, 50);
		title.setLocation(80, 20);
		this.getContentPane().add(title);
	}

	/**
	 * 	Label used for Username field.
	 */
	private void addUsername(){
		this.username = new JLabel("Username");	// 	adds a Username label
		this.username.setSize(800,80);			//	setting its size and
		this.username.setLocation(80,75);		//	location
		this.userfield=new JTextField();		//	adds text field for username
		this.userfield.addKeyListener(new KeyboardListener(this));	// sets a keylistener for enter
		this.userfield.setColumns(100);			//	adds a username text field		
		this.userfield.setSize(400, 30);		//	sets its size and location
		this.userfield.setLocation(200, 100);	//
		this.getContentPane().add(this.username);	//	adds username label and field
		this.getContentPane().add(this.userfield);	//	to this window
	}

	/**
	 * Label user for password field.
	 */
	private void addPassword(){
		this.password = new JLabel("Password");	//	adds a password label
		this.password.setSize(800,80);			//	sets its size and location
		this.password.setLocation(80,125);
		this.passfield=new JPasswordField();	//	adds a password field
		this.passfield.addKeyListener(new KeyboardListener(this));	// adds key listener for enter
		this.passfield.setColumns(100);			//	adds password field
		this.passfield.setSize(400, 30);		//	sets its size and location
		this.passfield.setLocation(200, 150);	
		this.getContentPane().add(this.password);	//	adds password label and
		this.getContentPane().add(this.passfield);	//	password field to this window
	}

	/**
	 * Buttons of the <code>Login</code> window. 
	 */
	private void addButtons(){
		this.cancelButton= new JButton();		//	creates Cancel button,
		this.cancelButton.setText("Cancel");	// 	sets text for Cancel button
		this.cancelButton.setSize(100, 30);		//	sets its size and location
		this.cancelButton.setLocation(250, 200);
		this.cancelButton.setActionCommand("Cancel");	// sets action command for Cancel button
		this.cancelButton.addActionListener(new LoginListeners(this));	// sets listener for Cancel button
		this.submitButton = new JButton();		// 	creates Submit button
		this.submitButton.setText("Submit");	//	sets its size and location
		this.submitButton.setSize(100, 30);		
		this.submitButton.setLocation(400, 200);
		this.submitButton.setActionCommand("Submit");	// sets action command for Sumbit button
		this.submitButton.addActionListener(new LoginListeners(this));	// sets listener for Submit button
		this.getContentPane().add(this.cancelButton);	//
		this.getContentPane().add(this.submitButton);	// adds Cancel and Sumbit buttons to this window
	}


	/** Keyboard listener for <code>Login</code> window.
	 * @author	Omada13
	 * @version 1
	 */
	public class KeyboardListener implements KeyListener {

		/**
		 * The login window.
		 */
		Login win;

		/**	Sets the login window to the given value.
		 * @param a	the login window.
		 */
		public KeyboardListener(Login a){
			this.win=a;
		}

		@SuppressWarnings({ "unused", "static-access" })
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				// if enter is typed
				// follows same procedure just as Submit button
				this.win.setVisible(false);		
				String str1 = userfield.getText();
				// encrypts the given password to compare
				// with the one stored in database
				
				SecureLogin sl = SecureLogin.getInstance();
				String str2 = sl.sha256(passfield.getText());
				// connects to database to compare the encrypted
				// password and check if this admin is already
				// connected
				DbConnection dbc = DbConnection.getInstance();
				Connection conn = null;
				Statement stmt = null;
				Statement stmt2 = null;
				ResultSet r = null ;
				boolean flag = false;
				try {
					Class.forName(dbc.getDriver());
				} catch (ClassNotFoundException ec) {
					ec.printStackTrace();
				}
				try {
					// connects to database and updates
					// this user state
					conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
					stmt = conn.createStatement();
					String query = "select * from Users where Username=\""+str1+"\" and Password=\""+str2+"\"";
					r = stmt.executeQuery(query);
					while(r.next()){
						if (r.getBoolean("Active") == false) {
							flag = true;
							stmt2 = conn.createStatement();
							String q = "update Users set Active=true where Username=\""+str1+"\" and Password=\""+str2+"\"";
							stmt2.executeUpdate(q);
						}
					}
				} catch (SQLException es) {
					es.printStackTrace();
				}
				finally{
					try {
						stmt.close();
						if (stmt2 != null) stmt2.close();
						conn.close();
						r.close();
					} catch (SQLException esq) {
						esq.printStackTrace();
					}
				}
				if (flag){
					// on successful login
					// start the server of AM
					HttpServer server = null;
					try {
						server = ServerStarting.startServer();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					// show main window/Navigation Panel
					NavigationPanel np = new NavigationPanel();
				}
				else{
					// show message in case of invalid credentials
					
					JOptionPane.showMessageDialog(this.win,
							"Incorrect username or password!"
									+ "\nPlease try again");
					userfield.setText(new String());
					passfield.setText(new String());
					this.win.setVisible(true);		
				}
			}			
		}
		@Override
		public void keyReleased(KeyEvent e) {}
		@Override
		public void keyTyped(KeyEvent e) {}
	}


	/** Listener for Sumbit and Cancel buttons.
	 * @author	Omada13
	 * @version	1
	 */
	public class LoginListeners implements ActionListener {
		/*
		 http://www.tutorialspoint.com/swing/swing_event_handling.htm
		 */
		/**
		 * The login window.
		 */
		Login win;
		
		/**	Sets the login window to the given value.
		 * @param a	the login window.
		 */
		public LoginListeners(Login a){
			this.win=a;
		}

		@SuppressWarnings({ "unused", "static-access" })
		public void actionPerformed(ActionEvent ae){
			String command = ae.getActionCommand();
			if (command.equals("Submit")){
				this.win.setVisible(false);
				// encrypts the given password to compare
				// with the one stored in database
				String str1 = userfield.getText();
				SecureLogin sl = SecureLogin.getInstance();
				String str2 = sl.sha256(passfield.getText());
				DbConnection dbc = DbConnection.getInstance();
				// connects to database to compare the encrypted
				// password and check if this admin is already
				// connected
				Connection conn = null;
				Statement stmt = null;
				Statement stmt2 = null;
				ResultSet r = null ;
				boolean flag = false;
				try {
					Class.forName(dbc.getDriver());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					// connects to database and updates
					// this user state
					conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
					stmt = conn.createStatement();
					String query = "select * from Users where Username=\""+str1+"\" and Password=\""+str2+"\"";
					r = stmt.executeQuery(query);
					while(r.next()){
						if (r.getBoolean("Active") == false){
							flag = true;
							stmt2 = conn.createStatement();
							String q = "update Users set Active=true where Username=\""+str1+"\" and Password=\""+str2+"\"";
							stmt2.executeUpdate(q);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally{
					try {
						stmt.close();
						if (stmt2!=null) stmt2.close();
						conn.close();
						r.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (flag){
					HttpServer server = null;
					try {
						server = ServerStarting.startServer();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					// show main window/Navigation Panel
					NavigationPanel np = new NavigationPanel();
				}
				else{
					// show message in case of invalid credentials
					JOptionPane.showMessageDialog(this.win,
							"Incorrect username or password!\nPlease try again");
					userfield.setText(new String());
					passfield.setText(new String());
					this.win.setVisible(true);		
				}
			}
			else if (command.equals("Cancel")){
				// actions taken on Cancel button
				this.win.setVisible(false);
				JOptionPane.showMessageDialog(this.win,"Server will not start");
				// exit in case of Cancel button
				System.exit(0);
			}
		}
	}
}