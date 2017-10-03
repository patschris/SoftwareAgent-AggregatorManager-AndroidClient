package sa.queue;

import java.util.LinkedList;
import java.util.Queue;

import sa.classes.NmapJob;

/** JobQueue is a singleton class consisted of a Queue that contains <code>NmapJob</code>s
 * to be executed by <code>OneTimeThread</code>s.
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class JobQueue {
	
	//http://crunchify.com/how-to-create-singleton-queue-global-object-for-fifo-first-in-first-out-in-java/
	/**
	 * A static queue containing the <code>NmapJob</code>s to be executed.
	 */
	static Queue <NmapJob> myQueue = new LinkedList<NmapJob>();
	
	/**
	 * The instance of this class. 
	 */
	private static JobQueue instance = null ;
	
	
	/** Creates an instance of this class if not created yet. Synchronized since many threads
	 * call this method.
	 * @return the instance of a <code>JobQueue</code>.
	 */
	public static synchronized JobQueue getInstance(){
		if (instance == null){
			instance = new JobQueue();
		}
		return instance;
	}
	
	/** Pushes a NmapJob in the JobQueue to be popped by some <code>OneTimeThread</code>s.
	 * Synchronized since many threads call this method. Notifies the threads waiting
	 * on pop to receive a job.
	 * @param job	the <code>NmapJob</code> to be pushed into <code>JobQueue</code>.
	 */
	public void push(NmapJob job) {
		synchronized (myQueue) {
			myQueue.add(job);
			myQueue.notify();
			}
	}
	
	/** Pops the first job when then JobQueue is not empty.
	 * 	Every <code>OneTimeThread</code> waits on this <code>JobQueue</code> 
	 * while it's empty and gets notified if a new job is pushed in the queue.
	 * @return	The first <code>NmapJob</code> in <code>JobQueue</code> or null
	 * 			if a thread was interrupted while waiting (during SA's termination).
	 */
	public NmapJob pop(){
		synchronized(myQueue){
			while(myQueue.isEmpty()){
				try {
					myQueue.wait();
				} catch (InterruptedException e) {
					return null;
				}
			}
			NmapJob n = myQueue.peek();
			myQueue.remove();
			System.out.println(Thread.currentThread().getName() + " took job " + n.getId());
			return n;
		}
		
	}
}