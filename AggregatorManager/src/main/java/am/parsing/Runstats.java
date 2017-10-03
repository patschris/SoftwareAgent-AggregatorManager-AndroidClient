package am.parsing;

/** Contains <code>Runstats</code> for an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Runstats {
	
	/**
	 * hosts info. 
	 */
	private RunstatsHosts rhosts;

	/**
	 * Creates a new RunStats and initializes its fields.
	 */
	public Runstats() {
		rhosts = null;
	}

	/**
	 * @return runstats hosts info for this command.
	 */
	public RunstatsHosts getRhosts() {
		return rhosts;
	}

	/** Sets runstats hosts info for an executed command to the given value.
	 * @param rhosts	runstats hosts info for this command.
	 */
	public void setRhosts(RunstatsHosts rhosts) {
		this.rhosts = rhosts;
	}

	@Override
	public String toString() {
		return "\n\tRunstats" + rhosts.toString();
	}	
}