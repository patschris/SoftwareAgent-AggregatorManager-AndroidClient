package am.parsing;

/** Contains scanning info of an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class ScanInfo {
	
	/**
	 * <code>ScanInfo</code> type for this command.
	 */
	private String type;
	
	/**
	 * <code>ScanInfo</code> protocol for this command.
	 */
	private String protocol;
	
	/**
	 * Number of services for this command.
	 */
	private String numservices;
	
	/**
	 * Services info for this command.
	 */
	private String services;
	
	/**
	 *  Creates a new <code>ScanInfo</code> and initializes its fields.
	 */
	public ScanInfo() {
		type = new String();
		protocol = new String();
		numservices = new String();
		services = new String();
	}

	/**
	 * @return <code>ScanInfo</code> type of this executed command.
	 */
	public String getType() {
		return type;
	}
	
	/** Sets <code>ScanInfo</code> type for this executed command to the given value.
	 * @param type	<code>ScanInfo</code> type of this command.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return <code>ScanInfo</code> protocol of this executed command.
	 */
	public String getProtocol() {
		return protocol;
	}
	
	/** Sets <code>ScanInfo</code> protocol for this executed command to the given value.
	 * @param protocol	<code>ScanInfo</code> protocol of this command.
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * @return number of services of this command.
	 */
	public String getNumservices() {
		return numservices;
	}
	
	/** Sets number of services for this executed command to the given value.
	 * @param numservices	number of services of this command.
	 */
	public void setNumservices(String numservices) {
		this.numservices = numservices;
	}
	
	/**
	 * @return services info for this executed command.
	 */
	public String getServices() {
		return services;
	}
	
	/** Sets services info for this executed command to the given value.
	 * @param services	services info for this command.
	 */
	public void setServices(String services) {
		this.services = services;
	}
	
	@Override
	public String toString() {
		return ("\n\tScaninfo\n\t\tType: " + type + "\n\t\tProtocol: " + protocol +
				"\n\t\tNumservices: " + numservices + "\n\t\tServices: " + services);
	}
}