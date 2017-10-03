package am.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import am.structures.RegisteredAgents;
import am.structures.ResultQueue;

/**
 * 	@author		C. Patsouras I. Venieris
 *	@version	1
 */
public class OutputGraph extends JFrame{

	private static final long serialVersionUID = 1L;
	/**
	 * title of this frame.
	 */
	private JLabel title;
	/**
	 * Label that contains dropdown list of this frame.
	 */
	private JLabel agentDropDown;
	/**
	 * Shows the agents' list.
	 */
	@SuppressWarnings("rawtypes")
	private JComboBox agentBox;
	/**
	 * Model of dropdown list.
	 */
	@SuppressWarnings("rawtypes")
	private DefaultComboBoxModel agentModel;
	/**
	 * Label that contains field to fill time.
	 */
	private JLabel timLabel;
	/**
	 * Field to fill time in seconds.
	 */
	private JTextField timeField;
	/**
	 * Label that contains text area to show results.
	 */
	private JLabel areaLabel;
	/**
	 * Displays the results requested.
	 */
	private JTextArea textArea;
	/**
	 * scroll panel for this frame.
	 */
	private JScrollPane scrollPane;
	/**
	 * button used for clearing the text area.
	 */
	private JButton clearButton;
	/**
	 * button used for submitting request for results.
	 */
	private JButton submitButton;
	/**
	 * Constructs an <code>OutputGraph</code>.
	 */
	public OutputGraph(){
		super("Output panel");
		this.setSize(700,550); /* setting size for this frame */
		this.setLayout(null); /* no default layout is used */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); /* dispose the frame on close */
		this.setResizable(false); /* frame is not resizable */
		this.titleLabel(); /* adds title to the frame*/
		this.agentLabel(); /* adds dropdown menu with SAs to the frame */
		this.timeLabel(); /* adds time field to fill time */
		this.textAreaLabel(); /* adds text area to show results */
		this.addButtons(); /* adds button to the frame */
		this.setVisible(true); /* makes the frame visible */
	}
	/**
	 * Adds title to this <code>OutputGraph</code> frame.
	 */
	private void titleLabel(){
		this.title = new JLabel("You can see output from an SA or from every SA");
		this.title.setSize(500, 50);
		this.title.setLocation(120, 10);
		this.title.setFont(new Font("Serif", Font.BOLD, 15));
		this.getContentPane().add(this.title);
	}
	/**
	 * @return a model list for viewing connected agents.
	 */
	@SuppressWarnings("rawtypes")
	public DefaultComboBoxModel getAgentModel() {
		return agentModel;
	}
	/**
	 * Adds dropdown menu with SAs to the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void agentLabel(){
		this.agentDropDown = new JLabel();
		this.agentDropDown.setLayout(new BorderLayout());
		this.agentDropDown.setSize(500, 25);
		this.agentDropDown.setLocation(50, 70);
		this.agentDropDown.add( new JLabel( "Select SA to see output:" ), BorderLayout.WEST );		
		this.agentModel = new DefaultComboBoxModel();
		this.agentBox = new JComboBox(this.agentModel);
		RegisteredAgents ra = RegisteredAgents.getInstance(null, null, this);
		this.agentModel.addElement("All");
		String[] ar = ra.getRegs();
		if (ar != null){
			for (int i = 0 ; i < ar.length ; i++) { 
				/* add every registered agent to this list*/
				this.agentModel.addElement(ar[i]);
			}
		}
		this.agentBox.setSelectedIndex(0); /* "All" is selected by default */
		this.agentBox.setMaximumRowCount(4);
		this.agentBox.setEditable(false);
		this.agentDropDown.add(this.agentBox, BorderLayout.EAST);		
		this.getContentPane().add(this.agentDropDown);
	}
	/**
	 * Adds time field to this frame. 
	 */
	private void timeLabel(){
		this.timLabel = new JLabel();
		this.timLabel.setLayout(new BorderLayout());
		this.timLabel.setSize(500,25);
		this.timLabel.setLocation(50, 110);
		this.timLabel.add(new JLabel("Output from last (seconds):"), BorderLayout.WEST);
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
		this.timeField.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Type 0 to see all results!");
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		this.timLabel.add(this.timeField,BorderLayout.EAST);
		this.getContentPane().add(this.timLabel);
	}
	/**
	 * Adds text area to this label in order to show requested results.
	 */
	private void textAreaLabel(){
		this.areaLabel = new JLabel();
		this.areaLabel.setLayout(new BorderLayout());
		this.areaLabel.setSize(550, 300);
		this.areaLabel.setLocation(50, 150);
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.scrollPane = new JScrollPane(this.textArea);
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.areaLabel.add(this.scrollPane);
		this.getContentPane().add(this.areaLabel);
	}
	/**
	 * Add button to this frame and listener for them.
	 */
	private void addButtons(){
		this.clearButton= new JButton();
		this.clearButton.setText("Clear");
		this.clearButton.setSize(100, 30);
		this.clearButton.setLocation(200, 460);
		this.clearButton.setActionCommand("Clear");
		this.clearButton.addActionListener(new OutputGraphListener(this));
		this.submitButton= new JButton();
		this.submitButton.setText("Submit");
		this.submitButton.setSize(100, 30);
		this.submitButton.setLocation(350, 460);
		this.submitButton.setActionCommand("Submit");
		this.submitButton.addActionListener(new OutputGraphListener(this));
		this.getContentPane().add(this.clearButton);
		this.getContentPane().add(this.submitButton);
	}
	/**
	 *  Listener for <code>OutputGraph</code> buttons.
	 * 	@author		Omada13
	 *	@version 	1
	 */
	public class OutputGraphListener implements ActionListener {
		/**
		 * the <code>OutputGraph</code> frame.
		 */
		private OutputGraph menu;
		/**
		 * Constructor of <code>OutputGraphListener</code>
		 * Sets the <code>OutputGraph</code> to the given value.
		 * @param m the <code>OutputGraph</code> frame.
		 */
		public OutputGraphListener(OutputGraph m){
			this.menu=m;
		}
		
		@Override
		public void actionPerformed(ActionEvent ae){
			String action = ae.getActionCommand();
			this.menu.setVisible(false);
			if (action.equals("Submit")) { /* if submit button is pressed */
				String timeString = timeField.getText();
				/* Check if user fills time field*/
				if (timeString.isEmpty()) {
					JOptionPane.showMessageDialog(this.menu,"Time is missing");
					this.menu.setVisible(true);
					return;
				}
				/* Check which agent is picked */
				String agent = (String) agentBox.getSelectedItem();
				String res = new String();
				if (agent.equals("All")) agent= "every agent";
				if (timeString.equals("0")) res = "All results from " + agent + "\n";
				else res = "Results from " + agent + " for last " + timeString + " seconds\n";
				ResultQueue resq = ResultQueue.getInstance();
				/* take requested results */
				if (agent.equals("every agent")){
					res += resq.getAllResults(Integer.parseInt(timeString));
				}
				else{
					String[] ar = agent.split(" ");
					int hashkey = Integer.parseInt(ar[2]);
					res += resq.getResultsByHash(hashkey,Integer.parseInt(timeString));
				}	
				/* show results into text area */
				textArea.setText(new String());
				textArea.setText(res);
				/* and empty rest fields */
				agentBox.setSelectedIndex(0);
				timeField.setText(new String());
			}
			else if (action.equals("Clear")) { /* if clear button is pressed*/
				/* empty every field */
				agentBox.setSelectedIndex(0);
				timeField.setText(new String());
				textArea.setText(new String());
			}
			this.menu.setVisible(true);
		}
	}
}