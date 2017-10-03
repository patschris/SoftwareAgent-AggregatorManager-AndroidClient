package am.classes;

import org.json.simple.JSONObject;
/** 
 * <code>AndoidResult</code> represents the output of an executed nmap command that will be sent to Android Client.
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class AndroidResults {
	/**
	 * Identifier of the NmapJob that this <code>AndroidResults</code> is about.
	 */
	private int id;
	/**
	 * 	Describes the output of the executed command.
	 */
	private String output;
	/**
	 * Date and time this Result was received by Aggregator Manager.
	 */
	private String timestamp;
	/**
	 *  HashKey describes the agent that sent this result. 
	 */
	private int hashkey;
	/**
	 * Command executed that this <code>AndroidResults</code> is about.
	 */
	private String command;
	
	/**
	 * Constructs an <code>AndroidResults</code> instance with the given value.
	 * @param id Identifier of the <code>NmapJob</code> that this <code>AndroidResults</code> is about.
	 * @param output Describes the output of the executed command.
	 * @param timestamp Date and time this Result was received by Aggregator Manager.
	 * @param hashkey HashKey describes the agent that sent this result.
	 * @param command Command executed that this <code>AndroidResults</code> is about.
	 */
	public AndroidResults(int id, String output, String timestamp, int hashkey,String command) {
		this.id = id;
		this.output = output;
		this.timestamp = timestamp;
		this.hashkey = hashkey;
		this.command = command;
	}
	/**
	 * @return identifier of the <code>NmapJob</code> that this <code>AndroidResults</code> is about.
	 */
	public int getId() {
		return id;
	}
	/** 
	 * @return Describes the output of the executed command.
	 */
	public String getOutput() {
		return output;
	}
	/** 
	 * @return Date and time this Result was received by Aggregator Manager. 	 
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/** 
	 * @return HashKey describes the agent that sent this result. 
	 */
	public int getHashkey() {
		return hashkey;
	}
	/** 
	 * @return Command executed that this <code>AndroidResults</code> is about.
	 */
	public String getCommand() {
		return command;
	}
	/** 
	 * @return JSON object that represents this <code>AndroidResults</code> object.
	 */
	@SuppressWarnings("unchecked")
	public  JSONObject JsonAndroidResults(){
		  JSONObject obj=new JSONObject();
		  obj.put("id", id);
		  obj.put("output", output);
		  obj.put("timestamp", timestamp);
		  obj.put("hashkey", hashkey);
		  obj.put("command", command);
		  return obj;
	} 
}