package am.parsing;

import java.util.ArrayList;

/** Contains lists of <code>Port</code>s and <code>ExtraPorts</code> 
 * for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Ports {
	
	/**
	 * A list of <code>ExtraPorts</code> for a host.
	 */
	private ArrayList <ExtraPorts> ePortsList;
	
	/**
	 * A list of <code>Port</code>s for a host.
	 */
	private ArrayList <Port> portList;
	
	
	/**
	 * Creates a new <code>Ports</code> and initializes its lists-fields.
	 */
	public Ports() {
		ePortsList = new ArrayList<ExtraPorts>(0);
		portList = new ArrayList<Port>(0);
	}
	
	/** Pushes an <code>ExtraPorts</code> to this host's <code>ExtraPorts</code> list.
	 * @param ep	the <code>ExtraPorts</code> to be pushed.
	 */
	public void push(ExtraPorts ep) {
		ePortsList.add(ep);
	}
	
	/** Pushes a <code>Port</code> to this host's <code>Port</code> list.
	 * @param p		the <code>Port</code> to be pushed.
	 */
	public void push(Port p) {
		portList.add(p);
	}
	
	/** Removes an <code>ExtraPorts</code> from this host's <code>ExtraPorts</code> list.
	 * @param ep	the <code>ExtraPorts</code> to be removed.
	 */
	public void pop(ExtraPorts ep) {
		ePortsList.remove(ep);
	}
	
	/** Removes a <code>Port</code> from this host's Port list.
	 * @param p		the <code>Port</code> to be removed.
	 */
	public void pop(Port p) {
		portList.remove(p);
	}
	
	/**
	 * @return the <code>ExtraPorts</code> list of this host.
	 */
	public ArrayList<ExtraPorts> getePortsList() {
		return ePortsList;
	}

	/** Sets the <code>ExtraPorts</code> list for this host to the given value.
	 * @param ePortsList	the <code>ExtraPorts</code> list of this host.
	 */
	public void setePortsList(ArrayList<ExtraPorts> ePortsList) {
		this.ePortsList = ePortsList;
	}

	/**
	 * @return the <code>Port</code> list of this host.
	 */
	public ArrayList<Port> getPortList() {
		return portList;
	}

	/** Sets the <code>Port</code> list for this host to the given value.
	 * @param portList	the Port list of this host.
	 */
	public void setPortList(ArrayList<Port> portList) {
		this.portList = portList;
	}

	@Override
	public String toString() {
		String s1 = "\n\t\tPorts";
		String s2 = new String();
		for (ExtraPorts ep : ePortsList){
			s2 +=  ep.toString();
		}
		for (Port p : portList){
			s2 += p.toString();
		}
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}