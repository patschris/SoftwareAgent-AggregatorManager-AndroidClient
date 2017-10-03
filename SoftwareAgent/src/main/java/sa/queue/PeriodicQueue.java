package sa.queue;

import java.util.LinkedList;
import java.util.Queue;

import sa.threads.PeriodicThread;

/** PeriodicQueue is a singleton class consisted of a queue that contains
 * 	<code>PeriodicThread</code>s. Used when AM notifies SA to stop executing a periodic command.
 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class PeriodicQueue {

	/**
	 * A queue containing <code>PeriodicThread</code>s.
	 */
	static Queue <PeriodicThread> TQueue = new LinkedList<PeriodicThread>();
	
	/**
	 * The instance of this class.
	 */
	private static PeriodicQueue instance = null;
	
	/**Creates an instance of this class if not created yet. Synchronized since many threads
	 * call this method.
	 * @return 	the instance of <code>PeriodicQueue</code>.
	 */
	public static synchronized PeriodicQueue getInstance(){
		if (instance == null){
			instance = new PeriodicQueue();
		}
		return instance;
	}

	/** Pushes a <code>PeriodicThread</code> to the queue of created <code>PeriodicThread</code>s.
	 * @param t the <code>PeriodicThread</code> to be pushed.
	 */
	public void push(PeriodicThread t) {
		TQueue.add(t) ;
	}

	/**
	 * Removes the head of the queue that contains the created <code>PeriodicThread</code>s.
	 */
	public void pop() {
		TQueue.remove();
	}
	
	/** Returns the thread that represents the <code>PeriodicThread</code> executing a periodic nmap job. 
	 * @param id	the id of the executing periodic nmap job.
	 * @return		the thread executing periodic job with id.
	 */
	public Thread getThread(int id) {
		Thread t = null;
		for (PeriodicThread p : TQueue){
			if (p.checkId(id)){
				t = p.getThread();
				break;
			}
		}
		return t;
	}

	/** Checks whether the queue of created <code>PeriodicThread</code>s is empty or not.
	 * @return true if the queue is empty, false if not so.
	 */
	public boolean emptyQueue(){
		return TQueue.isEmpty();
	}
	
}
