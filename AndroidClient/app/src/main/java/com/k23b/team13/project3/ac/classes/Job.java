package com.k23b.team13.project3.ac.classes;
/** <code>Job</code> represents a nmap job to be sent to Aggregator Manager.
 *
 * @author C. Patsouras I. Venieris
 * @version 1
 */
public class Job {
	/**
	 * The id for this nmap job.
	 */
	private int id;
	/**
	 * The parameters given for this nmap job.
	 */
	private String command;
	/**
	 * Indicates whether this is a periodic or one-time job.
	 * If true, this is a periodic job. If false, it's a one-time.
	 */
	private boolean periodic;
	/**
	 * Used for periodic jobs to describe time elapsed between
	 * command executions.
	 */
	private int time;
	/**
	 * Identifier of the Software Agent that will execute this job.
	 */
	private int hashkey;
	/**
	 * Android Client made this job (active user).
	 */
	private String user;
	/**
	 * Constructor of <code>Job</code> class.
	 * @param id The id for this nmap job.
	 * @param command The parameters given for this nmap job.
	 * @param periodic indicates whether this is a periodic or one-time job.
	 * @param time Used for periodic jobs to describe time elapsed between command executions.
	 * @param hashkey Identifier of the Software Agent that will execute this job.
	 * @param user Android Client made this job (active user).
	 */
	public Job(int id, String command, boolean periodic, int time, int hashkey,String user) {
		this.id = id;
		this.command = command;
		this.periodic = periodic;
		this.time = time;
		this.hashkey = hashkey;
		this.user = user;
	}
	/**
	 * @return The id for this job.
	 */
	public int getId() {
		return id;
	}
	/**
	 * Sets id of this job to the given value.
	 * @param id The id for this job.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return The parameters given for this job.
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @return true if this is a periodic <code>Job</code>, false if not so.
	 */
	public boolean isPeriodic() {
		return periodic;
	}
	/**
	 * @return the time in seconds between two command executions of this <code>Job</code>.
	 */
	public int getTime() {
		return time;
	}
	/**
	 * @return 	HashKey	describing the client will execute the <code>Job</code>.
	 */
	public int getHashkey() {
		return hashkey;
	}
	/**
	 * Sets Hashkey to the given value.
	 * @param hashkey describing the client will execute the <code>Job</code>.
	 */
	public void setHashkey(int hashkey) {
		this.hashkey = hashkey;
	}

	/**
	 * @return user who create the job.
	 */
	public String getUser() {
		return user;
	}

	@Override
	public String toString() {
		String s;
		s = this.id + ": " + command + "\n";
		s += hashkey + " agent executes every " + time + " sec";
		return s;
	}
}
