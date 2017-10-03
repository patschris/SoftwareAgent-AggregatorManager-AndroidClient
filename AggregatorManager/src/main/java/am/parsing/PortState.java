package am.parsing;

/** Contains <code>PortState</code> info for a host's <code>Port</code> found by an executed command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class PortState {

	/**
	 * State for this <code>PortState</code>.
	 */
	private String state;
	
	/**
	 * Reason for this <code>PortState</code>.
	 */
	private String reason;
	
	/**
	 * ReasonTTL for this <code>PortState</code>.
	 */
	private String reasonTtl;
	
	
	/**
	 * Creates a new <code>PortState</code> and initializes its fields.
	 */
	public PortState() {
		state = new String();
		reason = new String();
		reasonTtl = new String();
	}
	
	/**
	 * @return state for this port.
	 */
	public String getState() {
		return state;
	}
	
	/** Sets state for this port to the given value.
	 * @param state	state of this port.
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * @return reason of this PortState.
	 */
	public String getReason() {
		return reason;
	}
	
	/** Sets reason for this PortState to the given value.
	 * @param reason	reason for this PortState.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/**
	 * @return reasonTTL for this PortState.
	 */
	public String getReasonTtl() {
		return reasonTtl;
	}
	
	/** Sets reasonTTL for this PortState to the given value.
	 * @param reasonTtl	reasonTTL for this PortState.
	 */
	public void setReasonTtl(String reasonTtl) {
		this.reasonTtl = reasonTtl;
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\t\t\tState";
		String s2 = new String();
		if (state.isEmpty() == false) s2 += "\n\t\t\t\t\tState: " + state;
		if (reason.isEmpty() == false) s2 += "\n\t\t\t\t\tReason: " + reason;
		if (reasonTtl.isEmpty() == false ) s2 += "\n\t\t\t\t\tReasonttl: " + reasonTtl;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}