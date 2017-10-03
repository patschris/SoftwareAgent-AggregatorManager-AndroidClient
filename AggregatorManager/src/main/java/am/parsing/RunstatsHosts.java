package am.parsing;

/** Contains <code>RunstatsHosts</code> info for an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class RunstatsHosts {
	
	/**
	 * hosts marked "up" by this command.
	 */
	private String up;
	
	/**
	 * hosts marked "down" by this command.
	 */
	private String down;
	
	/**
	 * total hosts scanned by this command.
	 */
	private String total;
	
	
	/**
	 * Creates a new <code>RunstatsHosts</code> and initializes its fields.
	 */
	public RunstatsHosts() {
		up = null;
		down = null;
		total = null;
	}

	/**
	 * @return hosts marked "up" by this command.
	 */
	public String getUp() {
		return up;
	}

	/** Sets hosts marked "up" by this command to the given value.
	 * @param up	hosts marked "up" by this command.
	 */
	public void setUp(String up) {
		this.up = up;
	}

	/**
	 * @return hosts marked "down" by this command.
	 */
	public String getDown() {
		return down;
	}

	/** Sets hosts marked "down" by this command to the given value.
	 * @param down	hosts marked "down" by this command.
	 */
	public void setDown(String down) {
		this.down = down;
	}

	/**
	 * @return total hosts scanned by this command.
	 */
	public String getTotal() {
		return total;
	}

	/** Sets total hosts scanned by this command to the given value.
	 * @param total	total hosts scanned by this command.
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return ("\n\t\tHosts\n\t\t\tUp: " + up + "\n\t\t\tDown: " + down + "\n\t\t\tTotal: " + total);
	}
}
