package am.parsing;

/** Contains debugging info for an executed nmap command.
 *
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Debugging {

	/**
	 * Debugging level of executed command.
	 */
	private String level;

	public Debugging() {
		level = new String();
	}

	/**
	 * @return the debugging level of this command.
	 */
	public String getLevel() {
		return level;
	}

	/** Sets the debugging level of this command to the given value. 
	 * @param level	the debugging level for this command.
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString(){
		return ("\n\tDebugging\n\t\tLevel: " + level);
	}
}
