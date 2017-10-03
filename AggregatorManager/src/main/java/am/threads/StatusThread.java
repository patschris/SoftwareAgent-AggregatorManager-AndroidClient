package am.threads;

import am.structures.RegisteredAgents;


/**	<code>StatusThread</code> is used for checking each Registered Agent's last
 * 	request for nmap jobs time (measured in milliseconds since epoch) and modifying
 * 	its status (online/offline) if necessary. 
 * 	
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class StatusThread implements Runnable {
	
	/**
	 *	Time in seconds between two consecutive checks
	 *	(and modifications) of Agents' status, read from
	 *	property file.
	 */
	private int checkTime;
	
	/**
	 * 	Time in seconds that equals the 1/3 of the time that
	 * 	make an online Agent/client offline if he hasn't
	 * 	make a request for nmap jobs to execute, read from
	 * 	property file.
	 */
	private int onlinePeriod;
	
	/** Constructs a <code>StatusThread</code> with the given values.
	 * 	
	 * 	@param	checkTime		time in seconds between two consecutive checks.
	 * 	@param	onlinePeriod	time in seconds that equals 1/3 of modyfying
	 * 							an Agent's/client's status (online<->offline).
	 */
	public StatusThread(int checkTime, int onlinePeriod){
		this.checkTime = checkTime;
		this.onlinePeriod = onlinePeriod;
	}
	
	public void run(){
		while(true){
			// get the current time in milliseconds
			long curTime = System.currentTimeMillis();
			RegisteredAgents ra = RegisteredAgents.getInstance(null,null,null);
			// check whose Agent status should be modified
			if (ra != null) ra.modifyStatus(curTime, onlinePeriod);
			try {
				// repeat after checkTime seconds
				Thread.sleep(1000*checkTime);
			} catch (InterruptedException e) {
				// handling interrupt of the thread
				// if AM is exiting.
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
}