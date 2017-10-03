package am.services.agent;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import am.classes.Registration;
import am.structures.RegisteredAgents;

/**	
 *  <code>LogoutService</code> is a resource class under the path "/logout" accessed
 * 	by registered <code>SoftwareAgent</code>s to inform that is going to stop his
 * 	execution.
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("logout")
public class LogoutService {
	/**
	 *  <code>handle</code> is a GET method under the path
	 * 	"logout/<<code>SoftwareAgent</code>'s hashkey>" to inform that is going to stop them execution.
	 * 	where registered Agents  
	 * 	
	 * @param hashkey path parameter equal to the hashkey of the Agent/client that wants to logout.
	 * @return response to Agent/client that just sent a logout request. Sends 200 if everything was ok, else sends 500. 
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{hashkey}")
	public Response handle(@PathParam("hashkey") int hashkey) {
		RegisteredAgents ra = RegisteredAgents.getInstance(null, null, null);
		Registration re = ra.findAgent(hashkey);
		Response res = null;
		if (re != null){
			ra.pop(re);
			res = Response.status(200).build();
		}
		else{
			res = Response.status(500).build();
		}
		return res;
	}
}