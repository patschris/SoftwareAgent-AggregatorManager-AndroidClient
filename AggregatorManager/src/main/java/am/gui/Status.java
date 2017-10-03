package am.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import am.classes.NmapJob;
import am.structures.JobQueue;
import am.structures.RegisteredAgents;

/**
 * 	@author	C. Patsouras I. Venieris
 * 	@version 1
 */
public class Status extends JFrame {

	private static final long serialVersionUID = 1L;
	/**
	 * Label that contains the title of the frame.
	 */
	private JLabel header1;
	/**
	 * Label that contains the subtitle of the frame.
	 */
	private JLabel header2;
	/**
	 * Label that contains title of online agents' list.
	 */
	private JLabel leftTitle;
	/**
	 * Label that contains title of offline agents' list.
	 */
	private JLabel rightTitle;
	/**
	 * List model of online agents.
	 */
	@SuppressWarnings("rawtypes")
	private DefaultListModel onlineModel;
	/**
	 * List of online agents.
	 */
	@SuppressWarnings("rawtypes")
	private JList onlineList;
	/**
	 * Scroll panel that contains the list with the online agents.
	 */
	private JScrollPane OnlineScrollPane;
	/**
	 * List model of offline agents.
	 */
	@SuppressWarnings("rawtypes")
	private DefaultListModel offlineModel;
	/**
	 * List of offline agents.
	 */
	@SuppressWarnings("rawtypes")
	private JList offlineList;
	/**
	 * Scroll panel that contains the list with the offline agents.
	 */
	private JScrollPane OfflineScrollPane;
	/**
	 * button used for stopping an online agent.
	 */
	private JButton StopOnlineButton;
	/**
	 * button used for stopping an offline agent.
	 */
	private JButton StopOfflineButton;
	/**
	 * @return the list model of the online agents' list.
	 */
	@SuppressWarnings("rawtypes")
	public DefaultListModel getOnlineModel() {
		return onlineModel;
	}
	/**
	 * @return the list model of the offline agents' model
	 */
	@SuppressWarnings("rawtypes")
	public DefaultListModel getOfflineModel() {
		return offlineModel;
	}
	/**
	 * Constructs a <code>StatusGraph</code>.
	 */
	public Status() {
		super("Online/Offline Software Agents - Stop an Agent");
		this.setSize(700,500); /* setting size for this frame */
		this.setLayout(null); /* no default layout is used */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); /* dispose the frame on close */
		this.setResizable(false); /* frame is not resizable */
		this.addHeaders();
		this.addListTitles();
		this.addLists();
		this.addButtons();
		this.setVisible(true); /* makes the frame visible */
	}
	/**
	 *  Adds labels that contain title and subtitle of the frame.
	 */
	private void addHeaders() {
		this.header1 = new JLabel("You can see both online and offline Agents");
		this.header1.setSize(400, 30);
		this.header1.setLocation(150, 5);
		this.header1.setFont(new Font("Serif", Font.BOLD, 15));
		this.getContentPane().add(header1);
		this.header2 = new JLabel("Pick an Agent and press the proper button to stop him");
		this.header2.setSize(500, 30);
		this.header2.setLocation(100, 25);
		this.header2.setFont(new Font("Serif", Font.BOLD, 15));
		this.getContentPane().add(header2);
	}
	/**
	 * Adds labels that contain titles for online and offline agents' list.
	 */
	private void addListTitles() {
		this.leftTitle = new JLabel("Online Agents");
		this.leftTitle.setSize(400, 60);
		this.leftTitle.setLocation(80, 55);
		this.rightTitle = new JLabel("Offline Agents");
		this.rightTitle.setSize(400, 60);
		this.rightTitle.setLocation(450, 55);
		this.getContentPane().add(this.rightTitle);
		this.getContentPane().add(this.leftTitle);
	}
	/**
	 * Adds online and offline agents' list.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addLists(){
		RegisteredAgents ra = RegisteredAgents.getInstance(this,null,null);
		this.onlineModel = new DefaultListModel();
		/* get every online agent */
		String[] on = ra.getOnlineRegs();
		if (on != null){
			for (int i = 0 ; i < on.length ; i++) {
				/* put all online agents into this model */
				this.onlineModel.addElement(on[i]);
			}
		}
		/* choosing one online agent at a time to stop */
		this.onlineList = new JList(this.onlineModel);
		this.onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		/* adding the list in a scroll panel and scrollbars for scroll panel */
		this.OnlineScrollPane = new JScrollPane(this.onlineList);
		this.OnlineScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.OnlineScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.OnlineScrollPane.setBounds(30, 100, 280, 300);
		this.getContentPane().add(this.OnlineScrollPane);
		/* Do the same stuff for offline agents' list */
		this.offlineModel = new DefaultListModel();
		String[] off = ra.getOfflineRegs();
		if (off != null){
			for (int i = 0 ; i < off.length ; i++){
				this.offlineModel.addElement(off[i]);
			}
		}
		this.offlineList = new JList(this.offlineModel);
		this.offlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.OfflineScrollPane = new JScrollPane(this.offlineList);
		this.OfflineScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.OfflineScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.OfflineScrollPane.setBounds(400, 100, 280, 300);
		this.getContentPane().add(this.OfflineScrollPane);
	}
	/**
	 * Add buttons and listeners to this frame. 
	 */
	private void addButtons(){
		this.StopOnlineButton= new JButton();
		this.StopOnlineButton.setText("Stop Online Agent");
		this.StopOnlineButton.setSize(200, 30);
		this.StopOnlineButton.setLocation(60, 420);
		this.StopOnlineButton.setActionCommand("Stop Online Agent");
		this.StopOnlineButton.addActionListener(new StopButtonListener(this));
		this.StopOfflineButton = new JButton();
		this.StopOfflineButton.setText("Stop Offline Agent");
		this.StopOfflineButton.setSize(200, 30);
		this.StopOfflineButton.setLocation(450, 420);
		this.StopOfflineButton.setActionCommand("Stop Offline Agent");
		this.StopOfflineButton.addActionListener(new StopButtonListener(this));
		this.getContentPane().add(this.StopOnlineButton );
		this.getContentPane().add(this.StopOfflineButton );
	}
	

	/**
	 *  Listener for <code>Status</code> buttons.
	 * 	@author	Omada13
	 *	@version 1
	 */
	public class StopButtonListener implements ActionListener {
		/**
		 * the <code>Status</code> frame.
		 */
		Status win;
		/**	
		 *  Sets the <code>Status</code> to the given value.
		 * 	@param	a	the <code>Status</code> frame.
		 */
		public StopButtonListener(Status a){
			this.win=a;
		}
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			String command = ae.getActionCommand();
			this.win.setVisible(false);		
			if (command.equals("Stop Online Agent")) { /* if button for stopping an online agent is pressed */
				String choise1 = (String) onlineList.getSelectedValue();
				if (choise1 == null) { /* check if any agent is selected*/
					JOptionPane.showMessageDialog(this.win,"Select an option\nThen press the button");
					this.win.setVisible(true);		
					return;
				}
				/* create the command to stop the agent */
				String[] ar = choise1.split(" ");
				int hash = Integer.parseInt(ar[2]);
				JobQueue jq = JobQueue.getInstance(null);
				jq.push(new NmapJob(-1,"exit(0)",true,-1,hash));
				onlineModel.remove(onlineList.getSelectedIndex());
				String s1 = "You will stop online Agent:\n" + choise1;
				JOptionPane.showMessageDialog(this.win,s1);
				this.win.setVisible(true);
			}
			/* Do the same stuff for offline agents if the button for stopping an offline agent is pressed */
			else if (command.equals("Stop Offline Agent")) {
				String choise2 = (String) offlineList.getSelectedValue();
				if (choise2==null) {
					JOptionPane.showMessageDialog(this.win,"Select an option\nThen press the button");
					this.win.setVisible(true);		
					return;
				}
				String[] ar = choise2.split(" ");
				int hash = Integer.parseInt(ar[2]);
				JobQueue jq = JobQueue.getInstance(null);
				jq.push(new NmapJob(-1,"exit(0)",true,-1,hash));
				offlineModel.remove(offlineList.getSelectedIndex());
				String s2 = "You will stop online Agent:\n" + choise2;
				JOptionPane.showMessageDialog(this.win,s2);
				this.win.setVisible(true); 
			}
		}
	}
}