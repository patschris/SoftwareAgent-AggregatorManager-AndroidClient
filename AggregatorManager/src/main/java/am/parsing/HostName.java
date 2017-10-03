package am.parsing;


/** Contains hostname info for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class HostName {

	/**
	 * Name of this hostname.
	 */
	private String name;
	
	/**
	 * Type of this hostname.
	 */
	private String type;
	
	/**
	 * Creates a new <code>HostName</code> and initialize its fields.
	 */
	public HostName() {
		name = new String();
		type = new String();
	}
	
	/**
	 * @return name of this hostname.
	 */
	public String getName() {
		return name;
	}
	
	/** Sets name for this hostname to the given value.
	 * @param name	name of this hostname.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return type of this hostname.
	 */
	public String getType() {
		return type;
	}
	
	/** Sets type for this hostname to the given value.
	 * @param type	type of this hostname.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\t\tHostname";
		String s2 = new String();
		if (name.isEmpty() == false) s2 += "\n\t\t\t\tName: " + name;
		if (type.isEmpty() == false) s2 += "\n\t\t\t\tType: " + type;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}