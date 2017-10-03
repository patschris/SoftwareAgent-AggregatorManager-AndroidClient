package com.k23b.team13.project3.ac.classes;
/**
 * <code>RegisterInfo</code> represents the credentials to be sent to Aggregator Manager in order to create a new account.
 * @author C. Patsouras I. Venieris
 * @version 1
 */
public class RegisterInfo {
    /**
     * Username of client who made register request.
     */
    private String username;
    /**
     * Password of client who made register request.
     */
    private String password;
    /**
     * Constructor of <code>RegisterInfo</code> class.
     * @param username Username of client who made register request.
     * @param password Password of client who made register request.
     */
    public RegisterInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }
    /**
     * @return Username of client who made register request.
     */
    public String getUsername() {
        return username;
    }
    /**
     * @return Password of client who made register request.
     */
    public String getPassword() {
        return password;
    }
}
