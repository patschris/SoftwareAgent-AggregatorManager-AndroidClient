package am.parsing;

/** Contains PortUsed info for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class PortUsed {
	
	/**
	 * State of this <code>PortUsed</code>.
	 */
	private String state;
	
	/**
	 * Protocol info of this <code>PortUsed</code>.
	 */
	private String proto;
	
	/**
	 * ID number of this <code>PortUsed</code>.
	 */
	private String portID;
	
	
	/**
	 * Creates a new <code>PortUsed</code> and initializes its fields.
	 */
	public PortUsed() {
		state = new String();
		proto = new String();
		portID = new String();
	}
	
	/**
	 * @return state of this <code>PortUsed</code>.
	 */
	public String getState() {
		return state;
	}

	/** Sets state for this <code>PortUsed</code> to the given value.
	 * @param state	of this <code>PortUsed</code>.
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * @return protocol of this <code>PortUsed</code>.
	 */
	public String getProto() {
		return proto;
	}
	
	/** Sets protocol for this <code>PortUsed</code> to the given value.
	 * @param proto	protocol of this <code>PortUsed</code>.
	 */
	public void setProto(String proto) {
		this.proto = proto;
	}
	
	/**
	 * @return ID number of this <code>PortUsed</code>.
	 */
	public String getPortID() {
		return portID;
	}
	
	/** Sets ID number for this <code>PortUsed</code> to the given value.
	 * @param portID	ID number of this <code>PortUsed</code>.
	 */
	public void setPortID(String portID) {
		this.portID = portID;
	}
	
	@Override
	public String toString(){
		String s1 = "\n\t\t\tPortused";
		String s2 = new String();
		if (state.isEmpty() == false) s2 += "\n\t\t\t\tState: " + state;
		if (proto.isEmpty() == false) s2 += "\n\t\t\t\tProto: " + proto;
		if (portID.isEmpty() == false) s2 += "\n\t\t\t\tPortid: " + portID;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}