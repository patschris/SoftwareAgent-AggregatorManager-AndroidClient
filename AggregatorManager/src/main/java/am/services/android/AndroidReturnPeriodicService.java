package am.services.android;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import am.structures.JobQueue;

/**	
 *  <code>AndroidReturnPeriodicService</code> is a resource class under the path "/androidreturnedperiodic"
 *  accessed by registered Android Clients in order to take all the periodic jobs running in registered
 *  <code>Software Agent</code>s.
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("androidreturnperiodic")
public class AndroidReturnPeriodicService {
	/**
	 * @return String formatted Json array that contains every periodic command. 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response handle(){
		Response res = null;
		JobQueue jq  = JobQueue.getInstance(null);
		String str =  jq.JsonPeriodicals(); //periodics
		res = Response.status(200).entity(str).build();
		return res;
	}
}