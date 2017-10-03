package am.services.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import am.threads.StatusThread;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

/**	<code>ServerStarting</code> contains methods used for initializing
 * 	the grizzly server (<code>AggregatorManager</code>'s server) and
 * 	starts the <code>StatusThread</code>.
 * 	
 * 	@author 	C. Patsouras I. Venieris
 *	@version	1
 */
public class ServerStarting {
	
	/**
	 * 	The server used by <code>AggregatorManager</code>.
	 */
	private static HttpServer server;
	
	/**
	 * 	The <code>StatusThread</code> used for checking
	 * 	<code>Registration</code>s'/Agents status (online/offline)
	 * 	and modifying it if needed.
	 */
	private static Thread st;


    /**	Reading BASE_URI, port used for the grizzly server, time in seconds
     * 	between two consecutive checks by <code>StatusThread</code> and
     * 	1/3 of the time that an Agent/client is marked offline if it
     * 	hasn't send a request for nmap jobs to <code>AggregatorManager</code>.
     * 	
     * 	@return	the server that was just created.
     * 	@throws	IOException
     */
    public static HttpServer startServer() throws IOException {
    	// reading from property file
    	File file = new File("src/main/java/am/resources/server.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties prop = new Properties();
		prop.load(fileInput);
		fileInput.close();
		String BASE_URI = prop.getProperty("BASE_URI");
		String port = prop.getProperty("port");
		int onlinePeriod = Integer.parseInt(prop.getProperty("onlinePeriod"));
		int statusTime = Integer.parseInt(prop.getProperty("statusTime"));
		// setting resource packages
        ResourceConfig resourceConfig = new PackagesResourceConfig("am");
        // creating the status thread
        StatusThread st = new StatusThread(statusTime,onlinePeriod);
        // building the used URI
        URI uri = UriBuilder.fromUri(BASE_URI).port(Integer.parseInt(port)).build();
        Thread thr = new Thread(st);
        ServerStarting.st = thr;
        // starting the status thread
        thr.start();
        server =  GrizzlyServerFactory.createHttpServer(uri,resourceConfig);
        return server;
    }
    
    /**
     * 	@return	the <code>StatusThread</code> used for checking
     * 			Agent's status.
     */
    public static Thread getStatusThread(){
    	return ServerStarting.st;
    }

	/**
	 * 	@return	the server used by <code>AggregatorManager</code>.
	 */
	public static HttpServer getServer() {
		return server;
	}

	/**	Sets the server used by <code>AggregatorManager</code>
	 * 	to the given value.
	 * 	@param	server	the server used by AM.
	 */
	public static void setServer(HttpServer server) {
		ServerStarting.server = server;
	}
}