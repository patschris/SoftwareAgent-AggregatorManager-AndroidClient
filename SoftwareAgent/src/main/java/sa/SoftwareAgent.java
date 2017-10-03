package sa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import sa.classes.NmapJob;
import sa.classes.Registration;
import sa.queue.JobQueue;
import sa.queue.PeriodicQueue;
import sa.queue.ThreadQueue;
import sa.threads.*;


/**
 * <code>SoftwareAgent</code> is the main thread. Reads from the property
 * file "test.properties" number of <code>OneTimeThread</code>s to be created, 
 * sleep time for <code>OneTimeThread</code>s in seconds, max sleep time for
 * <code>SenderThread</code>, and base address of the <code>Aggregator Manager</code>.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 *
 */
public class SoftwareAgent {

	/**
	 * Main reads from property file the number of <code>OneTimeThread</code>s 
	 * to be created, time between requests to <code>Aggregator Manager</code>,
	 * AM's base address and max time between results sending to AM.
	 * Creates a JSON string with <code>Registration</code> info to send a "login
	 * request" to AM at path "/register" and keeps sending this request 
	 * until Software Agent is accepted.
	 * Once the Agent is accepted, main creates <code>OneTimeThread</code>s 
	 * and <code>SenderThread</code> and pushes them into a <code>ThreadQueue</code>, 
	 * used for the termination of the client.
	 * As soon as the threads are started, main thread sends periodically requests to AM's
	 * path "/request/<Agent Hashkey>" to receive JSON formatted nmap jobs.
	 * If job is one-time, it gets pushed into a <code>JobQueue</code> and will 
	 * be pulled by some <code>OneTimeThread</code> that will execute it.
	 * For a periodic job, a new <code>PeriodicThread</code> is created which 
	 * executes the same command with the given period.
	 * A job formatted like "(periodic_job_id,Stop,true,period,0)" indicates that
	 * periodic job with "periodic_job_id" must stop execution. 
	 * In that case main thread searches for the <code>PeriodicThread</code> that 
	 * handles this job in <code>PeriodicQueue</code> 
	 * (consisted of <code>PeriodicThread</code>s) and interrupts it.
	 * A job formatted like "(-1,exit(0),true,-1)" indicates this Agents' termination.
	 * Upon it's received, <code>ExitingThread</code> is started, which destroys 
	 * all the running processes (read from <code>ProcessQueue</code>) and interrupts 
	 * the running threads (read from <code>ThreadQueue</code>).
	 * Agent won't execute jobs not containing "-oX -" flag.
	 * 
	 */
	
	public static void main(String[] args) {

		/*http://www.avajava.com/tutorials/lessons/how-do-i-read-a-properties-file.htm */
		try {
			// reading from property file
			File file = new File("src/main/java/sa/test.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			final int numThreads = Integer.parseInt(properties.getProperty("oneTimeThreads"));
			final int maxSenderSleep = Integer.parseInt(properties.getProperty("maxSenderSleep"));
			final int mainSleep = Integer.parseInt(properties.getProperty("mainSleep"));
			final String addr = properties.getProperty("addr");
			// creating a client, a Registration with Agent info, formatting Registration
			// in JSON string and sending it to server until it's accepted.
			Client client = Client.create();
			Registration r = new Registration();
			Gson g = new Gson();
			String tosend = g.toJson(r);
			ClientResponse response = null;
			WebResource webResource = client.resource(addr + "/register");

			do{
				System.out.println("sending register request");
				try{
					response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, tosend);
				}catch (ClientHandlerException e1) {
					// handling server down case.
					// main will send a new request after an amount of time
					try{
						System.out.println("Server is probably down");
						Thread.sleep(1000*mainSleep);
					} catch (InterruptedException e) {
						// handling interrupt
						return;
					}
					continue;
				}

					if (response.getStatus() == 200){
						// AM granted access to SA.
						// SA will start threads and asking for nmap jobs.
						String output = response.getEntity(String.class);
						System.out.println("Access granted, server returned: " + response.getStatus() + " " + output);
					}
					else
					{	// AM didn't approve SA.
						// SA will send a login request again.
						System.out.println("Access denied, server returned: " + response.getStatus());
						try {
							Thread.sleep(1000*mainSleep);
						} catch (InterruptedException e) {
							return;
						}
					}
				} while(response == null || response.getStatus() != 200);

				SenderThread.setTime(maxSenderSleep);
				ThreadQueue tq = ThreadQueue.getInstance();
				// creating threads
				System.out.println("Creating " + numThreads + " one-time nmap threads!");
				tq.push(Thread.currentThread());
				JobQueue jq = JobQueue.getInstance() ;

				for (int i = 0 ; i < numThreads ; i ++){ // Creating one time job threads
					OneTimeThread t = new OneTimeThread();
					Thread t1 = new Thread(t);
					tq.push(t1);
					t1.start();
				}
				// Creating sender thread.
				// Giving as args AM's result path, this SA's hashkey and this client.
				SenderThread st = new SenderThread(addr + "/result",r.getHashKey(),client);
				Thread sthr = new Thread(st);
				tq.push(sthr);
				sthr.start();

				// shutdownHook thread
				ShutDownHook sdh = new ShutDownHook(addr,r.getHashKey(),client);
				Thread sdht= new Thread(sdh);
				Runtime.getRuntime().addShutdownHook(sdht);

				// Creating exiting thread which will be started when
				// server sends (-1,exit(0),true,-1) as a job.
				ExitingThread et = new ExitingThread(client);
				Thread tet = new Thread(et);

				// Getting an instance of the PeriodicQueue.
				// PeriodicQueue is used when SA must stop
				// executing a periodic command.
				PeriodicQueue pq = PeriodicQueue.getInstance();

				while(true){
					// Once client is logged in, sends request to AM for jobs that
					// this SA should execute in path "/request/<This SA's hashkey>.
					// Jobs are received in text, JSON formatted.
					// If a job is one-time, it gets pushed into JobQueue.
					// If it is periodic, a new thread is created to execute it.
					// For "Stop" job parameters, the right thread will be interrupted.
					// For (-1,exit(0),true,-1) job, exiting thread will be started.
					try{
						webResource = client.resource(addr + "/request/" + r.getHashKey());
						response = webResource.accept("text/plain").type("text/plain").get(ClientResponse.class);
						}catch (ClientHandlerException e1) {
							try{
								System.out.println("Server is probably down");
								Thread.sleep(1000*mainSleep);
								continue;
							} catch (InterruptedException e) {
								return;
							}
						}
					if (response.getStatus() == 200){
						System.out.println("Reading recieved jobs from AM..");
						String toexec = response.getEntity(String.class);
						Scanner scanner = new Scanner(toexec);
						// reading received jobs
						while (scanner.hasNextLine()) {
							String line = scanner.nextLine();
							JSONObject j = (JSONObject) new JSONParser().parse(line);
							NmapJob job = new NmapJob();
							job.setId(Integer.parseInt(j.get("id").toString()));	// id of nmap job
							job.setParameters(j.get("parameters").toString());		// parameters of job
							job.setPeriodical(Boolean.parseBoolean(j.get("periodical").toString()));	// periodic flag of job
							job.setTime(Integer.parseInt(j.get("time").toString()));// time period for job
							if (job.getId() == -1 && job.isPeriodical() == true && job.getTime() == -1 && job.getParameters().equals("exit(0)")){
								// SA must stop execution. Exiting thread is started.
								tet.start();
								scanner.close();
								return;
							}
							if (job.getParameters().contains("-oX -") == false && job.getParameters().contains("Stop") == false) {
								// execute only commands with XML output
								System.out.println("Instruction " + job.getId() + ": nmap " + job.getParameters()
										+" won't return results in XML format and i won't execute it");
								continue;
							}
							if (job.isPeriodical() == false) {
								// if job isn't periodic, add to job queue
								jq.push(job);
							}
							else{
								// if it is, create a new periodic thread
								// and push it to PeriodicQueue
								if (job.getParameters().contains("Stop")){
									// a periodic thread must stop.
									// Find this thread by searching the PeriodicQueue and interrupt it.
									Thread tostop = pq.getThread(job.getId());
									if (tostop != null){
										System.out.println("Periodic " + tostop.getName() + " executing " + job.getId() + " will stop");
										tostop.interrupt();
									}
								}
								else{
									PeriodicThread pt = new PeriodicThread(job);
									Thread t2 = new Thread(pt);
									pq.push(pt);	// push to periodic thread queue
									tq.push(t2);	//push to thread queue
									pt.setThread(t2);// setting this periodic thread's thread.
									t2.start();	// starting this periodic thread
								}
							}
						}
						scanner.close();
					}
					else{
						// SA has no new jobs to execute right now.
						System.out.println("AM returned: " + response.getStatus() + " " + response.getEntity(String.class));
					}
					Thread.sleep(1000*mainSleep);
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}