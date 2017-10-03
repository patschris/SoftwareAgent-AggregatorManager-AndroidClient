package com.k23b.team13.project3.ac.classes;
/**
 * <code>StopPeriodic</code> represents a request to stop a periodic job from a <code>SoftwareAgent</code>.
 * @author C. Patsouras I. Venieris
 * @version 1
 */
public class StopPeriodic {
	/**
	 * The id for this stop periodic request.
	 */
	private int id;
	/**
	 * The id of the job I want to stop.
	 */
	private int jobid;
	/**
	 * Android Client made this job (active user).
	 */
	private String user;
	/**
	 * Identifier of <code>Software Agent</code> whose job I want to stop.
	 */
	private int hashkey;
	/**
	 * Constructor of <code>StopPeriodic</code> class.
	 * @param id The id for this stop periodic request.
	 * @param jobid The id of the job I want to stop.
	 * @param user Android Client made this job (active user).
	 * @param hashkey Identifier of <code>Software Agent</code> whose job I want to stop.
	 */
	public StopPeriodic(int id, int jobid, String user, int hashkey) {
		this.id = id;
		this.jobid = jobid;
		this.user = user;
		this.hashkey = hashkey;
	}
	/**
	 * @return Identifier of <code>Software Agent</code> whose job I want to stop.
	 */
	public int getHashkey() {
		return hashkey;
	}
	/**
	 * Sets hashkey to the given value.
	 * @param hashkey Identifier of <code>Software Agent</code> whose job I want to stop.
	 */
	public void setHashkey(int hashkey) {
		this.hashkey = hashkey;
	}
	/**
	 * @return The id for this stop periodic request.
	 */
	public int getId() {
		return id;
	}
	/**
	 * Sets id to the given value.
	 * @param id The id for this stop periodic request.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return The id of the job I want to stop.
	 */
	public int getJobid() {
		return jobid;
	}
	/**
	 * Sets jobid to the given value.
	 * @param jobid The id of the job I want to stop.
	 */
	public void setJobid(int jobid) {
		this.jobid = jobid;
	}
	/**
	 * @return Android Client made this job (active user).
	 */
	public String getUser() {
		return user;
	}
	/**
	 * Sets user to the given value.
	 * @param user Android Client made this job (active user).
	 */
	public void setUser(String user) {
		this.user = user;
	}
}
