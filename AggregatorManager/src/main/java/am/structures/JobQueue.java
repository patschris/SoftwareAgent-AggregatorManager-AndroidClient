package am.structures;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.DefaultListModel;

import org.json.simple.JSONArray;

import am.classes.NmapJob;
import am.gui.DeletePeriodicJobGraph;
import am.resources.DbConnection;

import com.google.gson.Gson;

/** <code>JobQueue</code> is a singleton class consisted of a queue that contains <code>NmapJob</code>s
 *  to be sent to logged in Agents, a static counter used to assign a job identifier to every <code>NmapJob</code>
 *  and a <code>DeletePeriodicJobGraph</code> field used to update GUI when needed.
 *  
 * 	@author C. Patsouras I. Venieris
 * 	@version 1
 */
public class JobQueue {

	// http://crunchify.com/how-to-create-singleton-queue-global-object-for-fifo-first-in-first-out-in-java/
	
	/**
	 * 	Queue containing the <code>NmapJob</code>s to be sent to registered agents.
	 */
	private static Queue <NmapJob> myQueue = new LinkedList<NmapJob>();
	
	/**
	 * 	The instance of this class.
	 */
	private static JobQueue instance = null ;
	
	/**
	 * 	<code>DeletePeriodicJobGraph</code> used to update GUI when needed.
	 */
	private static DeletePeriodicJobGraph dpj = null;
	
	/**
	 * 	Counter used for assigning job identifiers to <code>NmapJob</code>s.
	 */
	private static int counter;


	/** Constructs a <code>JobQueue</code> if not created yet or returns the instance of <code>JobQueue</code>.
	 * 	Updates <code>DeletePeriodicJobGraph</code> to the given value if not null.
	 * 	Connects to database to retrieve number of existing jobs.
	 * 	@param 	dpjm	<code>DeletePeriodicJobGraph</code> needed to update GUI.
	 * 	@return	the instance of this class.
	 */
	public static synchronized JobQueue getInstance(DeletePeriodicJobGraph dpjm){
		if (instance == null){
			// if jobqueue isn't created yet
			// connect to database to obtain number of existing jobs in "NmapJobs" table
			instance = new JobQueue();
			DbConnection dbc = DbConnection.getInstance();
			Connection conn = null;
			Statement stmt = null;
			ResultSet r = null ;
			int counter = 0;
			try {
				Class.forName(dbc.getDriver());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
				stmt = conn.createStatement();
				// query for count of existing NmapJobs in database
				r = stmt.executeQuery("select Id from NmapJobs order by Id");
				while(r.next()){
					counter=r.getInt("Id");
				}
				// jobId giving will begin from n+1, if n jobs currently exists in database
				JobQueue.counter = counter + 1;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally{
				// close connection, statement and result set
				try {
					stmt.close();
					conn.close();
					r.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// if dpjm is not null, assign the given value to DeletePeriodicJobGraph field
		if ( dpjm != null) dpj = dpjm;
		return instance;
	}

	/** Pushes a job into the queue of <code>NmapJob</code>s and possibly inserts it in
	 *  "NmapJobs" table of the database.
	 * 	
	 * 	@param 	job	the NmapJob to be pushed into the Queue of NmapJobs. If job isn't
	 * 				indicating the termination of a SoftwareAgent or the termination of a
	 * 				running periodic job, job is inserted in "NmapJobs" table of the database.
	 */
	public void push(NmapJob job) {
		synchronized (myQueue) {
			// only add the job to the queue of jobs if
			// it's a terminating one
			myQueue.add(job);
			if (job.getParameters().equals("Stop")) return;
			if (job.getId() == -1) return;
			// if not, connect to database and insert
			// it in NmapJobs table.
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
				String query = "insert into NmapJobs (Id,Command,PeriodicFlag,Time,HashKey,Sent)"
						+ " values ("+job.getId()+",\""+job.getParameters()+"\","+job.isPeriodical()
						+",\""+job.getTime()+"\",\""+job.getHashKey()+ "\","+job.isSent() + ")";
				stmt.executeUpdate(query);
			} catch (SQLException es) {
				es.printStackTrace();
			}
			finally{
				// closing connection and statement
				try {
					stmt.close();
					conn.close();
				} catch (SQLException esq) {
					esq.printStackTrace();
				}
			}
			// updating counter for NmapJobs
			JobQueue.counter ++ ;
		}
	}

	/** Removes the job with "id" from the queue of <code>NmapJob</code>s.
	 * @param 	id	jobId of the job to be removed.	
	 */
	@SuppressWarnings("rawtypes")
	public void deleteJobById(int id){
		synchronized(myQueue){
			for (NmapJob job : myQueue){
				if (job.getId()==id) {
					myQueue.remove(job);
					if (dpj != null){
						DefaultListModel dlm = dpj.getModel();
						dlm.removeElement(job.toString());
					}
					return;
				}
			}
		}
	}

	
	/** Searches the queue of <code>NmapJob</code>s for jobs that should be sent to a specific Agent.
	 * 	Adds jobs to the list model of DeletePeriodicJobGraph if needed.
	 * 	
	 * 	@param	hashkey	hashkey of the Agent's jobs we are searching for.
	 * 	@return	A string that contains one JSON formatted NmapJob per line.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getJobsByHash(int hashkey){
		synchronized(myQueue){
			String output = new String();
			String s = new String();
			for (NmapJob job : myQueue){
				// if a job hasn't been sent to Agent with hashkey "hashkey"
				// create a line that contains the job in JSON format
				// and mark it as sent
				if (job.getHashKey() == hashkey && job.isSent() == false){
					Gson g = new Gson();
					s = g.toJson(job);
					output += s + "\n";
					job.setSent(true);
					// if this job is a periodic one but not a terminating one
					// update list model of DeletePeriodicJobGraph
					if (dpj != null && job.isPeriodical() && job.getParameters().equals("Stop") == false && job.getId() != -1){
						DefaultListModel dlm = dpj.getModel();
						dlm.addElement(job.toString());
					}
				}
			}
			return output;
		}
	}

	/**
	 * @return 	number of jobs that are currently in JobQueue and in "NmapJobs" table of the database
	 * 			without taking into consideration the ones that terminate an Agent and those about
	 * 			termination of a sent periodic thread. 
	 */
	public static int getCounter() {
		return counter;
	}

	/** Sets the counter of the existing <code>NmapJob</code>s (in JobQueue and in table "NmapJobs" of the database)
	 * 	to the given value.
	 * 	@param	counter	counter of the <code>NmapJob</code>s.
	 */
	public static void setCounter(int counter) {
		JobQueue.counter = counter;
	}

	/** Returns the NmapJob with jobId "id".
	 * 	@param	id	the id of the <code>NmapJob</code>.
	 * 	@return	the job with id equal to the given one or null if this id isn't describing one the existing jobs.
	 */
	public NmapJob getJobById(int id){
		synchronized(myQueue){
			for (NmapJob j : myQueue){
				if (j.getId() == id){
					return j;
				}
			}
			return null;
		}
	}

	/**
	 * @return 	a String array containing the periodic jobs that have been sent (excluding the terminating ones)
	 * 			or null if there are not.
	 */
	public String[] getPeriodicJobs(){
		synchronized (myQueue){
			String s = new String();
			for (NmapJob job : myQueue){
				if (job.isPeriodical() && job.isSent() == true && job.getParameters().equals("Stop") == false && job.getId()!=-1) {
					s += job.toString() + "\n";
				}
			}
			if (s.split("\n")[0].equals("")) return null;
			else return s.split("\n");
		}
	}
	
	/**	
	 * 	@return A JSON array consisted of the periodic jobs that have been sent to Software Agents. 
	 */
	@SuppressWarnings("unchecked")
	public String JsonPeriodicals(){
		JSONArray list = new JSONArray();
		synchronized (myQueue){
			for (NmapJob job : myQueue){
				if (job.isPeriodical() && job.isSent() == true && job.getParameters().equals("Stop") == false && job.getId()!=-1) {
					list.add(job.JsonNmap());
				}
			}
		}
		return list.toString();
	}
	
	/**
	 * 	Removes the one-time <code>NmapJob</code>s that have been sent to Agent with
	 * 	the specified hashkey from the <code>JobQueue</code>.
	 * 
	 * 	@param	hashkey	the hashkey of the Agent/client.
	 */
	public void cleanSentOneTime(int hashkey){
		synchronized (myQueue){
			Iterator <NmapJob> it = myQueue.iterator();
			while(it.hasNext()){
				NmapJob job = it.next();
				if (job.getHashKey() == hashkey && job.isSent() == true && job.isPeriodical() == false) it.remove();
			}
		}
	}
	
	/**	Removes the periodic <code>NmapJob</code>s that have been sent to Agent
	 * 	with the specified hashkey from the <code>JobQueue</code>.
	 * 
	 * 	@param hashkey	the hashkey of the Agent/client.
	 */
	@SuppressWarnings("rawtypes")
	public void cleanSentPeriodic(int hashkey){
		synchronized(myQueue){
			Iterator <NmapJob> it = myQueue.iterator();
			while(it.hasNext()){
				NmapJob job = it.next();
				if (job.getHashKey() == hashkey && job.isSent() == true && job.isPeriodical() == true){
					if (dpj != null){
						DefaultListModel dlm = dpj.getModel();
						dlm.removeElement(job.toString());
					}
					it.remove();
				}
			}
		}
	}
}
