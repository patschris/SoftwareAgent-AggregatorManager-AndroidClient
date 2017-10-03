package am.structures;

import java.util.LinkedList;
import java.util.Queue;

import javax.swing.DefaultListModel;

import am.classes.AndroidLogin;
import am.gui.AndroidMenu;

/** 
 *  <code>AndroidQueue</code> is a singleton class consisted of a queue that contains 
 * 	Android clients that wait for admin's approval or rejection.
 * 	
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class AndroidQueue {
	
	/**
	 * 	A queue containing android clients that wait for admin's approval/rejection.
	 */
	private static Queue <AndroidLogin> androidQueue = new LinkedList<AndroidLogin>();
	/**
	 * 	The instance of this class.
	 */	
	private static AndroidQueue instance = null ;

	/**
	 * AcceptDenyMenu used to update GUI when needed.
	 */
	private static AndroidMenu menu = null;
	
	
	/** Constructs an <code>AndroidQueue</code> if not created yet or returns the instance of it.
	 * 	Updates <code>AndroidMenu</code> to the given value if not null.
	 
	 * 	@param	m	<code>AcceptDenyMenu</code> needed to update GUI.
	 * 	@return		The instance of this class.
	 */
	public static AndroidQueue getInstance(AndroidMenu m){
		synchronized (androidQueue){
			if (instance == null){
				instance = new AndroidQueue();
			}
			if (m != null) menu = m;
			return instance;
		}
	}
	
	/** Pushes an <code>AndroidRegistration</code>/android client to the queue of those who wait for AM's approval/rejection.
	 * 	Updates the GUI adding the Android Registration to <code>AndroidMenu</code> list model if needed.
	 * 	@param	reg	the <code>AndroidRegistration</code> to be pushed.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void push(AndroidLogin reg) {
		synchronized (androidQueue){
			androidQueue.add(reg);
			if (menu != null){
				DefaultListModel dlm = menu.getModel();
				dlm.addElement(reg.getCredentials());
			}
		}
	}
	
	/** Removes an <code>AndroidRegistration</code>/android client from the queue of those who wait for AM's approval/rejection.
	 * 	Updates the GUI removing the Android Registration from <code>AndroidMenu</code> list model if needed.
	 * 
	 * 	@param r	the Android Registration to be removed.
	 */
	@SuppressWarnings("rawtypes")
	public void pop(AndroidLogin r){
		synchronized (androidQueue){
			androidQueue.remove(r);
			if (menu != null){
				DefaultListModel dlm = menu.getModel();
				dlm.removeElement(r.getCredentials());
			}
		}
	}
	
	/**
	 * 	@return a String array containing the Device Name of every Android Client that waits for
	 * 			AM's approval/rejection, null if there is no one.		 
	 */
	public String[] toShow() {
		synchronized(androidQueue) {
			String s = new String();
			for (AndroidLogin r : androidQueue){
				s += r.getCredentials() + "\n";
			}		
			if (s.split("\n")[0].equals("")) return null;
			else return s.split("\n");
		}
	}
	
	/** Searches in the queue of those Agents/clients who wait for AM's approval/rejection for
	 * 	an <code>AndroidLogin</code>/client with specific credentials.
	 * 	@param	str	credentials of <code>AndroidLogin</code>/client the search is for.
	 * 	@return	an <code>AndroidLogin</code>/client with the given credentials if there is one in the
	 * 			queue of android clients that wait for AM's approval/rejection, null if there's not.
	 */
	public AndroidLogin search(String str) {
		synchronized (androidQueue) {
			for (AndroidLogin r : androidQueue) {
				if (r.getCredentials().equals(str)) {
					return r;
				}
			}
			return null;
		}
	}
}