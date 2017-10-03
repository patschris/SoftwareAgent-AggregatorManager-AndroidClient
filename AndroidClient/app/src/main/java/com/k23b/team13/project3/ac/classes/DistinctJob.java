package com.k23b.team13.project3.ac.classes;

/**
 *  Distinct commands returned by web service.
 * 	@author		C. Patsouras I. Venieris
 *	@version 	1
 */
public class DistinctJob {
    /**
     * command returned by web service.
     */
    private String command;
    /**
     * Constructor of <code>DistinctJobs</code> class.
     * @param command command returned by web service.
     */
    public DistinctJob(String command) {
        this.command = command;
    }
    /**
     * @return command received from web service.
     */
    public String getCommand() {
        return command;
    }
}
