package am.services.android;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import am.resources.DbConnection;

/**  
*  <code>AndroidLogoutService</code> is a resource class under the path "/androidlogout" accessed
*  by registered Android Clients that to logout.
* 
* 	@author		C. Patsouras I. Venieris
*	@version 	1
*/
@Path("androidlogout")
public class AndroidLogoutService {
	/**
	 *  <code>handleLogout</code> is a GET method under the path "androidlogout/<Android Client's username>"
	 *  that makes an Android Client inactive. 	
	 *
	 * @param username Android Client's username who wants to logout.
	 * @return 200 for successful logout or 500 for SQL exception.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{username}")
	public Response handleLogout(@PathParam("username") String username){
		Response res=null;
		DbConnection dbc = DbConnection.getInstance();
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(dbc.getDriver());
		} catch (ClassNotFoundException ec) {
			ec.printStackTrace();
			res = Response.status(500).build();
		}
		try {
			conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
			stmt = conn.createStatement();
			String query = "update AndroidClients set Active=false where Username=\""+username+"\";";
			stmt.executeUpdate(query);
			res = Response.status(200).build();
		} catch (SQLException es) {
			es.printStackTrace();
			res = Response.status(500).build();
		}
		finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException esq) {
				esq.printStackTrace();
			}
		}
		return res;
	}
}