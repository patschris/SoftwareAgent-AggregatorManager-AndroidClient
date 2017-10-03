package am.services.agent;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import am.parsing.NmapParser;

/**	
 *  <code>ResultService</code> is a resource class under the path "/result" accessed
 * 	by registered <code>SoftwareAgent</code>s when sending XML results of nmap jobs
 * 	that were executed by them.
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("result")
public class ResultService {
	/**	
	 *  <code>receiveResult</code> is a POST method under the path
	 * 	"result/<<code>SoftwareAgent</code>'s hashkey>/<<code>NmapJob</code>'s id>"
	 * 	where Agents that have been granted access by <code>AggregatorManager</code> send
	 * 	XML output of nmap jobs that were executed by them.
	 * 	
	 * 	@param	hashkey path parameter equal to the hashkey of the Agent/client
	 * 					that is sending a nmap job XML output.
	 * 	
	 * 	@param	jobid	path parameter equal to the job identifier of which the result
	 * 					is been received.
	 * 	
	 * 	@param	result	the XML output of the executed <code>NmapJob</code> with id
	 * 					equal to "/result/../<<code>NmapJob</code>'s id>".
	 * 	
	 * 	@return response to Agent/client that just sent a nmap job result. 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{hashkey}/{jobid}")
	public Response receiveResult(@PathParam("hashkey") int hashkey, @PathParam("jobid") int jobid, String result){
		Response res = null;
		String response = new String();
		// pass as arguments to NmapParser the xml output of the job
		// hashkey of the agent that executed it and its identifier
		// if parsing was successful and no exception occurred
		if (NmapParser.parse(result,hashkey,jobid)){
			response = "Recieved result for job " + jobid;
			res = Response.status(200).entity(response).build();
		}
		else{
			// if not, send appropriate answer to agent/client
			response = "There was an error receiving result for job " + jobid;
			res = Response.status(500).entity(response).build();
		}
		// send response to client
		return res;
	}
}
