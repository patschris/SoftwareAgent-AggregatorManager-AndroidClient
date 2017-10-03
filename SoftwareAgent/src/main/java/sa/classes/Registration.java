package sa.classes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

/** Creates a <code>Registration</code> that will be sent to Aggregator Manager.
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Registration {

	/**
	 * This client's device name.
	 */
	private String deviceName;
	
	/**
	 * This client's IP address.
	 */
	private String interfaceIP;
	
	/**
	 * This client's MAC address.
	 */
	private String interfaceMAC;
	
	/**
	 * This client's OS version.
	 */
	private String OsVersion;
	
	/**
	 * This client's nmap version.
	 */
	private String nmapVersion;
	
	/**
	 * This client's hashkey. A unique identifier for this client.
	 */
	private int HashKey;


	/** Constructs a new <code>Registration</code> consisted of the appropriate fields.
	 * 
	 */
	public Registration(){
		/*
	http://stackoverflow.com/questions/7883542/getting-the-computer-name-in-java
	http://www.mkyong.com/java/how-to-get-mac-address-in-java/
		 */
		try {
			// gets this client's device name.
			this.deviceName=InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Enumeration<NetworkInterface> nets = null;
		try {
			// obtain this client's network interfaces.
			nets = NetworkInterface.getNetworkInterfaces();

			for (NetworkInterface netint : Collections.list(nets)){
				// Searching for this client's MAC address.
				// receiving the correct MAC address if the interface is not a loopback one.
				if (netint.isLoopback() == false) {
					byte[] mac = netint.getHardwareAddress();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));		
					}
					this.interfaceMAC = sb.toString();
					// obtain this interface's IP addresses, choosing the correct one to send to AM.
					Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
					for (InetAddress inetAddress : Collections.list(inetAddresses)) {
						if (inetAddress.isSiteLocalAddress()) this.interfaceIP = inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		// obtaining this client's OS name and version
		this.OsVersion=System.getProperty("os.name") + " " + System.getProperty("os.version") ;
		try {
			// obtaining this client's nmap version by running an "nmap -version" process.
			Process process = Runtime.getRuntime().exec("nmap -version");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s = new String();
			String line = new String();
			while ((line =reader.readLine()) != null){
				s += line ;
			}
			reader.close();
			String[] array = s.split(" ");
			this.nmapVersion=array[2];
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.HashKey=this.hashCode();
	}

	/**
	 * @return this client's device name.
	 */
	public String getDeviceName() {
		return deviceName;
	}
	/** Sets this client's device name to the given value.
	 * @param deviceName	this client's device name.
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	/**
	 * @return this client's IP address.
	 */
	public String getInterfaceIP() {
		return interfaceIP;
	}
	/** Sets this client's IP address to the given value.
	 * @param interfaceIP	this client's IP address.
	 */
	public void setInterfaceIP(String interfaceIP) {
		this.interfaceIP = interfaceIP;
	}
	/**
	 * @return this client's MAC address.
	 */
	public String getInterfaceMAC() {
		return interfaceMAC;
	}
	/** Sets this client's MAC address to the given value.
	 * @param interfaceMAC	this client's MAC address.
	 */
	public void setInterfaceMAC(String interfaceMAC) {
		this.interfaceMAC = interfaceMAC;
	}
	/**
	 * @return this client's OS version.
	 */
	public String getOsVersion() {
		return OsVersion;
	}
	/** Sets this client's OS version to the given value.
	 * @param osVersion	this client's OS version.
	 */
	public void setOsVersion(String osVersion) {
		OsVersion = osVersion;
	}
	/**
	 * @return this client's nmap version.
	 */
	public String getNmapVersion() {
		return nmapVersion;
	}
	/** Sets this client's nmap version to the given value.
	 * @param nmapVersion	this client's nmap version.
	 */
	public void setNmapVersion(String nmapVersion) {
		this.nmapVersion = nmapVersion;
	}
	/**
	 * @return this client's HashKey-identifier.
	 */
	public int getHashKey() {
		return HashKey;
	}
	/** Sets this client's HashKey-identifier to the given value.
	 * @param hashKey	this client's HashKey.
	 */
	public void setHashKey(int hashKey) {
		HashKey = hashKey;
	}
	/** Creating a HashKey - unique identifier for this client.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		Random r = new Random();
		int result = r.nextInt(10000);
		result = prime * result
				+ ((OsVersion == null) ? 0 : OsVersion.hashCode());
		result = prime * result
				+ ((deviceName == null) ? 0 : deviceName.hashCode());
		result = prime * result
				+ ((interfaceIP == null) ? 0 : interfaceIP.hashCode());
		result = prime * result
				+ ((interfaceMAC == null) ? 0 : interfaceMAC.hashCode());
		result = prime * result
				+ ((nmapVersion == null) ? 0 : nmapVersion.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Registration [deviceName=" + deviceName + ", interfaceIP="
				+ interfaceIP + ", interfaceMAC=" + interfaceMAC
				+ ", OsVersion=" + OsVersion + ", nmapVersion=" + nmapVersion
				+ ", HashKey=" + HashKey + "]";
	}

}
