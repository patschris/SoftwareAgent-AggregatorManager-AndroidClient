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

/**
 * @author	C. Patsouras I. Venieris
 * @version	1
 */
public class DeletePeriodicJobGraph extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 *  a model list for viewing periodic jobs. 
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
	 * button used for deleting a Periodic Job.
	 */
	private JButton deleteButton;

	/**
	 * @return the list model of this frame.
	 */
	@SuppressWarnings("rawtypes")
	public DefaultListModel getModel(){
		return this.model;
	}

	/**
	 * Constructs an <code>DeletePeriodicJobGraph</code>.
	 */
	public DeletePeriodicJobGraph(){
		super("Delete periodic commands");
		this.setSize(700, 550); /* setting size for this frame */
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); /* dispose the frame on close */
		this.setLayout(null); /* no default layout is used */
		this.setResizable(false); /* frame is not resizable */
		this.addTitle(); /* adds title to the frame*/
		this.addList(); /* adds the list to the frame */
		this.addButtons(); /* .. and the buttons */
		this.setVisible(true); /* makes the frame visible */
	}
	/**
	 * Adds title to this <code>DeletePeriodicJobGraph</code> frame.
	 */
	private void addTitle(){
		this.title = new JLabel("Delete a periodic command");
		this.title.setLocation(220, 30);
		this.title.setSize(500, 20);
		this.title.setFont(new Font("Serif", Font.BOLD, 15));
		this.getContentPane().add(this.title);
	}
	/**
	 * Adds a list to this <code>DeletePeriodicJobGraph</code> frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addList(){
		this.model = new DefaultListModel();
		JobQueue jq = JobQueue.getInstance(this);
		String[] ar = jq.getPeriodicJobs();
		if (ar != null){
			for (int i = 0; i < ar.length ; i++) {
				/* adds every periodic job to the list model */
				this.model.addElement(ar[i]);
			}
		}
		this.list = new JList(this.model);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); /* choosing one Job at a time to stop */
		/*
		 adding the list in a scroll panel and scrollbars for scroll panel
		 */		 
		this.scrollPane = new JScrollPane(this.list);
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setBounds(70, 80, 500, 350);
		this.getContentPane().add(this.scrollPane);
	}

	/**
	 * Add delete button to <code>DeletePeriodicJobGraph</code> and a listener for the button.
	 */
	private void addButtons(){
		this.deleteButton= new JButton();
		this.deleteButton.setText("Delete");
		this.deleteButton.setSize(100, 30);
		this.deleteButton.setLocation(300, 450);
		this.deleteButton.setActionCommand("Delete");
		this.deleteButton.addActionListener(new DeletePeriodicMenuListener(this));
		this.getContentPane().add(this.deleteButton);
	}

	/**
	 *  Listener for <code>DeletePeriodicJobGraph</code> buttons.
	 * 	@author		Omada13
	 *	@version 	1
	 */
	public class DeletePeriodicMenuListener implements ActionListener {

		/**
		 *  the <code>DeletePeriodicJobGraph</code> frame.
		 */
		DeletePeriodicJobGraph win;

		/**
		 * Sets the <code>DeletePeriodicJobGraph</code> to the given value.
		 * @param a the <code>DeletePeriodicJobGraph</code> frame.
		 */
		public DeletePeriodicMenuListener(DeletePeriodicJobGraph a) {
			this.win=a;
		}
		
		@Override
		public void actionPerformed(ActionEvent ae){
			this.win.setVisible(false);	
			String choise = (String) list.getSelectedValue();
			if (choise==null) { /* if no option where selected */
				JOptionPane.showMessageDialog(this.win,"Select an option\nThen press the button");
			}
			else {
				String s = "A periodic job deleted:\n" + list.getSelectedValue();  
				JOptionPane.showMessageDialog(this.win,s);
				String[] ar = choise.split(" ");
				JobQueue jq = JobQueue.getInstance(this.win);
				int pId = Integer.parseInt(ar[0]);
				jq.deleteJobById(pId);
				jq.push(new NmapJob(pId, "Stop",true,0,Integer.parseInt(ar[ar.length - 1])));
			}
			this.win.setVisible(true);
		}
	}
}