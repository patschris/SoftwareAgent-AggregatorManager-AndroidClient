package com.k23b.team13.project3.ac.classes;

/**
 *  Handles stop agent command will be sent to Aggregator Manager.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class KillAgent {
	/**
	 * Id of stop agent command.
	 */
	private int id;
	/**
	 * Describing the client will be stopped.
	 */
	private int hashkey;
	/**
	 * Android Client made this job (active user).
	 */
	private String user;
	/**
	 * Constructor of <code>Kill Agent</code> class.
	 * @param id Id of stop agent command.
	 * @param hashkey Describing the client will be stopped.
	 * @param user Android Client made this job (active user).
	 */
	public KillAgent(int id, int hashkey, String user) {
		this.id = id;
		this.hashkey = hashkey;
		this.user = user;
	}
	/**
	 * @return Id of stop agent command.
	 */
	public int getId() {
		return id;
	}
	/**
	 * Sets id to the given value.
	 * @param id Id of stop agent command.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Haskey of the client will be stopped.
	 */
	public int getHashkey() {
		return hashkey;
	}
	/**
	 * Sets hashkey to the given value.
	 * @param hashkey Describing the client will be stopped.
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
}
