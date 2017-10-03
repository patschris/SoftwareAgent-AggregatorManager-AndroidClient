package sa.threads;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import sa.queue.ThreadQueue;

/** ShutDownHook is called when Ctrl-C is given. Changes flags for threads, interrupt
 * them and join them.
 * !!! NOT NEEDED FOR PROJECT 2, ONLY AM WILL STOP A SA !!!
 * Exits for convenience for explicit termination of SA by one user.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class ShutDownHook implements Runnable {
	private String baseUrl;
	private int hashkey;
	private Client client;
	
	
	public ShutDownHook(String baseUrl, int hashkey, Client client) {
		this.baseUrl = baseUrl;
		this.hashkey = hashkey;
		this.client = client;
	}


	public void run() {
		ThreadQueue tq = ThreadQueue.getInstance();
		// set flags for thread termination to false					
		OneTimeThread.setFlag(false);
		SenderThread.setFlag(false);
		PeriodicThread.setFlag(false);
		while (tq.emptyQueue() == false) {
			// for every thread, interrupt it,join and pop from queue
			// interrupting or joining a zombie thread does no harm
			try {
				tq.getTop().interrupt();	
				tq.getTop().join(); 
				System.out.println(tq.getTop().getName() + " joined");
				tq.pop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		WebResource webResource = client.resource(baseUrl + "/logout/" + hashkey);
		try{
		@SuppressWarnings("unused")
		ClientResponse res = webResource.accept("text/plain").get(ClientResponse.class);
		} catch (ClientHandlerException e1) {
			// handling server down case.		
		}
		client.destroy();
	}
}
