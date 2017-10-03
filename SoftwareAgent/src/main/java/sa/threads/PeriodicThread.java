package sa.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sa.classes.NmapJob;
import sa.queue.ProcessQueue;
import sa.queue.ResultQueue;
import sa.classes.Result;


/**
 * <code>PeriodicThread</code>s are created to periodically execute a specific 
 * job - nmap command until AM informs SA to stop executing this specific command (when the
 * periodic thread is interrupted and exits.
 * The job to be executed is passed to <code>PeriodicThread</code>.<br> 
 * After executing a command, <code>PeriodicThread</code>s pass the XML output and jobId to
 * <code>ResultQueue</code> if no Ctrl-C is received while reading command's output or
 * AM didn't order SA's termination or this job's termination.<br>
 * A <code>static flag</code> is used for stopping execution of <code>PeriodicThread</code>s.
 *
 * If Ctrl-C is received by the user when reading a command's output, thread exits immediately.
 * !!! Not needed in Project 2 !!!
 *
 * @author C. Patsouras I. Venieris
 * @version 2
 *
 */
public class PeriodicThread implements Runnable {

	/**
	 * The job this thread will execute periodically.
	 */
	private NmapJob job;

	/**
	 * Static flag used for terminating periodic threads.
	 */
	private static boolean flag = true;
	
	/**
	 * The thread representing this PeriodicThread, used for termination of
	 * a periodic nmap job after AM's order. 
	 */
	private Thread cur = null;

	/** Creates a new periodic thread and set field with given value.
	 * @param job	the nmap job that this thread will execute periodically.	
	 */
	public PeriodicThread(NmapJob job) {
		this.job = job;
	}

	/** Sets the static flag for periodic threads to the given value.
	 * @param flag	the value for periodic flag.
	 */
	public static void setFlag(boolean flag){
		PeriodicThread.flag = flag;
	}

	/**
	 * @return the thread representing this <code>PeriodicThread</code>.
	 */
	public Thread getThread(){
		return this.cur;
	}

	/** Sets the thread representing this PeriodicThread to the given value.
	 * @param t	the thread representing this <code>PeriodicThread</code>.
	 */
	public void setThread(Thread t){
		this.cur = t;
	}

	/** Checks if this PeriodicThread executes the job with the given id
	 * @param id	the id of the job to be examined.
	 * @return		true if the given id is the jobId that this thread handles, false if not so.
	 */
	public boolean checkId(int id){
		return (this.job.getId() == id);
	}

	public void run() {
		System.out.println("I am the periodic " + Thread.currentThread().getName());
		// until flag gets false - Ctrl-C recieved, AM orders termination of SA, or
		// termination of this periodic job thread.
		String command = job.getParameters();
		ResultQueue rq = ResultQueue.getInstance();
		ProcessQueue pq = ProcessQueue.getInstance();
		while(PeriodicThread.flag == true) {
			// if Ctrl-C is received and thread hasn't been interrupted yet
			// avoid starting a process
			try {
				// executing the nmap command
				Process process = Runtime.getRuntime().exec(command);
				pq.push(process);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line2 = new String();
				String s = new String();
				while ((line2 = reader.readLine()) != null) {
					// recieving XML output
					s += line2 ;
				}
				reader.close();

				// Waits for process to finish execution and checks the exit value.
				// If process was successful ("exVal" = 0), passes XML output to ResultQueue.
				// If Ctrl-C is recieved while reading a command's output
				// called process is interrupted and returns 130 as exit value.
				// Thread should exit now and avoid sleeping and checking for "flag" value again.
				// If JobQueue isn't empty this may lead to a new command execution
				// and reading a command's output may require a significant amount of time.
				// !!! The above is useful only for Ctrl-C termination of the client !!!
				
				// If ExitingThread is called while reading a command's output, process will be
				// destroyed and return 143 as exit value. In that case thread will check 
				// flag again which will be false because of its alteration from ExitingThread.
				
				// If AM asked for SA to stop recording this periodic nmap command
				// the thread representing this PeriodicThread will be found by
				// PeriodicQueue.getThread(jobId) and will be interrupted.
				int exVal = process.waitFor();
				if (exVal == 0){
					// process finished successfully. Passes the XML output and jobId into the ResultQueue
					// and removes the process from the ProcessQueue.
					Result res = new Result(s,job.getId());
					rq.push(res);
					pq.remove(process);
				}
				// if Ctrl-C was received while reading's a process output thread will exit.
				// !!! Needed only for termination of the client by the user (Project 1).
				else if (exVal == 130) return;
				// if process was destroyed by the ExitingThread.
				else if (exVal == 143) System.out.println(Thread.currentThread().getName() + " stopped executing " + command + " after AM's signal");
				// error executing a nmap command (i.e. user isn't root).
				else System.out.println(Thread.currentThread().getName() + " error " + exVal + " executing " + command);
				Thread.sleep(1000*job.getTime());
			} 
			catch (IOException e) {

				e.printStackTrace();
			}
			catch (InterruptedException e) {
				// thread got interrupted while sleeping
				// or waiting for a process to finish.
				// thread should exit.
				// re-interrupting this thread and return
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}