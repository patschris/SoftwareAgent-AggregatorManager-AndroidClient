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
 *  <code>AndroidRemainingAgentsService</code> is a resource class under the path 
 *  "/androidremainingagents" accessed by registered Android Clients to send all the agent 
 *  termination requests that were inserted in sqlite database while there was no internet connection.
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("androidremainingagents")
public class AndroidRemainingAgentsService {
	/** 
	 * @param input String formmatted JSON array that contains remaining requests for stopping an agent.
	 * @return 200 if agents stopped successfully.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response handleRemainingAgents(String input){
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(input);
		JsonArray ar = el.getAsJsonArray();
		JobQueue jq = JobQueue.getInstance(null);
		for (int i = 0 ; i< ar.size(); i++){
			JsonObject ob =  ar.get(i).getAsJsonObject();
			int hashkey = ob.get("hashkey").getAsInt();
			jq.push(new NmapJob(-1,"exit(0)",true,-1,hashkey));
		}
		return Response.status(200).build();
	}
}