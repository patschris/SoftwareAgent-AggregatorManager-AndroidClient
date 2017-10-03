package am.structures;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import am.classes.Registration;
import am.gui.OutputGraph;
import am.gui.PeriodicMenu;
import am.gui.Status;
import am.resources.DbConnection;


/**	<code>RegisteredAgents</code> is a singleton class consisted of a list that contains <code>Registration</code>s
 * 	(SoftwareAgents/clients) that are accepted by AggregatorManager/server, and fields for <code>Status</code>,
 * 	<code>OutputGraph</code> and <code>PeriodicMenu</code> used for updating the appropriate GUI elements.
 * 
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class RegisteredAgents {

	//http://crunchify.com/how-to-create-singleton-queue-global-object-for-fifo-first-in-first-out-in-java/
	/**
	 * List containing the logged in SoftwareAgents/clients.
	 */
	private static LinkedList <Registration> agentList = new LinkedList<Registration>();
	
	/**
	 * The instance of this class.
	 */
	private static RegisteredAgents instance = null ;
	
	/**
	 * <code>Status</code> used for updating GUI's relative window when needed. 
	 */
	private static Status st = null;
	
	/**
	 * <code>PeriodicMenu</code> used for updating GUI's relative window when needed.
	 */
	private static PeriodicMenu pm = null;
	
	/**
	 * <code>OutputGraph</code> used for updating GUI's relative window when needed.
	 */
	private static OutputGraph og = null;


	/**	Constructs a <code>RegisteredAgents</code> if not created yet or returns
	 * 	the instance of <code>RegisteredAgents</code>. Updates <code>Status</code>,
	 * 	<code>PeriodicMenu</code> and <code>OutputGraph</code> to the given values
	 * 	if not null.
	 * 	@param	status	<code>Status</code> needed to update GUI's relative window.
	 * 	@param	perm	<code>PeriodicMenu</code> needed to update GUI's relative window.
	 * 	@param	outg	<code>OutputGraph</code> needed to update GUI's relative window.
	 * 	@return	the instance of this class.
	 */
	public static RegisteredAgents getInstance(Status status, PeriodicMenu perm, OutputGraph outg){
		synchronized (agentList){
			if (instance == null){
				instance = new RegisteredAgents();
			}
			// if status, perm or outg is not null, assign the given value(s)
			// to the appropriate field(s)
			if (status != null) st = status;
			if (perm != null) pm = perm;
			if (outg != null) og = outg;
			return instance;
		}
	}

	/** Pushes a <code>Registration</code> to the list of 
	 * 	<code>Registration</code>s/clients accepted by AggregatorManager/server
	 * 	and updates the appropriate GUI's window(s) if needed.
	 * 	@param	reg	the <code>Registration</code> to be pushed.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void push(Registration reg) {
		synchronized (agentList){
			agentList.add(reg);
			// if not null, update online list model of Status window.
			if (st != null){
				DefaultListModel dlm = st.getOnlineModel();
				dlm.addElement(reg.getname());
			}
			// if not null, update combo box model of PeriodicMenu
			if (pm != null){
				DefaultComboBoxModel dcm = pm.getAgentModel();
				dcm.addElement(reg.getname());
			}
			// if not null, update combo box model of OutputGraph
			if (og != null){
				DefaultComboBoxModel dcb = og.getAgentModel();
				dcb.addElement(reg.getname());
			}
		}
	}

	/**	Removes a <code>Registration</code> from the list of registered agents/clients
	 * 	and updates the appropriate GUI's windows if needed.
	 * 	Updates Agent's status in table "Agents" of the database.
	 * 	@param	r	the <code>Registration</code> to be removed.
	 */
	@SuppressWarnings("rawtypes")
	public void pop(Registration r){
		synchronized (agentList) {
			JobQueue jq = JobQueue.getInstance(null);
			jq.cleanSentPeriodic(r.getHashKey());
			agentList.remove(r);
			// if not null, update appropriate list model of Status window
			if (st != null){
				if (r.isOnline()){
					// if agent is online, remove from Status online list model
					DefaultListModel dlm = st.getOnlineModel();
					dlm.removeElement(r.getname());
				}
				else{
					// if agent is offline, remove from Status offline list model
					DefaultListModel dlm = st.getOfflineModel();
					dlm.removeElement(r.getname());
				}
			}
			// if not null, remove agent from PeriodicMenu combo box model
			if (pm != null){
				DefaultComboBoxModel dcm = pm.getAgentModel();
				dcm.removeElement(r.getname());
			}
			// if not null, remove agent from OutputGraph combo box model
			if (og != null){
				DefaultComboBoxModel dcb = og.getAgentModel();
				dcb.removeElement(r.getname());
			}
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
				String query = "update Agents set Online=false where Hashkey="+r.getHashKey();
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
		}
	}


	/**	Searches the list of <code>Registration</code>s/SoftwareAgents for
	 * 	a specific Agent.
	 * 
	 * 	@param	hashKey	hashkey of the Agent we are looking for.
	 * 	@return	the <code>Registration</code> with the given hashkey or
	 * 			null if there isn't one.
	 */
	public Registration findAgent(int hashKey) {
		synchronized (agentList) {
			for (Registration r : agentList) {
				if (r.getHashKey() == hashKey) return r;
			}
			return null;	
		}
	}

	/**	Modifies a <code>Registration</code>'s/agent's status if needed.
	 * 	An Agent is set to offline if he hasn't attempted a job request for
	 * 	a specific amount of time (3*<time defined in property file>).
	 * 	Updates the <code>Status</code> GUI's window list models.
	 * 
	 * 	@param	curTime	the current time, measured in milliseconds since the epoch.
	 * 	@param	period	seconds equal to 1/3 of the time that need to pass in order to
	 * 					consider an agent inactive if no job request has been sent during
	 * 					this time.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void modifyStatus(long curTime, int period){
		synchronized (agentList) {
			for (Registration r : agentList){
				// if an agent hasn't requested for new nmap jobs
				// for a specific amount of time, set it offline
				if ( (int) (curTime - r.getLastRequest())/1000 > 3*period){
					// if an agent is already offline, move to next one
					if (r.isOnline() == false) continue;
					r.setOnline(false);
					// otherwise, set it offline and update
					// Status list models if needed.
					if (st != null){
						DefaultListModel dlmon = st.getOnlineModel();
						DefaultListModel dlmoff = st.getOfflineModel();
						dlmon.removeElement(r.getname());
						dlmoff.addElement(r.getname());

					}
				}
			}
		}
	}

	/**	Searches for a specific <code>Registration</code>/agent sets its status to online
	 * 	and updates its last request for <code>NmapJob</code>s time (since epoch).
	 * 	Updates the <code>Status</code> list models if needed.
	 * 	
	 * 	@param	reg	the <code>Registration</code>/Agent we are looking for.
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void makeOnline(Registration reg) {
		synchronized (agentList) {
			for (Registration r : agentList) {
				if ( r == reg) {
					if (r.isOnline()) {
						// if this agent is already online
						// update its last request for jobs time
						r.setLastRequest(System.currentTimeMillis());
					}
					else {
						// otherwise mark it as online
						r.setOnline(true);
						r.setLastRequest(System.currentTimeMillis());
						// if not null, update Status list models
						if (st != null) {
							DefaultListModel dlmon = st.getOnlineModel();
							DefaultListModel dlmoff = st.getOfflineModel();
							dlmoff.removeElement(r.getname());
							dlmon.addElement(r.getname());
						}
					}
				}
			}
		}
	}

	/**
	 * 	@return a String array containing the currently online <code>Registration</code>s/Agents
	 * 			(their Hashkey numbers) or null if there aren't any.
	 */
	public String[] getOnlineRegs() {
		synchronized (agentList) {
			String s = new String();
			for (Registration r : agentList) {
				if (r.isOnline()) {
					s += "Software Agent " + r.getHashKey() + "\n";
				}
			}
			if (s.split("\n")[0].equals("")) return null;
			else return s.split("\n");
		}
	}

	/**
	 * 	@return	a String array containing the currently offline <code>Registration</code>s/Agents
	 * 			(their Hashkey numbers) or null if there aren't any.
	 */
	public String[] getOfflineRegs() {
		synchronized (agentList) {
			String s = new String();
			for (Registration r : agentList){
				if (r.isOnline() == false) {
					s += "Software Agent " + r.getHashKey() + "\n";
				}
			}
			if (s.split("\n")[0].equals("")) return null;
			else return s.split("\n");	
		}
	}

	/**
	 * 	@return a String array containing the agents that are registered (AM has approved
	 * 			their request for registration) regardless their online/offline status
	 * 			(their Hashkey numbers) or null if there aren't any.
	 */
	public String[] getRegs(){
		synchronized (agentList){
			String s = new String();
			for (Registration r: agentList){
				s += "Software Agent " + r.getHashKey() + "\n";
			}
			if (s.split("\n")[0].equals("")) return null;
			else return s.split("\n");
		}
	}
	
	/**
	 * @return 	A JSON formatted string (JSON Array) containing the logged in Software Agents
	 * 			(their hashkey number and online/offline status.
	 */
	@SuppressWarnings("unchecked")
	public String regsToJson() {
		JSONArray list = new JSONArray();
		synchronized (agentList){
			for (Registration r: agentList){
				JSONObject js = new JSONObject();
				js.put("hashkey",r.getHashKey());
				js.put("online",r.isOnline());
				list.add(js);
			}
		}
		return list.toString();
	}
}