package am.services.android;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import am.structures.RegisteredAgents;

/**
 *  <code>AndroidAgentsService</code> is a resource class under the path "/androidagents" accessed by
 * 	registered Android Clients that want to take the hashkey and the status of every <code>Software Agent</code>.
 * 
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("androidagents")
public class AndroidAgentsService {
	/**
	 * @return  a string formatted JSON array that contains hashkey and status (online/offline)
	 * 			of every agent as well as code 200.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response handleAgents (){
		RegisteredAgents ra = RegisteredAgents.getInstance(null, null, null);
		return Response.status(200).entity(ra.regsToJson()).build();
	}
}