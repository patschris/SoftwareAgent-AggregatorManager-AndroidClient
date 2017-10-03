package am.parsing;

import java.util.ArrayList;

/** Contains a list of <code>Host</code>s found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Hosts {

	private ArrayList <Host> hostList;

	/**
	 * Creates a new <code>Hosts</code> and  initialize its field-list.
	 */
	public Hosts() {
		hostList = new ArrayList<Host>(0);
	}

	/** Pushes a <code>Host</code> to the list of hosts.
	 * @param h	the <code>Host</code> to be pushed.
	 */
	public void push(Host h) {
		hostList.add(h);
	}

	/** Removes a <code>Host</code> from the lists of hosts.
	 * @param h	the Host to be removed.
	 */
	public void pop(Host h) {
		hostList.remove(h);
	}

	/**
	 * @return the list of <code>Host</code>s found by this nmap command.
	 */
	public ArrayList<Host> getHostList() {
		return hostList;
	}

	/** Sets the list of <code>Host</code>s found by this nmap command.
	 * @param hostList	The list of <code>Host</code>s.
	 */
	public void setHostList(ArrayList<Host> hostList) {
		this.hostList = hostList;
	}

	@Override
	public String toString() {
		String s = new String();
		for (Host h : hostList){
			s += h.toString();
		}
		return s;
	}	
}