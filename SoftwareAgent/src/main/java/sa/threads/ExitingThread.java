package sa.threads;

import com.sun.jersey.api.client.Client;

import sa.queue.ProcessQueue;
import sa.queue.ThreadQueue;

/** ExitingThread is called when AM notifies SA to terminate its execution.
 * 	Destroys the currently running processes, interrupts the running threads
 * 	and destroys this client.
 * 
 *  @author C. Patsouras I. Venieris
 *	@version 2
 */
public class ExitingThread implements Runnable {
	/**
	 * The client to be destroyed/this client.
	 */
	private Client client;
	
	/** Constructor of the <code>ExitingThread</code>.
	 * @param client	this client - client to be destroyed.
	 */
	public ExitingThread(Client client){
		this.client = client;
	}
	

	public void run(){
		ThreadQueue tq = ThreadQueue.getInstance();
		ProcessQueue pq = ProcessQueue.getInstance();
		System.out.println("AM stopping SA...");
		OneTimeThread.setFlag(false);
		SenderThread.setFlag(false);
		PeriodicThread.setFlag(false);
		Process p = null;
		while ((p = pq.getTop()) != null){
			// for every currently running process,destroy it
			// and pop from the ProcessQueue.
			p.destroy();
			pq.pop();
		}
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
		client.destroy();
	}
}
