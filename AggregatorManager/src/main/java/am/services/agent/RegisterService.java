package am.services.agent;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import am.classes.Registration;
import am.structures.PendingQueue;
import am.structures.RegisteredAgents;

/**
 *  <code>RegisterService</code> is a resource class under the path "/register" accessed
 * 	by unregistered <code>SoftwareAgent</code>s that want to register.
 * 
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
@Path("register")
public class RegisterService {
	/**
	 *  <code>handleRegister</code> is a POST method under the path "register" where Agents 
	 * 	send all necessary information in order to register to Aggregator Manager.
	 * 
	 *  @param input JSON string that represents the necessary information.
	 *  @return response to Agent/client that just sent the registration request.
	 *  		Accept code: 200
	 *  		Deny code: 403
	 *  		Parse exception code: 500
	 */	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response handleRegister(String input){
		AcceptBlocker ab = new AcceptBlocker();
		PendingQueue pa = PendingQueue.getInstance(null);
		RegisteredAgents ra = RegisteredAgents.getInstance(null, null, null);	
		String output = "Welcome";
		Response res = null;
		try {
			JSONObject j = (JSONObject) new JSONParser().parse(input);
			Registration reg = new Registration();
			reg.setDeviceName((String) j.get("deviceName"));
			reg.setInterfaceIP((String) j.get("interfaceIP"));
			reg.setInterfaceMAC((String) j.get("interfaceMAC"));
			reg.setNmapVersion((String) j.get("nmapVersion"));
			reg.setOsVersion((String) j.get("OsVersion"));
			reg.setHashKey(j.get("HashKey").toString());
			reg.setAb(ab);
			pa.push(reg);
			boolean acceptBlocker = ab.getAccept(); // blocks the service until button is pressed
			if (acceptBlocker == true){
				ra.push(reg);
				res = Response.status(200).entity(output).build();
			}
			else {
				res = Response.status(403).build();
			}
		} catch (ParseException e) {
			res = Response.status(500).build();
		}
		return res;
	}
}