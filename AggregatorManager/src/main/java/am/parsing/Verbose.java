package am.parsing;

/** Contains verbose info for an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Verbose {

	/**
	 * Verbose level for this command.
	 */
	private String level;

	
	/**
	 * Creates a new <code>Verbose</code> and initialize its field.
	 */
	public Verbose() {
		level = new String();
	}

	/**
	 * @return verbose level of an executed command.
	 */
	public String getLevel() {
		return level;
	}
	
	/** Sets verbose level for an executed command to the given value.
	 * @param level	verbose level of this command.
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	
	@Override
	public String toString(){
		return ("\n\tVerbose\n\t\tLevel: " + level);
	}
}