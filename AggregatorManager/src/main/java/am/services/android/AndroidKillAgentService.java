package am.services.android;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import am.classes.NmapJob;
import am.structures.JobQueue;

/**  
*  <code>AndroidKillAgentService</code> is a resource class under the path "/androidkillagent" accessed
*  by registered Android Clients that want to terminate the execution of a <code>Software Agent</code>.
* 
* 	@author		C. Patsouras I. Venieris
*	@version 	1
*/
@Path("androidkillagent")
public class AndroidKillAgentService {
	/**
	 * @param hashkey path parameter equal to the hashkey of the <code>Software Agent</code> that
	 *        Android Client wants to terminate.
	 * @return 200 if service was done.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{hashkey}")
	public Response handleKillAgent(@PathParam("hashkey") int hashkey){
		JobQueue jq = JobQueue.getInstance(null);
		jq.push(new NmapJob(-1,"exit(0)",true,-1,hashkey));
		return Response.status(200).build();
	}
}