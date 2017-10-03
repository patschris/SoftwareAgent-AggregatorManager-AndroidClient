package am.parsing;

/** Contains info about <code>OsClass</code> of a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class OsClass {

	/**
	 * Type for this host's <code>OsClass</code>.
	 */
	private String type;
	
	/**
	 * Vendor info for this host's <code>OsClass</code>.
	 */
	private String vendor;
	
	/**
	 * OSFamily info for this host's <code>OsClass</code>.
	 */
	private String osFamily;
	
	/**
	 * OSGen info for this host's <code>OsClass</code>.
	 */
	private String osGen;
	
	/**
	 * Accuracy for this host's <code>OsClass</code>.
	 */
	private String accuracy;

	
	/**
	 * Creates a new <code>OsClass</code> and initializes its fields.
	 */
	public OsClass() {
		type = new String();
		vendor = new String();
		osFamily = new String();
		osGen = new String();
		accuracy = new String();
	}
	
	/**
	 * @return type info for this host's <code>OsClass</code>.
	 */
	public String getType() {
		return type;
	}
	
	/** Sets type info for this host's <code>OsClass</code> to the given value.
	 * @param type	type info for this <code>OsClass</code>.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return vendor info for this host's <code>OsClass</code>.
	 */
	public String getVendor() {
		return vendor;
	}
	
	/** Sets vendor info for this host's <code>OsClass</code> to the given value.
	 * @param vendor	vendor info for this <code>OsClass</code>.
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
	/**
	 * @return OS family info for this host's <code>OsClass</code>.
	 */
	public String getOsFamily() {
		return osFamily;
	}
	
	/** Sets OS family info for this host's <code>OsClass</code> to the given value.
	 * @param osFamily	OS family info for this <code>OsClass</code>.
	 */
	public void setOsFamily(String osFamily) {
		this.osFamily = osFamily;
	}
	
	/**
	 * @return	OS generation info for this host's <code>OsClass</code>.
	 */
	public String getOsGen() {
		return osGen;
	}
	
	/** Sets OS generation info for this host's <code>OsClass</code> to the given value.
	 * @param osGen	OS generation info for this <code>OsClass</code>.
	 */
	public void setOsGen(String osGen) {
		this.osGen = osGen;
	}
	
	/**
	 * @return Accuracy of this <code>OsClass</code>.
	 */
	public String getAccuracy() {
		return accuracy;
	}
	
	/** Sets accuracy for this host's <code>OsClass</code> to the given value.
	 * @param accuracy	accuracy of this <code>OsClass</code>.
	 */
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	
	@Override
	public String toString(){
		String s1 = "\n\t\t\t\tOsclass";
		String s2 = new String();
		if (type.isEmpty() == false) s2 += "\n\t\t\t\t\tType: " + type;
		if (vendor.isEmpty() == false) s2 += "\n\t\t\t\t\tVendor: " + vendor;
		if (osFamily.isEmpty() == false) s2 += "\n\t\t\t\t\tOsfamily: " + osFamily;
		if (osGen.isEmpty() == false) s2 += "\n\t\t\t\t\tOsgen: " + osGen;
		if (accuracy.isEmpty() == false) s2 += "\n\t\t\t\t\tAccuracy: " + accuracy;
		if (s2.isEmpty()) return s2;
		else return (s1+s2) ;
	}
}