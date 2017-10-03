package com.k23b.team13.project3.ac.classes;
/**
 * <code>Result</code> represents the output of an executed nmap command.
 * @author C. Patsouras I. Venieris
 * @version 1
 */
public class Result {
    /**
     * The id for this result.
     */
    int id;
    /**
     * Output of the executed command.
     */
    String output;
    /**
     * Date and time this <code>Result</code> was received by AggregatorManager.
     */
    String timestamp;
    /**
     * Describes the agent that sent this result.
     */
    int hashkey;
    /**
     * Command executed that this <code>Result</code> is about.
     */
    String command;
    /**
     * Constructs a <code>Result</code> instance with the given value.
     * @param id The id for this result.
     * @param output Output of the executed command.
     * @param timestamp Date and time this <code>Result</code> was received by AggregatorManager.
     * @param hashkey Describes the agent that sent this result.
     * @param command Command executed that this <code>Result</code> is about.
     */
    public Result(int id, String output, String timestamp, int hashkey, String command) {
        this.id = id;
        this.output = output;
        this.timestamp = timestamp;
        this.hashkey = hashkey;
        this.command = command;
    }
    /**
     * @return The id for this result.
     */
    public int getId() {
        return id;
    }
    /**
     * Sets id to the given value.
     * @param id The id for this result.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return Output of the executed command.
     */
    public String getOutput() {
        return output;
    }
    /**
     * Sets output to the given value.
     * @param output Output of the executed command.
     */
    public void setOutput(String output) {
        this.output = output;
    }
    /**
     * @return Date and time this <code>Result</code> was received by AggregatorManager.
     */
    public String getTimestamp() {
        return timestamp;
    }
    /**
     * Sets timestamp to the given value.
     * @param timestamp Date and time this <code>Result</code> was received by AggregatorManager.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * @return Describes the agent that sent this result.
     */
    public int getHashkey() {
        return hashkey;
    }
    /**
     *  Sets hashkey to the given value.
     * @param hashkey Describes the agent that sent this result.
     */
    public void setHashkey(int hashkey) {
        this.hashkey = hashkey;
    }
    /**
     * @return Command executed that this <code>Result</code> is about.
     */
    public String getCommand() {
        return command;
    }
    /**
     * Sets command to the given value.
     * @param command Command executed that this <code>Result</code> is about.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString(){
        String str = new String();
        str+="From: Software Agent "+hashkey+"\n";
        str+="Executed: "+command+"\n";
        str+="JobID: "+id+"\n";
        str+=timestamp+"\n";
        str+="Output is:\n";
        str+= output+"\n";
        return str;
    }
}
