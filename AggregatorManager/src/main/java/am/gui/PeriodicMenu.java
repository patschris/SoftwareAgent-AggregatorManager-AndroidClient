package am.gui;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import am.classes.NmapJob;
import am.structures.JobQueue;
import am.structures.RegisteredAgents;

/**
 * @author	C. Patsouras I. Venieris
 * @version 1
 */
public class PeriodicMenu extends JFrame {
	
	private static final long serialVersionUID = 1L;
	/**
	 * title of this frame.
	 */
	private JLabel title;
	/**
	 * Label that contains dropdown list of registered agents.
	 */
	private JLabel agentDropDown; 
	/**
	 * Shows the agents' list.
	 */
	@SuppressWarnings("rawtypes")
	private JComboBox agentBox;
	/**
	 * Model of dropdown agents' list.
	 */
	@SuppressWarnings("rawtypes")
	private DefaultComboBoxModel agentModel;
	/**
	 * Label that contains field to fill command that will be sent to SA.
	 */
	private JLabel paramLabel;
	/**
	 * Field to fill command that will be sent to SA.
	 */
	private JTextField paramField;
	/**
	 *  Model of dropdown a periodic list.
	 */
	private JLabel periodLabel;
	/**
	 * Shows the periodic list.
	 */
	@SuppressWarnings("rawtypes")
	private JComboBox periodicBox;
	/**
	 * Contains two options to declare whether the job is periodic or not.
	 */
	@SuppressWarnings("rawtypes")
	private DefaultComboBoxModel periodicModel;
	/**
	 *  Label that contains field to fill time.
	 */
	private JLabel timLabel;
	/**
	 * Field to fill time in seconds.
	 */
	private JTextField timeField ;
	/**
	 *  button used for clearing every field of this frame.
	 */
	private JButton clearButton;
	/**
	 * button used for submitting a job that will be sent to an SA.
	 */
	private JButton submitButton;
	/**
	 * Constructs an <code>PeriodicMenu</code>.
	 */
	public PeriodicMenu() {
		super("Set new nmap job");
		this.setSize(700,500); /* setting size for this frame */
		this.setLayout(null); /* no default layout is used */
		this.titleLabel();
		this.agentLabel();
		this.parametersLabel();
		this.periodicLabel();
		this.timeLabel();
		this.addButtons();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); /* dispose the frame on close */
		this.setResizable(false); /* frame is not resizable */
		this.setVisible(true); /* makes the frame visible */
	}
	/**
	 * @return a model list for viewing registered agents.
	 */
	@SuppressWarnings("rawtypes")
	public DefaultComboBoxModel getAgentModel() {
		return agentModel;
	}
	/**
	 * Adds a label that contains the title of the frame.
	 */
	private void titleLabel(){
		this.title = new JLabel("Create a nmap job and send it to a Software Agent");
		this.title.setSize(500, 50);
		this.title.setLocation(100, 10);
		this.title.setFont(new Font("Serif", Font.BOLD, 15));
		this.getContentPane().add(this.title);
	}
	/**
	 * Adds a label that contains a dropdown menu with registered agents.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void agentLabel(){
		this.agentDropDown = new JLabel();
		this.agentDropDown.setLayout( new BorderLayout() );
		this.agentDropDown.setSize(500, 25);
		this.agentDropDown.setLocation(50, 70);
		this.agentDropDown.add( new JLabel( "Select SA to send job :" ), BorderLayout.WEST );		
		this.agentModel = new DefaultComboBoxModel();
		this.agentBox = new JComboBox(this.agentModel);
		RegisteredAgents ra = RegisteredAgents.getInstance(null, this, null);
		String[] ar = ra.getRegs();
		if (ar != null){
			for (int i = 0 ; i < ar.length ; i++) {
				/* add all registered agents to this model */
				this.agentModel.addElement(ar[i]);
			}
			this.agentBox.setSelectedIndex(0);
		}
		this.agentBox.setMaximumRowCount(4);
		this.agentBox.setEditable(false);
		this.agentDropDown.add(this.agentBox, BorderLayout.EAST);		
		this.getContentPane().add(this.agentDropDown);
	}
	/**
	 * Adds a label that contains a field to type the command you want to be executed.
	 */
	private void parametersLabel(){
		this.paramLabel = new JLabel();
		this.paramLabel.setLayout( new BorderLayout() );
		this.paramLabel.setSize(500, 25);
		this.paramLabel.setLocation(50, 130);
		this.paramLabel.add( new JLabel( "Add the command  you want:" ), BorderLayout.WEST );
		this.paramField = new JTextField(10);
		this.paramLabel.add(this.paramField, BorderLayout.EAST );
		this.getContentPane().add(this.paramLabel);
	}
	/**
	 * Adds a label that contains a dropdown menu to choose whether the job will be executed periodically or only for one time.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void periodicLabel(){
		this.periodLabel = new JLabel();
		this.periodLabel.setLayout(new BorderLayout());
		this.periodLabel.setSize(500, 25);
		this.periodLabel.setLocation(50, 200);
		this.periodLabel.add(new JLabel("Command will be executed: "), BorderLayout.WEST );
		this.periodicModel = new DefaultComboBoxModel();
		this.periodicBox = new JComboBox(this.periodicModel);
		this.periodicModel.addElement("Only One Time");
		this.periodicModel.addElement("Periodically");
		this.periodicBox.setSelectedIndex(0);
		this.periodicBox.setMaximumRowCount(2);
		this.periodicBox.setEditable(false);
		this.periodLabel.add(this.periodicBox, BorderLayout.EAST );
		this.getContentPane().add(this.periodLabel);
	}
	/**
	 *  Adds time field to this frame.
	 */
	private void timeLabel(){
		this.timLabel = new JLabel();
		this.timLabel.setLayout(new BorderLayout());
		this.timLabel.setSize(500, 25);
		this.timLabel.setLocation(50, 270);
		this.timLabel.add( new JLabel( "Set the period in seconds:" ), BorderLayout.WEST );
		this.timeField = new JTextField(8);
		/* In this field, user can only type numbers */
		this.timeField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9'))) {
					e.consume();
				}
			}
		});
		this.timLabel.add(this.timeField,BorderLayout.EAST);
		this.getContentPane().add(this.timLabel);
	}
	/**
	 * Add button to this frame and listener for them.
	 */
	private void addButtons(){
		this.clearButton= new JButton();
		this.clearButton.setText("Clear");
		this.clearButton.setSize(100, 30);
		this.clearButton.setLocation(200, 350);
		this.clearButton.setActionCommand("Clear");
		this.clearButton.addActionListener(new PeriodicMenuListener(this));
		this.getContentPane().add(this.clearButton);
		this.submitButton= new JButton();
		this.submitButton.setText("Submit");
		this.submitButton.setSize(100, 30);
		this.submitButton.setLocation(350, 350);
		this.submitButton.setActionCommand("Submit");
		this.submitButton.addActionListener(new PeriodicMenuListener(this));
		this.getContentPane().add(this.submitButton);
	}

	/**
	 * 	@author	Omada13
	 *	@version 1
	 */
	public class PeriodicMenuListener implements ActionListener {
		/**
		 * the <code>PeriodicMenu</code> frame.
		 */
		private PeriodicMenu menu;
		/**
		 * Constructor of <code>PeriodicMenuListener</code>
		 * Sets the <code>PeriodicMenu</code> to the given value.
		 * @param m the <code>PeriodicMenu</code> frame.
		 */
		public PeriodicMenuListener(PeriodicMenu m){
			this.menu=m;
		}
		
		@Override
		public void actionPerformed(ActionEvent ae){
			String action = ae.getActionCommand();
			this.menu.setVisible(false);
			if (action.equals("Submit")){ /* if submit button is pressed */
				String agent = (String) agentBox.getSelectedItem();
				String command = paramField.getText(); /* check if the command field is empty */
				if (command.isEmpty()) {
					JOptionPane.showMessageDialog(this.menu,"Command is missing");
					this.menu.setVisible(true);
					return;
				}
				String isPeriodical = (String) periodicBox.getSelectedItem();
				Boolean periodicFlag;
				if (isPeriodical.equals("Only One Time")){
					periodicFlag=false;
				}
				else {
					periodicFlag=true;
				}
				int time;
				if (periodicFlag) { /* If the job will be executed periodically, time field cannot be empty */
					String timeString = timeField.getText();
					if (timeString.isEmpty()){
						JOptionPane.showMessageDialog(this.menu,"Period is missing");
						this.menu.setVisible(true);
						return;
					}
					else {
						time = Integer.parseInt(timeString);
					}
				}
				else { /* if the job will be executed for one time, we don't care about time field */
					time = 0;
				}
				JobQueue jq = JobQueue.getInstance(null);
				String[] ar = agent.split(" ");
				/* push to JobQueue the newly created job */
				jq.push(new NmapJob(0,command,periodicFlag,time,Integer.parseInt(ar[2])));
				/* and empty all fields */
				agentBox.setSelectedIndex(0);
				paramField.setText(new String());
				periodicBox.setSelectedIndex(0);
				timeField.setText(new String());
			}
			else if (action.equals("Clear")){ /* if clear button is pressed */
				/* empty all fields */
				agentBox.setSelectedIndex(0);
				paramField.setText(new String());
				periodicBox.setSelectedIndex(0);
				timeField.setText(new String());
			}
			this.menu.setVisible(true);
		}
	}
}