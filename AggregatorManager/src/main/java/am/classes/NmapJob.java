package am.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;

import am.resources.DbConnection;
import am.structures.JobQueue;

/** <code>NmapJob</code> represents a nmap job to be executed by an Agent.
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
	
	/**
	 * Hashkey of the agent that will execute this job.
	 */
	private int hashKey;
	
	/**
	 * Indicates whether a job is sent to the <code>SoftwareAgent</code>.
	 */
	private boolean sent;

	/** Constructor of the new <code>NmapJob</code> class. Creates a new <code>NmapJob</code>.
	 * For Agent's or periodic job termination, this id will be equal to the id that is passed as an argument. 
	 * In any other case, id is equal to <code>JobQueue</code> counter.
	 * @param id			the id for this nmap job.
	 * @param parameters	the parameters given for this nmap job.
	 * @param periodical	indicates whether this is a periodic or one-time job.
	 * @param time			time elapsed between command executions in seconds.
	 * @param hashkey		the hashkey of the agent that this job will be sent
	 */
	public NmapJob(int id, String parameters, boolean periodical, int time,int hashkey) {
		if (id == -1 || parameters.equals("Stop")) {
			this.id = id;
		}
		else {
			this.id = JobQueue.getCounter();
		}
		this.parameters = parameters;
		this.periodical = periodical;
		this.time = time;
		this.hashKey = hashkey;
		this.sent = false;
	}
	
	/**
	 * 
	 * @return the hashKey of this <code>NmapJob</code>.
	 */
	public int getHashKey() {
		return hashKey;
	}
	
	/**
	 * @return the sent flag of this <code>NmapJob</code>
	 */
	public boolean isSent() {
		return sent;
	}
	
	/**
	 * Sets the sent flag for this <code>NmapJob</code> to the given value and updates the database.
	 * @param sent indicates whether a job is sent to the <code>SoftwareAgent</code>.
	 */
	public void setSent(boolean sent) {
		// mark the job as sent
		this.sent = sent;
		if (this.id == -1) return;
		if (this.parameters.equals("Stop")) return;
		// if this is not a termination job, update database
		DbConnection dbc = DbConnection.getInstance();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(dbc.getDriver());
		} catch (ClassNotFoundException ec) {
			ec.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
			stmt = conn.createStatement();
			String query = "update NmapJobs set Sent="+this.sent+" where Id="+this.id;
			stmt.executeUpdate(query);

		} catch (SQLException es) {
			es.printStackTrace();
		}
		finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException esq) {
				esq.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets this hashKey for this <code>NmapJob</code> to the given value
	 * @param hashKey the hashkey of the agent that this job will be sent
	 */
	public void setHashKey(int hashKey) {
		this.hashKey = hashKey;
	}

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

	/** Sets the flag which indicates if this <code>NmapJob</code> is periodic to the given value.
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
	
	/**
	 * 
	 * @return a string that contains the id, the command and the hashKey of the SA for this <code>NmapJob</code> 
	 */
	@Override
	public String toString(){
		return (this.id + " : " + this.parameters+" : " + this.hashKey); 
	}
	/**
	 * @return JSON object that represents this <code>NmapJob</code> object.
	 */
	@SuppressWarnings("unchecked")
	public  JSONObject JsonNmap(){
		JSONObject obj=new JSONObject();
		  obj.put("id", id);
		  obj.put("command", parameters);
		  obj.put("periodical", periodical);
		  obj.put("time", time);
		  obj.put("hashkey",hashKey);
		  return obj;
	}
}