package sa.classes;

/** <code>Result</code> describes the output of an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Result {

	/**
	 * The XML output of this executed nmap job.
	 */
	private String output;
	
	/**
	 * Identifier of the handled job.
	 */
	private int jobId;

	/** Creates a new <code>Result</code>.
	 * @param output 	the XML output of this executed nmap command.
	 * @param jobId		Identifier of the handled <code>NmapJob>. 
	 */
	public Result(String output, int jobId) {
		this.output = output;
		this.jobId = jobId;
	}

	/**
	 * @return the output of the executed command of this <code>Result</code>.
	 */
	public String getOutput() {
		return output;
	}
	/**
	 * @return	the jobId for the executed command of this <code>Result</code> as AM
	 * 			has send it.
	 */
	public int getJobId() {
		return jobId;
	}

	/** Sets the jobId for the executed command of this Result to the given value.
	 * @param jobId	the jobId for the executed command of this <code>Result</code> as AM
	 * 				has send it.
	 */
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	/** Sets the output for this executed command of this Result to the given value.
	 * @param output	the XML output of the executed command.
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	
	@Override
	public String toString() {
		String s = "Executed jobId: " + jobId;
		return s;
	}
}