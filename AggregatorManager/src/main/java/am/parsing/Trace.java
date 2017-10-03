package am.parsing;

import java.util.ArrayList;

/** Contains trace info for a <code>Host</code> found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Trace {

	/**
	 * <code>Trace</code> port of a <code>Host</code>.
	 */
	private String port;
	
	/**
	 * <code>Trace</code> protocol of a <code>Host</code>.
	 */
	private String proto;
	
	/**
	 * A list of hops for a <code>Host</code>.
	 */
	private ArrayList <Hop> hopList;
	
	
	/**
	 * Creates a new <code>Trace</code> and initializes its fields.
	 */
	public Trace(){
		port = new String();
		proto = new String();
		hopList = new ArrayList<Hop>(0);
	}

	/**
	 * @return Trace port of this host.
	 */
	public String getPort() {
		return port;
	}
	
	/** Sets trace port for this host to the given value.
	 * @param port	trace port of this host.
	 */
	public void setPort(String port) {
		this.port = port;
	}
	
	/**
	 * @return trace protocol of this host.
	 */
	public String getProto() {
		return proto;
	}
	
	/** Sets trace protocol for this host to the given value.
	 * @param proto	trace protocol of this host.
	 */
	public void setProto(String proto) {
		this.proto = proto;
	}
	
	/** Pushes a <code>Hop</code> to the list of found hops.
	 * @param h	the <code>Hop</code> to be pushed.
	 */
	void push(Hop h){
		hopList.add(h);
	}
	
	/** Removes a <code>Hop</code> from the list of found hops.
	 * @param h	the <code>Hop</code> to be removed.
	 */
	void pop(Hop h){
		hopList.remove(h);
	}
	
	/**
	 * @return	the list of found <code>Hop</code>s by this executed command.
	 */
	public ArrayList<Hop> getHopList() {
		return hopList;
	}
	
	/** Sets the list of found <code>Hop</code>s by this command to the given value.
	 * @param hopList	the list of hops.
	 */
	public void setHopList(ArrayList<Hop> hopList) {
		this.hopList = hopList;
	}
	
	@Override
	public String toString(){
		String s1 = "\n\t\tTrace";
		String s2 = new String();
		if (port.isEmpty() == false) s2 += "\n\t\t\tPort: " + port;
		if (proto.isEmpty() == false) s2 += "\n\t\t\tProto: " + proto;
		if (hopList != null){
			for (Hop h : hopList){
				s2 += h.toString();
			}
		}
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}	
}