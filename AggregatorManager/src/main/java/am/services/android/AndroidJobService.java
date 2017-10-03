package am.services.android;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import am.classes.NmapJob;
import am.structures.JobQueue;

/**
 *  <code>AndroidJobService</code> is a resource class under the path "/androidjob" accessed
 * 	by registered Android Clients that want to send a new job to a  
 *  <code>Software Agent</code>.
 * 
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("androidjob")
public class AndroidJobService {
	
	/**
	 *
	 *  @param input JSON string that represents the received job.
	 * 	@return response to Agent/client that just sent the request.
	 *  		Success code: 200
	 *  		Parse exception code: 500
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public Response handleJobs (String input) {
		Response res = null;
		try {
			JSONObject j = (JSONObject) new JSONParser().parse(input);
			String command = (String) j.get("command");
			boolean periodic = Boolean.parseBoolean((String)j.get("periodic").toString());
			int time = Integer.parseInt((String)j.get("time").toString());
			int hashkey = Integer.parseInt((String)j.get("hashkey").toString());
			JobQueue jq = JobQueue.getInstance(null);
			jq.push(new NmapJob(0,command, periodic, time, hashkey));
			res = Response.status(200).build();
		} catch (ParseException e) {
			e.printStackTrace();
			res = Response.status(500).build();
		}
		return res;
	}
}