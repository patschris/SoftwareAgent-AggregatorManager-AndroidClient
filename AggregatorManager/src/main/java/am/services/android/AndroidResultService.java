package am.services.android;

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

import org.json.simple.JSONArray;

import am.classes.AndroidResults;
import am.resources.DbConnection;

/**	
 *  <code>AndroidResultService</code> is a resource class under the path "/androidresult" accessed
 * 	by registered Android Clients when they want to take specific results from executed commands.
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("androidresult")
public class AndroidResultService {
	/**
	 *  <code>handleResults</code> is a GET method under the path
	 * 	"androidresult/<<code>SoftwareAgent</code>'s hashkey>/<number of results>>"
	 * 	where Android Clients ask for a specific number of results.
	 * 
	 * @param hashkey path parameter equal to the hashkey of the Agent/client whose results we want to take.
	 * @param number path parameter number of results we want to take. 
	 * 
	 * @return code 500 in sqlexception, otherwise code 200.
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{hashkey}/{number}")
	public Response handleResults(@PathParam("hashkey") String hashkey, @PathParam("number") int number){
		Response res=null;
		String query="select r.JobId, r.Output, r.Timestamp, r.Hashkey, n.Command "
				+ "from NmapJobs as n,Results as r where (n.Id=r.JobId";
		if (hashkey.equals("All")==false) {
			int hash = Integer.parseInt(hashkey);
			query+=" and r.HashKey=\""+hash+"\"";
		}
		query+=") order by r.ResultId desc limit "+number+";";
		DbConnection dbc = DbConnection.getInstance();
		Connection conn = null;
		Statement stmt = null;
		ResultSet r = null ;
		try {
			Class.forName(dbc.getDriver());
		} catch (ClassNotFoundException ec) {
			ec.printStackTrace();
			res = Response.status(500).build();
		}
		try {
			conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
			stmt = conn.createStatement();
			r = stmt.executeQuery(query);
			JSONArray list = new JSONArray();
			while (r.next()) {
				int id = r.getInt("JobId");
				String output = r.getString("Output");
				String timestamp = r.getString("Timestamp");
				int key = r.getInt("Hashkey");
				String command = r.getString("Command");
				AndroidResults ar = new AndroidResults(id,output,timestamp,key,command);
				list.add(ar.JsonAndroidResults());
			}
			res = Response.status(200).entity(list.toString()).build();
		} catch (SQLException es) {
			es.printStackTrace();
			res = Response.status(500).build();
		}
		finally{
			try {
				stmt.close();
				conn.close();
				r.close();
			} catch (SQLException esq) {
				esq.printStackTrace();
			}
		}
		return res;
	}
}