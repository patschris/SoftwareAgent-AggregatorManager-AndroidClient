package sa.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sa.classes.*;
import sa.queue.JobQueue;
import sa.queue.ProcessQueue;
import sa.queue.ResultQueue;

/**
 * <code>OneTimeThread</code>s try to pop an NmapJob from JobQueue, wait if it's empty
 * and get notified when a new job is pushed. Then they pop the first command from the queue
 * execute it and push the XML output and jobId (as given by the AM)
 * into the <code>ResultQueue</code> if process exit value is 0.<br>
 * If Ctrl-C is recieved when reading a process's output or this process is destroyed,
 * thread exits immediately.
 * <code>static flag</code> is used for stopping execution of <code>OneTimeThread</code>s.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 * 
 */
public class OneTimeThread implements Runnable {

	/**
	 * Used for termination of <code>OneTimeThread</code>s.
	 */
	private static boolean flag = true;

	/** Sets <code>flag</code> to the given <code>boolean</code> value.
	 * @param flag	a flag for handling execution of <code>OneTimeThread</code>s. 
	 * 				If <code>flag</code> is false <code>OneTimeThread</code>s stop executing.
	 */
	public static void setFlag(boolean flag) {
		OneTimeThread.flag = flag;
	}

	public void run(){
		System.out.println("I am the one time " + Thread.currentThread().getName());
		JobQueue jq = JobQueue.getInstance();
		ResultQueue rq = ResultQueue.getInstance();
		ProcessQueue pq = ProcessQueue.getInstance();
		// until flag gets false - Ctrl-C or AM's signal is recieved
		NmapJob job = null;
		while(OneTimeThread.flag == true) {
			// http://www.codejava.net/java-se/file-io/execute-operating-system-commands-using-runtime-exec-methods
			job = jq.pop(); // trying to pop a job from JobQueue
			// job may be null if ExitingThread was called and this thread got
			// interrupted waiting on the JobQueue to pop a job.
			if (job == null) return;
			try {
				String command = job.getParameters();

				// executing the nmap command
				Process process = Runtime.getRuntime().exec(command);
				pq.push(process);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = new String();
				String s = new String();
				while ((line = reader.readLine()) != null) {
					// recieving XML output 
					s += line;
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
				int exVal = process.waitFor();
				if (exVal == 0){
					// process finished successfully. Passes the XML output and jobId into the ResultQueue
					// and removes the process from the ProcessQueue.
					System.out.println(Thread.currentThread().getName() + " finished job " + job.getId());
					Result res = new Result(s, job.getId());
					rq.push(res);
					pq.remove(process);
				}
				// if Ctrl-C was received while reading's a process output thread will exit.
				// !!! Needed only for termination of the client by the user (Project 1).
				else if (exVal == 130) return;
				// if process was destroyed by the ExitingThread.
				else if (exVal == 143) System.out.println(Thread.currentThread().getName() + " stopped executing " + command + " after AM's signal for termination of SA");
				// error executing a nmap command (i.e. user isn't root).
				else System.out.println(Thread.currentThread().getName() + " error " + exVal + " executing " + command);
			}
			catch (InterruptedException e){
				// thread got interrupted while sleeping or waiting for a process to finish
				// thread should exit
				// re-interrupting this thread and return
				Thread.currentThread().interrupt();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}