package sa.threads;

import java.util.Random;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import sa.queue.ResultQueue;
import sa.classes.Result;

/**
 * <code>SenderThread</code> wakes up once in a while, checks the <code>ResultQueue</code>
 * and sends to AM one result at a time until the queue is not empty.<br>
 * SenderThread sends each Result to the path "/result/<HashKey of this SA>/<this result's job ID>".
 * Stops execution when Ctrl-C is recieved (static flag gets false) or thread gets interrupted or
 * after AM's command.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 *
 */
public class SenderThread implements Runnable {
	
	/**
	 * A flag used for the SenderThread termination.
	 */
	private static boolean flag = true;
	
	/**
	 * Maximum sleep time for SenderThread in seconds. 
	 */
	private static int time; 
	
	/**
	 * AM's path where sender thread sends results of this SA's jobs.
	 */
	private static String addr;
	
	/**
	 * HashKey for this SA.
	 */
	private static int hashkey;
	
	/**
	 * This client-SA.
	 */
	private static Client client;

	/** Constructor for <code>SenderThread</code>.
	 * @param addr		path to send results.
	 * @param hashkey	this SA's hashkey.
	 * @param client	this client-SA.
	 */
	public SenderThread(String addr,int hashkey, Client client){
		SenderThread.addr = addr;
		SenderThread.hashkey = hashkey;
		SenderThread.client = client;
	}
	
	/** Sets time to the given value.
	 * @param time max sleep time for <code>SenderThread</code> in seconds.
	 */
	public static void setTime(int time) {
		SenderThread.time = time;
	}

	/** Sets flag to the given value.
	 * @param flag 	when false due to receiving Ctrl-C or due to AM's order, 
	 * 				<code>SenderThread</code> stops execution.
	 */
	public static void setFlag(boolean flag) {
		SenderThread.flag = flag;
	}

	public void run() {
		System.out.println("I am the sender " + Thread.currentThread().getName());
		ResultQueue rq = ResultQueue.getInstance();
		Result r = null;
		ClientResponse response = null;
		WebResource webResource = null;
		// until flag gets false
		while (SenderThread.flag == true) {
			Random rn = new Random();
			// sender thread sleeps for random time (maximum time is read from property file)
			// and checks result queue when waking up
			int wt = rn.nextInt(SenderThread.time) + 1;
			try {
				Thread.sleep(wt*1000);
			} catch (InterruptedException e) {
				// thread got interrupted while sleeping
				// thread should exit.
				// re-interrupt this thread and return
				Thread.currentThread().interrupt();
				return;
			}
			String output = new String();
			while((r = rq.getTop()) != null){
			// gets the first result and ResultQueue and pops it if
			// it's successfully sent to server.	
				try{
					webResource = client.resource(addr + "/" + SenderThread.hashkey + "/" + r.getJobId());
					response = webResource.accept("text/plain").type("application/xml").post(ClientResponse.class, r.getOutput());
					output = response.getEntity(String.class);
				}catch (ClientHandlerException e1) {
					// handling server down case
					System.out.println("Server is probably down now");
					try {
						Thread.sleep(wt*1000);
					} catch (InterruptedException e) {
						// thread got interrupted while sleeping
						// thread should exit.
						// re-interrupt this thread and return
						Thread.currentThread().interrupt();
						return;
					}
					continue;
				}
				catch (NullPointerException e){
					// handling null pointer exception
					// response will be null if server is down for some reason.
					// SenderThread will try to send it until it's successfully sent.
					continue;
				}
				if (response.getStatus() == 200){
					// result was successfully sent
						System.out.println("Result sent to AM: " + response.getStatus() + " " + output);
						rq.pop();
				}
				// result was not send.
				// result doesn't get popped from the queue, sender thread will
				// try to send it again.
				else System.out.println("Couldn't send result to AM: " + response.getStatus() + " " + output);
			}
		}
	}
}
