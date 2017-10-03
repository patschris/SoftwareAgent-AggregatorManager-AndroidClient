package am.services.android;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import am.classes.NmapJob;
import am.structures.JobQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**	
 *  <code>AndroidRemainingPeriodicsService</code> is a resource class under the path 
 *  "/androidremainingperiodics" accessed by registered Android Clients to send all requests for 
 *  stopping a periodic job remained in sqlite database while there was no internet connection.
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("androidremainingperiodics")
public class AndroidRemainingPeriodicsService {
	/** 
	 * @param input String formatted JSON array that contains remaining requests for stopping an periodic job.
	 * @return 200 if periodic job stopped successfully.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response handleRemainingPeriodics(String input){
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(input);
		JsonArray ar = el.getAsJsonArray();
		JobQueue jq = JobQueue.getInstance(null);
		for (int i = 0 ; i< ar.size(); i++){
			JsonObject ob =  ar.get(i).getAsJsonObject();
			int hashkey = ob.get("hashkey").getAsInt();
			int jobid = ob.get("jobid").getAsInt();
			jq.deleteJobById(jobid);
			jq.push(new NmapJob(jobid,"Stop",true,0,hashkey));
		}
		return Response.status(200).build();
	}
}