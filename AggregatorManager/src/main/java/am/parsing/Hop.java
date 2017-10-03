package am.parsing;


/** Contains info about a hop found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Hop {
	
	/**
	 * TTL of this hop.
	 */
	private String ttl;
	
	/**
	 * IP address of this hop.
	 */
	private String ipaddr;
	
	/**
	 * RTT for this hop.
	 */
	private String rtt;
	
	/**
	 * Hostname for this hop.
	 */
	private String host;
	
	/**
	 * Creates a new <code>Hop</code> and initialize its fields.
	 */
	public Hop() {
		ttl = new String();
		ipaddr = new String();
		rtt = new String();
		host = new String();
	}
	
	/**
	 * @return	hostname for this hop.
	 */
	public String getHost() {
		return host;
	}
	
	/** Sets hostname for this hop to the given value.
	 * @param host	hostname of this hop.
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @return 		TTL for this hop
	 */
	public String getTtl() {
		return ttl;
	}
	
	/** Sets TTL for this hop the given value.
	 * @param ttl	TTL of this hop.
	 */
	public void setTtl(String ttl) {
		this.ttl = ttl;
	}
	
	/**
	 * @return		IP address for this hop.
	 */
	public String getIpaddr() {
		return ipaddr;
	}
	
	/** Sets the IP address for this hop to the given value.
	 * @param ipaddr	IP address of this hop.
	 */
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	
	/**
	 * @return		RTT for this hop.
	 */
	public String getRtt() {
		return rtt;
	}
	
	/** Sets RTT for this hop to the given value.
	 * @param rtt	RTT for this hop.
	 */
	public void setRtt(String rtt) {
		this.rtt = rtt;
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\t\tHop";
		String s2 = new String();
		if (ttl.isEmpty() == false ) s2 += "\n\t\t\t\tTtl: " + ttl;
		if (ipaddr.isEmpty() == false ) s2 += "\n\t\t\t\tIpaddr: " + ipaddr;
		if (rtt.isEmpty() == false ) s2 += "\n\t\t\t\tRtt: " + rtt;
		if (host.isEmpty() == false) s2 += "\n\t\t\t\tHost: " + host;
		if (s2.isEmpty()) return s2;
		else return  (s1+s2);
	}
}