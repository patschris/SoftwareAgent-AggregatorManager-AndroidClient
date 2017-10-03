package am.services.android;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import am.resources.DbConnection;
/**  
*   <code>AndroidRegistrationService</code> is a resource class under the path "/androidregister"
*   accessed by registered Android Clients who wish to create a new account.
* 
* 	@author		C. Patsouras I. Venieris
*	@version 	1
*/
@Path("androidregister")
public class AndroidRegistrationService {
	/**
	 * 
	 * @param input String formatted JSON object that contains username and password of the new account. 
	 * @return Account creates successfully: 200
	 * 		   Parse exception: 500 
	 * 		   Already used username: 403
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response handleRegistration(String input) {
		int counter = 0;
		Response res = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs=null;
		try {
			JSONObject j = (JSONObject) new JSONParser().parse(input);
			DbConnection dbc = DbConnection.getInstance();
			String query = "select count(*) as c from AndroidClients where Username=\""+(String)j.get("username")+"\";";
			conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
			stmt = conn.createStatement();
			rs=stmt.executeQuery(query);
			while(rs.next()){
				counter = rs.getInt("c");
			}
			if (counter == 0){
				res = Response.status(200).build();
				query = "insert into AndroidClients (Username,Password) values (\""+(String) j.get("username")+
						"\", \"" + (String) j.get("password") + "\");";
				stmt.executeUpdate(query);
			}
			else {
				/* already used username */
				res = Response.status(403).build();
			}
		} catch (ParseException e) {
			res = Response.status(500).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				stmt.close();
				conn.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
}