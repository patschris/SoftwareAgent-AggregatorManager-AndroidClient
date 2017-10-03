package am.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import am.classes.AndroidLogin;
import am.resources.DbConnection;
import am.services.agent.AcceptBlocker;
import am.structures.AndroidQueue;

/**
 * 
 * @author C. Patsouras I. Venieris
 * @version 1
 */
public class AndroidMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	/**
	 * a model list for viewing incoming registration requests.
	 */
	@SuppressWarnings("rawtypes")
	private DefaultListModel model;
	
	/**
	 * title of this frame.
	 */
	private JLabel title;
	
	/**
	 * list of this frame.
	 */
	@SuppressWarnings("rawtypes")
	private JList list;
	
	/**
	 * scroll panel for this frame.
	 */
	private JScrollPane scrollPane;
	
	/**
	 * button used for accepting an Agent.
	 */
	private JButton acceptButton;
	
	/**
	 * button used for denying access to an Agent.
	 */
	private JButton denyButton;

	/**
	 * @return the list model of this frame.
	 */
	
	@SuppressWarnings("rawtypes")
	public DefaultListModel getModel(){
		return this.model;
	}

	/**
	 * Constructs an <code>Android menu</code>.
	 */
	public AndroidMenu(){
		super("Android Clients' requests");
		// passing this window as an argument
		// to the pending queue for interaction.
		this.setSize(640, 550);		// setting size for this frame
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// dispose the frame on close
		this.setLayout(null);		// no default layout is used
		this.setResizable(false);	// frame is not resizable
		this.addTitle();
		this.addList();		// adds the list to the frame
		this.addButtons();	// .. and the buttons
		this.setVisible(true);
	}	
	/**
	 * Adds title to this <code>AndroidMenu</code> frame.
	 */
	private void addTitle(){
		this.title = new JLabel("Android Clients' requests");
		this.title.setLocation(210, 30);
		this.title.setSize(500, 20);
		this.title.setFont(new Font("Serif", Font.BOLD, 15));
		this.getContentPane().add(this.title);
	}

	/**
	 * Adds a list to this <code>AcceptDenyMenu</code> frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addList(){
		this.model = new DefaultListModel();
		AndroidQueue aq = AndroidQueue.getInstance(this);
		String[] array = aq.toShow();
		if (array != null){
			for (int i = 0; i < array.length ; i++){
				// adds every pending request to the list model
				this.model.addElement(array[i]);
			}
		}
		this.list = new JList(this.model);
		// choosing one Agent at a time to accept/deny
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// adding the list in a scroll panel
		// and scrollbars for scroll panel
		this.scrollPane = new JScrollPane(this.list);
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setBounds(70, 80, 500, 350);
		this.getContentPane().add(this.scrollPane);
	}

	/**
	 * Adds buttons to <code>AndroidMenu</code> and a listener for them.
	 */
	private void addButtons(){
		this.acceptButton= new JButton(); // button for accepting an Agent
		this.acceptButton.setText("Accept");
		this.acceptButton.setSize(100, 30);
		this.acceptButton.setLocation(200, 450);
		this.acceptButton.setActionCommand("Accept");
		this.acceptButton.addActionListener(new AndroidMenuListener(this));
		this.denyButton = new JButton();
		this.denyButton.setText("Deny"); // button for denying access to an Agent
		this.denyButton.setSize(100, 30);
		this.denyButton.setLocation(350, 450);
		this.denyButton.setActionCommand("Deny");
		this.denyButton.addActionListener(new AndroidMenuListener(this));
		this.getContentPane().add(this.acceptButton);
		this.getContentPane().add(this.denyButton);
	}

	/** 
	 *  Listener for <code>AndroidMenu</code> buttons.
	 * 	@author		Omada13
	 * 	@version	1
	 */
	public class AndroidMenuListener implements ActionListener {

		/**
		 * the <code>AndroidMenu</code> frame.
		 */
		AndroidMenu win;
		/**	
		 * Sets the <code>AndroidMenu</code> to the given value.
		 * 	@param	a	the <code>AndroidMenu</code>.
		 */
		public AndroidMenuListener(AndroidMenu a) {
			this.win=a;
		}

		@Override
		public void actionPerformed(ActionEvent ae){
			String command = ae.getActionCommand();
			this.win.setVisible(false);	
			String choise = (String) list.getSelectedValue();
			if (choise==null) {
				// if no option where selected
				JOptionPane.showMessageDialog(this.win,"Select an option\nThen press the button");
				this.win.setVisible(true);
				return;
			}
			// get the device Name of the selected Android Client
			String str = (String) list.getSelectedValue();
			// search for it in the PendingQueue
			AndroidQueue aq = AndroidQueue.getInstance(null);
			// obtain its registration info
			AndroidLogin r = aq.search(str);
			AcceptBlocker ap = null;
			// block it's request until an option is selected
			if (r != null) ap = r.getAb();			
			if (command.equals("Accept")) {
				// agent got accepted
				// unblock its request and send response to client
				ap.setAccept(true);
				String s = "Connection accepted:\n" + list.getSelectedValue();
				// connecting to database 
				DbConnection dbc = DbConnection.getInstance();
				Connection conn = null;
				Statement stmt = null;
				try {
					Class.forName(dbc.getDriver());
				} catch (ClassNotFoundException ec) {
					ec.printStackTrace();
				}
				try {
					// inserting the Agent into the database
					conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
					stmt = conn.createStatement();
					String query = "update AndroidClients set Active=true where Username=\""+r.getUsername()+"\";";
					stmt.executeUpdate(query);										
				} catch (SQLException es) {
					es.printStackTrace();
				}
				finally{
					try {
						// closing statement and connection
						stmt.close();
						conn.close();
					} catch (SQLException esq) {
						esq.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(this.win,s);
			}
			else if (command.equals("Deny")){
				// agent got rejected by AggregatorManager
				// unblock its request and send response
				ap.setAccept(false);
				String s = "Connection denied:\n" + list.getSelectedValue();  
				JOptionPane.showMessageDialog(this.win,s);
			}
			// remove the Agent from the PendingQueue
			aq.pop(r);
			this.win.setVisible(true);
		}
	}
}