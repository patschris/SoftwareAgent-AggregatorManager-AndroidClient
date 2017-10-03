package sa.queue;

import java.util.LinkedList;
import java.util.Queue;


/** A queue containing all the created <code>Thread</code>s (main thread, OneTime threads,
 *  periodic threads and the <code>SenderThread</code>).<br> No need to synchronize, only
 *  main thread, <code>ShutdownHook</code> or <code>ExitingThread</code> calls 
 *  <code>ThreadQueue</code> methods after main thread's termination.
 *  Singleton since only one instance of this class is needed.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class ThreadQueue {

	/**
	 * A static queue of all the created threads. 
	 */
	static Queue <Thread> TQueue = new LinkedList<Thread>();
	
	/**
	 * The instance of this class.
	 */
	private static ThreadQueue instance = null;
	
	/**Creates an instance of this class if not created yet. Synchronized since many threads
	 * call this method.
	 * @return	the instance of <code>ThreadQueue</code>.
	 */
	public static synchronized ThreadQueue getInstance(){
		if (instance == null){
			instance = new ThreadQueue();
		}
		return instance;
	}

	/** Pushes a <code>Thread</code> to the queue of created threads.
	 * @param t the <code>Thread</code> to be pushed.
	 */
	public void push(Thread t) {
		TQueue.add(t) ;
	}

	/**
	 * Removes the head of the queue that contains the created threads.
	 */
	public void pop() {
		TQueue.remove();
	}

	/**
	 * @return	the head of the queue that contains the created threads.
	 */
	public Thread getTop() {
		return TQueue.peek();
	}

	/** Checks whether the queue of created threads is empty or not.
	 * @return true if the queue is empty, false if not so.
	 */
	public boolean emptyQueue(){
		return TQueue.isEmpty();
	}
}