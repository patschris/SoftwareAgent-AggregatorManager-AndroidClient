package am.parsing;


/** Contains info about reasons for the extraports found from an executed nmap command. 
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class ExtraReasons {

	private String reason;
	private String count;
	
	/**
	 * Creates a new <code>ExtraReasons</code> and initialize its fields.
	 */
	public ExtraReasons(){
		reason = new String();
		count = new String();
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	@Override
	public String toString(){
		String s1 = "\n\t\t\t\tExtrareasons";
		String s2 = new String();
		if (reason.isEmpty() == false) s2 += "\n\t\t\t\t\tReason: " + reason;
		if (count.isEmpty() == false) s2 += "\n\t\t\t\t\tCount: " + count;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}