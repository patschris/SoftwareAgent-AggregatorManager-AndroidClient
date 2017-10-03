package am.classes;

import am.services.agent.AcceptBlocker;

/** 
 * <code>AndroidLogin</code> represents information that will be sent from some 
 * android client to <code>AggregatorManager</code> during a login request.
 * 
 * @author C. Patsouras I. Venieris
 * @version 1
 */

public class AndroidLogin {
	/**
	 * This android client's device name.
	 */
	private String deviceName;
	
	/**
	 * This android client's username.
	 */
	private String username;
	
	/**
	 * Password sent for this android client's username.
	 */
	private String password;
	
	/**
	 * Blocks <code>LoginService</code> until AM accept/deny this client.
	 */
	private AcceptBlocker ab;
	
	
	
	/**	Constructs a new <code>AndroidLogin</code>.
	 * 	@param	deviceName	this client's device name.
	 * 	@param	username	this client's username.
	 * 	@param	password	password given for this client's username.
	 * 	@param	ab			blocking <code>LoginService</code> until AM review the request.
	 */
	public AndroidLogin(String deviceName, String username, String password,
			AcceptBlocker ab) {
		this.deviceName = deviceName;
		this.username = username;
		this.password = password;
		this.ab = ab;
	}
	/**
	 * @return	username of this <code>AndroidLogin</code>/client.
	 */
	public String getUsername() {
		return username;
	}
	/**	Sets the username for this AndroidLogin to the given value.
	 * 	@param	username	username of this android client.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return password given by android client for this <code>AndroidLogin</code>.
	 */
	public String getPassword() {
		return password;
	}
	/**	Sets the password given for this <code>AndroidLogin</code> to the given value.
	 * 	@param	password	the password given for this <code>AndroidLogin</code>.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return 	this android client's device name.
	 */
	public String getDeviceName() {
		return deviceName;
	}
	/** Sets this android client's device name to the given value.
	 * @param 	deviceName	this client's device name.	
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	/**
	 * @return a String containing the username and deviceName of this android client.
	 */
	public String getCredentials(){
		return this.username + " @ " + this.deviceName;
	}
	/**
	 * 
	 * @return 	this android client's <code>AcceptBlocker</code>.
	 */
	public AcceptBlocker getAb() {
		return ab;
	}
	/** Sets this android client's <code>AcceptBlocker</code> to the given value.
	 * @param 	ab		this android client's <code>AcceptBlocker</code>.
	 */
	public void setAb(AcceptBlocker ab) {
		this.ab = ab;
	}
}