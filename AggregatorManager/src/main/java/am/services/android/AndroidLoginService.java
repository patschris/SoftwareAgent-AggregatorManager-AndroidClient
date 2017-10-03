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

import am.classes.AndroidLogin;
import am.resources.DbConnection;
import am.services.agent.AcceptBlocker;
import am.structures.AndroidQueue;

/**  
*  <code>AndroidKillAgentService</code> is a resource class under the path "/androidkillagent" accessed
*  by registered Android Clients that want to terminate the execution of a <code>Software Agent</code>.
* 
* 	@author		C. Patsouras I. Venieris
*	@version 	1
*/
@Path("androidlogin")
public class AndroidLoginService {
	/**
	 *  <code>handleLogin</code> is a POST method where Android Clients send their credentials
	 *  in order to login to the app.
	 * 	
	 * @param input String formatted JSON Object that contains device name, username and password.
	 * @return Successful login/User accepted: 200
	 * 		   Wrong password: 400
	 * 		   Unregistered user: 401
	 * 		   User denied: 403
	 * 		   User already logged in: 409
	 * 		   Parse exception: 500
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response handleLogin(String input){
		Response res = null; 
		String output = "Welcome";
		try {
			JSONObject j = (JSONObject) new JSONParser().parse(input);
			DbConnection dbc = DbConnection.getInstance();
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs=null;
			int counter = 0;
			String pass = new String();
			boolean isactive = false;
			try {
				Class.forName(dbc.getDriver());
			} catch (ClassNotFoundException ec) {
				ec.printStackTrace();
			}
			try {
				// inserting the Agent into the database
				conn = DriverManager.getConnection(dbc.getUrl(),dbc.getUsername(),dbc.getPassword());
				stmt = conn.createStatement();
				String query = "select Active,Password from AndroidClients where Username=\""+(String)j.get("username")+"\";";
				rs=stmt.executeQuery(query);
				counter = 0 ;
				while(rs.next()){
					counter++;
					isactive = rs.getBoolean("Active");
					pass = rs.getString("Password");
				}
				
			} catch (SQLException es) {
				es.printStackTrace();
			}
			finally{
				try {
					// closing statement and connection
					stmt.close();
					conn.close();
					rs.close();
				} catch (SQLException esq) {
					esq.printStackTrace();
				}
			}
			if (counter == 0){
				//user not found, must register first
				res = Response.status(401).build();
				
			}
			else{
				if (pass.equals((String)j.get("password")) == false){
					//user typed wrong password
					res = Response.status(400).build();
				}
				else{
					if(isactive == false){
						AcceptBlocker ab = new AcceptBlocker();
						AndroidLogin al = new AndroidLogin((String)j.get("device"),(String)j.get("username"),(String)j.get("password"), ab);
						AndroidQueue aq = AndroidQueue.getInstance(null);
						aq.push(al);
						boolean acceptBlocker = ab.getAccept();
						if (acceptBlocker == true){
							// user accepted
							res = Response.status(200).entity(output).build();
						}
						else{
							//user denied
							res = Response.status(403).build();
						}
					}
					else{
						// user has already logged in
						res = Response.status(409).build();
					}
				}
			}
		} catch (ParseException e) {
			res = Response.status(500).build();
		}
		return res;
	}
}