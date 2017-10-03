package am.services.agent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import am.classes.Registration;
import am.resources.DbConnection;
import am.structures.JobQueue;
import am.structures.RegisteredAgents;

/**
 *  <code>RequestService</code> is a resource class under the path "/request" accessed
 * 	by registered <code>SoftwareAgent</code>s when they want to received the jobs that
 * 	user made for them.
 *  
 * 	@author	C. Patsouras I. Venieris
 *	@version 1
 */
@Path("request")
public class RequestService {

	/**
	 *  <code>sendJobs</code> is a GET method under the path "request/<<code>SoftwareAgent</code>'s hashkey>"
	 * 	accessed by registered <code>SoftwareAgent</code>s when they want to received the jobs that 
	 *  user made for them.
	 * 
	 * @param hashkey  path parameter equal to the hashkey of the Agent/client that is sending request.
	 * @return a json string with the jobs and code 200 if there are jobs for this agent. 
	 * 			Otherwise, sends code 403.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{hashkey}")
	public Response sendJobs(@PathParam("hashkey") int hashkey) {
		RegisteredAgents ra = RegisteredAgents.getInstance(null,null,null);
		Registration r = ra.findAgent(hashkey);
		Registration regi=null;
		if (r == null) {
			DbConnection dbc = DbConnection.getInstance();
			Connection conn = null;
			Statement stmt = null;
			Statement stmt2=null;
			ResultSet rs = null ;
			try {
				Class.forName(dbc.getDriver());
			} catch (ClassNotFoundException ec) {
				ec.printStackTrace();
			}
			try {
				conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
				stmt = conn.createStatement();
				String query = "select * from Agents where HashKey=\""+hashkey+"\";";
				rs = stmt.executeQuery(query);
				while(rs.next()){
					regi = new Registration();
					regi.setAb(new AcceptBlocker());
					regi.setDeviceName(rs.getString("DeviceName"));
					regi.setHashKey(Integer.toString(hashkey));
					regi.setInterfaceIP( rs.getString("IpAddress"));
					regi.setInterfaceMAC(rs.getString("MacAddress"));
					regi.setOsVersion(rs.getString("OsVersion"));
					regi.setNmapVersion(rs.getString("NmapVersion"));
					regi.setOnline(true);
					regi.setLastRequest(System.currentTimeMillis());
					ra.push(regi);
					stmt2=conn.createStatement();
					String q = "update Agents set Online=true where Hashkey=\""+hashkey+"\";";
					stmt2.executeUpdate(q);
				}
			} catch (SQLException es) {
				es.printStackTrace();
			}
			finally{
				try {
					stmt.close();
					stmt2.close();
					conn.close();
					rs.close();
				} catch (SQLException esq) {
					esq.printStackTrace();
				}
			}
		}
		else {
			ra.makeOnline(r);
		}
		Response res = null;
		JobQueue q = JobQueue.getInstance(null);
		String s = q.getJobsByHash(hashkey);
		q.cleanSentOneTime(hashkey);
		if (s.isEmpty() == false){
			if(s.contains("exit(0)")){
				if (r!=null) ra.pop(r);
				else ra.pop(regi);
			}
			res = Response.status(200).entity(s).build();
		}
		else{
			res = Response.status(403).entity("no jobs for you").build();
		}
		return res;
	}
}