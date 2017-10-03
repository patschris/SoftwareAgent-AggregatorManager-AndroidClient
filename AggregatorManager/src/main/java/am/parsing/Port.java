package am.parsing;

/** Contains info about a host's <code>Port</code> found by an executed command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Port {
	
	/**
	 * ID of this port.
	 */
	private String portId;
	
	/**
	 * Protocol of this port.
	 */
	private String protocol;
	
	/**
	 * Service info for this port.
	 */
	private PortService service;
	
	/**
	 * State info of this port.
	 */
	private PortState state;
	
	
	/**
	 * Creates a new <code>Port</code> and initializes its fields.
	 */
	public Port() {
		portId = new String();
		protocol = new String();
		service = null;
		state = null;
	}

	/**
	 * @return	ID number of this port.
	 */
	public String getPortId() {
		return portId;
	}
	
	/** Sets ID number for this port to the given value.
	 * @param portId	ID number of this port.
	 */
	public void setPortId(String portId) {
		this.portId = portId;
	}
	
	/**
	 * @return	protocol of this port.
	 */
	public String getProtocol() {
		return protocol;
	}
	
	/** Sets protocol for this port to the given value.
	 * @param protocol	protocol of this port.
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * @return	service info for this port.
	 */
	public PortService getService() {
		return service;
	}
	
	/** Sets service info for this port to the given value.
	 * @param service	service info for this port.
	 */
	public void setService(PortService service) {
		this.service = service;
	}
	
	/**
	 * @return state info for this port.
	 */
	public PortState getState() {
		return state;
	}
	
	/** Sets state info for this port to the given value.
	 * @param state		state info for this port.
	 */
	public void setState(PortState state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		String s = new String();
		s += "\n\t\t\tPort";
		s += "\n\t\t\t\tPortId: " + portId;
		s += "\n\t\t\t\tPortProtocol: " + protocol;
		if (service != null) s += service.toString();
		if (state != null) s += state.toString();
		return s;	
	}
}