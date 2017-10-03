package sa.queue;

import java.util.LinkedList;
import java.util.Queue;

/** <code>ProcessQueue</code> is a singleton class consisted of a Queue 
 * 	that contains all the currently running nmap processes.
 * 	Locks the queue once a method of this class is called since many threads have access
 * 	on its methods. Used by <code>ExitingThread</code> for quick termination of the client. 
 * 	
 * 	@author C. Patsouras I. Venieris
 *	@version 2
 */
public class ProcessQueue {

	//http://crunchify.com/how-to-create-singleton-queue-global-object-for-fifo-first-in-first-out-in-java/
	/**
	 * A static queue containing all the currently running nmap processes.
	 */
	static Queue <Process> myQueue = new LinkedList <Process>();
	
	/**
	 * The instance of this class. 
	 */
	private static ProcessQueue instance = null ;


	/** Creates an instance of this class if not created yet. Synchronized since many threads
	 * call this method.
	 * @return	the instance of a <code>ProcessQueue</code>.
	 */
	public static synchronized ProcessQueue getInstance(){
		if (instance == null){
			instance = new ProcessQueue();
		}
		return instance;
	}

	/**
	 * @return the process on the head of the queue or null if it's empty.
	 */
	public Process getTop(){
		synchronized(myQueue){
			if (myQueue.isEmpty() == false) return myQueue.peek();
			else return null;
		}
	}
	
	/** Pushes a process to the queue of running processes.
	 * @param p	the process to be pushed.
	 */
	public void push(Process p){
		synchronized (myQueue) {
			myQueue.add(p);
		}
	}
	
	/** Removes a specific process from the queue of running processes.
	 * @param p	the process to be removed.
	 */
	public void remove(Process p){
		synchronized(myQueue){
			myQueue.remove(p);
		}
	}
	
	/**
	 * Removes the head of the queue of the running processes.
	 */
	public void pop(){
		synchronized (myQueue){
			myQueue.remove();
		}
	}
}