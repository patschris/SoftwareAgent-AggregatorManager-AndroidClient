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
*  <code>AndroidStopPeriodicService</code> is a resource class under the path "/androidstopperiodic" accessed
*  by registered Android Clients that want to terminate the execution of a periodic job running on a 
*  <code>Software Agent</code>.
* 
* 	@author		C. Patsouras I. Venieris
*	@version 	1
*/
@Path("androidstopperiodic")
public class AndroidStopPeriodicService {
	/**
	 * @param jobid path parameter equal to id of the job that should be stopped.
	 * @param hashkey path parameter equal to the hashkey of the Agent/client who runs the job with above id.
	 * @return 200 if service was done.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{jobid}/{hashkey}")
	
	public Response handleKillAgent(@PathParam("jobid") int jobid, @PathParam("hashkey") int hashkey){
		JobQueue jq = JobQueue.getInstance(null);
		jq.deleteJobById(jobid);
		jq.push(new NmapJob(jobid,"Stop",true,0,hashkey));
		return Response.status(200).build();
	}
}
