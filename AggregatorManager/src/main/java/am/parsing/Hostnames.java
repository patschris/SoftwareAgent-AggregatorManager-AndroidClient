package am.parsing;

import java.util.ArrayList;

/** Contains a list of hostnames for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Hostnames {

	/**
	 * A list consisted of hostnames for this host.
	 */
	private ArrayList <HostName> HostNameList;

	/**
	 * Creates a new <code>Hostnames</code> and initialize its list field.
	 */
	public Hostnames() {
		HostNameList = new ArrayList<HostName>(0);
	}
	
	/** Pushes a <code>Hostname</code> to this host's <code>Hostname</code> list.
	 * @param hostName	the <code>Hostname</code> to be pushed.
	 */
	public void push(HostName hostName) {
		HostNameList.add(hostName);
	}
	
	/** Removes a <code>Hostname</code> from this host's <code>Hostname</code> list.
	 * @param hostName	the <code>Hostname</code> to be removed.
	 */
	public void pop(HostName hostName){
		HostNameList.remove(hostName);
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\tHostnames\n";
		String s2 = new String();
		for (HostName h : HostNameList){
			s2 += h.toString();
		}
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}