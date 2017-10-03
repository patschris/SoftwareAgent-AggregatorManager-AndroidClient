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
 *  <code>AndroidRemainingJobsService</code> is a resource class under the path 
 *  "/androidremainingjobs" accessed by registered Android Clients to send all newly 
 *  created jobs remained in sqlite database while there was no internet connection.
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("androidremainingjobs")
public class AndroidRemainingJobsService {
	/**
	 * @param input String formmatted JSON array that contains remaining jobs that remained in
	 *  	  sqlite database while there was no internet connection.
	 * @return 200 if jobs sent successfully.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response handleRemainingJobs(String input){
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(input);
		JsonArray ar = el.getAsJsonArray();
		JobQueue jq = JobQueue.getInstance(null);
		for (int i = 0 ; i< ar.size(); i++){
			JsonObject ob =  ar.get(i).getAsJsonObject();
			String command = ob.get("command").getAsString();
			int hashkey = ob.get("hashkey").getAsInt();
			int time = ob.get("time").getAsInt();
			boolean periodic = ob.get("periodic").getAsBoolean();
			jq.push(new NmapJob(0, command, periodic, time, hashkey));
		}
		return Response.status(200).build();
	}
}