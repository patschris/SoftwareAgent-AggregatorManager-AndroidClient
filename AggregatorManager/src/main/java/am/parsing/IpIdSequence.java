package am.parsing;

/** Contains IPIDsequence info for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class IpIdSequence {
	
	/**
	 * Class info for a host's IPIDsequence.
	 */
	private String klass;
	/**
	 * Values of a host's IPIDsequence.
	 */
	private String values;
	
	/**
	 * Creates a new IpIdSequence and initializes its fields.
	 */
	public IpIdSequence() {
		klass = new String();
		values = new String();
	}
	
	/**
	 * @return class info for this host's IPIDsequence.
	 */
	public String getKlass() {
		return klass;
	}
	
	/** Sets class info for this host's IPIDsequence to the given value.
	 * @param klass		class info for this IPIDsequence.
	 */
	public void setKlass(String klass) {
		this.klass = klass;
	}
	
	/**
	 * @return	values of this host's IPIDsequence
	 */
	public String getValues() {
		return values;
	}
	
	/** Sets values for this host's IPIDsequence.
	 * @param values	values of this IPIDsequence.
	 */
	public void setValues(String values) {
		this.values = values;
	}
	
	@Override
	public String toString(){
		String s1 = "\n\t\tIpidsequence";
		String s2 = new String();
		if (klass.isEmpty() == false) s2 += "\n\t\t\tClass: " + klass;
		if (values.isEmpty() == false) s2 += "\n\t\t\tValues: " + values;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}