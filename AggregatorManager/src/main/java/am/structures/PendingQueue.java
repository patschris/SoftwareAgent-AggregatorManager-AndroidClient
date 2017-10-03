package am.structures;

import java.util.LinkedList;
import java.util.Queue;

import javax.swing.DefaultListModel;

import am.classes.Registration;
import am.gui.AcceptDenyMenu;


/** <code>PendingQueue</code> is a singleton class consisted of a queue that contains <code>Registration</code>s
 * 	(SoftwareAgents/clients) that wait for admin's approval or rejection.
 * 	
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class PendingQueue {

	/**
	 * 	A queue containing agents that wait for admin's approval/rejection.
	 */
	private static Queue <Registration> agentQueue = new LinkedList<Registration>();
	
	/**
	 * 	The instance of this class.
	 */
	private static PendingQueue instance = null ;
	
	/**
	 * AcceptDenyMenu used to update GUI when needed.
	 */
	private static AcceptDenyMenu menu = null;


	/** Constructs a <code>PendingQueue</code> if not created yet or returns the instance of it.
	 * 	Updates <code>AcceptDenyMenu</code> to the given value if not null.
	 
	 * 	@param	m	<code>AcceptDenyMenu</code> needed to update GUI.
	 * 	@return		The instance of this class.
	 */
	public static PendingQueue getInstance(AcceptDenyMenu m){
		synchronized (agentQueue){
			if (instance == null){
				instance = new PendingQueue();
			}
			if (m != null) menu = m;
			return instance;
		}
	}

	/** Pushes a <code>Registration</code>/client to the queue of those who wait for AM's approval/rejection.
	 * 	Updates the GUI adding the Registration to <code>AcceptDenyMenu</code> list model if needed.
	 * 	@param	reg	the <code>Registration</code> to be pushed.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void push(Registration reg) {
		synchronized (agentQueue){
			agentQueue.add(reg);
			if (menu != null){
				DefaultListModel dlm = menu.getModel();
				dlm.addElement(reg.getname());
			}
		}
	}

	/** Removes a <code>Registration</code>/client from the queue of those who wait for AM's approval/rejection.
	 * 	Updates the GUI removing the Registration from <code>AcceptDenyMenu</code> list model if needed.
	 * 
	 * 	@param r	the Registration to be removed.
	 */
	@SuppressWarnings("rawtypes")
	public void pop(Registration r){
		synchronized (agentQueue){
			agentQueue.remove(r);
			if (menu != null){
				DefaultListModel dlm = menu.getModel();
				dlm.removeElement(r.getname());
			}
		}
	}

	/** Searches in the queue of those Agents/clients who wait for AM's approval/rejection for
	 * 	an agent/client with a specific Hashkey.
	 * 	@param	h	hashkey of the agent/client the search is for.
	 * 	@return	a <code>Registration</code>/agent with the given hashkey if there is one in the
	 * 			queue of agents/clients that wait for AM's approval/rejection, null if there's not.
	 */
	public Registration search(int h) {
		synchronized (agentQueue) {
			for (Registration r : agentQueue) {
				if (r.getHashKey() == h) {
					return r;
				}
			}
			return null;
		}
	}

	/**
	 * 	@return a String array containing the hashkey of every Registration/Agent that waits for
	 * 			AM's approval/rejection, null if there is no one.		 
	 */
	public String[] toShow() {
		synchronized(agentQueue) {
			String s = new String();
			for (Registration r : agentQueue){
				s += "Software Agent " + r.getHashKey() + "\n";
			}		
			if (s.split("\n")[0].equals("")) return null;
			else return s.split("\n");
		}
	}
}
