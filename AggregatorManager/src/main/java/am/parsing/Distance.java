package am.parsing;

/** Contains distance info of an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Distance {

	/**
	 * The distance value for this command.
	 */
	private String value;

	/**
	 * Creates a new Distance and initialize its field.
	 */
	public Distance() {
		value = new String();
	}
	
	/**
	 * @return the distance value of this command.
	 */
	public String getValue() {
		return value;
	}
	
	/** Sets the distance value of this command to the given value.
	 * @param value	the distance value of this command.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		if (value.isEmpty()) return value;
		else return ("\n\t\tDistance\n\t\t\tvalue: "+value);
	}
}
