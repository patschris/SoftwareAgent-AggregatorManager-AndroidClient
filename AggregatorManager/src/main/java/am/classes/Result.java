package am.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import am.parsing.*;


/** <code>Result</code> represents the output of an executed nmap command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class Result {
	
	/**
	 *  HashKey describes the agent that sent this result. 
	 */
	private int Hashkey;
	
	/**
	 * 	<code>Nmaprun</code> describes the output of the executed command.
	 */
	private Nmaprun result;
	
	/**
	 * Date and time this <code>Result</code> was received by AggregatorManager.
	 */
	private Date curDate;
	
	/**
	 * <code>NmapJob</code> identifier for the executed job that this result is about.
	 */
	private int jobid;
	
	/**
	 * Command executed that this <code>Result</code> is about.
	 */
	private String command;
	
	/**
	 * Time (since epoch) this result was received by <code>AggregatorManager</code>.
	 */
	private long curTime;
	
	
	/** Constructs a <code>Result</code> instance with the given value.
	 * @param hashkey	client executed the result this job is about.
	 * @param result	<code>Nmaprun</code> output of this <code>Result</code>.
	 * @param jobid		job identifier that this <code>Result</code> is about.
	 * @param command	executed command that this <code>Result</code> is about.
	 */
	public Result(int hashkey, Nmaprun result, int jobid, String command) {
		this.Hashkey = hashkey;
		this.result = result;
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		this.curDate = calendar.getTime();
		this.jobid = jobid;
		this.command = command;
		this.curTime = System.currentTimeMillis();
	}

	/**
	 * @return the command executed this Result is about.
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return	NmapJob identifier that this Result is about.
	 */
	public int getJobid() {
		return jobid;
	}

	

	/**
	 * @return 	HashKey	describing the client that executed the NmapJob.
	 */
	public int getHashkey() {
		return Hashkey;
	}
	
	/**
	 * @return Nmaprun output of this Result.
	 */
	public Nmaprun getResult() {
		return result;
	}
	
	/**
	 * @return	time (since epoch) this Result was received by AggregatorManager.
	 */
	public long getCurTime() {
		return curTime;
	}
	
	/**
	 * @return Date (and time) this Result was received by AggreagatorManager.
	 */
	public Date getCurDate(){
		return curDate;
	}
	
	@Override
	public String toString() {
		String s = curDate + "\n";
		s += "ID: " + this.jobid + "\n";
		s += "command: " + this.command;
		s += this.result.toString();
		s += "\n\n\n\n";
		return s;
	}	
}