package am.parsing;

import java.util.ArrayList;

/** Contains <code>OsMatch</code> info for a host found by an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class OsMatch {
	
	/**
	 * Name for this host's OsMatch.
	 */
	private String name;
	
	/**
	 * Accuracy for this host's OsMatch.
	 */
	private String accuracy;
	
	/**
	 * Line info for this host's OsMatch.
	 */
	private String line;
	
	/**
	 * A list of <code>OsClass</code>es for this host's OsMatch.
	 */
	private ArrayList <OsClass> osc;
	
	
	/**
	 * Creates a new <code>OsMatch</code> and initialize its fields.
	 */
	public OsMatch() {
		name = new String();
		accuracy = new String();
		line = new String();
		osc = new ArrayList <OsClass>(0);
	}
	
	/**
	 * @return Name of this host's <code>OsMatch</code>.
	 */
	public String getName() {
		return name;
	}
	
	/** Sets name for this host's <code>OsMatch</code> to the given value.
	 * @param name	name of this <code>OsMatch</code>.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return accuracy of this host's <code>OsMatch</code>.
	 */
	public String getAccuracy() {
		return accuracy;
	}
	
	/** Sets accuracy for this host's <code>OsMatch</code> to the given value.
	 * @param accuracy	accuracy of this <code>OsMatch</code>.
	 */
	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}
	
	/**
	 * @return line info of this host's <code>OsMatch</code>.
	 */
	public String getLine() {
		return line;
	}
	
	/** Sets line info for this host's <code>OsMatch</code> to the given value.
	 * @param line	line info of this <code>OsMatch</code>.
	 */
	public void setLine(String line) {
		this.line = line;
	}
	
	/** Pushes an <code>OsClass</code> to the list of
	 * <code>OsMatch</code> <code>OsClass</code>es of this host.
	 * @param osC	the <code>OsClass</code> to be pushed.
	 */
	public void push(OsClass osC) {
		osc.add(osC);
	}
	
	/** Removes an <code>OsClass</code> from the list of
	 * <code>OsMatch</code> <code>OsClass</code>es of this host.
	 * @param osC	the <code>OsClass</code> to be removed.
	 */
	public void pop(OsClass osC) {
		osc.remove(osC);
	}
	
	@Override
	public String toString() {
		String s1 = "\n\t\t\tOsmatch";
		String s2 = new String();
		if (name.isEmpty() == false) s2 += "\n\t\t\t\tName: " + name;
		if (accuracy.isEmpty() == false) s2 += "\n\t\t\t\tAccuracy: " + accuracy;
		if (line.isEmpty() == false) s2 += "\n\t\t\t\tLine: " + line;
		for (OsClass o : osc) {
			s2 += o.toString();
		}
		if (s2.isEmpty()) return s2;
		else return (s1+s2);
	}
}