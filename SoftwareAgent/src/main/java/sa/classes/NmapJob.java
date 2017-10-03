package sa.classes;

/** <code>NmapJob</code> represents a nmap job to be executed.
 *
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class NmapJob {

	/**
	 * The id for this nmap job.
	 */
	private int id;
	
	/**
	 * The parameters given for this nmap job.
	 */
	private String parameters;
	
	/**
	 * Indicates whether this is a periodic or one-time job.
	 * If true, this is a periodic job. If false, it's a one-time.  
	 */
	private boolean periodical;
	
	/**
	 * Used for periodic jobs to describe time elapsed between 
	 * command executions.
	 */
	private int time;

	/** Creates a new <code>NmapJob</code>.
	 * @param id			the id for this nmap job.
	 * @param parameters	the parameters given for this nmap job.
	 * @param periodical	indicates whether this is a periodic or one-time job.
	 * @param time			time elapsed between command executions in seconds.
	 */
	public NmapJob(int id, String parameters, boolean periodical, int time) {
		this.id = id;
		this.parameters = parameters;
		this.periodical = periodical;
		this.time = time;
	}

	public NmapJob() {}

	/**
	 * @return the id of this <code>NmapJob</code>.
	 */
	public int getId() {
		return id;
	}
	
	/** Sets the id for this <code>NmapJob</code> to the given value.
	 * @param id the identifier for this <code>NmapJob</code>.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the parameters of this <code>NmapJob</code>.
	 */
	public String getParameters() {
		return parameters;
	}
	
	/** Sets the parameters for this <code>NmapJob</code> to the given value.
	 * @param parameters the arguments for this <code>NmapJob</code>.
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters; 
	}
	
	/**
	 * @return true if this is a periodic <code>NmapJob</code>, false if not so.
	 */
	public boolean isPeriodical() {
		return periodical;
	}
	
	/** Sets the flag which indicates if this <code>NmapJob</code> is periodic
	 *  to the given value.
	 * @param periodical	flag indicating if this <code>NmapJob</code> is periodic.
	 */
	public void setPeriodical(boolean periodical) {
		this.periodical = periodical;
	}
	
	/**
	 * @return the time in seconds between two command executions of this <code>NmapJob</code>.
	 */
	public int getTime() {
		return time;
	}
	
	/** Sets the period in seconds for this periodic <code>NmapJob</code> to the given value .
	 * @param time 	time elapsed between command executions in seconds.
	 */
	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public String toString(){
		String s = "Id = " + this.id + ", parameters = " + this.parameters + ", periodical = " + this.periodical + ", time = " + this.time  ;
		return s ;
	}
}