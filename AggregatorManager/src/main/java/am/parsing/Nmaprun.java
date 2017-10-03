package am.parsing;


/** Describes the output of an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Nmaprun {

	/**
	 * Scan info for a command.
	 */
	ScanInfo scaninfo;
	
	/**
	 * Verbose info for a command.
	 */
	
	Verbose verbose;
	/**
	 * Debugging info for a command.
	 */
	
	Debugging debugging;
	
	/**
	 * Hosts found by an executed command.
	 */
	Hosts hosts;
	
	/**
	 * Runstats of an executed command.
	 */
	Runstats runstats;
	
	/**
	 * Creates a new <code>Nmaprun</code> and initializes its fields.
	 */
	public Nmaprun() {
		scaninfo = null;
		verbose = null;
		debugging = null;
		hosts = null;
		runstats = null;
	}
	
	/**
	 * @return runstats for this command
	 */
	public Runstats getRunstats() {
		return runstats;
	}

	/** Sets runstats for an executed nmap command to the given value.
	 * @param runstats	runstats for this command.
	 */
	public void setRunstats(Runstats runstats) {
		this.runstats = runstats;
	}

	/**
	 * @return	Scan info for this command.
	 */
	public ScanInfo getScaninfo() {
		return scaninfo;
	}
	
	/** Sets scan info for an executed nmap command to the given value.
	 * @param scaninfo	scan info for this command.
	 */
	public void setScaninfo(ScanInfo scaninfo) {
		this.scaninfo = scaninfo;
	}
	
	/**
	 * @return	verbose info for this command.
	 */
	public Verbose getVerbose() {
		return verbose;
	}
	
	/** Sets verbose info for an executed nmap command to the given value.
	 * @param verbose	verbose info for this command.
	 */
	public void setVerbose(Verbose verbose) {
		this.verbose = verbose;
	}
	
	/**
	 * @return	debugging info for this command.
	 */
	public Debugging getDebbuging() {
		return debugging;
	}
	
	/** Sets debugging info for an executed nmap command to the given value.
	 * @param debugging	debugging info for this command.
	 */
	public void setDebbuging(Debugging debugging) {
		this.debugging = debugging;
	}
	
	/**
	 * @return hosts found by this command.
	 */
	public Hosts getHosts() {
		return hosts;
	}
	
	/** Sets hosts for an executed nmap command.
	 * @param hosts	hosts of this command.
	 */
	public void setHosts(Hosts hosts) {
		this.hosts = hosts;
	}
	
	@Override
	public String toString(){
		return ("\nNmaprun\n" + scaninfo.toString() + verbose.toString() +
				debugging.toString() + hosts.toString() + runstats.toString());
	}
}