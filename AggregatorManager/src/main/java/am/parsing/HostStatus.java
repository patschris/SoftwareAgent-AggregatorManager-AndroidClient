package am.parsing;

/** Contains status info for a host found by an executed nmap comamand.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class HostStatus {

	private String state;
	private String reason;
	private String reasonTtl;
	
	public HostStatus() {
		state = new String();
		reason = new String();
		reasonTtl = new String();
	}
	
	/**
	 * @return state of this <code>Host</code>.
	 */
	public String getState() {
		return state;
	}
	
	/** Sets state for this <code>Host</code> to the given value.
	 * @param state	state of this host
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * @return reason of this <code>HostStatus</code>.
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * @return reasonTTL of this <code>HostStatus</code>.
	 */
	public String getReasonTtl() {
		return reasonTtl;
	}
	
	/** Sets reasonTTL for this <code>HostStatus</code> to the given value.
	 * @param reasonTtl	reasonTTL of this <code>HostStatus</code>.
	 */
	public void setReasonTtl(String reasonTtl) {
		this.reasonTtl = reasonTtl;
	}
	
	/** Sets reason info for this <code>HostStatus</code> to the given value.
	 * @param reason	reason of this <code>HostStatus</code>.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Override
	public String toString(){
		String s1 = "\n\t\tStatus";
		String s2 = new String();
		if (state.isEmpty() == false) s2 += "\n\t\t\tState: " + state;
		if (reason.isEmpty() == false) s2 += "\n\t\t\tReason: " + reason;
		if (reasonTtl.isEmpty() == false) s2 += "\n\t\t\tReasonTtl: " + reasonTtl;
		if (s2.isEmpty()) return s2;
		else return (s1 + s2);
	}
}