package am.parsing;


/** Contains uptime info for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class HostUptime {
	
	/**
	 * Seconds this host is up.
	 */
	private String seconds;
	
	/**
	 * Last boot info of this host.
	 */
	private String lastboot;
	
	/**
	 * Creates a new <code>HostUptime</code> and initializes its fields.
	 */
	public HostUptime() {
		seconds=new String();
		lastboot = new String();
	}
	
	/**
	 * @return seconds 	seconds this host is up. 
	 */
	public String getSeconds() {
		return seconds;
	}
	
	/** Sets seconds this host is up to the given value.
	 * @param seconds	seconds this host is up. 
	 */
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	
	/**
	 * @return 	Last boot info of this host.
	 */
	public String getLastboot() {
		return lastboot;
	}
	
	/** Sets last boot info for this host to the given value.
	 * @param lastboot	Last boot info of this host.
	 */
	public void setLastboot(String lastboot) {
		this.lastboot = lastboot;
	}
	
	@Override
	public String toString(){
		String s1 = "\n\t\tUptime";
		String s2 = new String();
		if (seconds.isEmpty() == false) s2 += "\n\t\t\tSeconds: " + seconds;
		if (lastboot.isEmpty() == false) s2 += "\n\t\t\tLastboot: " + lastboot;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}