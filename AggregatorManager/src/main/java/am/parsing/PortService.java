package am.parsing;

/** Contains service info for a host's port found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class PortService {
	
	/**
	 * Name of this <code>PortService</code>.
	 */
	private String name;
	
	/**
	 * Product info for this <code>PortService</code>.
	 */
	private String product;
	
	/**
	 * Version info for this <code>PortService</code>.
	 */
	private String version;
	
	/**
	 * Extra info for this <code>PortService</code>.
	 */
	private String extraInfo;
	
	/**
	 * Method info for this <code>PortService</code>.
	 */
	private String method;
	
	/**
	 * Conf of this <code>PortService</code>.
	 */
	private String conf;
	
	
	/**
	 * Creates a new <code>PortService</code> and initializes its fields.
	 */
	public PortService() {
		name = new String();
		product = new String();
		version = new String();
		extraInfo = new String();
		method = new String();
		conf = new String();
	}
	
	/**
	 * @return name of this <code>PortService</code>.
	 */
	public String getName() {
		return name;
	}
	
	/** Sets name for this <code>PortService</code> to the given value.
	 * @param name	name of this <code>PortService</code>.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return extra info for this <code>PortService</code>.
	 */
	public String getExtraInfo() {
		return extraInfo;
	}
	
	/** Sets extra info for this <code>PortService</code> to the given value.
	 * @param extraInfo	extra info for this <code>PortService</code>.
	 */
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	
	
	/**
	 * @return method info for this <code>PortService</code>.
	 */
	public String getMethod() {
		return method;
	}
	
	/** Sets method info for this <code>PortService</code> to the given value.
	 * @param method	method info for this <code>PortService</code>.
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * @return conf of this <code>PortService</code>
	 */
	public String getConf() {
		return conf;
	}
	
	/** Sets conf for this <code>PortService</code> to the given value.
	 * @param conf	conf of this <code>PortService</code>.
	 */
	public void setConf(String conf) {
		this.conf = conf;
	}
	
	/**
	 * @return Product info for this <code>PortService</code>.
	 */
	public String getProduct() {
		return product;
	}
	
	/** Sets product info for this <code>PortService</code> to the given value.
	 * @param product	product info for this <code>PortService</code>.
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	
	/**
	 * @return version of this <code>PortService</code>.
	 */
	public String getVersion() {
		return version;
	}
	
	/** Sets version for this <code>PortService</code> to the given value.
	 * @param version	version of this <code>PortService</code>.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\t\t\tService";
		String s2 = new String();
		if (name.isEmpty() == false) s2 += "\n\t\t\t\t\tName: " + name;
		if (method.isEmpty() == false) s2 += "\n\t\t\t\t\tMethod: " + method;
		if (conf.isEmpty() == false) s2 += "\n\t\t\t\t\tConf: " + conf;
		if (extraInfo.isEmpty() == false) s2 += "\n\t\t\t\t\tExtrainfo: " + extraInfo;
		if (product.isEmpty() == false) s2 += "\n\t\t\t\t\tProduct: " + product;
		if (version.isEmpty() == false) s2 += "\n\t\t\t\t\tVersion: " + version +"\n";
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}	
}
