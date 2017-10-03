package am.parsing;


import java.util.ArrayList;

/** Contains host info of an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Host {

	/**
	 * Starting time for this host.
	 */
	private String stime;
	/**
	 * Ending time for this host.
	 */
	private String etime;
	/**
	 * Status for this host.
	 */
	private HostStatus status;
	/**
	 * An address list for this host.
	 */
	private ArrayList <HostAddress> addressList;
	/**
	 * Hostnames for this host.
	 */
	private Hostnames hostnames;
	/**
	 * Ports info for this host.
	 */
	private Ports ports;
	/**
	 * OS info for this host.
	 */
	private Os os;
	/**
	 * Uptime info for this host.
	 */
	private HostUptime uptime;
	/**
	 * Distance info for this host.
	 */
	private Distance distance;
	/**
	 * TCPsequence info for this host.
	 */
	private TcpSequence tcpsequence;
	/**
	 * IPIDsequence info for this host.
	 */
	private IpIdSequence ipidsequence;
	/**
	 * TCPTSsequence info for this host.
	 */
	private TcpTsSequence tcptssequence;
	/**
	 * Trace info for this host.
	 */
	private Trace trace;

	/**
	 *  Creates a new host and initializes its fields.
	 */
	public Host() {
		etime = new String();
		stime = new String();
		addressList = new ArrayList <HostAddress>(0);
		hostnames = null;
		ports = null;
		os = null;
		uptime = null;
		distance = null;
		tcpsequence = null;
		ipidsequence = null;
		tcptssequence = null;
		trace = null;
	}
	/**
	 * @return	starting time for this host.
	 */
	public String getStime() {
		return stime;
	}

	/** Sets starting time for this host to the given value.
	 * @param stime	starting time for this host.
	 */
	public void setStime(String stime) {
		this.stime = stime;
	}

	/**
	 * @return ending time for this host.
	 */
	public String getEtime() {
		return etime;
	}

	/** Sets ending time for this host to the given value.
	 * @param etime	ending time for this host.
	 */
	public void setEtime(String etime) {
		this.etime = etime;
	}

	/**
	 * @return status of this host.
	 */
	public HostStatus getStatus() {
		return status;
	}

	/** Sets status info for this host to the given value.
	 * @param status	status info for this host.
	 */
	public void setStatus(HostStatus status) {
		this.status = status;
	}

	/** Pushes an address to the address list this host has.
	 * @param had	the address to be pushed.
	 */
	public void push(HostAddress had){
		addressList.add(had);
	}
	
	/** Removes an address by the address list this host has.
	 * @param had	the address to be removed.
	 */
	public void pop(HostAddress had){
		addressList.remove(had);
	}
	
	/**
	 * @return 	the address list for this host.
	 */
	public ArrayList<HostAddress> getAddressList() {
		return addressList;
	}
	
	/** Sets the address list for this host to the given value.
	 * @param addressList	the address list for this host.
	 */
	public void setAddressList(ArrayList<HostAddress> addressList) {
		this.addressList = addressList;
	}
	
	/**
	 * @return	hostnames for this host.
	 */
	public Hostnames getHostnames() {
		return hostnames;
	}

	/** Sets the hostnames of this host to the given value.
	 * @param hostnames	hostnames of this host.
	 */
	public void setHostnames(Hostnames hostnames) {
		this.hostnames = hostnames;
	}

	/**
	 * @return	ports info for this host
	 */
	public Ports getPorts() {
		return ports;
	}

	/** Sets port info for this host to the given value.
	 * @param ports		port info for this host.
	 */
	public void setPorts(Ports ports) {
		this.ports = ports;
	}

	/**
	 * @return OS info for this host.
	 */
	public Os getOs() {
		return os;
	}

	/** Sets OS info for this host to the given value.
	 * @param os	OS info for this host. 
	 */
	public void setOs(Os os) {
		this.os = os;
	}

	/**
	 * @return	uptime info for this host.
	 */
	public HostUptime getUptime() {
		return uptime;
	}

	/** Sets uptime info for this host to the given value.
	 * @param uptime	uptime info for this host.
	 */
	public void setUptime(HostUptime uptime) {
		this.uptime = uptime;
	}

	/**
	 * @return distance info for this host.
	 */
	public Distance getDistance() {
		return distance;
	}

	/** Sets distance info for this host to the given value.
	 * @param distance	distance info for this host.
	 */
	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	/**
	 * @return	TCPsequence info for this host.
	 */
	public TcpSequence getTcpsequence() {
		return tcpsequence;
	}

	/** Sets TCPsequence info for this host to the given value.
	 * @param tcpsequence	TCPsequence info for this host.
	 */
	public void setTcpsequence(TcpSequence tcpsequence) {
		this.tcpsequence = tcpsequence;
	}

	/**
	 * @return	IPIDsequence info for this host.
	 */
	public IpIdSequence getIpidsequence() {
		return ipidsequence;
	}

	/** Sets IPIDsequence info for this host to the given value.
	 * @param ipidsequence	IPIDsequence info for this host.
	 */
	public void setIpidsequence(IpIdSequence ipidsequence) {
		this.ipidsequence = ipidsequence;
	}

	/**
	 * @return	TCPTSsequence info for this host.
	 */
	public TcpTsSequence getTcptssequence() {
		return tcptssequence;
	}

	/** Sets TCPTSsequence info for this host to the given value.
	 * @param tcptssequence	TCPTSsequence info for this host.
	 */
	public void setTcptssequence(TcpTsSequence tcptssequence) {
		this.tcptssequence = tcptssequence;
	}

	/**
	 * @return trace info for this host.
	 */
	public Trace getTrace() {
		return trace;
	}

	/** Sets trace info for this host to the given value.
	 * @param trace	trace info for this host.
	 */
	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	@Override
	public String toString(){
		String s = new String();
		s += "\n\tHost";
		if (stime.isEmpty()==false) s += "\n\t\tStarttime: " + stime;
		if (etime.isEmpty()==false) s += "\n\t\tEndtime: " + etime;
		if (status!=null) s += status.toString();
		if (addressList != null){
			for(HostAddress had : addressList){
				s += had.toString();
			}
		}
		if (hostnames!=null) s += hostnames.toString();
		if (ports!=null) s +=ports.toString();
		if (os!=null) s += os.toString();
		if (uptime!=null) s += uptime.toString();
		if (distance!=null) s += distance.toString();
		if (tcpsequence!=null) s += tcpsequence.toString();
		if (ipidsequence!=null) s += ipidsequence.toString();
		if (tcptssequence!=null) s += tcptssequence.toString();
		if (trace!=null) s += trace.toString();
		return s;
	}
}