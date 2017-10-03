package am.parsing;

import java.util.ArrayList;

/** Contains OS info for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Os {

	/**
	 * List of <code>PortUsed</code> info for a host.
	 */
	private ArrayList <PortUsed> portUsedList;
	/**
	 * List of <code>OsMatch</code>es for a host.
	 */
	private ArrayList <OsMatch> osMatchList;
	/**
	 * List of <code>OsClass</code>es for a host.
	 */
	private ArrayList <OsClass> osClassList;

	/**
	 * Creates a new <code>Os</code> and initiliazes its lists-fields.
	 */
	public Os() {
		portUsedList = new ArrayList<PortUsed>(0);
		osMatchList = new ArrayList<OsMatch>(0);
		osClassList = new ArrayList<OsClass>(0);
	}

	/** Pushes a <code>PortUsed</code> to the list of 
	 * <code>PortUsed</code> of this host.
	 * @param pu	the <code>PortUsed</code> to be pushed.
	 */
	public void push(PortUsed pu) {
		portUsedList.add(pu);
	}
	
	/** Pushes an <code>OSMatch</code> to the list of 
	 * <code>OsMatch</code>es of this host.
	 * @param om	the <code>OsMatch</code> to be pushed.
	 */
	public void push(OsMatch om) {
		osMatchList.add(om);
	}
	
	/** Pushes an <code>OsClass</code> to the list of
	 * <code>OsClass</code>es of this host.
	 * @param oc	the <code>OsClass</code> to be pushed.
	 */
	public void push(OsClass oc) {
		osClassList.add(oc);
	}
	
	/** Removes a <code>PortUsed</code> from the list of
	 * the <code>PortUsed</code> of this host.
	 * @param pu	the <code>PortUsed</code> to be pushed.
	 */
	public void pop(PortUsed pu) {
		portUsedList.remove(pu);
	}
	
	/** Removes an <code>OsMatch</code> from the list of
	 * <code>OsMatch</code>es of this host. 
	 * @param om	the <code>OsMatch</code> to be removed.
	 */
	public void pop(OsMatch om) {
		osMatchList.remove(om);
	}
	
	/** Removes an <code>OsClass</code> from the list of
	 * <code>OsClass</code>es of this host.
	 * @param oc	the <code>OsClass</code> to be removed.
	 */
	public void pop(OsClass oc) {
		osClassList.remove(oc);
	}
	
	/** 
	 * @return	the <code>PortUsed</code> of this host.
	 */
	public ArrayList<PortUsed> getPortUsedList() {
		return portUsedList;
	}
	
	/** Sets the <code>PortUsed</code> list for this host to the given value.
	 * @param portUsedList	the <code>PortUsed</code> list of this host. 
	 */
	public void setPortUsedList(ArrayList<PortUsed> portUsedList) {
		this.portUsedList = portUsedList;
	}
	
	/**
	 * @return the <code>OsMatch</code> list of this host. 
	 */
	public ArrayList<OsMatch> getOsMatchList() {
		return osMatchList;
	}
	
	/** Sets the <code>OsMatch</code> list for this host to the given value.
	 * @param osMatchList	the <code>OsMatch</code> list of this host.
	 */
	public void setOsMatchList(ArrayList<OsMatch> osMatchList) {
		this.osMatchList = osMatchList;
	}
	
	/**
	 * @return the <code>OsClass</code> list of this host.
	 */
	public ArrayList<OsClass> getOsClassList() {
		return osClassList;
	}
	
	/** Sets the <code>OsClass</code> list for this host to the given value.
	 * @param osClassList	the <code>OsClass</code> list of this host.
	 */
	public void setOsClassList(ArrayList<OsClass> osClassList) {
		this.osClassList = osClassList;
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\tOs";
		String s2 = new String();
		for (PortUsed pul : portUsedList){
			s2 += pul.toString();
		}
		for (OsClass ocl : osClassList){
			s2 += ocl.toString();
		}
		for (OsMatch oml : osMatchList){
			s2 += oml.toString();
		}
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}