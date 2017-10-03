package am.parsing;

/** Contains TCPsequence info for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class TcpSequence {
	
	/**
	 * Index of <code>TcpSequence</code>.
	 */
	private String index;
	
	/**
	 * Difficulty of <code>TcpSequence</code>.
	 */
	private String difficulty;
	
	/**
	 * Values of <code>TcpSequence</code>.
	 */
	private String values;
	
	
	/**
	 * Creates a new TcpSequence and initializes its fields.
	 */
	public TcpSequence(){
		index = new String();
		difficulty = new String();
		values = new String();
	}

	/**
	 * @return index of this host's <code>TcpSequence</code>.
	 */
	public String getIndex() {
		return index;
	}
	
	/** Sets index for this host's <code>TcpSequence</code> to the given value.
	 * @param index	index of this <code>TcpSequence</code>.
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	
	/**
	 * @return difficulty of this host's <code>TcpSequence</code>.
	 */
	public String getDifficulty() {
		return difficulty;
	}
	
	/** Sets difficulty for this host's <code>TcpSequence</code> to the given value.
	 * @param difficulty	difficulty of this <code>TcpSequence</code>.
	 */
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	/**
	 * @return values of this host's <code>TcpSequence</code>.
	 */
	public String getValues() {
		return values;
	}
	
	/** Sets values for this host's <code>TcpSequence</code> to the given value.
	 * @param values	values of this <code>TcpSequence</code>.
	 */
	public void setValues(String values) {
		this.values = values;
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\tTcpsequence";
		String s2 = new String();
		if (index.isEmpty() == false) s2 += "\n\t\t\tIndex: " + index;
		if (difficulty.isEmpty() == false) s2 += "\n\t\t\tDifficulty: " + difficulty;
		if (values.isEmpty() == false) s2 += "\n\t\t\tValues: " + values;
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}