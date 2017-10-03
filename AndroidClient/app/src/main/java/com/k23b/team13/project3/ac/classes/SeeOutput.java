package com.k23b.team13.project3.ac.classes;
/**
 * <code>SeeOutput</code> represents a request to take a specific number of results from a <code>SoftwareAgent</code>.
 * @author C. Patsouras I. Venieris
 * @version 1
 */
public class SeeOutput {
	/**
	 * The id of request.
	 */
	private int id;
	/**
	 * Identifier of <code>Software Agent</code> whose reults I want to take.
	 */
	private int hashkey;
	/**
	 * Amount of results I want to take.
	 */
	private int number;
	/**
	 * Android Client made this job (active user).
	 */
	private String user;
	/**
	 * Constructor of <code>SeeOutput</code> class/
	 * @param id The id of request.
	 * @param hashkey Identifier of <code>Software Agent</code> whose reults I want to take.
	 * @param number Amount of results I want to take.
	 * @param user Android Client made this job (active user).
	 */
	public SeeOutput(int id, int hashkey, int number, String user) {
		this.id = id;
		this.hashkey = hashkey;
		this.number = number;
		this.user = user;
	}
	/**
	 * @return The id of request.
	 */
	public int getId() {
		return id;
	}
	/**
	 * Sets id to the given value.
	 * @param id The id of request.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Identifier of <code>Software Agent</code> whose reults I want to take.
	 */
	public int getHashkey() {
		return hashkey;
	}
	/**
	 * Sets hashkey to the given value.
	 * @param hashkey Identifier of <code>Software Agent</code> whose reults I want to take.
	 */
	public void setHashkey(int hashkey) {
		this.hashkey = hashkey;
	}
	/**
	 * @return Amount of results I want to take.
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * Sets number to the given value.
	 * @param number Amount of results I want to take.
	 */
	public void setNumber(int number) {
		this.number = number;
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
