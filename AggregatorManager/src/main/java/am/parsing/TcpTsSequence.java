package am.parsing;

/** Contains TcpTsSequence info for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class TcpTsSequence {

	/**
	 * Class info for a <code>TcpTsSequence</code>.
	 */
	private String klass;
	
	/**
	 * Values of a <code>TcpTsSequence</code>.
	 */
	private String values;
	
	
	/**
	 * Creates a new <code>TcpTsSequence</code> and initializes its fields.
	 */
	public TcpTsSequence() {
		klass=new String();
		values=new String();
	}

	/**
	 * @return class info for a host's <code>TcpTsSequence</code>.
	 */
	public String getKlass() {
		return klass;
	}
	
	/** Sets class info for a host's <code>TcpTsSequence</code> to the given value.
	 * @param klass	class info for a <code>TcpTsSequence</code>.
	 */
	public void setKlass(String klass) {
		this.klass = klass;
	}
	
	/**
	 * @return values of a host's <code>TcpTsSequence</code>.
	 */
	public String getValues() {
		return values;
	}
	
	/** Sets values for a host's <code>TcpTsSequence</code>.
	 * @param values	values of <code>TcpTsSequence</code>.
	 */
	public void setValues(String values) {
		this.values = values;
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\ttcptssequence";
		String s2 = new String();
		if (klass.isEmpty() == false) s2 += "\n\t\t\tclass: " + klass;
		if (values.isEmpty() == false) s2 += "\n\t\t\tvalues: " + values;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}