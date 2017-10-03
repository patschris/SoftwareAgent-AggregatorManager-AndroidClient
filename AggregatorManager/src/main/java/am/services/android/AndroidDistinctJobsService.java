package am.services.android;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import am.resources.DbConnection;

/**
 *  <code>AndroidDistinctJobsService</code> is a resource class under the path "/androiddistinctjobs"
 *  accessed by registered Android Clients that want to take every distinct job that was assigned to 
 *  every <code>Software Agent</code>.
 * 
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("androiddistinctjobs")
public class AndroidDistinctJobsService {
	/**
	 * @return  a string formatted JSON array that contains every distinct command is stored
	 * 			into the database.
	 * 			Code 200: success
	 * 			Code 500: SQL exception
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response handledistinctjobs() {
		Response res=null;
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
			String query = "select Command from NmapJobs group by Command;";
			r = stmt.executeQuery(query);
			JSONArray list = new JSONArray();
			while (r.next()) {
				String command = r.getString("Command");
				JSONObject obj=new JSONObject();
				obj.put("command",command);
				list.add(obj);
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
