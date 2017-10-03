package com.k23b.team13.project3.ac.classes;
/**
 * Users connected to this app.
 * @author C. Patsouras I. Venieris
 * @version 1
 */
public class Users {
	/**
	 * Name of users who connect to the app.
	 */
	private String username;
	/**
	 * True if user is now connected, false otherwise.
	 */
	private boolean active;
	/**
	 * Construxtor of <code>Users</code> class.
	 * @param username Name of users who connect to the app.
	 * @param active True if user is now connected, false otherwise.
	 */
	public Users(String username, boolean active) {
		this.username = username;
		this.active = active;
	}
	/**
	 * @return Name of users who connect to the app.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Sets username to the given value.
	 * @param username Name of users who connect to the app.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return True if user is now connected, false otherwise.
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * Sets ative to the given value.
	 * @param active True if user is now connected, false otherwise.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
