package am.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import am.resources.DbConnection;
import am.services.agent.AcceptBlocker;


/*
http://stackoverflow.com/questions/7883542/getting-the-computer-name-in-java
http://www.mkyong.com/java/how-to-get-mac-address-in-java/
*/
/** 
 * <code>Registration</code> represents information that will be sent from some 
 * <code>SoftwareAgent</code> to <code>AggregatorManager</code> during a register request.
 * 
 * @author C. Patsouras I. Venieris
 * @version 1
 */

public class Registration {
	
	/**
	 * This client's device name.
	 */
	private String deviceName;
	
	/**
	 * Site local IP address for this client. 
	 */
	private String interfaceIP;
	
	/**
	 *	MAC address for this client's device.
	 */
	private String interfaceMAC;
	
	/**
	 * Operating System for this client's device.
	 */
	private String OsVersion;
	
	/**
	 * Nmap version for this client's device. 
	 */
	private String nmapVersion;
	
	/**
	 *  Hashkey for this client. (Primary key in db's "Agents" table).
	 */
	private int HashKey;
	
	/**
	 * Indicates whether this client is currently online or not.
	 */
	private boolean online;
	
	/**
	 * Time (since epoch) for this client's last job request.
	 */
	private long lastRequest;
	
	/**
	 * Blocks <code>RegisterService</code> until AM accept/deny this client.
	 */
	private AcceptBlocker ab;

	/**
	 * Constructor of the <code>Registration</code> class.
	 */
	public Registration(){
		deviceName = null;
		interfaceIP = null;
		interfaceMAC = null;
		OsVersion = null;
		nmapVersion = null;
		HashKey = 0;
		online = true;
		lastRequest = 0;
		ab = null;
	}

	/**
	 * 
	 * @return 	this client's <code>AcceptBlocker</code>.
	 */
	public AcceptBlocker getAb() {
		return ab;
	}


	/** Sets this client's <code>AcceptBlocker</code> to the given value.
	 * @param 	ab		this client's <code>AcceptBlocker</code>.
	 */
	public void setAb(AcceptBlocker ab) {
		this.ab = ab;
	}


	/**
	 * @return 	this client's last request for <code>NmapJob</code>s time (since epoch).
	 */
	public long getLastRequest() {
		return lastRequest;
	}


	/** Sets this client's last request for <code>NmapJob</code>s time (since epoch) to the given value.
	 * @param 	lastRequest		this client's last request time.
	 */
	public void setLastRequest(long lastRequest) {
		this.lastRequest = lastRequest;
	}


	/**
	 * @return 	this client's device name.
	 */
	public String getDeviceName() {
		return deviceName;
	}

	
	/**
	 * @return	true if this client is currently online, false if not so.
	 */
	public boolean isOnline() {
		return online;
	}

	/** Sets this client status to the given value (true for online/false for online)
	 * 	and updates the appropriate field of Agents table in database.
	 *
	 * @param 	online	this client's status.	
	 */
	public void setOnline(boolean online) {
		this.online = online;
		// connect to the database and update this client's status
		DbConnection dbc = DbConnection.getInstance();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(dbc.getDriver());
		} catch (ClassNotFoundException ec) {
			ec.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
			stmt = conn.createStatement();
			String query = "update Agents set Online="+this.online+" where HashKey="+this.HashKey;
			stmt.executeUpdate(query);
		} catch (SQLException es) {
			es.printStackTrace();
		}
		finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException esq) {
				esq.printStackTrace();
			}
		}
	}

	/** Sets this client's device name to the given value.
	 * @param 	deviceName	this client's device name.	
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	/** 
	 * @return	site local IP address of this client. 
	 */
	public String getInterfaceIP() {
		return interfaceIP;
	}
	
	/** Sets this clients site local IP address to the given value.
	 * @param 	interfaceIP	this client's IP address.	
	 */
	public void setInterfaceIP(String interfaceIP) {
		this.interfaceIP = interfaceIP;
	}
	
	/**
	 * @return	this client's device MAC address.
	 */
	public String getInterfaceMAC() {
		return interfaceMAC;
	}
	
	/** Sets this client's device MAC address to the given value.
	 * @param 	interfaceMAC	this client's device MAC address.	
	 */
	public void setInterfaceMAC(String interfaceMAC) {
		this.interfaceMAC = interfaceMAC;
	}
	
	/**
	 * @return	this client's device OS version.
	 */
	public String getOsVersion() {
		return OsVersion;
	}
	
	/** Sets this client's device OS version to the given value.
	 * @param 	osVersion	this client's device OS version.
	 */
	public void setOsVersion(String osVersion) {
		OsVersion = osVersion;
	}
	
	/**
	 * @return 	nmap version this client's device has installed.
	 */
	public String getNmapVersion() {
		return nmapVersion;
	}
	
	/** Sets nmap version this client's device has installed to the given value.
	 * @param 	nmapVersion	nmap version this client's device has installed.
	 */
	public void setNmapVersion(String nmapVersion) {
		this.nmapVersion = nmapVersion;
	}
	
	/**
	 * @return 	HashKey	describing this client (primary key for Agents table in database).
	 */
	public int getHashKey() {
		return HashKey;
	}
	
	/** Sets the HashKey describing this client to the given value.
	 * @param 	hashKey	a Hashkey desribing this client.	
	 */
	public void setHashKey(String hashKey) {
		HashKey = Integer.parseInt(hashKey);
	}
	
	/**
	 * @return a string to represent this Agent.
	 */
	public String getname(){
		return "Software Agent " + this.getHashKey();
	}

	@Override
	public String toString() {
		return "Registration [deviceName=" + deviceName + ", interfaceIP="
				+ interfaceIP + ", interfaceMAC=" + interfaceMAC
				+ ", OsVersion=" + OsVersion + ", nmapVersion=" + nmapVersion
				+ ", HashKey=" + HashKey + "]\nlast_request: " + this.getLastRequest() + "\nstatus: " + this.isOnline() + " ]\n";
	}
}