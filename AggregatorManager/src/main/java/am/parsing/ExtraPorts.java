package am.parsing;

/** Contains info about the extraports found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class ExtraPorts {
	
	/**
	 * State of extraports.
	 */
	private String state;
	/**
	 * Number of extraports.
	 */
	private String count;
	/**
	 * Reasons for found extraports.
	 */
	private ExtraReasons reasons;
	
	/**
	 * Creates a new <code>ExtraPorts</code> and initialize its fields.
	 */
	public ExtraPorts() {
		state = new String();
		count = new String();
		reasons = null;
	}

	/** Sets the reasons of extraports to the given value.
	 * @param 	reasons of these extraports.
	 */
	public void setReasons(ExtraReasons reasons) {
		this.reasons = reasons;
	}
	
	/**
	 * @return	reasons of these extraports.
	 */
	public ExtraReasons getReasons() {
		return reasons;
	}
	
	/**
	 * @return	the state of these extraports.
	 */
	public String getState() {
		return state;
	}
	
	/** Sets the state of these extraports to the given value.
	 * @param state	state of these extraports.
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * @return	number of these extraports.
	 */
	public String getCount() {
		return count;
	}
	
	/** Sets the number of these extraports.
	 * @param count	number of these extraports.
	 */
	public void setCount(String count) {
		this.count = count;
	}
	
	@Override	
	public String toString() {
		String s1 = "\n\t\t\tExtraports";
		String s2 = new String();
		if (state.isEmpty() == false) s2 += "\n\t\t\t\tState: " + state;
		if (count.isEmpty() == false) s2 += "\n\t\t\t\tCount: " + count;
		s2 += reasons.toString();
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}