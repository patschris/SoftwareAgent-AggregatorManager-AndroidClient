package sa.queue;

import java.util.LinkedList;
import java.util.Queue;

import sa.classes.Result;


/** Results of executed nmap commands. When a <code>ResultQueue</code> method
 * is called, <code>ResultQueue</code> gets locked since it is accessed by many threads.
 * Singleton class since only one instance of this is needed. 
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class ResultQueue {

	/**
	 * A static queue containing the results of executed nmap commands.
	 */
	static Queue <Result>  rq = new LinkedList<Result>();
	
	/**
	 * The instance of this class. 
	 */
	private static ResultQueue instance = null;

	/** Creates an instance of this class if not created yet. Synchronized since many threads
	 * call this method.
	 * @return	the instance of a <code>ResultQueue</code>.
	 */
	public static synchronized ResultQueue getInstance(){
		if (instance == null){
			instance = new ResultQueue();
		}
		return instance;
	}

	/** Pushes a <code>Result</code> to the queue of executed commands.
	 * @param r the <code>Result</code> to be pushed.
	 */
	public void push(Result r) {
		synchronized(rq){
			rq.add(r);
		}
	}

	/** Prints and removes the head of the results queue as long as it's not empty.
	 * 
	 */
	public void print(){
		synchronized (rq) {
			while(rq.isEmpty() == false) {
				System.out.println(rq.peek().toString());
				rq.remove();
			}
		}
	}

	/**
	 * @return the head of the queue containing the <code>Result</code>s or null if it's empty.
	 */
	public Result getTop(){
		synchronized (rq){
			Result r = null;
			if (rq.isEmpty() == false) r = rq.peek();
			return r;
		}
	}
	
	/**
	 * Removes the first <code>Result</code> in the queue if it's not empty.
	 */
	public void pop(){
		synchronized (rq){
			if (rq.isEmpty() == false) rq.remove();
		}
	}
}