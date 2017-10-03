package am.structures;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

import am.classes.Result;
import am.resources.DbConnection;


/**	<code>ResultQueue</code> is a singleton class constisted of a queue that contains
 * 	<code>Result</code>s of executed nmap commands.
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class ResultQueue {
	/**
	 * 	A queue that contains <code>Result</code>s of executed nmap commands.
	 */
	private static Queue <Result> resultQueue = new LinkedList<Result>();
	
	/**
	 *	The instance of this class. 
	 */
	private static ResultQueue instance = null ;

	/** Constructs a <code>ResultQueue</code> if not created yet, or returns the instance of it.
	 * 	
	 * 	@return	the instance of the <code>ResultQueue</code>.
	 */
	public static ResultQueue getInstance(){
		synchronized(resultQueue){
			if (instance == null){
				instance = new ResultQueue();
			}
			return instance;
		}
	}

	/** Pushes a <code>Result</code> into the queue of <code>Result</code>s and inserts it
	 * 	in "Results" table of the database.
	 * 	
	 * 	@param	r	the <code>Result</code> to be pushed and insterted in database.
	 */
	public void push(Result r) {
		synchronized (resultQueue){
			// pushes the result into the resultQueue
			resultQueue.add(r);
			// connecting to database and inserts it
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
				String query = "insert into Results (Output,Timestamp,HashKey,JobId)"
						+ " values (\""+r.getResult().toString()+"\",\""+r.getCurDate().toString()
						+"\","+r.getHashkey()+","+r.getJobid()+ ")";
				stmt.executeUpdate(query);

			} catch (SQLException es) {
				es.printStackTrace();
			}
			finally{
				// closing open connection and statement
				try {
					stmt.close();
					conn.close();
				} catch (SQLException esq) {
					esq.printStackTrace();
				}
			}
		}
	}

	/**	Removes a <code>Result</code> from the queue of <code>Result</code>s.
	 * 	
	 * 	@param r	the <code>Result</code> to be removed.
	 */
	public void pop(Result r) {
		synchronized(resultQueue){
			resultQueue.remove(r);
		}
	}

	/**	Returns a String containing the <code>Result</code>s of executed
	 * 	nmap commands that were sent from SoftwareAgents/clients
	 * 	to AggregatorManager during the last "time" seconds or since
	 * 	AggregatorManager started running.
	 * 	
	 * 	@param	time	seconds passed describing the results we are looking for.
	 * 					if equals to 0(zero), results since AM started running are asked.
	 * 	
	 * 	@return	a String containing the results received by every
	 * 			Agent/client during the last "time" seconds
	 * 			or since AM started running.
	 */
	public String getAllResults(int time){
		synchronized(resultQueue){
			String s = new String();
			if (time == 0){
				// if time is 0, fetch all the received results
				for (Result r : resultQueue ){
					s += r.toString();
				}
			}
			else{
				long curtime = System.currentTimeMillis();
				// else fetch the results during the last "time" seconds
				for (Result r : resultQueue){
					if ((int) (curtime - r.getCurTime())/1000 < time){
						s += r.toString();
					}
				}
			}
			return s;
		}
	}

	/** Returns a string containing the results for nmap commands	
	 * 	executed by a specific Agent/client, during the last "time"
	 * 	seconds or since the Agent registered to the AM.
	 * 
	 * 	@param	hashkey	hashkey describing the Agent/client whose
	 * 					results we are looking for.
	 * 	@param	time	time in seconds describing the results we are
	 * 					looking for. If equals to 0(zero) results
	 * 					since AM started running are asked.
	 *	
	 *	@return	a String containing the results received by 
	 * 			Agent/client with Hashkey "hashkey"
	 * 			during the last "time" seconds
	 * 			or since AM started running.
	 */
	public String getResultsByHash(int hashkey, int time){
		synchronized(resultQueue){
			String s = new String();
			if (time == 0){
				// if time is 0, fetch all the received results
				// from Agent with HashKey equal to hashkey
				for (Result r : resultQueue){
					if (r.getHashkey() == hashkey) s += r.toString();
				}
			}
			else{
				// else fetch the results during the last "time" seconds
				// from Agent with HashKey equal to hashkey
				long curtime = System.currentTimeMillis();
				for (Result r : resultQueue){
					if ((int) (curtime - r.getCurTime())/1000 < time && r.getHashkey() == hashkey){
						s += r.toString();
					}
				}
			}
			return s;
		}
	}
}