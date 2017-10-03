package am.parsing;


/** Contains address info for a <code>Host</code> found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class HostAddress {

	/**
	 * Address of this host.
	 */
	private String addr;
	/**
	 * Address type of this host.
	 */
	private String addrType;
	/**
	 * vendor of this host.
	 */
	private String vendor;

	/**
	 * Creates a new <code>HostAddress</code> and initialize its fields.
	 */
	public HostAddress() {
		addr = new String();
		addrType = new String();
		vendor = new String();
	}

	/**
	 * @return address of this host
	 */
	public String getAddr() {
		return addr;
	}
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/** Sets address for this host to the given value.
	 * @param addr	address of this host.
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}

	/**
	 * @return	address type of this host.
	 */
	public String getAddrType() {
		return addrType;
	}

	/** Sets address type for this host to the given value.
	 * @param addrType	address type of this host.
	 */
	public void setAddrType(String addrType) {
		this.addrType = addrType;
	}

	@Override
	public String toString(){
		String s1 = "\n\t\tAddress";
		String s2 = new String();
		if (addr.isEmpty() == false) s2 += "\n\t\t\tAddr: "+addr;
		if (addrType.isEmpty() == false) s2 += "\n\t\t\tAddrType: "+addrType;
		if (vendor.isEmpty() == false) s2 += "\n\t\t\tVendor: " + vendor;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}